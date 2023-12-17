package com.example.booknest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.booknest.databinding.BestDealCardBinding;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment implements cartAdapter.OnCountChangeListener{


    Cart cart;

    private int shippingCost = 250;

    // Display subtotal, shipping, and total in respective TextViews
    TextView tvSubtotal;
    TextView tvShipping;
    TextView tvTotal;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        RecyclerView rv = view.findViewById(R.id.recyclerView);


        cart = Cart.getInstance();

        cartAdapter adapter = new cartAdapter(getContext(),cart.getCartItems(),this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);

        // Display subtotal, shipping, and total in respective TextViews
        tvSubtotal = view.findViewById(R.id.getSubtotal);
        tvShipping = view.findViewById(R.id.getShipping);
        tvTotal = view.findViewById(R.id.getTotal);


        // Calculate total initially and set the values
        int[] prices = calculateTotalPrice(cart.getCartItems());
        int subtotal = prices[0];
        int totalPrice = prices[1];

        tvSubtotal.setText("Rs " + subtotal); // Update subtotal field
        tvShipping.setText("Rs " + shippingCost); // Set shipping cost
        tvTotal.setText("Rs " + totalPrice); // Update total field


        // Get the cartImage view
        ImageView cartImage = view.findViewById(R.id.cartImage);

        // Check if the cartList is empty and update visibility of cartImage
        if (cart.getCartItems().isEmpty()) {
            cartImage.setVisibility(View.VISIBLE);
        } else {
            cartImage.setVisibility(View.GONE);
        }




        //proceed to checkout

        Button checkoutBtn = view.findViewById(R.id.checkoutBtn);

        checkoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace current fragment with the new fragment
                CheckoutFragment newFragment = new CheckoutFragment();
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, newFragment);
                transaction.addToBackStack(null);  // Add to back stack to allow back navigation
                transaction.commit();
            }
        });




        return view;

    }


    // Function to calculate total price of items in the cart and include shipping cost
    private int[] calculateTotalPrice(List<bestDealModel> cartItems) {
        int totalPrice = 0;
        int subtotal = 0;

        for (bestDealModel item : cartItems) {
            String priceString = item.getBookPrice().replaceAll("\\D+", ""); // Remove non-digit characters
            int price = Integer.parseInt(priceString);
            int count = item.getCount();
            subtotal += (price * count);
        }

        // Calculate total cost (subtotal + shipping cost)
        totalPrice = subtotal + shippingCost;

        return new int[]{subtotal, totalPrice}; // Return both subtotal and total price
    }


    @Override
    public void onCountChanged() {
        int[] prices = calculateTotalPrice(cart.getCartItems());
        int subtotal = prices[0];
        int totalPrice = prices[1];


        tvSubtotal.setText("Rs " + subtotal); // Update subtotal field
        tvShipping.setText("Rs " + shippingCost); // Set shipping cost
        tvTotal.setText("Rs " + totalPrice); // Update total field

       // Toast.makeText(requireContext(), "Subtotal: " + subtotal + "\nTotal Price (including shipping): " + totalPrice, Toast.LENGTH_LONG).show();
    }
}