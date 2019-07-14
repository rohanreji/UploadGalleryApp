package com.themaskedbit.uploadgalleryapp.gallery.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImageList {
    ArrayList<Image> images;

    public ImageList(){
        images=new ArrayList<>();
    }

    public ArrayList<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.addAll(images);
    }

    public void clear(){
        images.clear();
    }
    public void setImages(Image... images) {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        this.images.addAll(Arrays.asList(images));
    }
}