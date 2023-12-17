package com.example.booknest;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class GetStarted extends AppCompatActivity {

    RelativeLayout registerNewMember;


    //Waleed Adnan
    ImageView BackButton;
    EditText Email, Password;
    TextView TextViewForgotPassword;

    private FirebaseAuth authProfile;
    private static final String TAG = "GetStarted";


    private SharedPreferences sharedPreferences;
    private static final String USER_PREFS = "UserPrefs";
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_IS_ADMIN = "isAdmin";

    private static final String KEY_USERNAME = "username";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        registerNewMember = findViewById(R.id.registerNewMember);

        sharedPreferences = getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);

        //Waleed Adnan
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);

        authProfile = FirebaseAuth.getInstance();

        Button btnlogin = findViewById(R.id.login);
        TextViewForgotPassword = findViewById(R.id.forgotPassword);

        ImageView imageViewShowHidePassword = findViewById(R.id.showhide_password);
        imageViewShowHidePassword.setImageResource(R.drawable.eye);
        imageViewShowHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add null check here
                if (Password.getTransformationMethod() != null && Password.getTransformationMethod().equals(android.text.method.PasswordTransformationMethod.getInstance())) {
                    imageViewShowHidePassword.setImageResource(R.drawable.ic_hide_pwd); // resource name for the 'hide password' icon
                    // Show Password
                    Password.setTransformationMethod(android.text.method.HideReturnsTransformationMethod.getInstance());
                } else {
                    imageViewShowHidePassword.setImageResource(R.drawable.eye); // resource name for the 'show password' icon
                    // Hide Password
                    Password.setTransformationMethod(android.text.method.PasswordTransformationMethod.getInstance());
                }
            }
        });

        TextViewForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GetStarted.this, "You can reset your Password", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(GetStarted.this, ForgotPassword.class));
            }
        });


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = Email.getText().toString();
                String password = Password.getText().toString();

                if (email.isEmpty()) {
                    Email.setError("Please enter your email");
                    Email.requestFocus();
                } if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Email.setError("Please enter a valid email");
                    Email.requestFocus();
                } else if (password.isEmpty()) {
                    Password.setError("Please enter your password");
                    Password.requestFocus();
                } else if (email.isEmpty() && password.isEmpty()) {
                    Email.setError("Please enter your email");
                    Email.requestFocus();
                    Password.setError("Please enter your password");
                    Password.requestFocus();
                } else {
                    LoginUser();
                }
            }
        });


        registerNewMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(GetStarted.this, Register.class);
                startActivity(intent);
            }
        });



        //Waleed Adnan
        BackButton = findViewById(R.id.back);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GetStarted.this, WelcomeScreen.class);
                startActivity(intent);
            }
        });

    }

    //Waleed Adnan
    private void LoginUser() {

        String userEmail = Email.getText().toString().trim();
        String userPassword = Password.getText().toString().trim();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(userEmail, userPassword)
                .addOnCompleteListener(GetStarted.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = auth.getCurrentUser();
                            if (user != null) {



                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
                                DatabaseReference userReference = reference.child(user.getUid());

                                userReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            // Here we can assume the snapshot key is the UID
                                            Password.setError(null);


                                            String usernamefromDB = snapshot.child("username").getValue(String.class);
                                            String addressfromDB = snapshot.child("address").getValue(String.class);
                                            String emailfromDB = snapshot.child("email").getValue(String.class);
                                            Boolean isAdmin = snapshot.child("isAdmin").getValue(Boolean.class);


                                            //shared preferences
                                            SharedPreferences.Editor editor = sharedPreferences.edit();
                                            editor.putString(KEY_USERNAME, usernamefromDB);
                                            editor.putString(KEY_EMAIL, userEmail);
                                            editor.putString(KEY_PASSWORD, userPassword);
                                            editor.putBoolean(KEY_IS_ADMIN, isAdmin); // Store isAdmin status
                                            editor.apply();

                                            Toast.makeText(GetStarted.this, "Welcome " + usernamefromDB, Toast.LENGTH_LONG).show();

                                            // Redirect based on isAdmin value
                                            if (isAdmin != null && isAdmin) {
                                                // User is an admin
                                                Intent adminIntent = new Intent(GetStarted.this, ManageInventory.class);
                                                startActivity(adminIntent);
                                            } else {

                                                Intent intent = new Intent(GetStarted.this, Dashboard.class);
                                                intent.putExtra("username", usernamefromDB);
                                                intent.putExtra("address", addressfromDB);
                                                intent.putExtra("email", emailfromDB);
                                                startActivity(intent);
                                            }







                                        } else {
                                            // Handle the case where the user data does not exist
                                            Toast.makeText(GetStarted.this, "No such User exists", Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // Handle error
                                    }
                                });
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                Password.setError("Invalid Credentials");
                                Password.requestFocus();
                            } else if (task.getException() instanceof FirebaseAuthInvalidUserException) {
                                Email.setError("No such User exists");
                                Email.requestFocus();
                            } else {
                                Toast.makeText(GetStarted.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });


    }
    //Waleed Adnan
//Check if user is already logged in
//    @Override
//    protected void onStart() {
//        super.onStart();
//        FirebaseUser currentUser = authProfile.getCurrentUser();
//        if (currentUser != null) {
//            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUser.getUid());
//            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        Boolean isAdmin = snapshot.child("isAdmin").getValue(Boolean.class);
//                        if (isAdmin != null && isAdmin) {
//                            // User is an admin, direct to admin activity
//                            Intent adminIntent = new Intent(GetStarted.this, ManageInventory.class);
//                            startActivity(adminIntent);
//                        } else {
//                            // User is not an admin, proceed to regular user dashboard
//                            Intent userIntent = new Intent(GetStarted.this, Dashboard.class);
//                            startActivity(userIntent);
//                        }
//                    } else {
//                        // Handle the case where user data does not exist
//                        Toast.makeText(GetStarted.this, "No user data found", Toast.LENGTH_SHORT).show();
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//                    // Handle error
//                }
//            });
//        } else {
//            // No user is signed in, proceed with normal flow
//            Toast.makeText(this, "Please Login", Toast.LENGTH_SHORT).show();
//        }
//    }

}