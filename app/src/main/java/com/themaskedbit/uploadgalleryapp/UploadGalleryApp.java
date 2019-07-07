package com.themaskedbit.uploadgalleryapp;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.themaskedbit.uploadgalleryapp.gallery.Model.User;
import com.themaskedbit.uploadgalleryapp.gallery.di.AppComponent;
import com.themaskedbit.uploadgalleryapp.gallery.di.AppModule;
import com.themaskedbit.uploadgalleryapp.gallery.di.DaggerAppComponent;
import com.themaskedbit.uploadgalleryapp.gallery.presenter.SharedPreferencesHelper;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

public class UploadGalleryApp extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> activityDispatchingAndroidInjector;

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        initAppComponent();
        appComponent.inject(this);
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent.builder()
                    .appModule(new AppModule(this))
                    .build();
    }

    public AppComponent getAppComponent(){
        return this.appComponent;
    }


    @Override
    public AndroidInjector<Activity> activityInjector() {
        return activityDispatchingAndroidInjector;
    }
}
