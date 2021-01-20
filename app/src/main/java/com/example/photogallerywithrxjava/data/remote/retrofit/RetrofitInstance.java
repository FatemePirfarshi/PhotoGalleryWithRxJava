package com.example.photogallerywithrxjava.data.remote.retrofit;

import android.database.Observable;

import com.example.photogallerywithrxjava.data.model.GalleryItem;
import com.example.photogallerywithrxjava.data.remote.NetworkParams;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import hu.akarnokd.rxjava3.retrofit.RxJava3CallAdapterFactory;
import io.reactivex.rxjava3.internal.schedulers.RxThreadFactory;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Converter;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitInstance {

    private static RetrofitInstance sInstance;
    private final Retrofit mRetrofit;
    private FlickerService mFlickerService;

    public static RetrofitInstance getInstance() {
        if (sInstance == null)
            sInstance = new RetrofitInstance();

        return sInstance;
    }

    private static Converter.Factory createGsonConverter() {
        Type type = new TypeToken<List<GalleryItem>>() {
        }.getType();

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(type, new GetGalleryItemDeserializer());
        Gson gson = gsonBuilder.create();

        return GsonConverterFactory.create(gson);
    }

    public Retrofit getRetrofit() {
        return mRetrofit;
    }

    private RetrofitInstance() {

        mRetrofit = new Retrofit.Builder()
                .baseUrl(NetworkParams.BASE_URL)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(createGsonConverter())
                .build();
    }

    public Observable<List<GalleryItem>> getGalleryItems(Map<String, String> options){
        return mFlickerService.listItems(options);
    }
}
