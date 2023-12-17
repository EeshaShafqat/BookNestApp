package com.example.booknest;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class SqliteHelper extends SQLiteOpenHelper {

    Context context;

    public static String CREATE_BOOKS_TABLE = "CREATE TABLE " + myBookContract.Books.TABLE_NAME + "(" +
            myBookContract.Books._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            myBookContract.Books._imgUrl + " TEXT, " +
            myBookContract.Books._bookCategory + " TEXT, " +
            myBookContract.Books._bookTitle + " TEXT, " +
            myBookContract.Books._bookAuthor + " TEXT, " +
            myBookContract.Books._bookPrice + " TEXT, " +
            myBookContract.Books._discount + " TEXT, " +
            myBookContract.Books._rvType + " TEXT)";

    public static String DROP_BOOKS_TABLE = "DROP TABLE IF EXISTS " + myBookContract.Books.TABLE_NAME;
    public SqliteHelper(@Nullable Context context) {

        super(context, myBookContract.DB_NAME, null, myBookContract.DB_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(DROP_BOOKS_TABLE);
        onCreate(db);

    }

    public void addBook(bestDealModel book ){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(myBookContract.Books._imgUrl,book.imgUrl);
        cv.put(myBookContract.Books._bookCategory,book.bookCategory);
        cv.put(myBookContract.Books._bookTitle,book.bookTitle);
        cv.put(myBookContract.Books._bookAuthor,book.bookAuthor);
        cv.put(myBookContract.Books._bookPrice,book.bookPrice);
        cv.put(myBookContract.Books._discount,book.discount);
        cv.put(myBookContract.Books._rvType,book.rvType);

        long result = db.insert(myBookContract.Books.TABLE_NAME,null,cv);

        if(result==-1){
            Toast.makeText(context,"local insertion failed", Toast.LENGTH_LONG).show();
        }
        else{

            Toast.makeText(context,"local insertion successful", Toast.LENGTH_LONG).show();
        }

        db.close();
    }

    public void deleteBook(String bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(myBookContract.Books.TABLE_NAME, myBookContract.Books._ID + " = ?", new String[]{bookId});
        db.close();

        if (rowsDeleted > 0) {
            // Deletion successful
            Toast.makeText(context, "Book deleted successfully from local database", Toast.LENGTH_SHORT).show();
        } else {
            // Deletion failed or no records found
            Toast.makeText(context, "No book found or deletion failed in local database", Toast.LENGTH_SHORT).show();
        }
    }


    public void deleteAllBooks() {
        SQLiteDatabase db = this.getWritableDatabase();
        int rowsDeleted = db.delete(myBookContract.Books.TABLE_NAME, null, null);
        db.close();

        if (rowsDeleted > 0) {
            // Deletion successful
            Toast.makeText(context, "All books deleted successfully (locally)", Toast.LENGTH_SHORT).show();
        } else {
            // Deletion failed or no records found
            Toast.makeText(context, "No books found or deletion failed (locally)", Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("Range")
    public List<bestDealModel> getAllBooks() {
        List<bestDealModel> bookList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + myBookContract.Books.TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                bestDealModel book = new bestDealModel();
                book.setBookID(cursor.getString(cursor.getColumnIndex(myBookContract.Books._ID)));
                book.setImgUrl(cursor.getString(cursor.getColumnIndex(myBookContract.Books._imgUrl)));
                book.setBookCategory(cursor.getString(cursor.getColumnIndex(myBookContract.Books._bookCategory)));
                book.setBookAuthor(cursor.getString(cursor.getColumnIndex(myBookContract.Books._bookAuthor)));
                book.setBookPrice(cursor.getString(cursor.getColumnIndex(myBookContract.Books._bookPrice)));
                book.setBookTitle(cursor.getString(cursor.getColumnIndex(myBookContract.Books._bookTitle)));
                book.setDiscount(Integer.parseInt(cursor.getString(cursor.getColumnIndex(myBookContract.Books._discount))));
                book.setRvType(cursor.getString(cursor.getColumnIndex(myBookContract.Books._rvType)));


                // Log the image URL
                Log.d("ImageURL", "Image URL in getAllBooks: " + book.getImgUrl());


                bookList.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bookList;
    }


    @SuppressLint("Range")
    public String getBookIDByName(String bookName) {
        SQLiteDatabase db = this.getReadableDatabase();
        String bookID = null;
        Cursor cursor = db.query(myBookContract.Books.TABLE_NAME, new String[]{myBookContract.Books._ID},
                myBookContract.Books._bookTitle + "=?", new String[]{bookName}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            bookID = cursor.getString(cursor.getColumnIndex(myBookContract.Books._ID));
            cursor.close();
        }
        db.close();
        return bookID;
    }





}
