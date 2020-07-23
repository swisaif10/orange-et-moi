package com.orange.ma.entreprise.views.main.settings;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Browser;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.orange.ma.entreprise.OrangeEtMoiPro;
import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.listeners.OnDialogButtonsClickListener;
import com.orange.ma.entreprise.listeners.OnItemSelectedListener;
import com.orange.ma.entreprise.models.commons.ResponseData;
import com.orange.ma.entreprise.models.dashboard.DashboardData;
import com.orange.ma.entreprise.models.settings.SettingsData;
import com.orange.ma.entreprise.models.settings.SettingsItem;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.viewmodels.SettingsVM;
import com.orange.ma.entreprise.views.authentication.AuthenticationActivity;
import com.orange.ma.entreprise.views.base.BaseFragment;
import com.orange.ma.entreprise.views.main.MainActivity;
import com.orange.ma.entreprise.views.main.adapters.SettingsAdapter;
import com.orange.ma.entreprise.views.main.dialogs.ChangeLanguageDialog;
import com.orange.ma.entreprise.views.main.dialogs.LogoutDialog;
import com.orange.ma.entreprise.views.main.webview.WebViewFragment;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

public class SettingsFragment extends BaseFragment implements OnItemSelectedListener, OnDialogButtonsClickListener {

    @BindView(R.id.settings_recycler)
    RecyclerView settingsRecycler;
    @BindView(R.id.loader)
    GifImageView loader;
    private PreferenceManager preferenceManager;
    private SettingsVM settingsVM;
    private Connectivity connectivity;
//    private Gson gson;
//    private SettingsData settingsData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        settingsVM = ViewModelProviders.of(this).get(SettingsVM.class);
        connectivity = new Connectivity(getContext(), this);

        settingsVM.getSettingsMutableLiveData().observe(this, this::handleSettingsDataResponse);
        settingsVM.getLogoutLiveData().observe(this, this::handleLogoutDataResponse);


