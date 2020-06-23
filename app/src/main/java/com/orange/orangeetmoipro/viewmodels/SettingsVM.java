package com.orange.orangeetmoipro.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.orange.orangeetmoipro.datamanager.retrofit.RestService;
import com.orange.orangeetmoipro.models.settings.SettingsData;

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


    public void getSettings() {
        Call<SettingsData> call = RestService.getInstance().endpoint().getSettings();
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
