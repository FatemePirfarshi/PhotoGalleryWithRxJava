package com.example.photogallerywithrxjava.data.repository;

import android.database.Observable;

import androidx.lifecycle.MutableLiveData;

import com.example.photogallerywithrxjava.data.model.GalleryItem;
import com.example.photogallerywithrxjava.data.remote.NetworkParams;
import com.example.photogallerywithrxjava.data.remote.retrofit.FlickerService;
import com.example.photogallerywithrxjava.data.remote.retrofit.RetrofitInstance;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;

public class PhotoRepository {

    private static final String TAG = "PhotoRepository";

    private final FlickerService mFlickerService;
//    Observable<List<GalleryItem>> observableItems;
    private final MutableLiveData<List<GalleryItem>> mPopularItemsLiveData = new MutableLiveData<>();

    public MutableLiveData<List<GalleryItem>> getPopularItemsLiveData() {
        return mPopularItemsLiveData;
    }

    public PhotoRepository() {
        Retrofit retrofit = RetrofitInstance.getInstance().getRetrofit();
        mFlickerService = retrofit.create(FlickerService.class);
//        observableItems = mFlickerService.listItems(NetworkParams.getPopularOptions());
    }

    public Future<Observable<List<GalleryItem>>> makeFuture() {
        final ExecutorService executor = Executors.newSingleThreadExecutor();
        final Callable<Observable<List<GalleryItem>>> networkCallable = new Callable<Observable<List<GalleryItem>>>() {

            @Override
            public Observable<List<GalleryItem>> call() throws Exception {
                return mFlickerService.listItems(NetworkParams.getPopularOptions());
            }
        };

        final Future<Observable<List<GalleryItem>>> observableFuture = new Future<Observable<List<GalleryItem>>>() {

            @Override
            public boolean cancel(boolean mayInterruptIfRunning) {
                if (mayInterruptIfRunning)
                    executor.shutdown();
                return false;
            }

            @Override
            public boolean isCancelled() {
                return executor.isShutdown();
            }

            @Override
            public boolean isDone() {
                return executor.isTerminated();
            }

            @Override
            public Observable<List<GalleryItem>> get() throws ExecutionException, InterruptedException {
                return executor.submit(networkCallable).get();
            }

            @Override
            public Observable<List<GalleryItem>> get(long timeout, TimeUnit unit) throws ExecutionException, InterruptedException, TimeoutException {
                return executor.submit(networkCallable).get(timeout, unit);
            }
        };
        return observableFuture;
    }

//    public Observable<List<GalleryItem>> getGalleryItems() {
//        return mFlickerService.listItems(NetworkParams.getPopularOptions());
//    }

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
