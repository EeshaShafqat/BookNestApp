package com.example.booknest;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class CheckoutFragment extends Fragment {


    Button btnAddAddress;
    private EditText etNewAddress;
    private RelativeLayout addressBox;
    Button btnSaveAddress;


    RelativeLayout rlCreditCard, rlCashOnDelivery;
    ImageView ivCreditCard, ivCashOnDelivery;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_checkout, container, false);

        // Initialize Views
        btnAddAddress = view.findViewById(R.id.btnAddAddress);
        etNewAddress = view.findViewById(R.id.etNewAddress);
        addressBox = view.findViewById(R.id.addressBox);
        btnSaveAddress = view.findViewById(R.id.save);


        TextView tvAddress = view.findViewById(R.id.tvAddress);

        // Set initial visibility
        btnAddAddress.setVisibility(View.VISIBLE);

        btnSaveAddress.setVisibility(View.INVISIBLE);
        etNewAddress.setVisibility(View.INVISIBLE);

        addressBox.setVisibility(View.GONE);


        // Set Click Listener for "Add a New Delivery Address" button
        btnAddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Toggle visibility of Views
                etNewAddress.setVisibility(View.VISIBLE);
                btnSaveAddress.setVisibility(View.VISIBLE);
            }
        });


        // Set Click Listener for Save button

        btnSaveAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the entered address from the EditText
                String newAddress = etNewAddress.getText().toString();

                if(newAddress.isEmpty()){
                    etNewAddress.setError("Please enter your address");
                    etNewAddress.requestFocus();
                    return;
                }

                else{

                    // Update the TextView in the chocolate-colored box with the new address

                    tvAddress.setText(newAddress);

                    // Toggle visibility of Views
                    btnAddAddress.setVisibility(View.VISIBLE);

                    etNewAddress.setVisibility(View.INVISIBLE);
                    btnSaveAddress.setVisibility(View.INVISIBLE);

                    addressBox.setVisibility(View.VISIBLE);

                }


            }
        });


        //make payment
        Button makePayment = view.findViewById(R.id.makePayment);

        makePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(requireContext(),PaymentReceived.class);
                startActivity(intent);

            }
        });

        //switch button check

       rlCreditCard = view.findViewById(R.id.rlCreditCard);
       rlCashOnDelivery = view.findViewById(R.id.rlCashOnDelivery);

       //images
         ivCreditCard = view.findViewById(R.id.ivCreditCard);
         ivCashOnDelivery = view.findViewById(R.id.ivCashOnDelivery);


       rlCreditCard.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               // Set background for all buttons to the default state
               ivCreditCard.setImageResource(R.drawable.filledcircle);
               ivCashOnDelivery.setImageResource(R.drawable.emptycircle);
           }
       });


           rlCashOnDelivery.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {

                   ivCreditCard.setImageResource(R.drawable.emptycircle);
                   ivCashOnDelivery.setImageResource(R.drawable.filledcircle);
               }
           });



        return view;
    }


}