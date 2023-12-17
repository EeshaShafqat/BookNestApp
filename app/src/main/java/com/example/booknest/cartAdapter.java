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
import java.util.List;

public class cartAdapter extends RecyclerView.Adapter<cartAdapter.MyViewHolder>{

    Context context;
    List<bestDealModel> cartList;

    private OnCountChangeListener countChangeListener;
    public interface OnCountChangeListener {
        void onCountChanged();
    }

    public cartAdapter(Context context, List<bestDealModel> cartList, OnCountChangeListener countChangeListener) {
        this.context = context;
        this.cartList = cartList;
        this.countChangeListener = countChangeListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(context).inflate(R.layout.add_to_cart_card,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.bookAuthor.setText(cartList.get(position).getBookAuthor());
        holder.bookCategory.setText(cartList.get(position).getBookCategory());
        holder.bookTitle.setText(cartList.get(position).getBookTitle());
        holder.bookPrice.setText(cartList.get(position).getBookPrice());
        holder.bookCount.setText(String.valueOf(cartList.get(position).getCount()));

        // String url = bestDealModelList.get(position).getImgUrl();
        String imagePath = cartList.get(position).getImgUrl();
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

//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED) {
//            // Permission granted, load the image
//
//
//
//            File imgFile = new File(imagePath);
//            Picasso.get().load(imgFile).into(holder.img, new Callback() {
//                @Override
//                public void onSuccess() {
//                    int imgWidth = holder.img.getWidth();
//                    int imgHeight = holder.img.getHeight();
//                    Log.d("ImageDimensions", "Image width: " + imgWidth + ", Image height: " + imgHeight);
//                }
//
//                @Override
//                public void onError(Exception e) {
//                    Log.e("ImageLoadError", "Error loading image: " + e.getMessage());
//                }
//            });
//
//
//
//
//        } else {
//            // Permission not granted, handle accordingly (show placeholder or take action)
//            holder.img.setImageResource(R.drawable.dorian);
//        }


        // Manually set click listeners for plus and minus buttons
        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                increaseItemCount(position);
            }
        });

        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                decreaseItemCount(position);
            }
        });

        holder.remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeItem(position);
            }
        });



    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder  {

        TextView bookCount, bookCategory,bookTitle,bookAuthor,bookPrice;
        ImageView img, plus, minus, remove ;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            bookCount = itemView.findViewById(R.id.count);
            bookCategory = itemView.findViewById(R.id.category);
            bookTitle = itemView.findViewById(R.id.title);
            bookAuthor = itemView.findViewById(R.id.author);
            bookPrice = itemView.findViewById(R.id.price);
            img = itemView.findViewById(R.id.imageView);
            plus = itemView.findViewById(R.id.plus);
            minus = itemView.findViewById(R.id.minus);
            remove = itemView.findViewById(R.id.remove);



        }

    }

    private void increaseItemCount(int position) {
        // Handle incrementing the item count
        int count = cartList.get(position).getCount();
        count++;
        cartList.get(position).setCount(count);
        notifyItemChanged(position);
        if (countChangeListener != null) {
            countChangeListener.onCountChanged();
        }
    }

    private void decreaseItemCount(int position) {
        // Handle decrementing the item count
        int count = cartList.get(position).getCount();
        if (count > 0) {
            count--;
            cartList.get(position).setCount(count);
            notifyItemChanged(position);
            if (countChangeListener != null) {
                countChangeListener.onCountChanged();
            }
        }
    }


    private void removeItem(int position) {
        cartList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, cartList.size());

        if (countChangeListener != null) {
            countChangeListener.onCountChanged();
        }
    }
}
