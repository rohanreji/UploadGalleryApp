package com.themaskedbit.uploadgalleryapp.gallery;

import com.themaskedbit.uploadgalleryapp.UploadGalleryApp;
import com.themaskedbit.uploadgalleryapp.gallery.di.DaggerAppComponent;
import com.themaskedbit.uploadgalleryapp.gallery.di.TestAppModule;

public class TestUploadGalleryApp extends UploadGalleryApp {
    @Override
    public void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                .appModule(new TestAppModule(this))
                .build();
    }
}
