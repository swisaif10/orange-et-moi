package com.orange.orangeetmoipro.utilities;

import android.content.Context;
import android.content.res.Configuration;

import com.orange.orangeetmoipro.datamanager.sharedpref.PreferenceManager;

import java.util.Locale;

public class LocaleManager {


    public static Context setLocale(Context context) {
        return updateResources(context, getLanguagePref(context));
    }

    public static Context setNewLocale(Context context, String language) {
        PreferenceManager preferenceManager = new PreferenceManager.Builder(context, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
        preferenceManager.putValue(Constants.LANGUAGE_KEY, language);
        return updateResources(context, language);
    }

    public static String getLanguagePref(Context context) {
        PreferenceManager preferenceManager = new PreferenceManager.Builder(context, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
        return preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr");
    }

    public static Context updateResources(Context context, String lang) {
        Locale myLocale;
        if (lang.equalsIgnoreCase("fr")) {
            myLocale = new Locale(lang, "FR");
        } else {
            myLocale = new Locale(lang, "TN");
        }
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
        return context;
    }
}
