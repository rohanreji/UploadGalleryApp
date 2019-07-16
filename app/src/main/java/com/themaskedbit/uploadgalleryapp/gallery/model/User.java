package com.themaskedbit.uploadgalleryapp.gallery.model;

import com.themaskedbit.uploadgalleryapp.gallery.manager.SharedPreferencesManager;

import java.util.UUID;

public class User {
    private String id;
    public User(SharedPreferencesManager sharedPreferencesManager) {
        id = sharedPreferencesManager.getUserId();
        if (id == null) {
            id = UUID.randomUUID().toString();
            sharedPreferencesManager.setUserId(id);
        }
    }

    public String getId() {
        return id;
    }
}
