package com.orange.ma.entreprise;

import android.content.Context;
import android.content.res.Configuration;

import androidx.multidex.MultiDexApplication;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.orange.ma.entreprise.utilities.LocaleManager;


public class OrangeEtMoiPro extends MultiDexApplication {

    private static OrangeEtMoiPro orangeApp;
    private FirebaseAnalytics mFirebaseAnalytics;


    public static OrangeEtMoiPro getInstance() {
        return orangeApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        orangeApp = this;

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.setLocale(base));
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        LocaleManager.setLocale(this);
    }


    public FirebaseAnalytics getFireBaseAnalyticsInstance() {
        return mFirebaseAnalytics;
    }


}
