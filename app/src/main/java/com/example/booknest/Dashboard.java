package com.example.booknest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class Dashboard extends AppCompatActivity {


    BottomNavigationView navbar;

    List<bestDealModel> bookList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        bookList = new ArrayList<>();

        //switching fragments with click
        navbar = findViewById(R.id.bottom_navigation);

        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = null;
                int itemId = item.getItemId();

                if (itemId == R.id.home) {

                    selected = new HomeFragment();

                } else if (itemId == R.id.account) {

                    selected = new AccountFragment();

                } else if (itemId == R.id.categories) {

                    selected = new CategoryFragment();

                }else if (itemId == R.id.cart) {

                    selected = new CartFragment();
                }



                if(selected!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,selected).commit();

                    // After the fragment transaction, retrieve the fragment instance and pass the bookList
                    if (selected instanceof HomeFragment) {
                        ((HomeFragment) selected).setBookList(bookList);

                    }if (selected instanceof CategoryFragment) {
                        ((CategoryFragment) selected).setBookList(bookList);

                    }
                }

                return true;

            }
        });



        loadData();



    }


    private void loadData() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("Home", "Connected to Internet");
            bookList.clear();

            // Call AsyncTask to fetch data from the server
            new FetchDataAsyncTask().execute();

        } else {
            Log.d("Home", "Not Connected to Internet");
            Toast.makeText(this, "Fetching data from SQLite", Toast.LENGTH_SHORT).show();
            // Handle SQLite data loading if needed


            SqliteHelper db = new SqliteHelper(this);
            List<bestDealModel> booksList = db.getAllBooks();

            bookList.clear();
            bookList.addAll(booksList);

            Log.d("Request", "Added to from Sqllite" + booksList);

            HomeFragment homeFragment = new HomeFragment();
            homeFragment.setBookList(bookList);

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
            RequestQueue queue = Volley.newRequestQueue(Dashboard.this);
            queue.add(stringRequest);

            return fetchedData;
        }


    }

    private void displayToastWithListContents(List<bestDealModel> list) {
        // Toast the contents of the list
        StringBuilder toastMessage = new StringBuilder();
        for (bestDealModel item : list) {
            toastMessage.append("Category: ").append(item.getBookCategory())
                    .append(", Title: ").append(item.getBookTitle())
                    .append(", Author: ").append(item.getBookAuthor())
                    .append(", Price: ").append(item.getBookPrice())
                    .append(", Discount: ").append(item.getDiscount())
                    .append(", Count: ").append(item.getCount())
                    .append(", RV Type: ").append(item.getRvType())
                    .append(", Image URL: ").append(item.getImgUrl())
                    .append("\n\n");
        }

        Toast.makeText(this, toastMessage.toString(), Toast.LENGTH_LONG).show();
    }




    private void updateUIWithData(List<bestDealModel> fetchedData) {
        if (fetchedData != null && fetchedData.size() > 0) {


            bookList.clear();
            bookList.addAll(fetchedData);


            //displayToastWithListContents(bookList);


            // Pass bookList to CategoryFragment
//            CategoryFragment categoryFragment = new CategoryFragment();
//            categoryFragment.setBookList(bookList);

              HomeFragment homeFragment = new HomeFragment();
              homeFragment.setBookList(bookList);


        } else {
            Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_SHORT).show();
        }
    }



}