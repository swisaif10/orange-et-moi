package com.orange.ma.entreprise.views.main.webview;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.views.base.BaseFragment;

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

    public static WebViewFragment newInstance(String url, String title) {
        WebViewFragment fragment = new WebViewFragment();
        Bundle args = new Bundle();
        args.putString("url", url);
        args.putString("title", title);
        fragment.setArguments(args);
        return fragment;
    }

    public WebViewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            url = getArguments().getString("url");
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

    @OnClick(R.id.back_btn)
    public void onViewClicked() {
        getActivity().onBackPressed();
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
        });
        webview.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleManager.setLocale(getContext());
        }
        webview.loadUrl(url);
    }

}