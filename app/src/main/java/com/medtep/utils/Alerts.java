package com.medtep.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by Alfonso on 08/05/2016.
 */

/**
 * Static class which make showing alerts easier
 */
public class Alerts {

    public static void showSimpleError(Context context, String msg) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton("OK", null)
                .show();
    }

    public static void showSimpleError(Context context, String msg, String option,
                                       final Runnable listener) {
        new AlertDialog.Builder(context)
                .setMessage(msg)
                .setPositiveButton(option, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.run();
                    }
                })
                .show();
    }
}
