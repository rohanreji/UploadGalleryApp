package com.themaskedbit.uploadgalleryapp.gallery.manager;

import androidx.annotation.Nullable;

import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;
import com.themaskedbit.uploadgalleryapp.gallery.view.MainActivityInterface;
import com.themaskedbit.uploadgalleryapp.gallery.view.editor.ImageEditFragmentInterface;

public interface ViewManager extends ImageEditFragmentInterface.EditorListener {
    void setView(MainActivityInterface view);
    void start(@Nullable final IdlingResourceApp idlingResource);
    void loadImages(@Nullable final IdlingResourceApp idlingResource);
    void stop();
}
