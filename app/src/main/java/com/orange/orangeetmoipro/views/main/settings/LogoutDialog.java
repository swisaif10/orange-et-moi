package com.orange.orangeetmoipro.views.main.settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.utilities.LocaleManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class LogoutDialog extends Dialog {

    public interface DialogCallBack{
        void onConfirm(LogoutDialog dialog);
    }

    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.message)
    TextView message;
    @BindView(R.id.background1)
    ImageView background1;
    @BindView(R.id.background2)
    ImageView background2;
    @BindView(R.id.confirm_btn)
    Button confirmBtn;
    @BindView(R.id.cancel_btn)
    Button cancelBtn;
    private Activity context;
    private AlertDialog alertDialog;
    private DialogCallBack callBack;
    private String selectedLang;

    public LogoutDialog(Activity context,String lang,DialogCallBack callBack) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
        this.callBack = callBack;
        this.selectedLang = lang;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = LayoutInflater.from(context).inflate(R.layout.logout_dialog, null, false);
        ButterKnife.bind(this, view);
        this.setContentView(view);
        init();
    }

    @OnClick({R.id.confirm_btn, R.id.cancel_btn})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.cancel_btn:
                dismiss();
                break;
            case R.id.confirm_btn:
                callBack.onConfirm(this);
                break;
            default:
                break;
        }
    }

    private void init() {
        if (selectedLang.equalsIgnoreCase("ar")){
            background1.setScaleX(-1);
            background2.setScaleX(-1);
        }

    }

}
