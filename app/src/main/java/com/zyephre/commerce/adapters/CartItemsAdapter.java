package com.zyephre.commerce.adapters;

import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.zyephre.commerce.R;
import com.zyephre.commerce.databinding.LayoutItemCartBinding;
import com.zyephre.commerce.models.ProductModel;

import java.util.ArrayList;
import java.util.Map;

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.ViewHolder> {
    public CartItemsAdapter(ArrayList<ProductModel> productModels, Map<Integer, Integer> cart, SharedPreferences sharedPreferences) {
        this.productModels = productModels;
        this.cart = cart;
        this.sharedPreferences = sharedPreferences;
    }

    private ArrayList<ProductModel> productModels;
    private Map<Integer, Integer> cart;
    private SharedPreferences sharedPreferences;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutItemCartBinding view = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.layout_item_cart, parent, false);
        return new CartItemsAdapter.ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return productModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LayoutItemCartBinding binding;

        public ViewHolder(LayoutItemCartBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductModel model) {
            binding.setProduct(model);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LayoutItemCartBinding binding = holder.binding;
        ProductModel productModel = productModels.get(position);
        holder.bind(productModel);
        Glide.with(binding.getRoot()).load(productModel.getThumbnail()).into(holder.binding.cartProductImage);
    }
}
