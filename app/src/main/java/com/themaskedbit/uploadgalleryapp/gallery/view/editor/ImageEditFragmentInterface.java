package com.themaskedbit.uploadgalleryapp.gallery.view.editor;

import android.graphics.Bitmap;

import androidx.annotation.Nullable;

import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;

import java.io.File;

public interface ImageEditFragmentInterface {

    void setEditorListener(@Nullable final IdlingResourceApp idlingResource,
                           EditorListener listener);

    /*
     * interface for the inteacting with this editor fragment.
     */
    interface EditorListener {
        void onEditorSaved(@Nullable final IdlingResourceApp idlingResource,
                           File file, Bitmap cropping);

        void onEditorClosed();
    }
}
