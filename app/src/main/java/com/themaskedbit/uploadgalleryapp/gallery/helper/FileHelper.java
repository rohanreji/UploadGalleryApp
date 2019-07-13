package com.themaskedbit.uploadgalleryapp.gallery.helper;

import android.content.Context;

import com.themaskedbit.uploadgalleryapp.R;

import java.io.File;

public class FileHelper {
    public static File getCacheFile(final Context context) {
        String prefix = context.getResources().getString(R.string.temp_image_prefix);
        String suffix = context.getResources().getString(R.string.temp_image_suffix);
        File file = new File(context.getCacheDir(), prefix+suffix);
        if (!file.exists()) {
            try {

                File.createTempFile(prefix, suffix, context.getCacheDir());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return file;
    }

    public static boolean deleteCacheFile(final Context context) {
        String prefix = context.getResources().getString(R.string.temp_image_prefix);
        String suffix = context.getResources().getString(R.string.temp_image_suffix);
        File file = new File(context.getCacheDir(), prefix+suffix);
        if (!file.exists()) {
            return false;
        }

        boolean result = file.delete();
        return file.delete();
    }
}
