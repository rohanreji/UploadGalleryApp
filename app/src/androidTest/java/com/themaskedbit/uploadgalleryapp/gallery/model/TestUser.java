package com.themaskedbit.uploadgalleryapp.gallery.model;

import com.themaskedbit.uploadgalleryapp.gallery.manager.SharedPreferencesManager;

public class TestUser extends User {

    private static String test;
    private String id;

    public TestUser(SharedPreferencesManager sharedPreferencesManager) {
        super(sharedPreferencesManager);
        id="0x1";
    }

    @Override
    public String getId() {
        return test!=null ? test: id;
    }

    public static void set(String set) {
        test = set;
    }

    public static void reset() {
        test = null;
    }
}
