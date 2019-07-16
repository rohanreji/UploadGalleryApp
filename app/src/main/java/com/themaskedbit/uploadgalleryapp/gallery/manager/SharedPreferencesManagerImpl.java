package com.themaskedbit.uploadgalleryapp.gallery.manager;

import android.content.SharedPreferences;
import android.net.Uri;

import androidx.annotation.NonNull;

public class SharedPreferencesManagerImpl implements SharedPreferencesManager {

    static final String ID_KEY="USER_ID";
    static final String IMG_URI="IMG_URI";
    private SharedPreferences sharedPreferences;

    public SharedPreferencesManagerImpl(@NonNull SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public String getUserId() {
        return sharedPreferences.getString(ID_KEY, null);
    }

    @Override
    public void setUserId(String id) {
        sharedPreferences.edit().putString(ID_KEY, id).commit();
    }

    @Override
    public void setImageUri(Uri uri) {
        sharedPreferences.edit().putString(IMG_URI, String.valueOf(uri)).commit();
    }

    @Override
    public Uri getImageUri() {
        return Uri.parse(sharedPreferences.getString(IMG_URI, ""));
    }


}
