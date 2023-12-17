package com.example.booknest;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class Register extends AppCompatActivity {

    RelativeLayout signInPage;
    //Waleed Adnan
    ImageView BackButton;

    EditText Username, Email, Password, ConfirmPassword;

    EditText Address;
    Button Register_Button;
    private static final String TAG = "Register";

    FirebaseAuth auth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Toast.makeText(this, "You can register now", Toast.LENGTH_LONG).show();

        signInPage = findViewById(R.id.signInPage);

        //Waleed Adnan
        BackButton = findViewById(R.id.back);
        Username = findViewById(R.id.username);
        Email = findViewById(R.id.email);
        Password = findViewById(R.id.password);
        ConfirmPassword = findViewById(R.id.confirm_password);
        Address = findViewById(R.id.address);
        CheckBox checkBox = findViewById(R.id.adminCheckbox);

        Register_Button = findViewById(R.id.register_btn);

        Register_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = Username.getText().toString();
                String email = Email.getText().toString();
                String password = Password.getText().toString();
                String confirm_password = ConfirmPassword.getText().toString();
                String address =  Address.getText().toString();
                boolean isAdmin = checkBox.isChecked();

                if (username.isEmpty()){
                    Username.setError("Please enter your username");
                    Username.requestFocus();
                }
                else if (email.isEmpty()){
                    Email.setError("Please enter your email");
                    Email.requestFocus();
                }
                else if (password.isEmpty()){
                    Password.setError("Please enter your password");
                    Password.requestFocus();
                }
                else if (confirm_password.isEmpty()){
                    ConfirmPassword.setError("Please confirm your password");
                    ConfirmPassword.requestFocus();
                }

                else if (address.isEmpty()){
                    Address.setError("Please enter your address");
                    Address.requestFocus();
                }

                else if (!password.equals(confirm_password)){
                    ConfirmPassword.setError("Password does not match");
                    ConfirmPassword.requestFocus();

                    Password.clearComposingText();
                    ConfirmPassword.clearComposingText();

                }
                else if (password.length() < 6){
                    Password.setError("Weak Password! Password must be at least 6 characters");
                    Password.requestFocus();
                }

                else {
                    Register_User (username, email,address, password,isAdmin);
                }
            }
        });


        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, WelcomeScreen.class);
                startActivity(intent);
            }
        });


        signInPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, GetStarted.class);
                startActivity(intent);
            }
        });
    }


    //Waleed Adnan
    private void Register_User(String username, String email, String address, String password, Boolean isAdmin) {
        auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            try {
                                throw task.getException();
                            } catch (FirebaseAuthWeakPasswordException e) {
                                Password.setError("Weak Password! Password must be at least 6 characters");
                                Password.requestFocus();
                            } catch (FirebaseAuthInvalidCredentialsException e) {
                                Email.setError("Invalid email format");
                                Email.requestFocus();
                            } catch (FirebaseAuthUserCollisionException e) {
                                Email.setError("Email already in use");
                                Email.requestFocus();
                            } catch (Exception e) {
                                 Log.e(TAG, e.getMessage());
                                Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(Register.this, "User Registered Successfully", Toast.LENGTH_LONG).show();
                            FirebaseUser user = auth.getCurrentUser();

                            if(user != null) {

                                Toast.makeText(Register.this, "im in if condition", Toast.LENGTH_LONG).show();

                                // Update the constructor to include the address
                                ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(username, email, address, password,isAdmin);

                                DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("users");

                                referenceProfile.child(user.getUid()).setValue(writeUserDetails)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                if (isAdmin != null && isAdmin) {
                                                    // User is an admin, direct to admin activity
                                                    Intent adminIntent = new Intent(Register.this, ManageInventory.class);
                                                    startActivity(adminIntent);
                                                    finish();

                                                } else {
                                                    // User is not an admin, proceed to regular user dashboard
                                                    Intent intent = new Intent(Register.this, Dashboard.class);
                                                    intent.putExtra("username", username);
                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    startActivity(intent);
                                                    finish();
                                                }

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(Register.this, "User Profile Creation Failed", Toast.LENGTH_LONG).show();
                                            }
                                        });

//
                            }
                        }
                    }
                });
    }



}