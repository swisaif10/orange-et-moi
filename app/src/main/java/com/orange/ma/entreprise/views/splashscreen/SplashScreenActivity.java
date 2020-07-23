package com.orange.ma.entreprise.views.splashscreen;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Browser;
import android.util.Log;

import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.orange.ma.entreprise.OrangeEtMoiPro;
import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.fcm.NotificationObject;
import com.orange.ma.entreprise.listeners.OnDialogButtonsClickListener;
import com.orange.ma.entreprise.models.controlversion.ControlVersionData;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.viewmodels.SpalshVM;
import com.orange.ma.entreprise.views.authentication.AuthenticationActivity;
import com.orange.ma.entreprise.views.base.BaseActivity;
import com.orange.ma.entreprise.views.main.MainActivity;
import com.orange.ma.entreprise.views.selectlanguage.SelectLanguageActivity;

import static com.orange.ma.entreprise.utilities.Constants.ACTION_TYPE;
import static com.orange.ma.entreprise.utilities.Constants.APP_VIEW;
import static com.orange.ma.entreprise.utilities.Constants.DEEP_LINK;
import static com.orange.ma.entreprise.utilities.Constants.DEFAULT;
import static com.orange.ma.entreprise.utilities.Constants.ENDPOINT;
import static com.orange.ma.entreprise.utilities.Constants.ENDPOINT_TITLE;
import static com.orange.ma.entreprise.utilities.Constants.IMAGE;
import static com.orange.ma.entreprise.utilities.Constants.INSCRIPTION;
import static com.orange.ma.entreprise.utilities.Constants.IN_APP_URL;
import static com.orange.ma.entreprise.utilities.Constants.MESSAGE;
import static com.orange.ma.entreprise.utilities.Constants.OUT_APP_URL;
import static com.orange.ma.entreprise.utilities.Constants.TITLE;

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

        if (getIntent().getExtras() != null)
            handleIntent(getIntent());
        spalshVM.getversionMutableLiveData().observe(this, this::handleVersionCheckResponse);
        new Handler().postDelayed(this::getVersionCheck, 3000);

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        //Log.d("TAG", "onCreate: Dynamic link host "+ pendingDynamicLinkData.getLink());
                    }
                });
        //firebaseAnalyticsEvent
        OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().setCurrentScreen(this, "page_splash", null);
        Log.d("TAG", "onCreate: firebase "+FirebaseInstanceId.getInstance().getToken());
    }

    private void goToNextActivity() {
        Intent intent = getIntent();
        if(intent.getData()!=null)
            deepLink();
        else {
            if (preferenceManager.getValue(Constants.FIRST_TIME, true)) {
                intent = new Intent(SplashScreenActivity.this, SelectLanguageActivity.class);
            } else {
                if (preferenceManager.getValue(Constants.IS_LOGGED_IN, false) || preferenceManager.getValue(Constants.IS_AUTHENTICATED, false))
                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                else
                    intent = new Intent(SplashScreenActivity.this, AuthenticationActivity.class);
            }
            startActivity(intent);
        }
        finish();
    }

    private void handleIntent(Intent intent) {
        NotificationObject notification = new NotificationObject();
        notification.setTitle(intent.getStringExtra(TITLE));
        notification.setMessage(intent.getStringExtra(MESSAGE));
        notification.setImageUrl(intent.getStringExtra(IMAGE));
        notification.setActionType(intent.getStringExtra(ACTION_TYPE));
        notification.setEndPoint(intent.getStringExtra(ENDPOINT));
        notification.setEndPointTitle(intent.getStringExtra(ENDPOINT_TITLE));
        if(Utilities.isNullOrEmpty(preferenceManager.getValue(Constants.TOKEN_KEY, ""))){
            switch (notification.getActionType()) {
                case INSCRIPTION:
                    intent = new Intent(this, AuthenticationActivity.class);
                    intent.putExtra("link", notification.getActionType());
                    startActivity(intent);
                    break;
                default:
                    intent = new Intent(this, AuthenticationActivity.class);
                    startActivity(intent);
            }
        }else if (notification.getActionType() != null) {
            switch (notification.getActionType()) {
                case DEEP_LINK:
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(notification.getEndPoint()));
                    startActivity(intent);
                    break;
                case IN_APP_URL:
                    intent = new Intent(this, MainActivity.class);
                    if(!notification.getEndPoint().startsWith("http"))
                        notification.setEndPoint("https://"+notification.getEndPoint());
                    intent.putExtra(ENDPOINT, notification.getEndPoint());
                    intent.putExtra(ENDPOINT_TITLE, notification.getEndPointTitle());
                    startActivity(intent);
                    break;
                case OUT_APP_URL:
                    if(!notification.getEndPoint().startsWith("http"))
                        notification.setEndPoint("https://"+notification.getEndPoint());
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(ContextCompat.getColor(this, R.color.black));
                    CustomTabsIntent customTabsIntent = builder.build();
                    if(!Utilities.isNullOrEmpty(preferenceManager.getValue(Constants.TOKEN_KEY, null))){
                        Bundle headers = new Bundle();
                        headers.putString(Constants.X_AUTHORIZATION,preferenceManager.getValue(Constants.TOKEN_KEY, ""));
                        customTabsIntent.intent.putExtra(Browser.EXTRA_HEADERS, headers);
                    }
                    customTabsIntent.launchUrl(this, Uri.parse(notification.getEndPoint()));
                    break;
                case APP_VIEW:
                    intent = new Intent(this, MainActivity.class);
                    intent.putExtra(ENDPOINT, notification.getEndPoint());
                    startActivity(intent);
                    break;
                case DEFAULT:
                default:
                    intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
            }
        }

    }

    private void getVersionCheck() {
        if (connectivity.isConnected()) {
            spalshVM.getVersionCheck(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
        } else
            goToNextActivity();
    }

    private void handleVersionCheckResponse(ControlVersionData controlVersionData) {
        if (controlVersionData == null) {
            Utilities.showErrorPopup(this, getString(R.string.generic_error));
        } else {
            int code = controlVersionData.getHeader().getCode();
            if (code == 200 || code == 204) {
                String status = controlVersionData.getResponse().getStatus();
                if (status.equalsIgnoreCase("current")) {
                    goToNextActivity();
                } else {
                    Utilities.showUpdateDialog(this, "", "", status, new OnDialogButtonsClickListener() {
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
                Utilities.showErrorPopup(this, controlVersionData.getResponse().getMessage());
        }
    }

    private void deepLink() {
        String host = getIntent().getData().getHost();
        Intent intent;
        if(Utilities.isNullOrEmpty(preferenceManager.getValue(Constants.TOKEN_KEY, ""))){
            switch (host) {
                case "inscription":
                    intent = new Intent(SplashScreenActivity.this, AuthenticationActivity.class);
                    intent.putExtra("link", host);
                    startActivity(intent);
                    break;
                default:
                    startActivity(new Intent(SplashScreenActivity.this, AuthenticationActivity.class));
            }
        }else{
            switch (host) {
                case "inscription":
                    intent = new Intent(SplashScreenActivity.this, AuthenticationActivity.class);
                    intent.putExtra("link", host);
                    startActivity(intent);
                    break;
                case "login":
                    startActivity(new Intent(SplashScreenActivity.this, AuthenticationActivity.class));
                    break;
                case "setting":
                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    intent.putExtra("link", host);
                    startActivity(intent);
                    break;
                case "bonjour":
                    intent = new Intent(SplashScreenActivity.this, MainActivity.class);
                    startActivity(intent);
                    break;
                default:
                    startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            }
        }

    }
}