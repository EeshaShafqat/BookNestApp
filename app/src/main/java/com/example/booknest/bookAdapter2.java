package com.example.booknest;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

public class bookAdapter2 extends RecyclerView.Adapter<bookAdapter2.MyViewHolder>{

    bestDealModel currentItem;
    static Context context;
    static List<bestDealModel> bestDealModelList;

    public bookAdapter2(Context context, List<bestDealModel> bestDealModelList) {
        this.context = context;
        this.bestDealModelList = bestDealModelList;


    }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View row = LayoutInflater.from(context).inflate(R.layout.books_card,parent,false);
            return new MyViewHolder(row);
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

            currentItem = bestDealModelList.get(position);

        holder.bookCategory.setText(bestDealModelList.get(position).getBookCategory());
        holder.bookTitle.setText(bestDealModelList.get(position).getBookTitle());
        holder.bookAuthor.setText(bestDealModelList.get(position).getBookAuthor());
        holder.bookPrice.setText(bestDealModelList.get(position).getBookPrice());


            // String url = bestDealModelList.get(position).getImgUrl();
            String imagePath = bestDealModelList.get(position).getImgUrl();
            Log.d("ImageUrl", "Image URL in adapter: " + imagePath); // Add this line to log the imgUrl
            // Load the image using Picasso

            if (imagePath != null && !imagePath.isEmpty()) {
                // Load image using Picasso or any other library from the file path
                File imgFile = new File(imagePath);
                Picasso.get().load(imgFile).into(holder.img);
                // Picasso.get().load(imagePath).into(holder.img);
            } else {
                // Handle the case where imagePath is null or empty
                holder.img.setImageResource(R.drawable.dorian);
                Toast.makeText(context, "placeholder image", Toast.LENGTH_SHORT).show();
            }

//            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
//                    == PackageManager.PERMISSION_GRANTED) {
//                // Permission granted, load the image
//
//
//
//                File imgFile = new File(imagePath);
//                Picasso.get().load(imgFile).into(holder.img, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        int imgWidth = holder.img.getWidth();
//                        int imgHeight = holder.img.getHeight();
//                        Log.d("ImageDimensions", "Image width: " + imgWidth + ", Image height: " + imgHeight);
//                    }
//
//                    @Override
//                    public void onError(Exception e) {
//                        Log.e("ImageLoadError", "Error loading image: " + e.getMessage());
//                    }
//                });
//
//
//
//
//            } else {
//                // Permission not granted, handle accordingly (show placeholder or take action)
//                holder.img.setImageResource(R.drawable.dorian);
//            }


            // Set OnClickListener for the item
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Replace the fragment and pass data to the new fragment
                    Fragment newFragment = new ProductFragment();
                    ((ProductFragment) newFragment).setBook(currentItem);
                    ((ProductFragment) newFragment).setBook(currentItem);

                    Bundle bundle = new Bundle();
                    bundle.putString("category", bestDealModelList.get(position).getBookCategory());
                    bundle.putString("title", bestDealModelList.get(position).getBookTitle());
                    bundle.putString("author", bestDealModelList.get(position).getBookAuthor());
                    bundle.putString("price", String.valueOf(bestDealModelList.get(position).getBookPrice()));
                    bundle.putString("imagePath", String.valueOf(bestDealModelList.get(position).getImgUrl()));

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

        TextView bookCategory, bookTitle, bookAuthor,  bookPrice;

        ImageView img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bookCategory = itemView.findViewById(R.id.category);
            bookTitle = itemView.findViewById(R.id.title);
            bookAuthor = itemView.findViewById(R.id.author);
            bookPrice = itemView.findViewById(R.id.price);

            img = itemView.findViewById(R.id.imageView);
        }
    }






}
