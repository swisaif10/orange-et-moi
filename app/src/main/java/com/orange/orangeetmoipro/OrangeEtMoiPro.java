package com.orange.orangeetmoipro;

import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;

import androidx.annotation.NonNull;

import com.orange.orangeetmoipro.datamanager.sharedpref.PreferenceManager;
import com.orange.orangeetmoipro.utilities.Constants;

import java.util.Locale;

public class OrangeEtMoiPro extends Application {

    private PreferenceManager preferenceManager;
    private Locale locale = null;

    @Override
    public void onCreate() {
        super.onCreate();

        preferenceManager = new PreferenceManager.Builder(this, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        String lang = preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr");
        if (lang.equalsIgnoreCase("fr")) {
            locale = new Locale(lang, "FR");
        } else {
            locale = new Locale(lang, "TN");
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }
}
