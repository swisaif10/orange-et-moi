package com.orange.ma.entreprise.views.main.browser;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.views.base.BaseFragment;
import com.orange.ma.entreprise.views.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

public class ExternalBrowserFragment extends BaseFragment {

    @BindView(R.id.loader)
    GifImageView loader;
    private String url;
    private int flag;
    private PreferenceManager preferenceManager;

    public static ExternalBrowserFragment newInstance(String url) {
        ExternalBrowserFragment fragment = new ExternalBrowserFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        fragment.setArguments(args);
        return fragment;
    }

    public ExternalBrowserFragment() {

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
        flag = 0;
        View view = inflater.inflate(R.layout.fragment_browser, container, false);
        ButterKnife.bind(this,view);
        loader.setVisibility(View.VISIBLE);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (flag == 0)
            init();
        else
            ((MainActivity) getActivity()).moveToDashboardFragment();
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
            if (!Utilities.isNullOrEmpty(preferenceManager.getValue(Constants.TOKEN_KEY, null)) && url.contains(Constants.EX_SSO_TOKEN)) {
                String token = preferenceManager.getValue(Constants.TOKEN_KEY, "").replace("Bearer ", "");
                url = url.replace(Constants.EX_SSO_TOKEN, token);
            }
            Utilities.openInBrowser(getContext(),url);
        }
    }
}
