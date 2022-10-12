package com.zyephre.commerce.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zyephre.commerce.R;
import com.zyephre.commerce.databinding.LayoutProductItemBinding;
import com.zyephre.commerce.models.ProductModel;

import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.Inflater;

public class ProductsGridAdapter extends RecyclerView.Adapter<ProductsGridAdapter.ViewHolder> {

    ArrayList<ProductModel> products;
    Map<Integer, Integer> cart;
    SharedPreferences sharedPreferences;
    Context context;
    Gson gson = new Gson();

    public ProductsGridAdapter(ArrayList<ProductModel> products, Map<Integer, Integer> cart, SharedPreferences sharedPreferences, Context context) {
        this.products = products;
        this.cart = cart;
        this.sharedPreferences = sharedPreferences;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutProductItemBinding view = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.layout_product_item, parent, false);
        return new ViewHolder(view);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public LayoutProductItemBinding binding;

        public ViewHolder(LayoutProductItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(ProductModel model) {
            binding.setProduct(model);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductModel productModel = products.get(position);
        LayoutProductItemBinding binding = holder.binding;

        if (cart.containsKey(productModel.getId())){
            productModel.setInCart(true);
            productModel.setSelectedQuantity(cart.get(productModel.getId()));
        } else {
            productModel.setInCart(false);
        }
        holder.bind(productModel);

        Glide.with(context).load(productModel.getThumbnail()).placeholder(binding.productImage.getDrawable()).dontAnimate().into(holder.binding.productImage);

        binding.productQuantityIncrease.setOnClickListener(v -> {
            productModel.increaseQuantity();
            notifyItemChanged(holder.getAdapterPosition());
        });

        binding.productQuantityDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                productModel.decreaseQuantity();
                notifyItemChanged(holder.getAdapterPosition());
            }
        });

        binding.productAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        cart.put(productModel.getId(), productModel.getSelectedQuantity());
                        sharedPreferences.edit().putString("cart", gson.toJson(cart)).commit();
                    }
                });
                productModel.setInCart(true);
                notifyItemChanged(position);
            }
        });

        binding.productRemoveFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().post(new Runnable() {
                    @Override
                    public void run() {
                        cart.remove(productModel.getId());
                        sharedPreferences.edit().putString("cart", gson.toJson(cart)).commit();
                    }
                });
                productModel.setInCart(false);
                notifyItemChanged(getItemViewType(position));
            }
        });

    }

    public void updateCart(Map<Integer, Integer> updatedCart) {
        cart = updatedCart;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
