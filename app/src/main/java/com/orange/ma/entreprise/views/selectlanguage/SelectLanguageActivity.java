package com.orange.ma.entreprise.views.selectlanguage;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.views.authentication.AuthenticationActivity;
import com.orange.ma.entreprise.views.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectLanguageActivity extends BaseActivity {


    @BindView(R.id.french_checkbox)
    CheckBox frenchCheckbox;
    @BindView(R.id.french_txt)
    TextView frenchTxt;
    @BindView(R.id.arabic_checkbox)
    CheckBox arabicCheckbox;
    @BindView(R.id.arabic_txt)
    TextView arabicTxt;
    private String selectedLang = LocaleManager.ENGLISH;
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

    @OnClick({R.id.french_layout, R.id.arabic_layout, R.id.next_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.french_layout:
                frenchCheckbox.setChecked(true);
                arabicCheckbox.setChecked(false);
                frenchTxt.setTextColor(getResources().getColor(R.color.black));
                arabicTxt.setTextColor(getResources().getColor(R.color.grey));
                selectedLang = LocaleManager.ENGLISH;
                break;
            case R.id.arabic_layout:
                frenchCheckbox.setChecked(false);
                arabicCheckbox.setChecked(true);
                frenchTxt.setTextColor(getResources().getColor(R.color.grey));
                arabicTxt.setTextColor(getResources().getColor(R.color.black));
                selectedLang = LocaleManager.ARABIC;
                break;
            case R.id.next_btn:
                setNewLocale(this, selectedLang);
                break;
            default:
                break;
        }
    }

    private void setNewLocale(AppCompatActivity mContext, @LocaleManager.LocaleDef String language) {
        LocaleManager.setNewLocale(this, language);

        preferenceManager.putValue(Constants.FIRST_TIME, false);

        Intent intent = new Intent(this, AuthenticationActivity.class);
        startActivity(intent);
        finish();
    }
}
