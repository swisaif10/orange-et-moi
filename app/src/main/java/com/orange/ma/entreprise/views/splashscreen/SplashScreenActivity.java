package com.orange.ma.entreprise.views.splashscreen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.lifecycle.ViewModelProviders;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.listeners.DialogButtonsClickListener;
import com.orange.ma.entreprise.models.controlversion.ControlVersionData;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.viewmodels.SpalshVM;
import com.orange.ma.entreprise.views.authentication.AuthenticationActivity;
import com.orange.ma.entreprise.views.base.BaseActivity;
import com.orange.ma.entreprise.views.main.MainActivity;
import com.orange.ma.entreprise.views.selectlanguage.SelectLanguageActivity;

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

        if (getIntent().getData() != null)
            deepLink();
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

    private void deepLink() {
        String host = getIntent().getData().getHost();
        Intent intent;
        switch (host) {
            case "inscription":
                intent = new Intent(SplashScreenActivity.this, AuthenticationActivity.class);
                intent.putExtra("link", host);
                startActivity(intent);
                break;
            case "login":
                startActivity(new Intent(SplashScreenActivity.this, AuthenticationActivity.class));
                break;
            case "parametres":
                intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                intent.putExtra("link", host);
                startActivity(intent);
                break;
            default:
                startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
        }
    }
}