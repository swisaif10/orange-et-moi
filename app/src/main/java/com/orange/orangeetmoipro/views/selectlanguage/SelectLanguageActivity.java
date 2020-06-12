package com.orange.orangeetmoipro.views.selectlanguage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.datamanager.sharedpref.PreferenceManager;
import com.orange.orangeetmoipro.utilities.Constants;
import com.orange.orangeetmoipro.views.authentication.AuthenticationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectLanguageActivity extends AppCompatActivity {

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
        preferenceManager.putValue(Constants.FIRST_TIME, false);
        Intent intent = new Intent(SelectLanguageActivity.this, AuthenticationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
