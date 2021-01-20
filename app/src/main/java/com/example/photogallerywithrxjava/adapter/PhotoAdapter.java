package com.example.photogallerywithrxjava.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.photogallerywithrxjava.R;
import com.example.photogallerywithrxjava.data.model.GalleryItem;
import com.example.photogallerywithrxjava.databinding.ListItamPhotoGalleryBinding;
import com.example.photogallerywithrxjava.viewmodel.PhotoGalleryViewModel;
import com.squareup.picasso.Picasso;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoHolder> {

    private final PhotoGalleryViewModel mViewModel;

    public PhotoAdapter(PhotoGalleryViewModel viewModel) {
        mViewModel = viewModel;
    }

    @NonNull
    @Override
    public PhotoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        ListItamPhotoGalleryBinding binding =
                DataBindingUtil.inflate(
                        LayoutInflater.from(mViewModel.getApplication()),
                        R.layout.list_itam_photo_gallery,
                        parent,
                        false);
        return new PhotoHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoHolder holder, int position) {
        GalleryItem item = mViewModel.getPopularItemsLiveData().getValue().get(position);
        holder.bindGalleryItem(item, position);
    }

    @Override
    public int getItemCount() {
        return mViewModel.getPopularItemsLiveData().getValue().size();
    }

    class PhotoHolder extends RecyclerView.ViewHolder {

        private final ListItamPhotoGalleryBinding mBinding;

        public PhotoHolder(ListItamPhotoGalleryBinding binding) {
            super(binding.getRoot());
            mBinding = binding;
        }

        public void bindGalleryItem(GalleryItem item, int position) {

            Picasso.get()
                    .load(item.getUrl())
                    .placeholder(R.mipmap.ic_android_placeholder)
                    .into(mBinding.itemImageView);
        }
    }
}
