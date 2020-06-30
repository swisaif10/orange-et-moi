package com.orange.orangeetmoipro.utilities;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.listeners.DialogButtonsClickListener;


public class Utilities {

    public static void showErrorPopup(Context context, String message, String title) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.server_error_dialog, null, false);
        Button ok = view.findViewById(R.id.ok_btn);
        TextView msg = view.findViewById(R.id.message);
        ConstraintLayout container = view.findViewById(R.id.container);

        msg.setText(message);

        ok.setOnClickListener(v -> dialog.dismiss());
        container.setOnClickListener(v -> dialog.dismiss());
        dialog.setContentView(view);
        dialog.show();
    }

    public static void showUpdateDialog(Context context, String message, String title, String status, DialogButtonsClickListener dialogButtonsClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.version_update_dialog, null, false);
        Button update = view.findViewById(R.id.update_btn);
        Button cancel = view.findViewById(R.id.cancel_btn);
        TextView titleTv = view.findViewById(R.id.title);
        TextView msg = view.findViewById(R.id.message);

        titleTv.setText(title);
        msg.setText(message);

        if (status.equalsIgnoreCase("blocked"))
            cancel.setVisibility(View.GONE);

        update.setOnClickListener(v -> {
            dialog.dismiss();
            dialogButtonsClickListener.firstChoice();
        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
            dialogButtonsClickListener.secondChoice();
        });
        dialog.setContentView(view);
        dialog.show();
    }

    public static void showCompleteProfileDialog(Context context, String message, String title, DialogButtonsClickListener dialogButtonsClickListener) {

        if (context == null) {
            return;
        }

        final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);

        View view = LayoutInflater.from(context).inflate(R.layout.complete_profile_dialog, null, false);
        Button ok = view.findViewById(R.id.ok_btn);
        Button cancel = view.findViewById(R.id.cancel_btn);
        TextView titleTv = view.findViewById(R.id.title);
        TextView msg = view.findViewById(R.id.description);


        ok.setOnClickListener(v -> {
            dialog.dismiss();
            dialogButtonsClickListener.firstChoice();
        });
        cancel.setOnClickListener(v -> {
            dialog.dismiss();
            dialogButtonsClickListener.secondChoice();
        });
        dialog.setContentView(view);
        dialog.show();
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

    public static int calculate(String password, String lang) {
        int score = 0;
        boolean upper = false;
        boolean lower = false;
        boolean digit = false;
        boolean specialChar = false;
        boolean arabicChar = false;
        for (int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if (!arabicChar && isArabic(password.codePointAt(i))) {
                arabicChar = true;
                score++;
            } else if (!specialChar && !isArabic(password.codePointAt(i)) && !Character.isLetterOrDigit(c)) {
                score++;
                specialChar = true;
            } else {
                if (!digit && Character.isDigit(c)) {
                    score++;
                    digit = true;
                } else if (Character.isUpperCase(c) && !upper && !arabicChar) {
                    score++;
                    upper = true;
                } else if (Character.isLowerCase(c) && !lower && !arabicChar) {
                    score++;
                    lower = true;
                }
            }
        }
        return score;
    }

    public static boolean isArabic(int c) {
        return c >= 0x0600 && c <= 0x06E0;
    }
}
