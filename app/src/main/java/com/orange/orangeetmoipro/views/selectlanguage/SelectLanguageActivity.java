package com.orange.orangeetmoipro.views.selectlanguage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.views.authentication.AuthenticationActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectLanguageActivity extends AppCompatActivity {

    @BindView(R.id.french_btn)
    Button frenchBtn;
    @BindView(R.id.arabic_btn)
    Button arabicBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        setContentView(R.layout.activity_select_language);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.french_btn, R.id.arabic_btn})
    public void onViewClicked(View view) {
        Intent intent = new Intent(SelectLanguageActivity.this, AuthenticationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        /*switch (view.getId()) {
            case R.id.french_btn:
                frenchBtn.setBackgroundColor(getResources().getColor(R.color.orange));
                arabicBtn.setBackground(getResources().getDrawable(R.drawable.grey_border_background));
                frenchBtn.setTextColor(getResources().getColor(R.color.white));
                arabicBtn.setTextColor(getResources().getColor(R.color.grey));
                break;
            case R.id.arabic_btn:
                arabicBtn.setBackgroundColor(getResources().getColor(R.color.orange));
                frenchBtn.setBackground(getResources().getDrawable(R.drawable.grey_border_background));
                arabicBtn.setTextColor(getResources().getColor(R.color.white));
                frenchBtn.setTextColor(getResources().getColor(R.color.grey));
                break;
        }*/
    }
}
