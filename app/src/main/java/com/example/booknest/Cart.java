package com.example.booknest;

import java.util.ArrayList;
import java.util.List;

public class Cart {

    private static Cart instance;
    private List<bestDealModel> cartItems;

    public Cart() {
         cartItems = new ArrayList<>();
    }

    public Cart(List<bestDealModel> cartItems) {
        this.cartItems = cartItems;
    }

    public static Cart getInstance() {
        if (instance == null) {
            synchronized (Cart.class) {
                if (instance == null) {
                    instance = new Cart();
                }
            }
        }
        return instance;
    }

    public static void setInstance(Cart instance) {
        Cart.instance = instance;
    }

    public List<bestDealModel> getCartItems() {
        return cartItems;
    }

    public void setCartItems(List<bestDealModel> cartItems) {
        this.cartItems = cartItems;
    }


    public void addItem(bestDealModel item) {
        cartItems.add(item);
    }


}
