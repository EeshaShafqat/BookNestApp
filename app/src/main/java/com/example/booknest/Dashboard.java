package com.example.booknest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Dashboard extends AppCompatActivity {


    BottomNavigationView navbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);


        //switching fragments with click
        navbar = findViewById(R.id.bottom_navigation);

        navbar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment selected = null;
                int itemId = item.getItemId();

                if (itemId == R.id.home) {

                    selected = new HomeFragment();

                } else if (itemId == R.id.account) {

                    selected = new AccountFragment();

                } else if (itemId == R.id.categories) {

                    selected = new CategoryFragment();

                }else if (itemId == R.id.cart) {

                    selected = new CartFragment();
                }



                if(selected!=null){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainerView,selected).commit();
                }

                return true;

            }
        });






    }
}