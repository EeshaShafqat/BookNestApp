package com.example.booknest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.MyViewHolder>{

    Context context;
    List<bestDealModel> cartList;

    public cartAdapter(Context context, List<bestDealModel> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public cartAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(context).inflate(R.layout.add_to_cart_card,parent,false);
        return new cartAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull cartAdapter.MyViewHolder holder, int position) {

        holder.bookAuthor.setText(cartList.get(position).getBookAuthor());
        holder.bookCategory.setText(cartList.get(position).getBookCategory());
        holder.bookTitle.setText(cartList.get(position).getBookTitle());
        holder.bookPrice.setText(cartList.get(position).getBookPrice());
        holder.bookCount.setText(String.valueOf(cartList.get(position).getCount()));


    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bookCount, bookCategory,bookTitle,bookAuthor,bookPrice;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bookCount = itemView.findViewById(R.id.count);
            bookCategory = itemView.findViewById(R.id.category);
            bookTitle = itemView.findViewById(R.id.title);
            bookAuthor = itemView.findViewById(R.id.author);
            bookPrice = itemView.findViewById(R.id.price);

        }
    }
}
