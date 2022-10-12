package com.zyephre.commerce.ui.main.fragments;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zyephre.commerce.SuccessActivity;
import com.zyephre.commerce.adapters.CartItemsAdapter;
import com.zyephre.commerce.databinding.FragmentCartBinding;
import com.zyephre.commerce.models.OrderModel;
import com.zyephre.commerce.models.ProductModel;
import com.zyephre.commerce.ui.main.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.Map;
import java.util.Random;

public class CartFragment extends Fragment {

    private MainViewModel mViewModel;
    private SharedPreferences sharedPreferences;
    private FragmentCartBinding binding;
    private final Gson gson = new Gson();
    private Map<Integer, Integer> cart;
    private ArrayList<ProductModel> productsInCart;
    private float amount = 0;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        cart = gson.fromJson(sharedPreferences.getString("cart", "{}"), new TypeToken<Map<Integer, Integer>>() {
        }.getType());
        if (cart.isEmpty()) {
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        sharedPreferences = getActivity().getSharedPreferences("zyephr", Context.MODE_PRIVATE);
        Map<Integer, Integer> cart = gson.fromJson(sharedPreferences.getString("cart", "{}"), new TypeToken<Map<Integer, Integer>>() {
        }.getType());


        binding.setCart(cart);

        mViewModel.getProducts().observe(getViewLifecycleOwner(), productModels -> {
            productsInCart = new ArrayList<>();
            for (ProductModel productModel : productModels) {
                if (cart.containsKey(productModel.getId())) {
                    productModel.setSelectedQuantity(cart.get(productModel.getId()));
                    productsInCart.add(productModel);
                }
            }
            RecyclerView recyclerView = binding.cartRecyclerView;
            CartItemsAdapter cartItemsAdapter = new CartItemsAdapter(productsInCart, cart, sharedPreferences);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.getItemAnimator().setChangeDuration(0);
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(cartItemsAdapter);
            for (ProductModel productModel : productModels) {
                if (cart.containsKey(productModel.getId())) {
                    amount += productModel.getPrice() * cart.get(productModel.getId());
                }
            }
            binding.cartTotalAmount.setText("₹" + amount);
        });

        binding.cartPlaceOrder.setOnClickListener(v -> {
            String email = binding.cartUserEmail.getText().toString(), address = binding.cartUserAddress.getText().toString();
            if(email.isEmpty()||address.isEmpty()){
                Toast.makeText(getContext(), "Please enter email id and address.", Toast.LENGTH_SHORT).show();
                return;
            }
            Random random = new Random();
            int id = random.nextInt(1000);
            OrderModel orderModel = new OrderModel(id, email, address, productsInCart, amount);
            mViewModel.addOrder(orderModel);
            cart.clear();
            sharedPreferences.edit().putString("cart", gson.toJson(cart)).commit();
            startActivity(new Intent(getActivity(), SuccessActivity.class));
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}