package br.com.jcfontes.monetcollector.utils;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.Toast;

public final class Notify {
    public static void showNotify(Context context, String message){
        Toast.makeText(context,message, Toast.LENGTH_SHORT).show();
    }

    public static void showSnackbar(View view, String strMessage) {
        Snackbar
            .make(view, strMessage, Snackbar.LENGTH_LONG)
            .setAction("Action", null)
                .show();
    }
}
