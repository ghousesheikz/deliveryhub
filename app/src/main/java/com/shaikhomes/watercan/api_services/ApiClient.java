package com.shaikhomes.watercan.api_services;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


@SuppressLint("Registered")
public class ApiClient extends Application {
    static Context Appcontext;
    static ApiInterface service;

    public static final String BASE_URL ="https://deliveryhub.shaikhomes.com/";
  // public static final String BASE_URL ="https://delapi.shaikhomes.com/";
    private static Retrofit retrofit = null;

    @Override
    public void onCreate() {
        super.onCreate();
        Appcontext = getApplicationContext();
    }

    public static Retrofit getClient(Context context) {
        if (retrofit == null) {
            OkHttpClient client = new OkHttpClient.Builder()

                    .connectTimeout(90, TimeUnit.SECONDS)
                    .readTimeout(90, TimeUnit.SECONDS)
                    .writeTimeout(90, TimeUnit.SECONDS)
                    .build();
            retrofit = new Retrofit.Builder().client(client).baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().excludeFieldsWithoutExposeAnnotation() .create()))
                    .build();
          //  Log.e("String url", BASE_URL);
        }
        return retrofit;
    }
}
