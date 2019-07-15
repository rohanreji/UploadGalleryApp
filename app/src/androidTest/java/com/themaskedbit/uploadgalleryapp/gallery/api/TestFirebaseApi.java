package com.themaskedbit.uploadgalleryapp.gallery.api;

import android.net.Uri;

import com.themaskedbit.uploadgalleryapp.gallery.manager.ViewManager;
import com.themaskedbit.uploadgalleryapp.gallery.model.Image;
import com.themaskedbit.uploadgalleryapp.gallery.model.ImageList;
import com.themaskedbit.uploadgalleryapp.gallery.model.TestUser;
import com.themaskedbit.uploadgalleryapp.gallery.model.User;
import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;

import java.io.File;

public class TestFirebaseApi implements ApiHelper {
    private TestUser testUser;
    private ImageList imageList;
    private ViewManager viewManager;

    public TestFirebaseApi(TestUser user, ImageList imageList){
        this.testUser = user;
        this.imageList = imageList;
    }
    @Override
    public void setPresenter(ViewManager manager) {
        this.viewManager = manager;
    }

    @Override
    public void uploadImages(Uri uri, File file, IdlingResourceApp idlingResource, String name) {

    }

    @Override
    public void downloadImages(IdlingResourceApp idlingResource) {
        final Image img1 = new Image("image1","https://picsum.photos/id/760/200/300");
        final Image img2 = new Image("image1","https://picsum.photos/id/237/200/300");
        final Image img3 = new Image("image1","https://picsum.photos/id/159/200/300?grayscale");

        switch (testUser.getId()) {
            case "user1": {
                imageList.clear();
                imageList.setImages(img1);
                viewManager.fetchDone(imageList.getImages());
                viewManager.fetchComplete();
                break;
            }
            case "user2": {
                imageList.clear();
                imageList.setImages(img1);
                viewManager.fetchDone(imageList.getImages());
                imageList.setImages(img2);
                viewManager.fetchDone(imageList.getImages());
                viewManager.fetchComplete();
                break;
            }
            case "user3": {
                imageList.clear();
                viewManager.fetchComplete();
                break;
            }
            case "error": {
                imageList.clear();
                Exception e =new Exception();
                viewManager.fetchError(e);
                break;
            }
            default: {

            }
        }
    }

    @Override
    public void cancelUpload() {

    }

    @Override
    public void cancelDownload() {

    }
}
