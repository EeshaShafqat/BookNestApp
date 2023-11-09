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

import com.example.booknest.databinding.BestDealCardBinding;

import java.util.ArrayList;
import java.util.List;


public class CartFragment extends Fragment {



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        RecyclerView rv = view.findViewById(R.id.recyclerView);

        List<bestDealModel> cartList = new ArrayList<>();
        cartList.add(new bestDealModel(1,"Novel","Tuesday Mooney Talks to Ghosts","Kate Racculia","Rs. 3300"));
        cartList.add(new bestDealModel(1,"Adult Narrative","Hello Dream","Cristina Camerena, Lady Desatia","Rs. 1000"));

        cartAdapter adapter = new cartAdapter(getContext(),cartList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(adapter);



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
}