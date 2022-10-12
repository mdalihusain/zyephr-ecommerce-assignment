package com.zyephre.commerce.models;

import java.util.List;

public class OrderModel {
    int orderId;
    String userId;
    String address;
    float totalAmount;
    List<ProductModel> productModels;

    public OrderModel(int orderId, String userId, String address, List<ProductModel> productModels, float totalAmount) {
        this.orderId = orderId;
        this.userId = userId;
        this.address = address;
        this.totalAmount = totalAmount;
        this.productModels = productModels;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<ProductModel> getProductModels() {
        return productModels;
    }

    public void setProductModels(List<ProductModel> productModels) {
        this.productModels = productModels;
    }

    public float getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(float totalAmount) {
        this.totalAmount = totalAmount;
    }
}
