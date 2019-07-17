package com.themaskedbit.uploadgalleryapp.gallery.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.common.api.Api;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.themaskedbit.uploadgalleryapp.gallery.api.ApiHelper;
import com.themaskedbit.uploadgalleryapp.gallery.api.FirebaseApi;
import com.themaskedbit.uploadgalleryapp.gallery.manager.SharedPreferencesManagerImpl;
import com.themaskedbit.uploadgalleryapp.gallery.manager.ViewManager;
import com.themaskedbit.uploadgalleryapp.gallery.manager.ViewManagerImpl;
import com.themaskedbit.uploadgalleryapp.gallery.model.Image;
import com.themaskedbit.uploadgalleryapp.gallery.model.ImageList;
import com.themaskedbit.uploadgalleryapp.gallery.model.User;
import com.themaskedbit.uploadgalleryapp.UploadGalleryApp;
import com.themaskedbit.uploadgalleryapp.gallery.manager.SharedPreferencesManager;
import com.themaskedbit.uploadgalleryapp.gallery.view.gallery.GalleryAdapter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class AppModule {
    private static final String PREF_NAME = "GALLERY_PREF";
    private UploadGalleryApp app;

    public AppModule(UploadGalleryApp app) {
        this.app = app;
    }

    SharedPreferences getSharedPreference() {
        return app.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    @Provides
    @Singleton
    Context provideContext() {
        return app;
    }


    @Provides
    @Singleton
    SharedPreferencesManager provideSharedPreferenceHelper(){
        return new SharedPreferencesManagerImpl(getSharedPreference());
    }


    @Provides
    @Singleton
    User provideUser(SharedPreferencesManager sharedPreferencesManager){
        return user(sharedPreferencesManager);
    }

    User user(SharedPreferencesManager sharedPreferencesManager) {
        return new User(sharedPreferencesManager);
    }

    @Provides
    @Singleton
    Picasso providePicasso(Context context) {
        return picasso(context);
    }

    Picasso picasso(final Context context) {
        return Picasso.with(context);
    }

    @Provides
    @Singleton
    ImageList provideImageList() {
        return new ImageList();
    }

    @Provides
    @Singleton
    FirebaseDatabase provideFirebasDatabase() {
        return FirebaseDatabase.getInstance();
    }
    @Provides
    @Singleton
    DatabaseReference provideDatabaseReference(User user) {
        return FirebaseDatabase.getInstance().getReference().child("images");
    }

    @Provides
    @Singleton
    StorageReference provideStorageReference(User user) {
        return FirebaseStorage.getInstance().getReference().child("images");
    }

    @Provides
    @Singleton
    ApiHelper provideApiHelper(StorageReference storageReference, DatabaseReference databaseReference, FirebaseDatabase firebaseDatabase, ImageList imageList, User user) {
        return getApiHelper(storageReference,databaseReference, firebaseDatabase, imageList, user);
    }

    ApiHelper getApiHelper(StorageReference storageReference, DatabaseReference databaseReference, FirebaseDatabase firebaseDatabase, ImageList imageList, User user) {
        return new FirebaseApi(storageReference,databaseReference, firebaseDatabase, imageList, user);
    }

    @Provides
    @Singleton
    ViewManager provideViewManager(ApiHelper apiHelper) {
        return new ViewManagerImpl(apiHelper);
    }



    @Provides
    GalleryAdapter provideGalleryAdapter(Picasso picasso, ImageList imageList) {
        return new GalleryAdapter(picasso, imageList);
    }


}
