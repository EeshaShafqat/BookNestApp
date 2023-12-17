package com.example.booknest;


import android.Manifest;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {


    Button weekButton, monthButton, yearButton;

    //Waleed Adnan
    private TextView TextViewHomeUsername;
    private FirebaseAuth authProfile;
    private String Name;


    List<bestDealModel> bookList;
    List<bestDealModel> bestDealModelList,  topBooksList , latestBooksList ,upcomingBooksList;
    List<bestDealModel> filteredTopBooks;
    RecyclerView rv1,rv2,rv3,rv4;
    bestDealAdapter adapter;
    bookAdapter adapter2;
    bookAdapter2 adapter3;
    bookAdapter3 adapter4;

    private static final int READ_REQUEST_CODE = 100;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        TextViewHomeUsername = view.findViewById(R.id.TextViewHomeUsername);

        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(getActivity(), "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();

        } else {
            showUserProfile(firebaseUser);
        }


        SearchView searchView = view.findViewById(R.id.searchview);
        searchView.setQueryHint("Happy Reading");


        //buttons
        weekButton = view.findViewById(R.id.week);
        monthButton = view.findViewById(R.id.month);
        yearButton = view.findViewById(R.id.year);

        // Set click listeners for the buttons
        weekButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(weekButton);
            }
        });

        monthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(monthButton);
            }
        });

        yearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleButtonClick(yearButton);
            }
        });


        //*******************************************************************************************

        filteredTopBooks = new ArrayList<>();

        // Separate bookList into different lists based on rvType
        bestDealModelList = new ArrayList<>();
        topBooksList = new ArrayList<>();
        latestBooksList = new ArrayList<>();
        upcomingBooksList = new ArrayList<>();

        // Best Deals Recycler View
        rv1 = view.findViewById(R.id.box1);

        //Top Books Recycler View
        rv2 = view.findViewById(R.id.box2);

        //Latest Books Recycler View
        rv3 = view.findViewById(R.id.box3);

        //Upcoming Books Recycler View
        rv4 = view.findViewById(R.id.box4);

        //*******************************************************************************************



        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    READ_REQUEST_CODE);
        }

       // displayToastWithListContents(bookList) ;



        adapter = new bestDealAdapter(requireContext(), bestDealModelList);
        rv1.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv1.setLayoutManager(layoutManager);

        // Initialize adapters for rv2, rv3, rv4
        Log.d("SetupRV", "Setting adapter2 to rv2");
        adapter2 = new bookAdapter(requireContext(), filteredTopBooks);
        rv2.setAdapter(adapter2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv2.setLayoutManager(layoutManager2);

        Log.d("SetupRV", "Setting adapter3 to rv3");
        adapter3 = new bookAdapter2(requireContext(), latestBooksList);
        rv3.setAdapter(adapter3);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv3.setLayoutManager(layoutManager3);

        Log.d("SetupRV", "Setting adapter4 to rv4");
        adapter4 = new bookAdapter3(requireContext(), upcomingBooksList);
        rv4.setAdapter(adapter4);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv4.setLayoutManager(layoutManager4);


        if (bookList != null && !bookList.isEmpty()) {
            // Clear existing data lists before repopulating them
            bestDealModelList.clear();
            topBooksList.clear();
            latestBooksList.clear();
            upcomingBooksList.clear();

            for (bestDealModel book : bookList) {
                String rvType = book.getRvType();
                if (rvType.equals("top")) {
                    Log.d("UpdateUI", "Adding to topBooksList: " + book.getBookTitle());
                    topBooksList.add(book);
                } else if (rvType.equals("latest")) {
                    Log.d("UpdateUI", "Adding to latestBooksList: " + book.getBookTitle());
                    latestBooksList.add(book);
                } else if (rvType.equals("upcoming")) {
                    Log.d("UpdateUI", "Adding to upcomingBooksList: " + book.getBookTitle());
                    upcomingBooksList.add(book);
                } else if (rvType.equals("best")) {
                    Log.d("UpdateUI", "Adding to bestDealModelList: " + book.getBookTitle());
                    bestDealModelList.add(book);
                }
            }

            // Notify data set changed for each adapter
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }


            if (adapter2 != null) {
                adapter2.notifyDataSetChanged();
            }

            if (adapter3 != null) {
                adapter3.notifyDataSetChanged();
            }


            if (adapter4 != null) {
                adapter4.notifyDataSetChanged();
            }


            // setupRecyclerView();


        } else {
            Toast.makeText(requireContext(), "Failed to update", Toast.LENGTH_SHORT).show();
        }

        filterTopBooksListByTime("week");



        return view;

    }


    private void showUserProfile(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        Toast.makeText(requireContext(), userID, Toast.LENGTH_SHORT).show();

        // Retrieve the intent that started this activity
        Intent intent = requireActivity().getIntent();

        if (intent.hasExtra("username")) {
            // Get the username passed through intent
            Name  = intent.getStringExtra("username");

            // Now you can use this username as needed in your activity

            TextViewHomeUsername.setText(Name);
        }





//        DatabaseReference referenceProfile = FirebaseDatabase.getInstance().getReference("users");
//        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                ReadWriteUserDetails read_user_details = snapshot.getValue(ReadWriteUserDetails.class);
//
//                if (read_user_details != null) {
//                    //Toast.makeText(getActivity(), "User's details are available", Toast.LENGTH_LONG).show();
//                    Name = read_user_details.getUsername();
//                    TextViewHomeUsername.setText(Name);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(getActivity(), "Something went wrong! User's details are not available at the moment", Toast.LENGTH_LONG).show();
//            }
//        });
    }


    // Method to handle button click and update their checked state
    @SuppressLint("ResourceAsColor")
    private void handleButtonClick(Button clickedButton) {
        // Set background for all buttons to the default state
        weekButton.setBackgroundResource(R.drawable.button2_bg);
        weekButton.setTextColor(getResources().getColor(R.color.black));

        monthButton.setBackgroundResource(R.drawable.button2_bg);
        monthButton.setTextColor(getResources().getColor(R.color.black));

        yearButton.setBackgroundResource(R.drawable.button2_bg);
        yearButton.setTextColor(getResources().getColor(R.color.black));

        // Set background for the clicked button to the checked state
        clickedButton.setBackgroundResource(R.drawable.button_bg);
        clickedButton.setTextColor(getResources().getColor(R.color.white));

        // Filter topBooksList based on byTime attribute and update rv2
        String selectedTime = "";
        if (clickedButton == weekButton) {
            selectedTime = "week";
        } else if (clickedButton == monthButton) {
            selectedTime = "month";
        } else if (clickedButton == yearButton) {
            selectedTime = "year";
        }

        filterTopBooksListByTime(selectedTime);
    }

    //********************************************************************* */







    // Handle permission request result
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == READ_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                Toast.makeText(requireActivity(), "Permission granted", Toast.LENGTH_SHORT).show();

            } else {
                // Permission denied, handle accordingly (show a message or take action)
                Toast.makeText(requireActivity(), "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }




    private void setupRecyclerView() {
        // Initialize the adapter with an empty list initially

        adapter = new bestDealAdapter(requireContext(), bestDealModelList);
        rv1.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv1.setLayoutManager(layoutManager);

        // Initialize adapters for rv2, rv3, rv4
        Log.d("SetupRV", "Setting adapter2 to rv2");
        adapter2 = new bookAdapter(requireContext(), topBooksList);
        rv2.setAdapter(adapter2);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv2.setLayoutManager(layoutManager2);

        Log.d("SetupRV", "Setting adapter3 to rv3");
        adapter3 = new bookAdapter2(requireContext(), latestBooksList);
        rv3.setAdapter(adapter3);
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv3.setLayoutManager(layoutManager3);

        Log.d("SetupRV", "Setting adapter4 to rv4");
        adapter4 = new bookAdapter3(requireContext(), upcomingBooksList);
        rv4.setAdapter(adapter4);
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv4.setLayoutManager(layoutManager4);
    }



    public void setBookList(List<bestDealModel> bookList) {
        this.bookList = bookList;

    }

    private void displayToastWithListContents(List<bestDealModel> list) {

        if (list != null) {


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

            Toast.makeText(requireActivity(), toastMessage.toString(), Toast.LENGTH_LONG).show();

        } else {
            // Handle the case where the list is null
            Toast.makeText(requireActivity(), "List is null", Toast.LENGTH_SHORT).show();
        }

    }

    // Method to filter topBooksList by byTime attribute
    private void filterTopBooksListByTime(String selectedTime) {
        filteredTopBooks.clear(); // Clear previous data

        if (selectedTime.isEmpty()) {
            selectedTime = "week"; // Set default filter to "This Week"
        }

        for (bestDealModel book : topBooksList) {
            if (book.getByTime().equals(selectedTime)) {
                filteredTopBooks.add(book);
            }
        }

        // Update rv2 with the filtered list
        adapter2.notifyDataSetChanged();
    }

}
