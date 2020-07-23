package com.orange.ma.entreprise.views.main.webview;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.views.base.BaseFragment;
import com.orange.ma.entreprise.views.main.MainActivity;

import java.util.HashMap;
import java.util.Map;

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
    private String urlVebView;
    private Connectivity connectivity;
    private PreferenceManager preferenceManager;

    public static WebViewFragment newInstance(String url, String title,String urlVebView) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("title", title);
        if(!Utilities.isNullOrEmpty(urlVebView)) args.putString("urlVebView", urlVebView);
        fragment.setArguments(args);
        return fragment;
    }

    public static WebViewFragment newInstance(String url, String title) {
        return WebViewFragment.newInstance(url, title, null);
    }

    public WebViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        connectivity = new Connectivity(getContext(), this);
        preferenceManager = new PreferenceManager.Builder(getContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        if (getArguments() != null) {
            url = getArguments().getString("url");
            titleTxt = getArguments().getString("title");
            titleTxt = getArguments().getString("title");
            urlVebView = getArguments().getString("urlVebView");
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
//        ((MainActivity) getActivity()).tabLayout.getTabAt(0).select();
//        if (fragmentNavigation != null)
//            fragmentNavigation.pushFragment(new DashboardFragment());
        ((MainActivity) getActivity()).onBackPressed();
    }

    private void init() {

        title.setText(titleTxt);
        loader.setVisibility(View.VISIBLE);
        webview.setOnLongClickListener(v -> true);
        webview.setLongClickable(false);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                loader.setVisibility(View.GONE);
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {
                Log.d("TAG", "shouldInterceptRequest: "+url);
                return super.shouldInterceptRequest(view, url);
            }
        });
        webview.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleManager.setLocale(getContext());
        }
        if (connectivity.isConnected()) {
            if (!Utilities.isNullOrEmpty(preferenceManager.getValue(Constants.TOKEN_KEY, null)) && !Utilities.isNullOrEmpty(urlVebView)) {
                String token = preferenceManager.getValue(Constants.TOKEN_KEY, "").replace("Bearer ","");
                urlVebView = urlVebView.concat("?url=").concat(url).concat("&token=").concat(token);
                webview.loadUrl(urlVebView);
            } else
                webview.loadUrl(url);

        }
    }

}