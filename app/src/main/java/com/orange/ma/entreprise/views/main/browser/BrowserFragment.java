package com.orange.ma.entreprise.views.main.browser;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Browser;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.views.base.BaseFragment;
import com.orange.ma.entreprise.views.main.MainActivity;

public class BrowserFragment extends BaseFragment {

    private String url;
    private int flag;
    PreferenceManager preferenceManager;

    public static BrowserFragment newInstance(String url) {
        BrowserFragment fragment = new BrowserFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    public BrowserFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferenceManager = new PreferenceManager.Builder(getContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        if (getArguments() != null)
            url = getArguments().getString("url");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        preferenceManager = new PreferenceManager.Builder(getContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
        //init();
        flag=0;
        return inflater.inflate(R.layout.fragment_browser, container, false);
    }



    @Override
    public void onResume() {
        super.onResume();
        if(flag==0)
            init();
        else
            ((MainActivity)getActivity()).moveToDashboardFragment();
        flag++;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            init();
    }

    private void init() {
        if (url != null) {
            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.black));
            CustomTabsIntent customTabsIntent = builder.build();
            if(!Utilities.isNullOrEmpty(preferenceManager.getValue(Constants.TOKEN_KEY, null))){
                Bundle headers = new Bundle();
                headers.putString(Constants.X_AUTHORIZATION,preferenceManager.getValue(Constants.TOKEN_KEY, ""));
                customTabsIntent.intent.putExtra(Browser.EXTRA_HEADERS, headers);
            }
            customTabsIntent.launchUrl(getContext(), Uri.parse(url));
        }
    }
}