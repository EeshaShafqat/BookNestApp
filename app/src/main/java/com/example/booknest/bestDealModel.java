package com.example.booknest;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Random;

public class bestDealModel implements Parcelable {

    String bookID;

    String imgUrl;

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public void setByTime(String byTime) {
        this.byTime = byTime;
    }

    String bookCategory;
    String bookTitle;

    String bookAuthor;

    String bookPrice;

    int discount;
    int count;

    String rvType;

    String byTime;

    public String getByTime() {
        return byTime;
    }


    public void setByTime() {
        // Array of choices: week, month, year
        String[] timeChoices = {"week", "month", "year"};

            // Generate a random index
            Random random = new Random();
            int randomIndex = random.nextInt(timeChoices.length);

            // Set this.byTime to a randomly chosen value from the array
            this.byTime = timeChoices[randomIndex];


    }

    public String getRvType() {
        return rvType;
    }

    public void setRvType(String rvType) {
        this.rvType = rvType;
    }

    public bestDealModel(){}

    public bestDealModel(String imgUrl, String bookCategory, String bookTitle, String bookAuthor, String bookPrice, int discount,  String rvType) {
        this.imgUrl = imgUrl;
        this.bookCategory = bookCategory;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookPrice = bookPrice;
        this.discount = discount;
        this.count = 1;
        this.rvType = rvType;
    }

    public bestDealModel(int count, String bookCategory, String bookTitle, String bookAuthor, String bookPrice) {
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

    public bestDealModel(String bookCategory, String bookTitle, String bookAuthor, String bookPrice, int discount,String imgUrl) {
        this.bookCategory = bookCategory;
        this.bookTitle = bookTitle;
        this.bookAuthor = bookAuthor;
        this.bookPrice = bookPrice;
        this.discount = discount;
        this.imgUrl = imgUrl;
    }

    public static final Creator<bestDealModel> CREATOR = new Creator<bestDealModel>() {
        @Override
        public bestDealModel createFromParcel(Parcel in) {
            return new bestDealModel(in);
        }

        @Override
        public bestDealModel[] newArray(int size) {
            return new bestDealModel[size];
        }
    };

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

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {

        dest.writeString(imgUrl);
        dest.writeString(bookCategory);
        dest.writeString(bookTitle);
        dest.writeString(bookAuthor);
        dest.writeString(bookPrice);
        dest.writeString(String.valueOf(discount));
        dest.writeString(String.valueOf(count));


    }

    protected bestDealModel(Parcel in) {
        imgUrl = in.readString();
        bookCategory = in.readString();
        bookTitle = in.readString();
        bookAuthor = in.readString();
        bookPrice = in.readString();
        discount = Integer.parseInt(in.readString()); // Parse String to int while reading
        count = Integer.parseInt(in.readString());    // Parse String to int while reading
    }

}
