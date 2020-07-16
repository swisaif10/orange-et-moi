package com.orange.ma.entreprise.views.main.dialogs;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.listeners.OnDialogButtonsClickListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LogoutDialog extends Dialog {

    @BindView(R.id.background1)
    ImageView background1;
    @BindView(R.id.background2)
    ImageView background2;
    private Activity context;
    private String lang;
    private OnDialogButtonsClickListener onDialogButtonsClickListener;

    public LogoutDialog(Activity context, String lang, OnDialogButtonsClickListener onDialogButtonsClickListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
        this.onDialogButtonsClickListener = onDialogButtonsClickListener;
        this.lang = lang;
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
                onDialogButtonsClickListener.firstChoice();
                break;
            default:
                break;
        }
    }

    private void init() {
        if (lang.equalsIgnoreCase("ar")) {
            background1.setScaleX(-1);
            background2.setScaleX(-1);
        }
    }
}
