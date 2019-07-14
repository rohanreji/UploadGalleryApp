package com.themaskedbit.uploadgalleryapp.gallery.api;

import android.net.Uri;
import android.provider.ContactsContract;

import com.themaskedbit.uploadgalleryapp.gallery.manager.ViewManager;
import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;

import java.io.File;

public interface ApiHelper {
    void setPresenter(ViewManager manager);
    void uploadImages(Uri uri, File file, IdlingResourceApp idlingResource, String name);
    void downloadImages(IdlingResourceApp idlingResource);
    void cancelUpload();
    void cancelDownload();
}
