package com.orange.orangeetmoipro.views.main.dashboard;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.datamanager.sharedpref.PreferenceManager;
import com.orange.orangeetmoipro.utilities.Constants;
import com.orange.orangeetmoipro.views.authentication.AuthenticationActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingsFragment extends Fragment {
    private PreferenceManager preferenceManager;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager.Builder(getContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @OnClick(R.id.logout)
    public void onViewClicked() {
        preferenceManager.putValue(Constants.IS_LOGGED_IN, false);
        startActivity(new Intent(getActivity(), AuthenticationActivity.class));
        getActivity().finish();

    }
}