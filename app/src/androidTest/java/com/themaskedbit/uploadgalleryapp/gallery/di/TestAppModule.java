package com.themaskedbit.uploadgalleryapp.gallery.di;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.themaskedbit.uploadgalleryapp.UploadGalleryApp;
import com.themaskedbit.uploadgalleryapp.gallery.api.ApiHelper;
import com.themaskedbit.uploadgalleryapp.gallery.api.FirebaseApi;
import com.themaskedbit.uploadgalleryapp.gallery.api.TestFirebaseApi;
import com.themaskedbit.uploadgalleryapp.gallery.model.ImageList;
import com.themaskedbit.uploadgalleryapp.gallery.model.TestUser;
import com.themaskedbit.uploadgalleryapp.gallery.model.User;
import com.themaskedbit.uploadgalleryapp.gallery.manager.SharedPreferencesManager;

import javax.inject.Singleton;

import dagger.Provides;

public class TestAppModule extends AppModule {
    public TestAppModule(UploadGalleryApp app) {
        super(app);
    }

    @NonNull
    @Override
    User user(SharedPreferencesManager sharedPreferencesManager) {
        return new TestUser(sharedPreferencesManager);
    }

    @Override
    ApiHelper provideApiHelper(StorageReference storageReference, DatabaseReference databaseReference, FirebaseDatabase firebaseDatabase, ImageList imageList) {
        return new TestFirebaseApi();
    }

}
