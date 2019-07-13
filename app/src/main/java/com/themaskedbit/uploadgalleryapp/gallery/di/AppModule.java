package com.themaskedbit.uploadgalleryapp.gallery.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.squareup.picasso.Picasso;
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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

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
    ViewManager provideViewManager(User user, ImageList imageList) {
        return new ViewManagerImpl(user, Schedulers.io(), AndroidSchedulers.mainThread(), imageList);
    }

    @Provides
    GalleryAdapter provideGalleryAdapter(Picasso picasso) {
        return new GalleryAdapter(picasso);
    }

    @Provides
    @Singleton
    ImageList provideImageList() {
        return new ImageList();
    }
}
