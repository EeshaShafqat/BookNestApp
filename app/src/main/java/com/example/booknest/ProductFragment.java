package com.example.booknest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class ProductFragment extends Fragment {

   bestDealModel book;

    private static final String ARG_CATEGORY = "category";
    private static final String ARG_TITLE = "title";
    private static final String ARG_AUTHOR = "author";
    private static final String ARG_PRICE = "price";

    private static final String ARG_IMGPATH = "imagePath";


    private String category;
    private String title;
    private String author;
    private String price;

    private String imagePath;

    Button addToCart;

    public ProductFragment() {
        // Required empty public constructor
    }

    public static ProductFragment newInstance(String category, String title, String author, String price, String imagePath) {
        ProductFragment fragment = new ProductFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CATEGORY, category);
        args.putString(ARG_TITLE, title);
        args.putString(ARG_AUTHOR, author);
        args.putString(ARG_PRICE, price);
        args.putString(ARG_IMGPATH, imagePath);
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
            imagePath = getArguments().getString(ARG_IMGPATH);
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
        ImageView leftImage = view.findViewById(R.id.leftImage);

        addToCart = view.findViewById(R.id.button);

        addToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Cart cart = Cart.getInstance();
                cart.addItem(book);
                Toast.makeText(requireContext(),"Added to Cart", Toast.LENGTH_LONG).show();

            }
        });

        //rating needs to be passed too

        categoryTextView.setText(category);
        titleTextView.setText(title);
        authorTextView.setText(author);
        priceTextView.setText(price);


        File imgFile = new File(imagePath);
        Picasso.get().load(imgFile).into(leftImage, new Callback() {
            @Override
            public void onSuccess() {

                Log.d("Success", "Loading image successful ");
            }

            @Override
            public void onError(Exception e) {
                Log.e("ImageLoadError", "Error loading image: " + e.getMessage());
            }
        });



        return view;
    }




    private File createFileFromInputStream(InputStream inputStream) {
        try {
            File file = new File(getContext().getCacheDir(), "temp_image.jpg");
            OutputStream outputStream = new FileOutputStream(file);
            byte[] buffer = new byte[4 * 1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    public void setBook(bestDealModel book) {
        this.book = book;


    }

}
