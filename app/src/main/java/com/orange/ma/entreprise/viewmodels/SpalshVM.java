package com.orange.ma.entreprise.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.orange.ma.entreprise.datamanager.retrofit.ApiUrls;
import com.orange.ma.entreprise.datamanager.retrofit.RestService;
import com.orange.ma.entreprise.models.controlversion.ControlVersionData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SpalshVM extends AndroidViewModel {

    private MutableLiveData<ControlVersionData> versionMutableLiveData;

    public SpalshVM(@NonNull Application application) {
        super(application);

        init();
    }

    public MutableLiveData<ControlVersionData> getversionMutableLiveData() {
        return versionMutableLiveData;
    }

    private void init() {
        versionMutableLiveData = new MutableLiveData<>();
    }


    public void getVersionCheck(String lang) {
        Call<ControlVersionData> call = RestService.getInstance().endpoint().getVersionCheck(ApiUrls.AUTHORIZATION,lang);
        call.enqueue(new Callback<ControlVersionData>() {
            @Override
            public void onResponse(Call<ControlVersionData> call, Response<ControlVersionData> response) {
                versionMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ControlVersionData> call, Throwable t) {
                versionMutableLiveData.setValue(null);
            }
        });
    }
}
