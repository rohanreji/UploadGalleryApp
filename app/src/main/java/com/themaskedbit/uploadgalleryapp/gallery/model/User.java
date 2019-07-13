package com.themaskedbit.uploadgalleryapp.gallery.model;

import com.themaskedbit.uploadgalleryapp.gallery.manager.SharedPreferencesManager;

public class User {
    private String id;
    public User(SharedPreferencesManager sharedPreferencesManager) {
        id = sharedPreferencesManager.getUserId();
        if (id == null) {
            //right now hard coding the id.
            id = "0x1";
            sharedPreferencesManager.setUserId(id);
        }
    }

    public String getId() {
        return id;
    }
}
