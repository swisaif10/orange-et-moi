package com.orange.orangeetmoipro.views.main.settings;

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

import com.orange.orangeetmoipro.R;
import com.orange.orangeetmoipro.datamanager.sharedpref.PreferenceManager;
import com.orange.orangeetmoipro.listeners.OnItemSelectedListener;
import com.orange.orangeetmoipro.models.settings.SettingsData;
import com.orange.orangeetmoipro.models.settings.SettingsItem;
import com.orange.orangeetmoipro.utilities.Connectivity;
import com.orange.orangeetmoipro.utilities.Constants;
import com.orange.orangeetmoipro.utilities.Utilities;
import com.orange.orangeetmoipro.viewmodels.SettingsVM;
import com.orange.orangeetmoipro.views.authentication.AuthenticationActivity;
import com.orange.orangeetmoipro.views.base.BaseFragment;
import com.orange.orangeetmoipro.views.main.adapters.SettingsAdapter;
import com.orange.orangeetmoipro.views.main.dialogs.ChangeLanguageDialog;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import pl.droidsonroids.gif.GifImageView;

public class SettingsFragment extends BaseFragment implements OnItemSelectedListener {

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

        settingsVM.getSettingsMutableLiveData().observe(this, this::handleSettingsData);


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

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSettings();
    }

    @Override
    public void onItemSelected(String action) {
        switch (action) {
            case "deconnexion":
                new LogoutDialog(getActivity(), preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"), new LogoutDialog.DialogCallBack() {
                    @Override
                    public void onConfirm(LogoutDialog dialog) {
                        preferenceManager.putValue(Constants.IS_LOGGED_IN, false);
                        startActivity(new Intent(getActivity(), AuthenticationActivity.class));
                        dialog.dismiss();
                        getActivity().finish();
                    }
                }).show();

                break;
            case "action_language":
                new ChangeLanguageDialog(getActivity(), preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr")).show();
                break;
            default:
                break;
        }
    }

    private void init(ArrayList<SettingsItem> settingsItems) {
        settingsRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        SettingsAdapter settingsAdapter = new SettingsAdapter(getContext(), settingsItems, this::onItemSelected, preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
        settingsRecycler.setAdapter(settingsAdapter);


    }

    private void getSettings() {
        if (connectivity.isConnected()){
            loader.setVisibility(View.VISIBLE);
            settingsVM.getSettingsList(preferenceManager.getValue(Constants.LANGUAGE_KEY, "fr"));
        }
        else
            Utilities.showErrorPopup(getContext(), getString(R.string.no_internet), "");
    }

    private void handleSettingsData(SettingsData settingsData) {
        loader.setVisibility(View.GONE);
        if (settingsData == null) {
            Utilities.showErrorPopup(getContext(), getString(R.string.generic_error), "");
        } else {
            int code = settingsData.getHeader().getCode();
            if (code == 200) {
                init(settingsData.getResponse().getData());
            } else
                Utilities.showErrorPopup(getContext(), settingsData.getHeader().getMessage(), "");
        }
    }

}