package com.example.booknest;

import android.Manifest;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

public class viewBooksAdapter extends RecyclerView.Adapter<viewBooksAdapter.MyViewHolder> {


    bestDealModel currentItem;
    static Context context;
    static List<bestDealModel> bestDealModelList;
    public viewBooksAdapter(Context context, List<bestDealModel> bestDealModelList) {
        this.context = context;
        this.bestDealModelList = bestDealModelList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View row = LayoutInflater.from(context).inflate(R.layout.view_books_card,parent,false);
        return new MyViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {


        holder.bookCategory.setText(bestDealModelList.get(position).getBookCategory());
        holder.bookTitle.setText(bestDealModelList.get(position).getBookTitle());
        holder.bookAuthor.setText(bestDealModelList.get(position).getBookAuthor());
        holder.bookPrice.setText(bestDealModelList.get(position).getBookPrice());


        currentItem = bestDealModelList.get(position);


        //int imageResource = context.getResources().getIdentifier("image15", "drawable", context.getPackageName());
        //Picasso.get().load(imageResource).into(holder.img);

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

//
//        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
//                == PackageManager.PERMISSION_GRANTED) {
//            // Permission granted, load the image
//
//            if (imagePath != null && !imagePath.isEmpty()) {
//                // Load image using Picasso or any other library from the file path
//                // File imgFile = new File(imagePath);
//                // Picasso.get().load(imgFile).into(imageViewBookImage);
//                Picasso.get().load(imagePath).into(holder.img);
//            } else {
//                // Handle the case where imagePath is null or empty
//                Toast.makeText(context, "Image path is invalid", Toast.LENGTH_SHORT).show();
//            }
//
//
//        } else {
//            // Permission not granted, handle accordingly (show placeholder or take action)
//            Toast.makeText(context, "placeholder image", Toast.LENGTH_SHORT).show();
//            holder.img.setImageResource(R.drawable.dorian);
//        }



        // Set OnClickListener for the book
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editBook(position);
            }
        });

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteBook(position);
            }
        });





    }

    @Override
    public int getItemCount() {
        return bestDealModelList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView bookCategory, bookTitle, bookAuthor,  bookPrice, bookDiscount;
        ImageView img, editButton, deleteButton;
        public MyViewHolder(@NonNull View itemView) {

            super(itemView);

            bookCategory = itemView.findViewById(R.id.category);
            bookTitle = itemView.findViewById(R.id.title);
            bookAuthor = itemView.findViewById(R.id.author);
            bookPrice = itemView.findViewById(R.id.price);
            img = itemView.findViewById(R.id.imageView);
            editButton = itemView.findViewById(R.id.edit);
            deleteButton = itemView.findViewById(R.id.delete);

        }
    }


    private void editBook(int position) {
        // Handle incrementing the item count

//        bestDealModelList.get(position)
//        cartList.get(position).setCount(count);
//        notifyItemChanged(position);

    }

    private void deleteBook(int position) {
        // Get the book ID of the item to be deleted

        //delete from server
        String bookID = bestDealModelList.get(position).getBookID();
        new DeleteBookTask().execute(bookID);

        // Remove the item from the local SQLite database
        String bookName = bestDealModelList.get(position).getBookTitle(); // Fetch book name
        SqliteHelper sqliteHelper = new SqliteHelper(context);

        // Search for the book in the local database
        String localBookID = sqliteHelper.getBookIDByName(bookName);

        if ( bookID!=null && localBookID != null) {
            sqliteHelper.deleteBook(localBookID);

            // Remove the item from the RecyclerView and notify the adapter
            bestDealModelList.remove(position);
            notifyItemRemoved(position);
        } else {
            // Handle case where book is not found locally
            Toast.makeText(context, "Book not found in local database", Toast.LENGTH_SHORT).show();
        }
    }


     class DeleteBookTask extends AsyncTask<String, Void, String> {
         @Override
         protected String doInBackground(String... params) {
             if (params.length == 0) {
                 return null;
             }
             String bookID = params[0];
             try {
                 URL url = new URL("http://192.168.1.4/booknest/delete_book.php"); // Replace with your PHP script URL
                 HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                 conn.setRequestMethod("POST");
                 conn.setDoOutput(true);

                 OutputStream outputStream = conn.getOutputStream();
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                 String postParams = "bookID=" + URLEncoder.encode(bookID, "UTF-8");
                 writer.write(postParams);
                 writer.flush();
                 writer.close();
                 outputStream.close();

                 InputStream inputStream = conn.getInputStream();
                 BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                 StringBuilder stringBuilder = new StringBuilder();
                 String line;
                 while ((line = bufferedReader.readLine()) != null) {
                     stringBuilder.append(line).append("\n");
                 }
                 bufferedReader.close(); // Close the reader

                 return stringBuilder.toString();

             } catch (IOException e) {
                 e.printStackTrace();
             }
             return null;
         }

     }


        protected void onPostExecute(String result) {
            // Handle the result here on the UI thread
            if (result != null) {
                // Handle the response based on the result string
                Log.d("DeleteBookResponse", result); // Log the response
                // You can perform UI updates or any other action based on the result
                // For example, show a Toast or update the UI based on the server response
                if (result.equals("Book deleted successfully")) {
                    Toast.makeText(context, "Book deleted successfully", Toast.LENGTH_SHORT).show();
                } else if (result.equals("Failed to delete the book")) {
                    Toast.makeText(context, "Failed to delete the book", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Handle null or empty result
                Toast.makeText(context, "No response from server", Toast.LENGTH_SHORT).show();
            }
        }
    }





