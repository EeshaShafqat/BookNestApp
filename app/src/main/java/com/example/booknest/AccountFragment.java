package com.example.booknest;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;


public class AccountFragment extends Fragment {

    //Waleed Adnan
    Button Edit,Logout;

    TextView Username, Email, Address, Password;

    private FirebaseAuth auth;
    FirebaseUser firebaseUser;
    ImageView profilePic;

    String profilepicURL;
    private static final int DP_REQUEST_CODE = 110; // Request code for image picker

    Uri selectedImg;
    String userID;
    FirebaseStorage storage;
    StorageReference storageRef;

    SharedPreferences sharedPreferences;
    private static final String USER_PREFS = "UserPrefs";
    String photoUrl;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        Edit = view.findViewById(R.id.Edit);
        Logout = view.findViewById(R.id.Logout);

        Username = view.findViewById(R.id.name);
        Email = view.findViewById(R.id.email);
        Address = view.findViewById(R.id.address);
        Password = view.findViewById(R.id.password);
        profilePic = view.findViewById(R.id.account_circle);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();
        userID = firebaseUser.getUid();

        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        sharedPreferences = requireContext().getSharedPreferences(USER_PREFS, Context.MODE_PRIVATE);

        if (isNetworkAvailable()) {
            showAllUserData();
        } else {
            showCachedUserData();
        }



        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Edit.setBackgroundResource(R.drawable.button_bg);
                Edit.setTextColor(getResources().getColor(R.color.white));

                Logout.setBackgroundResource(R.drawable.button2_bg);
                Logout.setTextColor(getResources().getColor(R.color.black));


                String username = Username.getText().toString();
                String email = Email.getText().toString();
                String password = Password.getText().toString();
                String address =  Address.getText().toString();

                editUserData(username,email,address,password,profilepicURL);

                if(profilepicURL != null && !profilepicURL.isEmpty()) {
                    uploadImageToFirebase(selectedImg);
                }


            }
        });

        TextView addTextView = view.findViewById(R.id.heading);
        addTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(requireContext(), insertBooks.class);
                startActivity(intent);
            }
        });
        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logout.setBackgroundResource(R.drawable.button_bg);
                Logout.setTextColor(getResources().getColor(R.color.white));

                Edit.setBackgroundResource(R.drawable.button2_bg);
                Edit.setTextColor(getResources().getColor(R.color.black));

                if (firebaseUser != null) {
                    FirebaseAuth.getInstance().signOut();
                }

                // Clear shared preferences
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                // Move to WelcomeScreen
                startActivity(new Intent(getActivity(), WelcomeScreen.class));
                requireActivity().finish();
            }
        });



        // photo from gallery
        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, DP_REQUEST_CODE);
            }
        });



        return view;
    }


    private void showAllUserData () {


        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("users");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails read_user_details = snapshot.getValue(ReadWriteUserDetails.class);

                if(read_user_details != null){
                    //Toast.makeText(getActivity(), "User's details are available", Toast.LENGTH_LONG).show();
                    String name = read_user_details.getUsername();
                    Username.setText(name);

                    String email = read_user_details.getEmail();
                    Email.setText(email);

                    String address = read_user_details.getAddress();
                    Address.setText(address);

                    String password = read_user_details.getPassword();
                    Password.setText(password);

                    // Get the reference to the user's image in Firebase Storage
                    StorageReference usersRef = storageRef.child("images/users/" + userID + "/profile");

                    usersRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            photoUrl = uri.toString();
                            Picasso.get()
                                   .load(photoUrl)
                                   .placeholder(R.drawable.account_circle)
                                   .error(R.drawable.arrow_back)
                                   .into(profilePic);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Handle failure to retrieve the image URL
                            Toast.makeText(getActivity(), "Failed to retrieve image", Toast.LENGTH_SHORT).show();
                        }
                    });

                    // Cache data locally using SharedPreferences
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", read_user_details.getUsername());
                    editor.putString("email", read_user_details.getEmail());
                    editor.putString("address", read_user_details.getAddress());
                    editor.putString("password", read_user_details.getPassword());
                    editor.putString("profilepicURL", photoUrl);
                    editor.apply();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
            }
        });
    }


    //eesha
    private void editUserData (String username, String email, String address, String password, String profilepicURL){


        ReadWriteUserDetails writeUserDetails = new ReadWriteUserDetails(username, email, address, password,profilepicURL);

        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("users");
        referenceProfile.child(userID).setValue(writeUserDetails)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(requireContext(), "Your Profile is Updated", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(requireContext(), "Error updating profile", Toast.LENGTH_LONG).show();
                    }
                });


    }

     public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);


        if (requestCode == DP_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            selectedImg = data.getData();
            profilepicURL = selectedImg.toString();

            //Toast.makeText(requireContext(),profilepicURL,Toast.LENGTH_SHORT).show();
            //set dp
            Picasso.get().load(profilepicURL).into(profilePic);


        }

    }

    private void uploadImageToFirebase(Uri selectedImg) {


        // Create a reference to the image in Firebase Storage
        StorageReference usersRef = storageRef.child("images/users/" + userID + "/profile");

        // Upload the image to Firebase Storage
        usersRef.putFile(selectedImg)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        // Get the download URL of the uploaded image
                        usersRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                profilepicURL = uri.toString();
                                // Update user details including the image URL
                                editUserData(Username.getText().toString(), Email.getText().toString(),
                                        Address.getText().toString(), Password.getText().toString(),
                                        profilepicURL);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Handle unsuccessful uploads
                        Toast.makeText(requireContext(), "Failed to upload image", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showCachedUserData() {
        String username = sharedPreferences.getString("username", "");
        String email = sharedPreferences.getString("email", "");
        String address = sharedPreferences.getString("address", "");
        String password = sharedPreferences.getString("password", "");
        String profilepicURL = sharedPreferences.getString("profilepicURL", "");

        // Use cached data to set UI components...
        Username.setText(username);
        Email.setText(email);
        Address.setText(address);
        Password.setText(password);

        // Load profile pic using Picasso or any other method
        // Picasso.get().load(profilepicURL).into(profilePic);
    }

    private boolean isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) requireContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()){
            return true;
        }
        else return false;
    }

}