package com.example.booknest;

import android.provider.BaseColumns;

public class myBookContract {

    public static String DB_NAME = "myBooks.db";
    public static int DB_VERSION = 1;

//Books table schema
    public static class Books implements BaseColumns{

        public static String TABLE_NAME = "booksTable";

        public static String _ID = "bookID";
        public static String _imgUrl = "imgUrl";
        public static String _bookCategory ="bookCategory";
        public static String _bookTitle = "bookTitle";
        public static String _bookAuthor ="bookAuthor";
        public static String _bookPrice ="bookPrice";
        public static String _discount ="discount";
        public static String _rvType ="rvType";


    }




}
