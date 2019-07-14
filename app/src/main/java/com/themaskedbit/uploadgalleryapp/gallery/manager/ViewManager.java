package com.themaskedbit.uploadgalleryapp.gallery.manager;

import androidx.annotation.Nullable;

import com.themaskedbit.uploadgalleryapp.gallery.model.Image;
import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;
import com.themaskedbit.uploadgalleryapp.gallery.view.MainActivityInterface;
import com.themaskedbit.uploadgalleryapp.gallery.view.editor.ImageEditFragmentInterface;

import java.util.List;

public interface ViewManager extends ImageEditFragmentInterface.EditorListener {
    void setView(MainActivityInterface view);
    void start(@Nullable final IdlingResourceApp idlingResource);
    void uploadSuccess(Image image);
    void uploadError(Exception e);
    void fetchDone(List<Image> imageList);
    void fetchComplete();
    void fetchError(Exception e);
    void stop();
}
