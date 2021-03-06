package com.orange.ma.entreprise.views.main.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.utilities.LocaleManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ChangeLanguageDialog extends Dialog {

    @BindView(R.id.french_checkbox)
    CheckBox frenchCheckbox;
    @BindView(R.id.french_txt)
    TextView frenchTxt;
    @BindView(R.id.french_layout)
    LinearLayout frenchLayout;
    @BindView(R.id.arabic_checkbox)
    CheckBox arabicCheckbox;
    @BindView(R.id.arabic_txt)
    TextView arabicTxt;
    @BindView(R.id.arabic_layout)
    LinearLayout arabicLayout;
    @BindView(R.id.background1)
    ImageView background1;
    @BindView(R.id.background2)
    ImageView background2;
    private Activity context;
    private String selectedLang;

    public ChangeLanguageDialog(Activity context, String lang) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
        this.selectedLang = lang;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.change_language_dialog, null, false);
        ButterKnife.bind(this, view);
        this.setContentView(view);
        init();
    }

    @OnClick({R.id.french_layout, R.id.arabic_layout, R.id.close_btn, R.id.next_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.french_layout:
                frenchCheckbox.setChecked(true);
                arabicCheckbox.setChecked(false);
                frenchTxt.setTextColor(context.getResources().getColor(R.color.black));
                arabicTxt.setTextColor(context.getResources().getColor(R.color.grey));
                //selectedLang = LocaleManager.ENGLISH;
                break;
            case R.id.arabic_layout:
                frenchCheckbox.setChecked(false);
                arabicCheckbox.setChecked(true);
                frenchTxt.setTextColor(context.getResources().getColor(R.color.grey));
                arabicTxt.setTextColor(context.getResources().getColor(R.color.black));
                //selectedLang = LocaleManager.ARABIC;
                break;
            case R.id.close_btn:
                dismiss();
                break;
            case R.id.next_btn:
                if (frenchCheckbox.isChecked())
                    selectNewLang("fr");
                else
                    selectNewLang("ar");
                break;
            default:
                break;
        }
    }

    private void init() {
        if (selectedLang.equalsIgnoreCase("fr"))
            frenchLayout.performClick();
        else {
            arabicLayout.performClick();
            background1.setScaleX(-1);
            background2.setScaleX(-1);
        }

    }

    private void selectNewLang(String lang) {
        if (!selectedLang.equalsIgnoreCase(lang)) {
            LocaleManager.setNewLocale(context, lang);
            Intent intent = context.getIntent();
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
        }
        dismiss();
    }
}
