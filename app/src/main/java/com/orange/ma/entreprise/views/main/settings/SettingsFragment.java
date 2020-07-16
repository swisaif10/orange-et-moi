package com.orange.ma.entreprise.views.main.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.orange.ma.entreprise.OrangeEtMoiPro;
import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.listeners.OnDialogButtonsClickListener;
import com.orange.ma.entreprise.listeners.OnItemSelectedListener;
import com.orange.ma.entreprise.models.commons.ResponseData;
import com.orange.ma.entreprise.models.settings.SettingsData;
import com.orange.ma.entreprise.models.settings.SettingsItem;
import com.orange.ma.entreprise.utilities.Connectivity;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.LocaleManager;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.viewmodels.SettingsVM;
import com.orange.ma.entreprise.views.authentication.AuthenticationActivity;
import com.orange.ma.entreprise.views.base.BaseFragment;
import com.orange.ma.entreprise.views.main.adapters.SettingsAdapter;
import com.orange.ma.entreprise.views.main.dialogs.ChangeLanguageDialog;
import com.orange.ma.entreprise.views.main.dialogs.LogoutDialog;

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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSettings();
    }

    @Override
    public void onItemSelected(String action) {
        switch (action) {
            case "deconnexion":
                new LogoutDialog(getActivity(), preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"), this).show();
                //firebaseAnalyticsEvent
                Bundle bundle = new Bundle();
                bundle.putString("Langue", LocaleManager.getLanguagePref(getContext()));
                bundle.putString("RC_entreprise", preferenceManager.getValue(Constants.LOGIN_KEY, ""));
                bundle.putString("Nom_Element_params", action);
                OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().logEvent("page_parametres", bundle);
                break;
            case "action_language":
                new ChangeLanguageDialog(getActivity(), preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr")).show();
                bundle = new Bundle();
                bundle.putString("Langue", LocaleManager.getLanguagePref(getContext()));
                bundle.putString("Nom_Element_params", action);
                OrangeEtMoiPro.getInstance().getFireBaseAnalyticsInstance().logEvent("page_parametres", bundle);
                break;
            default:
                break;
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
            settingsVM.getSettingsList(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
        } else
            Utilities.showErrorPopup(getContext(), getString(R.string.no_internet));
    }

    private void handleSettingsDataResponse(SettingsData settingsData) {
        loader.setVisibility(View.GONE);
        if (settingsData == null) {
            Utilities.showErrorPopup(getContext(), getString(R.string.generic_error));
        } else {
            int code = settingsData.getHeader().getCode();
            if (code == 200) {
                init(settingsData.getResponse().getData());
            } else
                Utilities.showErrorPopup(getContext(), settingsData.getHeader().getMessage());
        }
    }

    private void logout() {
        if (connectivity.isConnected()) {
            loader.setVisibility(View.VISIBLE);
            settingsVM.logout(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
        } else
            Utilities.showErrorPopup(getContext(), getString(R.string.no_internet));
    }

    private void handleLogoutDataResponse(ResponseData responseData) {
        loader.setVisibility(View.GONE);
        if (responseData == null) {
            Utilities.showErrorPopup(getContext(), getString(R.string.generic_error));
        } else {
            int code = responseData.getHeader().getCode();
            if (code == 200) {
                preferenceManager.putValue(Constants.IS_LOGGED_IN, false);
                startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                getActivity().finish();
            } else
                Utilities.showErrorPopup(getContext(), responseData.getHeader().getMessage());
        }
    }
}