package com.orange.orangeetmoipro.views.authentication;

import android.os.Bundle;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.views.authentication.fragments.SigninFragment;
import com.orange.orangeetmoipro.views.base.BaseActivity;

import butterknife.ButterKnife;

public class AuthenticationActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);
        ButterKnife.bind(this);
        getSupportFragmentManager().beginTransaction().
                add(R.id.container, new SigninFragment()).
                commit();
    }
}
