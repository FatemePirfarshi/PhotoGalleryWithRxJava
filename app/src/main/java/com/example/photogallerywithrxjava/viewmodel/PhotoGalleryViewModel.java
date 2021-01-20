package com.example.photogallerywithrxjava.viewmodel;

import android.app.Application;
import android.database.Observable;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.photogallerywithrxjava.data.model.GalleryItem;
import com.example.photogallerywithrxjava.data.remote.NetworkParams;
import com.example.photogallerywithrxjava.data.remote.retrofit.RetrofitInstance;
import com.example.photogallerywithrxjava.data.repository.PhotoRepository;

import org.reactivestreams.Subscription;

import java.util.List;

public class PhotoGalleryViewModel extends AndroidViewModel {

    private final PhotoRepository mRepository;
    private final LiveData<List<GalleryItem>> mPopularItemsLiveData;

    public LiveData<List<GalleryItem>> getPopularItemsLiveData() {
        return mPopularItemsLiveData;
    }

    public PhotoGalleryViewModel(@NonNull Application application) {
        super(application);

        mRepository = new PhotoRepository();
        mPopularItemsLiveData = mRepository.getPopularItemsLiveData();
    }

    public void fetchPopularItemsAsync() {
        mRepository.fetchPopularItemsAsync();
//        Subscription subscription = RetrofitInstance.getInstance().getGalleryItems(NetworkParams.getPopularOptions()).
    }

    public Observable<List<GalleryItem>> getGalleryItems() {
        return mRepository.getGalleryItems();
    }
}
