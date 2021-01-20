package com.example.photogallerywithrxjava.data.remote.retrofit;

import android.database.Observable;

import com.example.photogallerywithrxjava.data.model.GalleryItem;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface FlickerService {

//    @GET(".")
//    Call<List<GalleryItem>> listItems(@QueryMap Map<String, String> options);

    @GET(".")
    Observable<List<GalleryItem>> listItems(@QueryMap Map<String, String> options);
}
