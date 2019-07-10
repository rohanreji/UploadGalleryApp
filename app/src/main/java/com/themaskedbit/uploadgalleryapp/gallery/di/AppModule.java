package com.themaskedbit.uploadgalleryapp.gallery.di;

import android.content.Context;
import android.content.SharedPreferences;

import com.squareup.picasso.Picasso;
import com.themaskedbit.uploadgalleryapp.gallery.model.User;
import com.themaskedbit.uploadgalleryapp.UploadGalleryApp;
import com.themaskedbit.uploadgalleryapp.gallery.presenter.SharedPreferencesHelper;
import com.themaskedbit.uploadgalleryapp.gallery.presenter.SharedPreferencesHelperImpl;

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
    SharedPreferencesHelper provideSharedPreferenceHelper(){
        return new SharedPreferencesHelperImpl(getSharedPreference());
    }


    @Provides
    @Singleton
    User provideUser(SharedPreferencesHelper sharedPreferencesHelper){
        return user(sharedPreferencesHelper);
    }

    User user(SharedPreferencesHelper sharedPreferencesHelper) {
        return new User(sharedPreferencesHelper);
    }

    @Provides
    @Singleton
    Picasso providePicasso(Context context) {
        return picasso(context);
    }

    Picasso picasso(final Context context) {
        return Picasso.with(context);
    }
}
