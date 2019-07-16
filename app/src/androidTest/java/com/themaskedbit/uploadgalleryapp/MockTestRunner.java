package com.themaskedbit.uploadgalleryapp;

import android.app.Application;
import android.content.Context;

import androidx.test.runner.AndroidJUnitRunner;

import com.themaskedbit.uploadgalleryapp.gallery.TestUploadGalleryApp;


public class MockTestRunner extends AndroidJUnitRunner {
    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, TestUploadGalleryApp.class.getName(), context);
    }
}