        preferenceManager = new PreferenceManager.Builder(getContext(), Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();
        //firebaseAnalyticsEvent

        OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().setCurrentScreen(getActivity(), "page_parametres", LocaleManager.getLanguagePref(getContext()));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.bind(this, view);
//        loadoldSetting();
        return view;
    }

//    private void loadoldSetting() {
//        try {
//            String dashString = preferenceManager.getValue(Constants.M_SETTING,null);
//            gson = new Gson();
//            if (!Utilities.isNullOrEmpty(dashString)&&!dashString.equals("null")){
//                settingsData = gson.fromJson(dashString, SettingsData.class);
//            }
//            if(settingsData!=null)
//                handleSettingsDataResponse(settingsData);
//        }catch (JsonSyntaxException ex){
//            ex.printStackTrace();
//        }
//    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSettings();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            loader.setVisibility(View.VISIBLE);
            getSettings();
        }
    }

    @Override
    public void onItemSelected(SettingsItem settingsItem) {
        if (!settingsItem.getActionType().equalsIgnoreCase("none")) {
            Fragment fragment;
            if (settingsItem.getActionType().equalsIgnoreCase("internal")) {
                switch (settingsItem.getAction()) {
                    case "deconnexion":
                        new LogoutDialog(getActivity(), preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"), this).show();
                        Bundle bundle = new Bundle();
                        bundle.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getContext()));
                        bundle.putString(Constants.FIREBASE_RC_KEY, preferenceManager.getValue(Constants.LOGIN_KEY, ""));
                        bundle.putString(Constants.FIREBASE_ELEMENT_NAME_KEY, settingsItem.getAction());
                        OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().logEvent("page_parametres", bundle);
                        break;
                    case "action_language":
                        new ChangeLanguageDialog(getActivity(), preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr")).show();
                        bundle = new Bundle();
                        bundle.putString(Constants.FIREBASE_LANGUE_KEY, LocaleManager.getLanguagePref(getContext()));
                        bundle.putString(Constants.FIREBASE_ELEMENT_NAME_KEY, settingsItem.getAction());
                        OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().logEvent("page_parametres", bundle);
                        break;
                    case "home":
                        ((MainActivity)getActivity()).moveToDashboardFragment();break;
                    default:
                        break;
                }
            } else if (settingsItem.isInApp()) {
                fragment = WebViewFragment.newInstance(settingsItem.getAction(), settingsItem.getTitle());
                ((MainActivity)getActivity()).switchFragment(fragment,"");
            } else {
                CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                builder.setToolbarColor(ContextCompat.getColor(getContext(), R.color.black));
                CustomTabsIntent customTabsIntent = builder.build();
                String urlVebView = settingsItem.getAction();
                if (!Utilities.isNullOrEmpty(preferenceManager.getValue(Constants.TOKEN_KEY, null))&&urlVebView.contains(Constants.EX_SSO_TOKEN)) {
                    String token = preferenceManager.getValue(Constants.TOKEN_KEY, "").replace("Bearer ","");
                    urlVebView = urlVebView.replace(Constants.EX_SSO_TOKEN,token);
                }
                customTabsIntent.launchUrl(getContext(), Uri.parse(urlVebView));
            }
        }
    }

    @Override
    public void firstChoice() {
        logout();
    }

    @Override
    public void secondChoice() {

    }

    private void init(List<SettingsItem> settingsItems) {
        settingsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        SettingsAdapter settingsAdapter = new SettingsAdapter(getContext(), settingsItems, this::onItemSelected, preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
        settingsRecycler.setAdapter(settingsAdapter);
    }

    private void getSettings() {
        if (connectivity.isConnected()) {
            loader.setVisibility(View.VISIBLE);
            settingsVM.getSettingsList(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"), preferenceManager.getValue(Constants.TOKEN_KEY, ""));
        } else
            Utilities.showErrorPopup(getContext(), getString(R.string.no_internet));
    }

    private void handleSettingsDataResponse(SettingsData settingsData) {
        loader.setVisibility(View.GONE);
        if (settingsData == null) {
            Utilities.showErrorPopup(getContext(), getString(R.string.generic_error));
        } else {
            int code = settingsData.getHeader().getCode();
            switch(code){
                case 200:
                    init(settingsData.getResponse().getData());
                    //preferenceManager.putValue(Constants.M_SETTING,gson.toJson(settingsData));
                    break;
                case 403:
                    Intent intent = new Intent(getContext(), AuthenticationActivity.class);
                    intent.putExtra(Constants.ERROR_CODE,settingsData.getHeader().getCode());
                    intent.putExtra(Constants.ERROR_MESSAGE,settingsData.getHeader().getMessage());
                    startActivity(intent);
                    getActivity().finish();
                    break;
                default:
                    Utilities.showErrorPopup(getContext(), settingsData.getHeader().getMessage());
            };

        }
    }

    private void logout() {
        if (connectivity.isConnected()) {
            loader.setVisibility(View.VISIBLE);
            settingsVM.logout(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"), preferenceManager.getValue(Constants.TOKEN_KEY, ""));
        } else
            Utilities.showErrorPopup(getContext(), getString(R.string.no_internet));
    }

    private void handleLogoutDataResponse(ResponseData responseData) {
        loader.setVisibility(View.GONE);
        if (responseData == null) {
            Utilities.showErrorPopup(getContext(), getString(R.string.generic_error));
        } else {
            int code = responseData.getHeader().getCode();
            switch(code){
                case 200:
                    preferenceManager.putValue(Constants.IS_LOGGED_IN, false);
                    preferenceManager.clearValue(Constants.TOKEN_KEY);
                    preferenceManager.putValue(Constants.IS_AUTHENTICATED, false);
                    startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                    getActivity().finish();
                    break;
                case 403:
                    Intent intent = new Intent(getContext(), AuthenticationActivity.class);
                    intent.putExtra(Constants.ERROR_CODE,responseData.getHeader().getCode());
                    intent.putExtra(Constants.ERROR_MESSAGE,responseData.getHeader().getMessage());
                    startActivity(intent);
                    getActivity().finish();
                    break;
                default:
                    Utilities.showErrorPopup(getContext(), responseData.getHeader().getMessage());
            };
        }
    }
}