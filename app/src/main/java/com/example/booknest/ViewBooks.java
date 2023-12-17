package com.example.booknest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ViewBooks extends AppCompatActivity {

    private RecyclerView recyclerView;
    private viewBooksAdapter adapter;

    List<bestDealModel> bookList;

    private static final int READ_REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_books);




        recyclerView = findViewById(R.id.recyclerView);

        // Assuming you have a list of data for the adapter
        bookList = new ArrayList<>();



        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        int permissionStatus = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
//
//        if (permissionStatus != PackageManager.PERMISSION_GRANTED) {
//            // Permission is denied
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                Toast.makeText(this, "require permission", Toast.LENGTH_LONG).show();
//            } else {
//                // Redirect the user to the app settings page
//                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
//                Uri uri = Uri.fromParts("package", getPackageName(), null);
//                intent.setData(uri);
//                startActivity(intent);
//            }
//        }

        loadData();

        //get all books code


    }

    private void loadData() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("Home", "Connected to Internet");
            bookList.clear();

            // Call AsyncTask to fetch data from the server
            new ViewBooks.FetchDataAsyncTask().execute();

        } else {
            Log.d("Home", "Not Connected to Internet");
            Toast.makeText(this, "Fetching data from SQLite", Toast.LENGTH_SHORT).show();
            // Handle SQLite data loading if needed


            SqliteHelper db = new SqliteHelper(this);
            List<bestDealModel> booksList = db.getAllBooks();


            Log.d("Request", "Added to from Sqllite" + booksList);


            adapter = new viewBooksAdapter(this, booksList);
            recyclerView.setAdapter(adapter);
        }
    }

    private class FetchDataAsyncTask extends AsyncTask<Void, Void, List<bestDealModel>> {
        @Override
        protected List<bestDealModel> doInBackground(Void... voids) {
            List<bestDealModel> fetchedData = new ArrayList<>();

            String webApiUrl = "http://192.168.1.4/booknest/get_book.php";

            StringRequest stringRequest = new StringRequest(Request.Method.POST, webApiUrl,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject object = new JSONObject(response);
                                if (object.getInt("Status") == 1) {
                                    JSONArray booksArray = object.getJSONArray("Books");

                                    // Parse JSON response and populate fetchedData list
                                    for (int i = 0; i < booksArray.length(); i++) {
                                        JSONObject bookObject = booksArray.getJSONObject(i);
                                        String bookID = bookObject.getString("bookID");
                                        String bookCategory = bookObject.getString("bookCategory");
                                        String bookTitle = bookObject.getString("bookTitle");
                                        String author = bookObject.getString("bookAuthor");
                                        String price = bookObject.getString("bookPrice");

                                        String rvType = bookObject.getString("rvType");

                                        String discountStr = bookObject.getString("discount");
                                        int discount = Integer.parseInt(discountStr);

                                        String imgUrl = bookObject.getString("imgUrl");

                                        Log.d("ImageUrl", "Image URL: " + imgUrl); // Add this line to log the imgUrl


                                        // Create bestDealModel object and add it to fetchedData list
                                        bestDealModel model = new bestDealModel(imgUrl, bookCategory, bookTitle, author, price, discount, rvType);
                                        model.setByTime();
                                        model.setBookID(bookID);
                                        fetchedData.add(model);


                                    }

                                    updateUIWithData(fetchedData);

                                } else {
                                    Log.e("FetchDataAsyncTask", "Failed with status: " + object.getInt("status"));
                                }
                            } catch (JSONException e) {
                                Log.e("FetchDataAsyncTask", "JSON Exception: " + e.getMessage());
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError e) {
                            // Handle error case
                            Log.e("FetchDataAsyncTask", "Error: " + e.getMessage());
                        }
                    });

            Log.d("FetchDataAsyncTask", "Finished doInBackground method");
            // Add the request to the Volley request queue
            RequestQueue queue = Volley.newRequestQueue(ViewBooks.this);
            queue.add(stringRequest);

            return fetchedData;
        }


    }

    private void updateUIWithData(List<bestDealModel> fetchedData) {
        if (fetchedData != null && fetchedData.size() > 0) {


            bookList.clear();
            bookList.addAll(fetchedData);


            //displayToastWithListContents(bookList);

            adapter = new viewBooksAdapter(this, bookList);
            recyclerView.setAdapter(adapter);


        } else {
            Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
        }
    }



    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == READ_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with data loading
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, handle accordingly
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}