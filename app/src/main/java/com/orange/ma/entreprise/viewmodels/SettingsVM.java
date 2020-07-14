package com.orange.ma.entreprise.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.orange.ma.entreprise.datamanager.retrofit.RestService;
import com.orange.ma.entreprise.models.settings.SettingsData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsVM extends AndroidViewModel {

    private MutableLiveData<SettingsData> settingsMutableLiveData;

    public SettingsVM(@NonNull Application application) {
        super(application);

        init();
    }

    public MutableLiveData<SettingsData> getSettingsMutableLiveData() {
        return settingsMutableLiveData;
    }

    private void init() {
        settingsMutableLiveData = new MutableLiveData<>();
    }


    public void getSettingsList(String lang) {
        Call<SettingsData> call = RestService.getInstance().endpoint().getSettingsList(lang);
        call.enqueue(new Callback<SettingsData>() {
            @Override
            public void onResponse(Call<SettingsData> call, Response<SettingsData> response) {
                settingsMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<SettingsData> call, Throwable t) {
                settingsMutableLiveData.setValue(null);
            }
        });
    }
}
