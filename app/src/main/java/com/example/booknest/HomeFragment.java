package com.example.booknest;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.appcompat.widget.SearchView;


import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    Button weekButton, monthButton, yearButton ;




    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

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



        //Best Deals Recycler View

        RecyclerView rv1 = view.findViewById(R.id.box1);

        List<bestDealModel> bestDealModelList = new ArrayList<>();
        bestDealModelList.add(new bestDealModel("Novel","Tuesday Mooney Talks to Ghosts","Kate Racculia", "Rs 3300",12));
        bestDealModelList.add(new bestDealModel("Novel","What should be wild","Julia Fine", "Rs 4500",18));

        bestDealAdapter adapter = new bestDealAdapter(requireContext(), bestDealModelList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv1.setLayoutManager(layoutManager);
        rv1.setAdapter(adapter);


        //Top Books Recycler View

        RecyclerView rv2 = view.findViewById(R.id.box2);

        List<bestDealModel> topBooksList = new ArrayList<>();
        topBooksList.add(new bestDealModel("Classics","The Picture of Dorian Gray","Oscar Wilde", "Rs 2500"));
        topBooksList.add(new bestDealModel("Classics","The Catcher in the Rye","J.D.Salinger", "Rs 3000"));

        bookAdapter adapter2 = new bookAdapter(requireContext(), topBooksList);

        LinearLayoutManager layoutManager2 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv2.setLayoutManager(layoutManager2);
        rv2.setAdapter(adapter2);



        //Latest Books Recycler View

        RecyclerView rv3 = view.findViewById(R.id.box3);

        List<bestDealModel> latestBooksList = new ArrayList<>();
        latestBooksList.add(new bestDealModel("Young Adults","Nine Liars","Maureen Johnson", "Rs 1600"));
        latestBooksList.add(new bestDealModel("Fantasy","Sorrow and Starlight","Caroline Meckham, Susanne Valenti", "Rs 3000"));

        bookAdapter adapter3 = new bookAdapter(requireContext(),  latestBooksList);

        LinearLayoutManager layoutManager3 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv3.setLayoutManager(layoutManager3);
        rv3.setAdapter(adapter3);


        //Upcoming Books Recycler View

        RecyclerView rv4 = view.findViewById(R.id.box4);

        List<bestDealModel> upcomingBooksList = new ArrayList<>();
        upcomingBooksList.add(new bestDealModel("Fantasy","Queen of Myth and Monsters","Scarlet St Clair", "Rs 3000"));
        upcomingBooksList.add(new bestDealModel("Fantasy","Sorrow and Starlight","Caroline Meckham, Susanne Valenti", "Rs 3000"));

        bookAdapter adapter4 = new bookAdapter(requireContext(),  upcomingBooksList);

        LinearLayoutManager layoutManager4 = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        rv4.setLayoutManager(layoutManager4);
        rv4.setAdapter(adapter4);




        return view;

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
    }


}