package com.themaskedbit.uploadgalleryapp.gallery.helper;

import android.content.Context;
import android.graphics.Bitmap;

import com.themaskedbit.uploadgalleryapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

/*
 * File helper class
 */
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

    public static File saveBitmap(final File file, final Bitmap bitmap) {
        OutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }

}
