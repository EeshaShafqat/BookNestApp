package com.example.booknest;




import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.ResultSet;

public class WelcomeScreen extends AppCompatActivity {

    private SharedPreferences sharedPreferences;
    private static final String USER_PREFS = "UserPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_ADMIN = "isAdmin";

    private static final String KEY_USERNAME = "username";
    Button getStarted;
    TextView register;

    private FirebaseAuth authProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_screen);

        getStarted = findViewById(R.id.getStarted);
        register = findViewById(R.id.register);

        authProfile = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);

        getStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(WelcomeScreen.this, GetStarted.class);
                startActivity(intent);

            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeScreen.this, Register.class);
                startActivity(intent);
            }
        });
    }



    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = authProfile.getCurrentUser();

        if (!isNetworkAvailable()) {
            // If internet is not available, check SharedPreferences

            String savedUsername = sharedPreferences.getString(KEY_USERNAME, "");
            String savedEmail = sharedPreferences.getString(KEY_EMAIL, "");
            String savedPassword = sharedPreferences.getString(KEY_PASSWORD, "");

            boolean isAdmin = sharedPreferences.getBoolean(KEY_IS_ADMIN, false);

            if (!savedEmail.isEmpty() && !savedPassword.isEmpty()) {
                // If there are saved credentials in SharedPreferences
                if (isAdmin) {
                    // User is an admin, direct to admin activity
                    Intent adminIntent = new Intent(WelcomeScreen.this, ManageInventory.class);
                    startActivity(adminIntent);
                } else {
                    // User is not an admin, proceed to regular user dashboard
                    Intent userIntent = new Intent(WelcomeScreen.this, Dashboard.class);
                    userIntent.putExtra("username", savedUsername);
                    startActivity(userIntent);
                }
            } else {
                // No saved credentials, proceed with normal flow
                Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
            }
        } else {
            // If internet is available, proceed with the regular flow using Firebase
            if (currentUser != null) {
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            Boolean isAdmin = snapshot.child("isAdmin").getValue(Boolean.class);
                            if (isAdmin != null && isAdmin) {
                                // User is an admin, direct to admin activity
                                Intent adminIntent = new Intent(WelcomeScreen.this, ManageInventory.class);
                                startActivity(adminIntent);
                            } else {
                                // User is not an admin, proceed to regular user dashboard
                                Intent userIntent = new Intent(WelcomeScreen.this, Dashboard.class);
                                startActivity(userIntent);
                            }
                        } else {
                            // Handle the case where user data does not exist
                            Toast.makeText(WelcomeScreen.this, "No user data found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle error
                    }
                });
            } else {
                // No user is signed in, proceed with normal flow
                Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else return false;
    }


}