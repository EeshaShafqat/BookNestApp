package com.example.booknest;

import android.os.Bundle;
import androidx.core.content.ContextCompat;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CategoryFragment extends Fragment implements CategoryAdapter.OnItemClickListener {


    private List<bestDealModel> bookList;
    RecyclerView rv;

    List<String> categories;

    private CategoryAdapter adapter;

    String selectedCategory;

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




            rv= view.findViewById(R.id.recyclerView);





        if (bookList != null && !bookList.isEmpty()) {
            categories = extractCategories(bookList);

            if (categories != null && !categories.isEmpty()) {
                // Display categories as a toast message
                StringBuilder toastMessage = new StringBuilder("Categories: ");
                for (String category : categories) {
                    toastMessage.append(category).append(", ");
                }
                Toast.makeText(requireContext(), toastMessage.toString(), Toast.LENGTH_SHORT).show();

                // Update RecyclerView with categories
                adapter = new CategoryAdapter(requireContext(), categories);
                adapter.setListener(this);

                RecyclerView.LayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
                rv.setLayoutManager(layoutManager);
                rv.setAdapter(adapter);
            }

        }
//


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



    public void setBookList(List<bestDealModel> bookList) {
        this.bookList = bookList;

    }


    private List<String> extractCategories(List<bestDealModel> bookList) {
        List<String> categories = new ArrayList<>();
        for (bestDealModel book : bookList) {
            String category = book.getBookCategory();
            if (!categories.contains(category)) {
                categories.add(category);
                Log.d("CategoryFragment", "Added category: " + category);
            }
        }


        return categories;
    }


    @Override
    public void onItemClick(int position) {
        // Filter bookList based on selected category
        selectedCategory = categories.get(position);

        List<bestDealModel> filteredList = new ArrayList<>();
        for (bestDealModel book : bookList) {
            if (book.getBookCategory().equals(selectedCategory)) {
                filteredList.add(book);
            }
        }

        // Pass filtered list to a new fragment or activity to display
        if (!filteredList.isEmpty()) {
            // For example, open a new fragment passing the filtered list
            displayFilteredBooks(filteredList);
        } else {
            // Handle case where no books of this category exist
            Toast.makeText(requireContext(), "No books found for this category", Toast.LENGTH_SHORT).show();
        }
    }

    private void displayFilteredBooks(List<bestDealModel> filteredList) {
        List<bestDealModel> uniqueFilteredList = new ArrayList<>();

        // Iterate through each book in the filtered list
        for (bestDealModel book : filteredList) {
            boolean isDuplicate = false;

            // Check if the book title already exists in uniqueFilteredList
            for (bestDealModel uniqueBook : uniqueFilteredList) {
                if (uniqueBook.getBookTitle().equals(book.getBookTitle())) {
                    isDuplicate = true;
                    break; // Stop further checking if a duplicate is found
                }
            }

            // If the book title doesn't exist in the uniqueFilteredList, add it
            if (!isDuplicate) {
                uniqueFilteredList.add(book);
            }
        }

        // Pass the unique filtered list to the fragment for display
        if (!uniqueFilteredList.isEmpty()) {
            Fragment fragment = new FilteredBooksFragment();
            ((FilteredBooksFragment) fragment).setBooks(uniqueFilteredList, selectedCategory);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerView, fragment)
                    .addToBackStack(null)
                    .commit();
        } else {
            Toast.makeText(requireContext(), "No unique books found for this category", Toast.LENGTH_SHORT).show();
        }
    }







}