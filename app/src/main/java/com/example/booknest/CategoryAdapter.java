package com.example.booknest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {

    Context context;
    List<String> ls;
    public CategoryAdapter(Context context, List<String> ls) {
        this.context = context;
        this.ls = ls;

    }

    @NonNull
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(context).inflate(R.layout.category_card,parent,false);
        return new CategoryAdapter.MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryAdapter.MyViewHolder holder, int position) {

        holder.CategoryName.setText(ls.get(position));

    }

    @Override
    public int getItemCount() {
        return ls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView CategoryName;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            CategoryName = itemView.findViewById(R.id.categoryName);

        }
    }
}
