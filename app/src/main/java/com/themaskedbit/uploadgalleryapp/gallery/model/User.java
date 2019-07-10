package com.themaskedbit.uploadgalleryapp.gallery.model;

import com.themaskedbit.uploadgalleryapp.gallery.presenter.SharedPreferencesHelper;

public class User {
    private String id;
    public User(SharedPreferencesHelper sharedPreferencesHelper) {
        id = sharedPreferencesHelper.getUserId();
        if (id == null) {
            //right now hard coding the id.
            id = "0x1";
            sharedPreferencesHelper.setUserId(id);
        }
    }

    public String getId() {
        return id;
    }
}
