package com.orange.orangeetmoipro.views.authentication.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.datamanager.sharedpref.PreferenceManager;
import com.orange.orangeetmoipro.models.cgu.CGUData;
import com.orange.orangeetmoipro.utilities.Connectivity;
import com.orange.orangeetmoipro.utilities.Constants;
import com.orange.orangeetmoipro.utilities.LocaleManager;
import com.orange.orangeetmoipro.utilities.Utilities;
import com.orange.orangeetmoipro.viewmodels.AuthenticationVM;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CGUFragment extends Fragment {


    @BindView(R.id.description)
    WebView webview;
    @BindView(R.id.background)
    ImageView background;
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
        getCGU();
    }

    @OnClick({R.id.accept_btn, R.id.refuse_btn})
    public void onViewClicked(View view) {
        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.accept_btn:
                intent.putExtra("cgu", true);
                break;
            case R.id.refuse_btn:
                intent.putExtra("cgu", false);
                break;
        }
        getTargetFragment().onActivityResult(getTargetRequestCode(), Activity.RESULT_OK, intent);
        getActivity().getSupportFragmentManager().popBackStack();
    }

    private void init(String data) {
        if (preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr").equalsIgnoreCase("ar"))
            background.setScaleX(-1);
        webview.setOnLongClickListener(v -> true);
        webview.setLongClickable(false);
        webview.getSettings().setDefaultTextEncodingName("utf-8");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleManager.setLocale(getContext());
        }
        webview.loadDataWithBaseURL(null, data, "text/html", "utf-8", null);

    }

    private void getCGU() {
        if (connectivity.isConnected())
            authenticationVM.getCGU(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
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