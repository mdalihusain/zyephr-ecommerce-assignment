package com.zyephre.commerce.models;

import static android.content.ContentValues.TAG;

import android.util.Log;
import android.view.View;

public class ProductModel {
    private int id;
    private String title;
    private String description;
    private float price;
    private float discountPercentage;
    private float rating;
    private float stock;
    private String brand;
    private String category;
    private String thumbnail;
    private int selectedQuantity = 1;
    private boolean inCart = false;

    public boolean isInCart() {
        return inCart;
    }

    public void setInCart(boolean inCart) {
        this.inCart = inCart;
    }

    public int getSelectedQuantity() {
        return selectedQuantity;
    }

    public void setSelectedQuantity(int selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }
// Getter Methods

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public float getPrice() {
        return price;
    }

    public float getDiscountPercentage() {
        return discountPercentage;
    }

    public float getRating() {
        return rating;
    }

    public float getStock() {
        return stock;
    }

    public String getBrand() {
        return brand;
    }

    public String getCategory() {
        return category;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    // Setter Methods

    public void setId(int id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public void setDiscountPercentage(float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public void setStock(float stock) {
        this.stock = stock;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void increaseQuantity(){
//        int qty = selectedQuantity;
//        Log.d(TAG, "increaseQuantity: " + qty);
        if (selectedQuantity<10)
        selectedQuantity++;
//        selectedQuantity = qty;
//        Log.d(TAG, "increaseQuantity: " + qty);
    }

    public void decreaseQuantity(){
//        int qty = selectedQuantity;
//        Log.d(TAG, "decreaseQuantity: " + qty);
        if (selectedQuantity>0)
            selectedQuantity--;
//        selectedQuantity = qty+"";
//        Log.d(TAG, "decreaseQuantity: " + qty);
    }


}
