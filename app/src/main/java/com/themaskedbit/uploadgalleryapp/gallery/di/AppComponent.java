package com.themaskedbit.uploadgalleryapp.gallery.di;

import com.themaskedbit.uploadgalleryapp.UploadGalleryApp;

import javax.inject.Singleton;

import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(modules = {AndroidInjectionModule.class, AndroidSupportInjectionModule.class, AppModule.class, ActivityBuilder.class})
public interface AppComponent extends AndroidInjector<UploadGalleryApp> {
    void inject(UploadGalleryApp app);
}
