package com.example.booknest;

import android.os.Bundle;
import androidx.core.content.ContextCompat;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public class CategoryFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_category, container, false);

        SearchView searchView = view.findViewById(R.id.searchView);
        searchView.setQueryHint("Search title/author/ISBN no");

// Get the EditText within the SearchView
        EditText searchEditText = searchView.findViewById(androidx.appcompat.R.id.search_src_text);

// Set the text color to black
        searchEditText.setTextColor(ContextCompat.getColor(requireContext(), R.color.black));

// Set the query hint text color
        searchEditText.setHintTextColor(ContextCompat.getColor(requireContext(), R.color.black));




        RecyclerView rv = view.findViewById(R.id.recyclerView);

        List<String> ls = new ArrayList<>();
        ls.add(new String("Non-Fiction"));
        ls.add(new String("Classics"));
        ls.add(new String("Fantasy"));
        ls.add(new String("Romance"));
        ls.add(new String("Thriller"));
        ls.add(new String("Mystery"));
        ls.add(new String("Horror"));
        ls.add(new String("Biography"));
        ls.add(new String("Science Fiction"));
        ls.add(new String("Historical Fiction"));
        ls.add(new String("Young Adult"));

        CategoryAdapter adapter = new CategoryAdapter(requireContext(), ls);

        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);

        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);



        //code for filter

        ImageView filterIcon = view.findViewById(R.id.filterIcon);
        filterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFilterDialog();
            }
        });




        return view;

    }


    private void showFilterDialog() {
        FilterDialogFragment filterDialogFragment = new FilterDialogFragment();
        filterDialogFragment.show(getParentFragmentManager(), "FilterDialog");
    }





}