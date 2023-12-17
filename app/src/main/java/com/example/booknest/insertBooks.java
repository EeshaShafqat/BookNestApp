package com.example.booknest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class insertBooks extends AppCompatActivity {

    EditText editTextBookCategory, editTextBookTitle,
            editTextBookAuthor, editTextBookPrice, editTextDiscount, editTextCount, rvType;
    ImageView imageViewBookImage;
    Button buttonInsertBook;
    String imagePath;


    private static final int REQUEST_CODE = 110; // Request code for image picker


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_books);

        imagePath="";

        // Finding views by their IDs
        editTextBookCategory = findViewById(R.id.editTextBookCategory);
        editTextBookTitle = findViewById(R.id.editTextBookTitle);
        editTextBookAuthor = findViewById(R.id.editTextBookAuthor);
        editTextBookPrice = findViewById(R.id.editTextBookPrice);
        editTextDiscount = findViewById(R.id.editTextDiscount);

        imageViewBookImage = findViewById(R.id.imageViewBookImage);
        buttonInsertBook = findViewById(R.id.buttonInsertBook);
        rvType = findViewById(R.id.rvType);

        SqliteHelper db = new SqliteHelper(this);

        // Set onClickListener for the insert button
        buttonInsertBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Perform insert operation here
                // Retrieve values from the fields
                String bookCategory = editTextBookCategory.getText().toString();
                String bookTitle = editTextBookTitle.getText().toString();
                String bookAuthor = editTextBookAuthor.getText().toString();
                String bookPrice = editTextBookPrice.getText().toString();
                String discount = editTextDiscount.getText().toString();
                String type = rvType.getText().toString();

                // Perform further operations (e.g., database insertion) using the retrieved values
                // You can use these values to insert the book details into your database
                // Display a toast to indicate the button click
                Toast.makeText(insertBooks.this, "Insert button clicked", Toast.LENGTH_SHORT).show();

                //add to local db

                bestDealModel book = new bestDealModel( imagePath, bookCategory, bookTitle,bookAuthor, bookPrice, Integer.parseInt(discount), type);

                db.addBook(book);



                //Add to db through post request
                intitateBooksDataPostRequest( bookCategory, bookTitle,bookAuthor, bookPrice, discount, type, new RegistrationCallback() {
                    @Override
                    public void onRegistrationComplete(boolean success) {
                        if (success) {

                            Toast.makeText(insertBooks.this, "Book added to Database", Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(insertBooks.this, "Failed to Add Book", Toast.LENGTH_SHORT).show();

                        }
                    }
                });



            };
        });

        // Set onClickListener for the image view (for image selection)
        imageViewBookImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });


    }


    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri selectedImg = data.getData();


            // Assuming 'selectedImg' is the Uri of the selected image
            InputStream inputStream = null;
            try {
                inputStream = getContentResolver().openInputStream(selectedImg);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            File outputFile = new File(getFilesDir(), "image.jpg"); // Choose a suitable file name

            OutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(outputFile);
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }

            byte[] buffer = new byte[1024];
            int bytesRead;
            while (true) {
                try {
                    if (!((bytesRead = inputStream.read(buffer)) != -1)) break;
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    outputStream.write(buffer, 0, bytesRead);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }

// Close streams
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            imagePath = outputFile.getAbsolutePath(); // Use this path for storage

             File imgFile = new File(imagePath);
             Picasso.get().load(imgFile).into(imageViewBookImage);

            //*******************************************************


        }
    }


    private interface RegistrationCallback {
        void onRegistrationComplete(boolean success);
    }
    private void intitateBooksDataPostRequest( String bookCategory, String bookTitle, String bookAuthor, String bookPrice, String discount, String type, RegistrationCallback callback) {
        Log.d("Registration", "Initiating post req");

        String webApiUrl = "http://192.168.1.4/booknest/insert_book.php";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, webApiUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Log the full response
                        Log.d("Books", "Response: " + response);


                        try {
                            JSONObject object = new JSONObject(response);

                            if (object.getInt("Status") == 1) {
                                Log.d("Insertion", "Post success");
                                //Toast.makeText(Registration.this, response, Toast.LENGTH_SHORT).show();


                                callback.onRegistrationComplete(true);
                            } else {
                                // Handle other status values if needed
                                Log.d("Insertion", "Post failed with status: " + object.getInt("status"));
                                callback.onRegistrationComplete(false);
                            }

                        } catch (JSONException e) {
                            // Handle the case where the response is not a JSON object
                            Log.d("Registration", "JSONException: " + e.getMessage());
                            e.printStackTrace(); // Add this line to log the stack trace
                            callback.onRegistrationComplete(false);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError e) {
                        // Handle error
                        Toast.makeText(insertBooks.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d("Registration", "Post fail: " + e.getMessage());
                        callback.onRegistrationComplete(false);
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();




                params.put("bookCategory", bookCategory);
                params.put("bookTitle", bookTitle);
                params.put("bookAuthor", bookAuthor);
                params.put("bookPrice", bookPrice);
                params.put("discount", discount);
                params.put("rvType", type);
                params.put("imgUrl", imagePath);


                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(insertBooks.this);
        queue.add(stringRequest);
    }

//    private String getImagePath(Uri uri) {
//        String path = "";
//
//        String[] projection = { MediaStore.Images.Media.DATA };
//        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
//
//        if (cursor != null && cursor.moveToFirst()) {
//            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//            path = cursor.getString(columnIndex);
//            cursor.close();
//        }
//
//        return path;
//    }


}
