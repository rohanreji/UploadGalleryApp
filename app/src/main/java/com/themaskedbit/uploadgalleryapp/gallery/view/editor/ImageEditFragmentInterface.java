package com.themaskedbit.uploadgalleryapp.gallery.view.editor;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;

import java.io.File;

import io.reactivex.Single;

public interface ImageEditFragmentInterface {

    void setEditorListener(@Nullable final IdlingResourceApp idlingResource,
                                EditorListener listener);

    interface EditorListener {
        void onEditorCropped(@Nullable final IdlingResourceApp idlingResource,
                                  File file, Single<Bitmap> cropping);

        void onEditorClosed();
    }
}
