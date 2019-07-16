package com.themaskedbit.uploadgalleryapp.gallery.manager;

import android.net.Uri;

/*
 * Interface that should be implemented by any caching manager.
 */
public interface SharedPreferencesManager {
    /**
     * return user id from the cache
     */
    String getUserId();
    /**
     * Set user id back to the cache
     * @param id the id of the user to be stored
     */
    void setUserId(String id);
    /**
     * For temporarily caching the temp-file image uri, for uploading
     *
     * @param uri Uri of the cache file
     */
    void setImageUri(Uri uri);
    /**
     * For retruning the stored cached-file uri
     *
     * @return {@link Uri}
     */
    Uri getImageUri();

}
