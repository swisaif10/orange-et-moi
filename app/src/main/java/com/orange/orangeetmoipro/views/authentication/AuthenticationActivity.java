package com.orange.orangeetmoipro.views.authentication;

import android.os.Bundle;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.views.authentication.fragments.LoginFragment;
import com.orange.orangeetmoipro.views.authentication.fragments.SignInFragment;
import com.orange.orangeetmoipro.views.base.BaseActivity;

import butterknife.ButterKnife;

public class AuthenticationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);

        if (getIntent().getBooleanExtra("from_logout",false))
            addFragment(new LoginFragment(),"login");
        else
            addFragment(new SignInFragment(),"sign_in");
    }

    @Override
    public void onBackPressed(){
        if (getSupportFragmentManager().getBackStackEntryCount() == 1){
            finish();
        }
        else {
            super.onBackPressed();
        }
    }
}
