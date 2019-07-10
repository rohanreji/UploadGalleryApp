package com.themaskedbit.uploadgalleryapp.gallery.di;

import com.themaskedbit.uploadgalleryapp.gallery.view.MainActivity;
import com.themaskedbit.uploadgalleryapp.gallery.view.editor.ImageEditFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBuilder {
    @ContributesAndroidInjector(modules = MainActivityModule.class)
    abstract MainActivity contributeMainActivityInjector();

    @ContributesAndroidInjector(modules = ImageEditFragmentModule.class)
    abstract ImageEditFragment contributeImageEditFragmentInjector();
}
