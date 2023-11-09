package com.example.booknest;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


public class AccountFragment extends Fragment {

    Button Edit,Logout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        Edit = view.findViewById(R.id.Edit);
        Logout = view.findViewById(R.id.Logout);

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Edit.setBackgroundResource(R.drawable.button_bg);
                Edit.setTextColor(getResources().getColor(R.color.white));

                Logout.setBackgroundResource(R.drawable.button2_bg);
                Logout.setTextColor(getResources().getColor(R.color.black));


            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Logout.setBackgroundResource(R.drawable.button_bg);
                Logout.setTextColor(getResources().getColor(R.color.white));

                Edit.setBackgroundResource(R.drawable.button2_bg);
                Edit.setTextColor(getResources().getColor(R.color.black));
            }
        });

        return view;
    }
}