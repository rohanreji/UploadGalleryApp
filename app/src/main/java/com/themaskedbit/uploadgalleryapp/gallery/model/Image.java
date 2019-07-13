package com.themaskedbit.uploadgalleryapp.gallery.model;

import androidx.annotation.NonNull;

public class Image{
    public String imageName;

    public String imageURL;

    public Image() {

    }

    public Image(String name, String url) {

        this.imageName = name;
        this.imageURL= url;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageURL() {
        return imageURL;
    }

}