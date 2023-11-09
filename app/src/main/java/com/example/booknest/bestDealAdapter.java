package com.example.booknest;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class bestDealAdapter extends RecyclerView.Adapter<bestDealAdapter.MyViewHolder> {

    static Context context;
    static List<bestDealModel> bestDealModelList;
    public bestDealAdapter(Context context, List<bestDealModel> bestDealModelList) {
        this.context = context;
        this.bestDealModelList = bestDealModelList;

    }

    @NonNull
    @Override
    public bestDealAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(context).inflate(R.layout.best_deal_card,parent,false);
        return new bestDealAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull bestDealAdapter.MyViewHolder holder, int position) {


        holder.bookCategory.setText(bestDealModelList.get(position).getBookCategory());
        holder.bookTitle.setText(bestDealModelList.get(position).getBookTitle());
        holder.bookAuthor.setText(bestDealModelList.get(position).getBookAuthor());
        holder.bookPrice.setText(bestDealModelList.get(position).getBookPrice());
        holder.bookDiscount.setText(String.valueOf(bestDealModelList.get(position).getDiscount()));

        // Set OnClickListener for the item
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Replace the fragment and pass data to the new fragment
                Fragment newFragment = new ProductFragment();

                Bundle bundle = new Bundle();
                bundle.putString("category", bestDealModelList.get(position).getBookCategory());
                bundle.putString("title", bestDealModelList.get(position).getBookTitle());
                bundle.putString("author", bestDealModelList.get(position).getBookAuthor());
                bundle.putString("price", String.valueOf(bestDealModelList.get(position).getBookPrice()));

                newFragment.setArguments(bundle);

                FragmentTransaction transaction = ((FragmentActivity) context).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragmentContainerView, newFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });




    }

    @Override
    public int getItemCount() {
        return bestDealModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bookCategory, bookTitle, bookAuthor,  bookPrice, bookDiscount;
        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            bookCategory = itemView.findViewById(R.id.category);
            bookTitle = itemView.findViewById(R.id.title);
            bookAuthor = itemView.findViewById(R.id.author);
            bookPrice = itemView.findViewById(R.id.price);
            bookDiscount = itemView.findViewById(R.id.discount);

        }
    }
}
