package com.orange.orangeetmoipro.views.splashscreen;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.datamanager.sharedPref.PreferenceManager;
import com.orange.orangeetmoipro.listeners.VersionControlChoiceDialogClickListener;
import com.orange.orangeetmoipro.models.controlversion.ControlVersionData;
import com.orange.orangeetmoipro.utilities.Connectivity;
import com.orange.orangeetmoipro.utilities.Constants;
import com.orange.orangeetmoipro.utilities.Utilities;
import com.orange.orangeetmoipro.viewmodels.SpalshVM;
import com.orange.orangeetmoipro.views.authentication.AuthenticationActivity;
import com.orange.orangeetmoipro.views.main.MainActivity;
import com.orange.orangeetmoipro.views.selectlanguage.SelectLanguageActivity;


public class SplashScreenActivity extends AppCompatActivity {

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
        new Handler().postDelayed(() -> getVersionCheck(), 3000);
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void getVersionCheck() {
        if (connectivity.isConnected()) {
            spalshVM.getVersionCheck();
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
                    Utilities.showUpdateDialog(this, controlVersionData.getResponse().getMessage(), getString(R.string.update_dialog_title), status, new VersionControlChoiceDialogClickListener() {
                        @Override
                        public void onAccept() {
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(controlVersionData.getResponse().getLink())));
                        }

                        @Override
                        public void onRefuse() {
                            goToNextActivity();
                        }
                    });
                }
            } else
                Utilities.showErrorPopup(this, controlVersionData.getResponse().getMessage(), "");
        }
    }
}