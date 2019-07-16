package com.themaskedbit.uploadgalleryapp.gallery.view.gallery;

import com.themaskedbit.uploadgalleryapp.gallery.model.Image;

import java.util.List;

public interface GalleryInterface {
    void setImages(List<Image> images);
    void addImage(Image image);
}
