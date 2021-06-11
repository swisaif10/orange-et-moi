package com.orange.ma.entreprise.views.main.webview;

import android.app.AlertDialog;
import android.content.Context;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.EncryptedSharedPreferences;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.views.base.BaseFragment;
import com.orange.ma.entreprise.views.main.MainActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;

public class WebViewFragment extends BaseFragment {

    @BindView(R.id.webview)
    WebView webview;
    @BindView(R.id.loader)
    GifImageView loader;
    @BindView(R.id.title)
    TextView title;
    private String url;
    private String titleTxt;
    private Connectivity connectivity;
    private PreferenceManager preferenceManager;
    private EncryptedSharedPreferences encryptedSharedPreferences;

    public WebViewFragment() {
    }

    public static WebViewFragment newInstance(String url, String title) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectivity = new Connectivity(getContext(), this);
        preferenceManager = new PreferenceManager.Builder(getContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        encryptedSharedPreferences = new EncryptedSharedPreferences();

        encryptedSharedPreferences.getEncryptedSharedPreferences(getContext());

        if (getArguments() != null) {
            url = getArguments().getString("url");
            titleTxt = getArguments().getString("title");
            titleTxt = getArguments().getString("title");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_web_view, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden)
            init();
    }

    @OnClick(R.id.back_btn)
    public void onViewClicked() {
        ((MainActivity) getActivity()).onBackPressed();
    }

    private void init() {

        title.setText(titleTxt);
        loader.setVisibility(View.VISIBLE);
        webview.setOnLongClickListener(v -> true);
        webview.setLongClickable(false);
        webview.getSettings().setJavaScriptEnabled(false);
        webview.setWebViewClient(new MyBrowser());
        webview.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleManager.setLocale(getContext());
        }
        if (connectivity.isConnected()) {
            if (!Utilities.isNullOrEmpty(encryptedSharedPreferences.getValue(Constants.TOKEN_KEY, null)) && url.contains(Constants.EX_SSO_TOKEN)) {
                String token = encryptedSharedPreferences.getValue(Constants.TOKEN_KEY, "").replace("Bearer ", "");
                url = url.replace(Constants.EX_SSO_TOKEN, token);
            }
            webview.loadUrl(url);

        }
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Attention");
            builder.setPositiveButton("Ok", (dialog, which) -> handler.proceed());
            builder.setNegativeButton("Cancel", (dialog, which) -> handler.cancel());
            final AlertDialog dialog = builder.create();
            dialog.show();

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            loader.setVisibility(View.GONE);
        }
    }


}