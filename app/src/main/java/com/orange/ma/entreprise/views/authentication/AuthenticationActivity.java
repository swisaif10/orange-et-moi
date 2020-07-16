package com.orange.ma.entreprise.views.authentication;

import android.os.Bundle;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.views.authentication.fragments.LoginFragment;
import com.orange.ma.entreprise.views.authentication.fragments.SignInFragment;
import com.orange.ma.entreprise.views.base.BaseActivity;

import butterknife.ButterKnife;

public class AuthenticationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);

        if (getIntent().getStringExtra("link") != null && getIntent().getStringExtra("link").equalsIgnoreCase("inscription"))
            addFragment(new SignInFragment(), "sigin");
        else
            addFragment(new LoginFragment(), "login");
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
            finish();
        } else {
            super.onBackPressed();
        }
    }
}
