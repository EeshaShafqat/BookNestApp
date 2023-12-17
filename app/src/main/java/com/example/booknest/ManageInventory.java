package com.example.booknest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

public class ManageInventory extends AppCompatActivity {


    private FirebaseAuth auth;
    FirebaseUser firebaseUser;

    SharedPreferences sharedPreferences;
    private static final String USER_PREFS = "UserPrefs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_inventory);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        sharedPreferences = this.getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);

        Button btnAddBook = findViewById(R.id.btnAddBook);
        btnAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageInventory.this, insertBooks.class);
                startActivity(intent);
            }
        });

        Button btnViewBooks = findViewById(R.id.btnViewBooks);
        btnViewBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageInventory.this, ViewBooks.class);
                startActivity(intent);
            }
        });



         //Delete all books
        Button btnDeleteAllBooks = findViewById(R.id.btnDeleteAllBooks);

        SqliteHelper db = new SqliteHelper(this);
        btnDeleteAllBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DeleteAllBooksTask().execute();

                db.deleteAllBooks();
            }
        });

        Button btnAnotherButton1 = findViewById(R.id.btnAnotherButton1);
        btnAnotherButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add code for Another Button 1 click
            }
        });

        Button btnAnotherButton2 = findViewById(R.id.btnAnotherButton2);

        btnAnotherButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add code for Another Button 2 click
            }
        });

        Button btnAnotherButton3 = findViewById(R.id.btnAnotherButton3);
        btnAnotherButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add code for Another Button 3 click
            }
        });

        Button btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (firebaseUser != null) {
                    FirebaseAuth.getInstance().signOut();
                }

                // Clear shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Move to WelcomeScreen
                startActivity(new Intent(ManageInventory.this, WelcomeScreen.class));
                finish();
            }
        });
    }


    private class DeleteAllBooksTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            try {
                URL url = new URL("http://192.168.1.4/booknest/delete_all_books.php"); // Replace with your PHP script URL
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");

                InputStream inputStream = conn.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuilder stringBuilder = new StringBuilder();
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuilder.append(line).append("\n");
                }
                return stringBuilder.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            // Handle the response from the PHP script here
            if (result != null) {
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    int status = jsonObject.optInt("Status");

                    if (status == 1) {
                        // Deletion successful
                        Toast.makeText(getApplicationContext(), "All books deleted successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        // Deletion failed or no records found
                        Toast.makeText(getApplicationContext(), "No books found or deletion failed", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // Handle JSON parsing error
                }
            } else {
                // Handle the case where the result is null or there's an error
                Toast.makeText(getApplicationContext(), "Failed to connect. Please check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }


}
