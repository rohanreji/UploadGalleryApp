package com.themaskedbit.uploadgalleryapp.gallery.di;

import androidx.annotation.NonNull;

import com.themaskedbit.uploadgalleryapp.UploadGalleryApp;
import com.themaskedbit.uploadgalleryapp.gallery.model.TestUser;
import com.themaskedbit.uploadgalleryapp.gallery.model.User;
import com.themaskedbit.uploadgalleryapp.gallery.manager.SharedPreferencesManager;

public class TestAppModule extends AppModule {
    public TestAppModule(UploadGalleryApp app) {
        super(app);
    }

    @NonNull
    @Override
    User user(SharedPreferencesManager sharedPreferencesManager) {
        return new TestUser(sharedPreferencesManager);
    }
}
