package com.themaskedbit.uploadgalleryapp.gallery.di;

import com.themaskedbit.uploadgalleryapp.gallery.view.MainActivity;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity contributeMainActivityInjector();
}
