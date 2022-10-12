package com.zyephre.commerce;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.zyephre.commerce.databinding.ActivitySuccessBinding;

public class SuccessActivity extends AppCompatActivity {
    ActivitySuccessBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySuccessBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        getSupportActionBar().hide();


        Glide.with(this).load(R.drawable.preview).into(binding.successGif);

        binding.successBackButton.setOnClickListener(v->onBackPressed());

    }
}