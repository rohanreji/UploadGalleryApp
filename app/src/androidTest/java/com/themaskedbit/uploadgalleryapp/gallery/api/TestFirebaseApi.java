package com.themaskedbit.uploadgalleryapp.gallery.api;

import android.net.Uri;

import com.themaskedbit.uploadgalleryapp.gallery.manager.ViewManager;
import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;

import java.io.File;

public class TestFirebaseApi implements ApiHelper {
    @Override
    public void setPresenter(ViewManager manager) {

    }

    @Override
    public void uploadImages(Uri uri, File file, IdlingResourceApp idlingResource, String name) {

    }

    @Override
    public void downloadImages(IdlingResourceApp idlingResource) {

    }

    @Override
    public void cancelUpload() {

    }

    @Override
    public void cancelDownload() {

    }
}
