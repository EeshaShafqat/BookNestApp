package com.example.booknest;

public class bestDealModel {

    String bookCategory;
    String bookTitle;

    String bookAuthor;

    String bookPrice;

    int discount;
    int count;


    public bestDealModel(int count,String bookCategory, String bookTitle, String bookAuthor, String bookPrice) {
        this.bookCategory = bookCategory;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookPrice = bookPrice;
        this.count = count;
    }

    public bestDealModel(String bookCategory, String bookTitle, String bookAuthor, String bookPrice) {
        this.bookCategory = bookCategory;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookPrice = bookPrice;
    }

    public bestDealModel(String bookCategory, String bookTitle, String bookAuthor, String bookPrice, int discount) {
        this.bookCategory = bookCategory;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookPrice = bookPrice;
        this.discount = discount;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getBookCategory() {
        return bookCategory;
    }

    public void setBookCategory(String bookCategory) {
        this.bookCategory = bookCategory;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor = bookAuthor;
    }

    public String getBookPrice() {
        return bookPrice;
    }

    public void setBookPrice(String bookPrice) {
        this.bookPrice = bookPrice;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
