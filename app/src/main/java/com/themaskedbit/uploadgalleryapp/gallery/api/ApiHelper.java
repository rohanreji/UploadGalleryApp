package com.themaskedbit.uploadgalleryapp.gallery.api;

import android.net.Uri;
import com.themaskedbit.uploadgalleryapp.gallery.manager.ViewManager;
import com.themaskedbit.uploadgalleryapp.gallery.test.IdlingResourceApp;
import java.io.File;

/*
 * Interface that should be implemented by any API backend for this app.
 */
public interface ApiHelper {
    /**
     * Set presenter for the backend
     *
     * @param manager presenter to be attached to this Api
     */
    void setPresenter(ViewManager manager);
    /**
     * Upload images to backend
     *
     * @param uri Uri of the cache file
     * @param file file to which the image is save temporarily
     * @param idlingResource used for espresso testing
     * @param name name of the file to be saved in database
     */
    void uploadImages(Uri uri, File file, IdlingResourceApp idlingResource, String name);
    /**
     * Download images from backend
     *
     * @param idlingResource used for espresso testing
     */
    void downloadImages(IdlingResourceApp idlingResource);
    /**
     * To cancel uploads during on stop of the activity, if upload
     * is still pending. It avoids leaks.
     * *
     */
    void cancelUpload();
    /**
     * To cancel downloads during on stop of the activity, if upload
     * is still pending. It avoids leaks.
     * *
     */
    void cancelDownload();
}
