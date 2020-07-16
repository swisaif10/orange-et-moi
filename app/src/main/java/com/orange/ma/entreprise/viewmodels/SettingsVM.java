package com.orange.ma.entreprise.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.orange.ma.entreprise.datamanager.retrofit.RestService;
import com.orange.ma.entreprise.models.commons.ResponseData;
import com.orange.ma.entreprise.models.settings.SettingsData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsVM extends AndroidViewModel {

    private MutableLiveData<SettingsData> settingsLiveData;
    private MutableLiveData<ResponseData> logoutLiveData;

    public SettingsVM(@NonNull Application application) {
        super(application);

        init();
    }

    public MutableLiveData<SettingsData> getSettingsMutableLiveData() {
        return settingsLiveData;
    }

    public MutableLiveData<ResponseData> getLogoutLiveData() {
        return logoutLiveData;
    }

    private void init() {
        settingsLiveData = new MutableLiveData<>();
        logoutLiveData = new MutableLiveData<>();
    }

    public void getSettingsList(String lang) {
        Call<SettingsData> call = RestService.getInstance().endpoint().getSettingsList(lang);
        call.enqueue(new Callback<SettingsData>() {
            @Override
            public void onResponse(Call<SettingsData> call, Response<SettingsData> response) {
                settingsLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<SettingsData> call, Throwable t) {
                settingsLiveData.setValue(null);
            }
        });
    }

    public void logout(String lang) {
        Call<ResponseData> call = RestService.getInstance().endpoint().logout(lang);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                logoutLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                logoutLiveData.setValue(null);
            }
        });
    }
}
