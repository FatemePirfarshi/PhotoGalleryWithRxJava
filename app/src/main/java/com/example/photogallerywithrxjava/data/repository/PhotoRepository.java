package com.example.photogallerywithrxjava.data.repository;

import android.database.Observable;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.photogallerywithrxjava.data.model.GalleryItem;
import com.example.photogallerywithrxjava.data.remote.NetworkParams;
import com.example.photogallerywithrxjava.data.remote.retrofit.FlickerService;
import com.example.photogallerywithrxjava.data.remote.retrofit.RetrofitInstance;

import org.reactivestreams.Subscription;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PhotoRepository {

    private static final String TAG = "PhotoRepository";

    private final FlickerService mFlickerService;
    private final MutableLiveData<List<GalleryItem>> mPopularItemsLiveData = new MutableLiveData<>();

    public MutableLiveData<List<GalleryItem>> getPopularItemsLiveData() {
        return mPopularItemsLiveData;
    }

    public PhotoRepository() {
        Retrofit retrofit = RetrofitInstance.getInstance().getRetrofit();
        mFlickerService = retrofit.create(FlickerService.class);
    }

    public Observable<List<GalleryItem>> getGalleryItems() {
        return mFlickerService.listItems(NetworkParams.getPopularOptions());
    }

    //this method can be run in any thread.
    public void fetchPopularItemsAsync() {
//        Call<List<GalleryItem>> call =
//                mFlickerService.listItems(NetworkParams.getPopularOptions());

        Observable<List<GalleryItem>> observableItems = mFlickerService.listItems(NetworkParams.getPopularOptions());

        io.reactivex.rxjava3.core.Observable.just(mFlickerService.listItems(NetworkParams.getPopularOptions()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Observable<List<GalleryItem>>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull Observable<List<GalleryItem>> listObservable) {
                        mPopularItemsLiveData.setValue((List<GalleryItem>) listObservable);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
//        Subscription subscription = RetrofitInstance.getInstance().getGalleryItems(NetworkParams.getPopularOptions());

//        call.enqueue(new Callback<List<GalleryItem>>() {
//
//            //this run on main thread
//            @Override
//            public void onResponse(Call<List<GalleryItem>> call, Response<List<GalleryItem>> response) {
//                List<GalleryItem> items = response.body();
//
//                for (int i = 0; i < items.size(); i++) {
//                    Log.e(TAG, items.get(i).getUrl());
//                }
//                //update adapter of recyclerview
//                mPopularItemsLiveData.setValue(items);
//            }
//
//            //this run on main thread
//            @Override
//            public void onFailure(Call<List<GalleryItem>> call, Throwable t) {
//                Log.e(TAG, t.getMessage(), t);
//            }
//        });
    }
}
