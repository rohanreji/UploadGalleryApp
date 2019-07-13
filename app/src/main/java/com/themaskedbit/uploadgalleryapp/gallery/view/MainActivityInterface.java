package com.themaskedbit.uploadgalleryapp.gallery.view;

import com.themaskedbit.uploadgalleryapp.gallery.model.Image;

import java.util.List;

//TODO: have methods that will help upload and fetch images, and managing the editor
public interface MainActivityInterface {
    void onFetchImagesStarted();
    void onFetchImageDone(List<Image> imageList);
    void onFetchImagesError(Exception e);
    void onEditorClosed();
    void onUploadImageError(Exception e);
    void onUploadImageCompleted(Image image);
    void onUploadImageStarted();
}
