package com.themaskedbit.uploadgalleryapp.gallery.view.util;

import android.content.Context;
import android.content.DialogInterface;

import androidx.appcompat.app.AlertDialog;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class DialogBuilder {
    private Map<String, AlertDialog> alerts = new HashMap<>();

    @Inject
    DialogBuilder() {

    }

    public void showDialog(String key, Context context, int title, int message, int okStringId,
                           DialogInterface.OnClickListener okListener, int cancelStringId,
                           DialogInterface.OnClickListener cancelListener){
        AlertDialog dialog =alerts.get(key);
        if(dialog==null || !dialog.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle(title)
                    .setMessage(message);
            if(okStringId!=0) {
                builder.setPositiveButton(okStringId, okListener);
            }
            if(cancelStringId!=0) {
                builder.setNegativeButton(cancelStringId,cancelListener);
            }
            final AlertDialog alertDialog = builder.create();
            alerts.put(key, alertDialog);
            alertDialog.show();
        }

    }
}
