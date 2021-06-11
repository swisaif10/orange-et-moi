package com.orange.ma.entreprise.utilities;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.util.DisplayMetrics;

import androidx.annotation.StringDef;

import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

public class LocaleManager {

    public static final String ENGLISH = "fr";
    public static final String ARABIC = "ar";

    public static Context setLocale(Context context) {
        return updateResources(context, getLanguagePref(context));
    }

    public static Context setNewLocale(Context context, @LocaleDef String language) {
        setLanguagePref(context, language);
        return updateResources(context, language);
    }

    public static String getLanguagePref(Context context) {
        PreferenceManager preferenceManager = new PreferenceManager.Builder(context, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        return preferenceManager.getValue(Constants.LANGUAGE_KEY, ENGLISH);
    }

    private static void setLanguagePref(Context context, String localeKey) {
        PreferenceManager preferenceManager = new PreferenceManager.Builder(context, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        preferenceManager.putValue(Constants.LANGUAGE_KEY, localeKey);
    }

    private static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration configuration = new Configuration(res.getConfiguration());
        DisplayMetrics displayMetrics = res.getDisplayMetrics();
        configuration.setLocale(locale);
        res.updateConfiguration(configuration, displayMetrics);
        return context;
    }

    public static String getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return Build.VERSION.SDK_INT >= 24 ? config.getLocales().get(0).getLanguage() : config.locale.getLanguage();
    }

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ENGLISH, ARABIC})
    public @interface LocaleDef {
        String[] SUPPORTED_LOCALES = {ENGLISH, ARABIC};
    }
}
