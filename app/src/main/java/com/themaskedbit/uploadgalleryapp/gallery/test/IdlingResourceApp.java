package com.themaskedbit.uploadgalleryapp.gallery.test;


import androidx.annotation.Nullable;
import androidx.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicBoolean;

public class IdlingResourceApp implements IdlingResource {

    @Nullable
    private volatile ResourceCallback callback;
    private AtomicBoolean isIdle = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return isIdle.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.callback = callback;
    }

    public void setIdleState(boolean isIdleNow) {
        isIdle.set(isIdleNow);
        if (isIdleNow && callback != null) {
            callback.onTransitionToIdle();
        }
    }

    public static void set(IdlingResourceApp resource, boolean state) {
        if (resource != null) {
            resource.setIdleState(state);
        }
    }
}
