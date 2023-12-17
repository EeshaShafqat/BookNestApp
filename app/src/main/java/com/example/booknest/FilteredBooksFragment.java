package com.example.booknest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class FilteredBooksFragment extends Fragment {

    RecyclerView box;

    public List<bestDealModel> bookList;
    String selectedCategory;

    TextView category;


    public FilteredBooksFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filtered_books, container, false);

        box = view.findViewById(R.id.box);
        category = view.findViewById(R.id.category);

        category.setText(selectedCategory);

        // Set up RecyclerView with adapter
        bookAdapter adapter = new bookAdapter(requireContext(),bookList); // Create your BookAdapter with bookList
        box.setLayoutManager(new LinearLayoutManager(getContext()));
        box.setAdapter(adapter);


        return view;

    }

    public void setBooks(List<bestDealModel> filteredList, String selectedCategory){
        this.bookList = filteredList;
        this.selectedCategory = selectedCategory;

        if(box != null) {
            bookAdapter adapter = new bookAdapter(requireContext(),bookList); // Create your BookAdapter with bookList
            box.setAdapter(adapter);
        }


    }
}