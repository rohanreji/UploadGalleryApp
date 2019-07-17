package com.themaskedbit.uploadgalleryapp.gallery.view;

import android.net.Uri;

import com.themaskedbit.uploadgalleryapp.gallery.model.Image;

import java.util.List;

//This interface have methods that will help upload and fetch images, and managing the editor
public interface MainActivityInterface {
    void onFetchImagesStarted();
    void onFetchImageOver();
    void onFetchImageDone(List<Image> imageList);
    void onFetchImagesError(Exception e);
    void onEditorClosed();
    void onUploadImageError(Exception e);
    void onUploadImageCompleted(Image image);
    void onUploadImageStarted();

    //no need of sub-interface as the activity call the presenter function only for starting fetch. And that is handled by DI.
}
