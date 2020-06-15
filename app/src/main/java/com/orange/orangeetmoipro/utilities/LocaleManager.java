package com.orange.orangeetmoipro.utilities;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;

import androidx.annotation.StringDef;

import com.orange.orangeetmoipro.datamanager.sharedpref.PreferenceManager;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Locale;

public class LocaleManager {

    @Retention(RetentionPolicy.SOURCE)
    @StringDef({ENGLISH, ARABIC})
    public @interface LocaleDef {
        String[] SUPPORTED_LOCALES = {ENGLISH, ARABIC};
    }

    public static final String ENGLISH = "en";
    public static final String ARABIC = "ar";

    public static Context setLocale(Context mContext) {
        return updateResources(mContext, getLanguagePref(mContext));
    }

    public static Context setNewLocale(Context mContext, @LocaleDef String language) {
        setLanguagePref(mContext, language);
        return updateResources(mContext, language);
    }

    private static String getLanguagePref(Context mContext) {
        PreferenceManager preferenceManager = new PreferenceManager.Builder(mContext, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
        return preferenceManager.getValue(Constants.LANGUAGE_KEY, ENGLISH);
    }

    private static void setLanguagePref(Context mContext, String localeKey) {
        PreferenceManager preferenceManager = new PreferenceManager.Builder(mContext, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
        preferenceManager.putValue(Constants.LANGUAGE_KEY, localeKey);
    }

    private static Context updateResources(Context context, String language) {
        Locale locale;
        if (language.equalsIgnoreCase(ARABIC))
            locale = new Locale(language, "TN");
        else
            locale = new Locale(language);
        Locale.setDefault(locale);
        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);
        context = context.createConfigurationContext(config);
        return context;
    }

    public static Locale getLocale(Resources res) {
        Configuration config = res.getConfiguration();
        return Build.VERSION.SDK_INT >= 24 ? config.getLocales().get(0) : config.locale;
    }
}
