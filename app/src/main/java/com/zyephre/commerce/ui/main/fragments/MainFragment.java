package com.zyephre.commerce.ui.main.fragments;

import static android.content.Context.MODE_PRIVATE;

import androidx.lifecycle.ViewModelProvider;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zyephre.commerce.R;
import com.zyephre.commerce.adapters.ProductsGridAdapter;
import com.zyephre.commerce.databinding.FragmentMainBinding;
import com.zyephre.commerce.ui.main.viewmodels.MainViewModel;

import java.util.HashMap;
import java.util.Map;

public class MainFragment extends Fragment {

    private MainViewModel mViewModel;
    private FragmentMainBinding binding;
    private SharedPreferences sharedPreferences;
    private Map<Integer, Integer> cart;
    private ProductsGridAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentMainBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        cart = new Gson().fromJson(sharedPreferences.getString("cart", "{}"), new TypeToken<Map<Integer, Integer>>() {
        }.getType());
        if(adapter!=null) {
            adapter.updateCart(cart);
        }
        super.onResume();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPreferences = getContext().getSharedPreferences("zyephr", MODE_PRIVATE);
        if (!sharedPreferences.contains("cart")) {
            cart = new HashMap<>();
            Gson gson = new Gson();
            sharedPreferences.edit().putString("cart", gson.toJson(cart)).commit();
        } else {
            cart = new Gson().fromJson(sharedPreferences.getString("cart", "{}"), new TypeToken<Map<Integer, Integer>>() {
            }.getType());
        }

        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        mViewModel.getStoreDetails().observe(getViewLifecycleOwner(), storeInfoModel -> {
            binding.setStore(storeInfoModel);
            Glide.with(getContext())
                    .load(storeInfoModel.getImageUrl())
                    .into(binding.storeImage);
        });

        mViewModel.getProducts().observe(getViewLifecycleOwner(), productModels -> {
            RecyclerView recyclerView = binding.storeProductsGrid;
            adapter = new ProductsGridAdapter(productModels, cart, sharedPreferences, getActivity());
            GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
            recyclerView.getItemAnimator().setChangeDuration(0);
            recyclerView.setLayoutManager(layoutManager);
            recyclerView.setAdapter(adapter);
        });

        binding.viewCartLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (cart.isEmpty()) {
                    Toast.makeText(getContext(), "No Products in Cart!", Toast.LENGTH_SHORT).show();
                    return;
                }
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.container, CartFragment.class, null)
                        .setReorderingAllowed(true)
                        .addToBackStack("cart")
                        .commit();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}