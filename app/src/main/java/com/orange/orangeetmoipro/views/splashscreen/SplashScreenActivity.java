package com.orange.orangeetmoipro.views.splashscreen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.lifecycle.ViewModelProviders;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.datamanager.sharedpref.PreferenceManager;
import com.orange.orangeetmoipro.listeners.DialogButtonsClickListener;
import com.orange.orangeetmoipro.models.controlversion.ControlVersionData;
import com.orange.orangeetmoipro.utilities.Connectivity;
import com.orange.orangeetmoipro.utilities.Constants;
import com.orange.orangeetmoipro.utilities.Utilities;
import com.orange.orangeetmoipro.viewmodels.SpalshVM;
import com.orange.orangeetmoipro.views.authentication.AuthenticationActivity;
import com.orange.orangeetmoipro.views.base.BaseActivity;
import com.orange.orangeetmoipro.views.main.MainActivity;
import com.orange.orangeetmoipro.views.selectlanguage.SelectLanguageActivity;

public class SplashScreenActivity extends BaseActivity {

    private SpalshVM spalshVM;
    private Connectivity connectivity;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        spalshVM = ViewModelProviders.of(this).get(SpalshVM.class);
        connectivity = new Connectivity(this, this);

        preferenceManager = new PreferenceManager.Builder(this, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        spalshVM.getversionMutableLiveData().observe(this, this::handleVersionCheckResponse);
        new Handler().postDelayed(this::getVersionCheck, 3000);
    }

    private void goToNextActivity() {
        Intent intent;
        if (preferenceManager.getValue(Constants.FIRST_TIME, true)) {
            intent = new Intent(SplashScreenActivity.this, SelectLanguageActivity.class);
        } else {
            if (preferenceManager.getValue(Constants.IS_LOGGED_IN, false))
                intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            else
                intent = new Intent(SplashScreenActivity.this, AuthenticationActivity.class);
        }
        startActivity(intent);
        finish();
    }

    private void getVersionCheck() {
        if (connectivity.isConnected()) {
            spalshVM.getVersionCheck(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
        } else
            goToNextActivity();
    }

    private void handleVersionCheckResponse(ControlVersionData controlVersionData) {
        if (controlVersionData == null) {
            Utilities.showErrorPopup(this, getString(R.string.generic_error), "");
        } else {
            int code = controlVersionData.getHeader().getCode();
            if (code == 200 || code == 204) {
                String status = controlVersionData.getResponse().getStatus();
                if (status.equalsIgnoreCase("current")) {
                    goToNextActivity();
                } else {
                    Utilities.showUpdateDialog(this, "", "", status, new DialogButtonsClickListener() {
                        @Override
                        public void firstChoice() {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(controlVersionData.getResponse().getLink())));
                        }

                        @Override
                        public void secondChoice() {
                            goToNextActivity();
                        }
                    });
                }
            } else
                Utilities.showErrorPopup(this, controlVersionData.getResponse().getMessage(), "");
        }
    }
}