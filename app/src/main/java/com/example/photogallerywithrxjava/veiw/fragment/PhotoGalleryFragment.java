package com.example.photogallerywithrxjava.veiw.fragment;

import android.database.Observable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.photogallerywithrxjava.R;
import com.example.photogallerywithrxjava.adapter.PhotoAdapter;
import com.example.photogallerywithrxjava.data.model.GalleryItem;
import com.example.photogallerywithrxjava.databinding.FragmentPhotoGalleryBinding;
import com.example.photogallerywithrxjava.viewmodel.PhotoGalleryViewModel;

import org.reactivestreams.Subscription;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class PhotoGalleryFragment extends Fragment {

    private static final String TAG = "PGF";
    private static final int SPAN_COUNT = 3;

    private FragmentPhotoGalleryBinding mBinding;
    private PhotoGalleryViewModel mViewModel;
    private Subscription mSubscription;

    public PhotoGalleryFragment() {
        // Required empty public constructor
    }

    public static PhotoGalleryFragment newInstance() {
        PhotoGalleryFragment fragment = new PhotoGalleryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(PhotoGalleryViewModel.class);
        mViewModel.fetchPopularItemsAsync();

//        try {
////            mViewModel.makeFutureGalleryItems().get().
//    //        Observable<List<GalleryItem>> observable = mViewModel.getGalleryItems();
//    //        mSubscription = io.reactivex.rxjava3.core.Observable.just()
//
//    //        Subscription subscription = RetrofitInstance.getInstance().getGalleryItems(NetworkParams.getPopularOptions());
//
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        setLiveDataObservers();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_photo_gallery,
                container,
                false);
        initViews();

        return mBinding.getRoot();
    }

    private void initViews() {
        mBinding.rvPhotoGallery
                .setLayoutManager(new GridLayoutManager(getContext(), SPAN_COUNT));
    }

    private void setLiveDataObservers() {
        mViewModel.getPopularItemsLiveData().observe(this, new Observer<List<GalleryItem>>() {
            @Override
            public void onChanged(List<GalleryItem> items) {
                setupAdapter(items);
            }
        });
    }

    private void setupAdapter(List<GalleryItem> items) {
        PhotoAdapter adapter = new PhotoAdapter(mViewModel);
        mBinding.rvPhotoGallery.setAdapter(adapter);
    }
}