package com.themaskedbit.uploadgalleryapp.gallery.presenter;

import android.net.Uri;

public interface SharedPreferencesHelper {
    String getUserId();
    void setUserId(String id);

    void setImageUri(Uri uri);
    Uri getImageUri();
}
