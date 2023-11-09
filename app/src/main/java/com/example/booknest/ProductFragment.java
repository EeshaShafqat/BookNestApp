package com.example.booknest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ProductFragment extends Fragment {

    private static final String ARG_CATEGORY = "category";
    private static final String ARG_TITLE = "title";
    private static final String ARG_AUTHOR = "author";
    private static final String ARG_PRICE = "price";

    private String category;
    private String title;
    private String author;
    private String price;

    public ProductFragment() {
        // Required empty public constructor
    }

    public static ProductFragment newInstance(String category, String title, String author, String price) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_AUTHOR, author);
        args.putString(ARG_PRICE, price);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            category = getArguments().getString(ARG_CATEGORY);
            title = getArguments().getString(ARG_TITLE);
            author = getArguments().getString(ARG_AUTHOR);
            price = getArguments().getString(ARG_PRICE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        // Set values to TextViews
        TextView categoryTextView = view.findViewById(R.id.getCategory);
        TextView titleTextView = view.findViewById(R.id.heading);
        TextView authorTextView = view.findViewById(R.id.getAuthor);
        TextView priceTextView = view.findViewById(R.id.getPrice);

        //rating needs to be passed too

        categoryTextView.setText(category);
        titleTextView.setText(title);
        authorTextView.setText(author);
        priceTextView.setText(price);

        return view;
    }
}
