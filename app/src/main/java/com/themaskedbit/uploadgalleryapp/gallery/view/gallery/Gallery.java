package com.themaskedbit.uploadgalleryapp.gallery.view.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.themaskedbit.uploadgalleryapp.R;
import com.themaskedbit.uploadgalleryapp.databinding.LayoutGalleryBinding;
import com.themaskedbit.uploadgalleryapp.gallery.model.Image;

import java.util.List;

public class Gallery extends Fragment implements GalleryInterface{
    private GalleryAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LayoutGalleryBinding galleryBinding = DataBindingUtil.inflate(
                inflater, R.layout.layout_gallery, container, false);
        galleryBinding.galleryRv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        galleryBinding.galleryRv.setAdapter(adapter);
        return galleryBinding.getRoot();
    }

    public void setAdapter(GalleryAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void setImages(List<Image> images) {
        adapter.setImages(images);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void addImage(Image image) {
        adapter.addImage(image);
        adapter.notifyDataSetChanged();
    }
}
