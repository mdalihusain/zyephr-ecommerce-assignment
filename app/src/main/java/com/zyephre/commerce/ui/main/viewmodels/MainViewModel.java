package com.zyephre.commerce.ui.main.viewmodels;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zyephre.commerce.models.OrderModel;
import com.zyephre.commerce.models.ProductModel;
import com.zyephre.commerce.models.StoreInfoModel;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<StoreInfoModel> store;
    private MutableLiveData<ArrayList<ProductModel>> products;
    private Gson gson = new Gson();

    public MainViewModel(@NonNull Application application) {
        super(application);
    }

    // TODO: Implement the ViewModel
    public LiveData<StoreInfoModel> getStoreDetails() {
        // CHECKING IF THE METHOD IS BEING CALLED FOR THE FIRST TIME
        if (store == null) {
            store = new MutableLiveData<StoreInfoModel>();
            fetchStoreDetails();
        }
        return store;
    }

    // TODO: Implement the ViewModel
    public LiveData<ArrayList<ProductModel>> getProducts() {
        // CHECKING IF THE METHOD IS BEING CALLED FOR THE FIRST TIME
        if (products == null) {
            products = new MutableLiveData<ArrayList<ProductModel>>();
            fetchProducts();
        }
        return products;
    }

    private void fetchProducts() {
        String productJson = getJsonFromAssets("products.json");
        ArrayList<ProductModel> productsList = gson.fromJson(productJson, new TypeToken<ArrayList<ProductModel>>() {
        }.getType());
        Log.d(TAG, "fetchProducts: " + productsList.size());
        products.postValue(productsList);
    }

    private void fetchStoreDetails() {
        String storeJson = getJsonFromAssets("storeInfo.json");
        store.postValue(gson.fromJson(storeJson, StoreInfoModel.class));
    }

    public String getJsonFromAssets(String fileName) {
        String jsonString;
        try {
            InputStream is = getApplication().getAssets().open(fileName);

            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            jsonString = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return jsonString;
    }

    public void addOrder(OrderModel order) {
        try {
            FileInputStream is = getApplication().openFileInput("orders.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            String jsonString = new String(buffer, "UTF-8");
            ArrayList<OrderModel> orders = gson.fromJson(jsonString, new TypeToken<ArrayList<OrderModel>>() {
            }.getType());
            orders.add(order);
            writeJson("orders.json", gson.toJson(orders));
        } catch (FileNotFoundException e) {
            ArrayList<OrderModel> orders = new ArrayList<>();
            orders.add(order);
            writeJson("orders.json", gson.toJson(orders));
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeJson(String fileName, String json) {
        File file = new File(getApplication().getFilesDir(), fileName);
        File userFile = new File(getApplication().getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), fileName);
        try {
            FileWriter fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);
            bufferedWriter.close();
            fileWriter = new FileWriter(userFile);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(json);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}