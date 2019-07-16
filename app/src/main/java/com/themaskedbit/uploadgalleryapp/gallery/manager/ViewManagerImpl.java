package com.themaskedbit.uploadgalleryapp.gallery.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import androidx.annotation.Nullable;

import com.themaskedbit.uploadgalleryapp.gallery.api.ApiHelper;
import com.themaskedbit.uploadgalleryapp.gallery.helper.FileHelper;
import com.themaskedbit.uploadgalleryapp.gallery.model.Image;
import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;
import com.themaskedbit.uploadgalleryapp.gallery.view.MainActivityInterface;

import java.io.File;
import java.util.List;

public class ViewManagerImpl implements ViewManager {
    private MainActivityInterface view;
    private ApiHelper apiHelper;

    public ViewManagerImpl(ApiHelper apiHelper) {
        this.apiHelper = apiHelper;
        apiHelper.setPresenter(this);
    }

    @Override
    public void onEditorSaved(@Nullable IdlingResourceApp idlingResource, final File file, Bitmap cropping) {
        view.onUploadImageStarted();
        File imageFile = FileHelper.saveBitmap(file, cropping);
        final Uri uri = Uri.fromFile(imageFile);
        apiHelper.uploadImages(uri, imageFile, idlingResource, System.currentTimeMillis() + ".jpg");
    }

    @Override
    public void onEditorClosed() {
        if (view != null) {
            view.onEditorClosed();
        }
    }

    @Override
    public void setView(MainActivityInterface view) {
        this.view = view;
    }

    @Override
    public void start(@Nullable IdlingResourceApp idlingResource) {
        view.onFetchImagesStarted();

        apiHelper.downloadImages(idlingResource);
    }


    @Override
    public void uploadSuccess(Image image) {
        view.onUploadImageCompleted(image);
    }

    @Override
    public void uploadError(Exception e) {
        view.onUploadImageError(e);
    }

    @Override
    public void fetchDone(List<Image> imageList) {
        view.onFetchImageDone(imageList);
    }

    @Override
    public void fetchComplete() {
        view.onFetchImageOver();
    }


    @Override
    public void fetchError(Exception e) {
        view.onFetchImagesError(e);
    }

    @Override
    public void stop() {
        apiHelper.cancelDownload();
        apiHelper.cancelUpload();
    }


}