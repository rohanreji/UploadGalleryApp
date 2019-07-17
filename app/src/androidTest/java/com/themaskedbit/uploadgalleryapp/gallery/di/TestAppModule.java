package com.themaskedbit.uploadgalleryapp.gallery.di;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.StorageReference;
import com.themaskedbit.uploadgalleryapp.UploadGalleryApp;
import com.themaskedbit.uploadgalleryapp.gallery.api.ApiHelper;
import com.themaskedbit.uploadgalleryapp.gallery.api.TestFirebaseApi;
import com.themaskedbit.uploadgalleryapp.gallery.manager.SharedPreferencesManager;
import com.themaskedbit.uploadgalleryapp.gallery.model.ImageList;
import com.themaskedbit.uploadgalleryapp.gallery.model.TestUser;
import com.themaskedbit.uploadgalleryapp.gallery.model.User;

import dagger.Module;

@Module
public class TestAppModule extends AppModule {
    public TestAppModule(final UploadGalleryApp app) {
        super(app);
    }


    @NonNull
    @Override
    User user(SharedPreferencesManager sharedPreferencesManager) {
        return new TestUser(sharedPreferencesManager);
    }

    @Override
    ApiHelper getApiHelper(StorageReference storageReference, DatabaseReference databaseReference, FirebaseDatabase firebaseDatabase, ImageList imageList, User user) {
        return new TestFirebaseApi((TestUser) user, imageList);
    }

}
