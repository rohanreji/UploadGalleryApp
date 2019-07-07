package com.themaskedbit.uploadgalleryapp.gallery.presenter;

import android.content.SharedPreferences;

import androidx.annotation.NonNull;

public class SharedPreferencesHelperImpl implements SharedPreferencesHelper {

    static final String ID_KEY="USER_ID";
    private SharedPreferences sharedPreferences;

    public SharedPreferencesHelperImpl(@NonNull SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public String getUserId() {
        return sharedPreferences.getString(ID_KEY, null);
    }

    @Override
    public void setUserId(String id) {
        sharedPreferences.edit().putString(ID_KEY, id).apply();
    }
}
