package com.orange.ma.entreprise.views.authentication.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.orange.ma.entreprise.OrangeEtMoiPro;
import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.models.cgu.CGUData;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.viewmodels.AuthenticationVM;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pl.droidsonroids.gif.GifImageView;

public class CGUFragment extends Fragment {


    @BindView(R.id.description)
    WebView webview;
    @BindView(R.id.background)
    ImageView background;
    @BindView(R.id.loader)
    GifImageView loader;
    private AuthenticationVM authenticationVM;
    private Connectivity connectivity;
    private PreferenceManager preferenceManager;

    public CGUFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        authenticationVM = ViewModelProviders.of(this).get(AuthenticationVM.class);
        connectivity = new Connectivity(getContext(), this);

        authenticationVM.getCguMutableLiveData().observe(this, this::handleCGULoginResponse);

        preferenceManager = new PreferenceManager.Builder(getContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().setCurrentScreen(getActivity(),"page_cgu_inscription", LocaleManager.getLanguagePref(getContext()));

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cgu, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr").equalsIgnoreCase("ar"))
            background.setScaleX(-1);
        getCGU();
    }

    @OnClick({R.id.accept_btn, R.id.refuse_btn})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.accept_btn:
                intent.putExtra("cgu", true);
                //firebaseAnalyticsEvent
                Bundle bundle = new Bundle();
                bundle.putString("Langue",LocaleManager.getLanguagePref(getContext()));
                OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().logEvent("clic_validation_cgu_inscription", bundle);
                break;
            case R.id.refuse_btn:
                intent.putExtra("cgu", false);
                break;
        }
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void init(String data) {
        webview.setOnLongClickListener(v -> true);
        webview.setLongClickable(false);
        webview.setWebViewClient(new WebViewClient() {

            public void onPageFinished(WebView view, String url) {
                loader.setVisibility(View.GONE);
            }
        });
        webview.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleManager.setLocale(getContext());
        }
        webview.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);

    }

    private void getCGU() {
        if (connectivity.isConnected()) {
            loader.setVisibility(View.VISIBLE);
            authenticationVM.getCGU(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
        }
    }

    private void handleCGULoginResponse(CGUData cguData) {
        if (cguData == null) {
            Utilities.showErrorPopup(getContext(), getString(R.string.generic_error), "");
        } else {
            int code = cguData.getHeader().getCode();
            if (code == 200) {
                init(cguData.getCGUResponse().getCGU().getDescription());
            } else
                Utilities.showErrorPopup(getContext(), cguData.getHeader().getMessage(), "");
        }
    }
}