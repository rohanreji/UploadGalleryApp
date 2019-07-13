package com.themaskedbit.uploadgalleryapp.gallery.manager;

import android.net.Uri;

import java.io.File;

public interface SharedPreferencesManager {
    String getUserId();
    void setUserId(String id);

    void setImageUri(Uri uri);
    Uri getImageUri();

}
