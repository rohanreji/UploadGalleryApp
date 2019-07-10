package com.themaskedbit.uploadgalleryapp.gallery.di;

import androidx.annotation.NonNull;

import com.themaskedbit.uploadgalleryapp.UploadGalleryApp;
import com.themaskedbit.uploadgalleryapp.gallery.model.TestUser;
import com.themaskedbit.uploadgalleryapp.gallery.model.User;
import com.themaskedbit.uploadgalleryapp.gallery.presenter.SharedPreferencesHelper;

import javax.inject.Singleton;

import dagger.Provides;

public class TestAppModule extends AppModule {
    public TestAppModule(UploadGalleryApp app) {
        super(app);
    }

    @NonNull
    @Override
    User user(SharedPreferencesHelper sharedPreferencesHelper) {
        return new TestUser(sharedPreferencesHelper);
    }
}
