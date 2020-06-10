package com.orange.orangeetmoipro.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.listeners.VersionControlChoiceDialogClickListener;


public class Utilities {

    public static void showErrorPopup(Context context, String message, String title) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.server_error_dialog, null, false);
        Button ok = view.findViewById(R.id.ok_btn);
        TextView titleTv = view.findViewById(R.id.title);
        TextView msg = view.findViewById(R.id.message);
        ConstraintLayout container = view.findViewById(R.id.container);

        //titleTv.setText(title);
        msg.setText(message);

        ok.setOnClickListener(v -> dialog.dismiss());
        container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    public static void showErrorPopup(Context context, String message, String title, View.OnClickListener onClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.server_error_dialog, null, false);
        Button ok = view.findViewById(R.id.ok_btn);
        TextView titleTv = view.findViewById(R.id.title);
        TextView msg = view.findViewById(R.id.message);
        ConstraintLayout container = view.findViewById(R.id.container);

        //titleTv.setText(title);
        msg.setText(message);
        ok.setOnClickListener(v -> {
            dialog.dismiss();
            onClickListener.onClick(v);
        });
        //container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    public static void showUpdateDialog(Context context, String message, String title, String status, VersionControlChoiceDialogClickListener versionControlChoiceDialogClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.version_update_dialog, null, false);
        Button update = view.findViewById(R.id.update_btn);
        Button cancel = view.findViewById(R.id.cancel_btn);
        TextView titleTv = view.findViewById(R.id.title);
        TextView msg = view.findViewById(R.id.message);
        ConstraintLayout container = view.findViewById(R.id.container);

        //titleTv.setText(title);
        //msg.setText(message);

        if (status.equalsIgnoreCase("blocked"))
            cancel.setVisibility(View.GONE);

        update.setOnClickListener(v -> {
            dialog.dismiss();
            versionControlChoiceDialogClickListener.onAccept();
        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
            versionControlChoiceDialogClickListener.onRefuse();
        });
        //container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    public static String getUIDFromDevice(Context context) {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public static void hideSoftKeyboard(Context context, View view) {
        if (context == null || view == null) {
            return;
        }
        try {
            InputMethodManager inputMethodManager =
                    (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception e) {
            Log.e("", e.getMessage());
        }
    }

}
