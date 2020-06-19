package com.orange.orangeetmoipro.views.selectlanguage;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.datamanager.sharedpref.PreferenceManager;
import com.orange.orangeetmoipro.utilities.Constants;
import com.orange.orangeetmoipro.views.authentication.AuthenticationActivity;
import com.orange.orangeetmoipro.views.base.BaseActivity;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectLanguageActivity extends BaseActivity {

    @BindView(R.id.french_btn)
    Button frenchBtn;
    @BindView(R.id.arabic_btn)
    Button arabicBtn;
    private PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_language);
        ButterKnife.bind(this);

        preferenceManager = new PreferenceManager.Builder(this, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

    }

    @OnClick({R.id.french_btn, R.id.arabic_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.french_btn:
                changeLanguage("fr");
                break;
            case R.id.arabic_btn:
                //changeLanguage("ar");
                break;
        }
    }

    private void changeLanguage(String lang) {
        Locale myLocale;
        if (lang.equalsIgnoreCase("fr")) {
            myLocale = new Locale(lang, "FR");
        } else {
            myLocale = new Locale(lang, "TN");
        }
        Locale.setDefault(myLocale);
        Configuration config = new Configuration();
        config.locale = myLocale;
        getResources().updateConfiguration(config, getResources().getDisplayMetrics());

        preferenceManager.putValue(Constants.LANGUAGE_KEY, lang);
        preferenceManager.putValue(Constants.FIRST_TIME, false);

        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivity(intent);
        finish();
    }

}
