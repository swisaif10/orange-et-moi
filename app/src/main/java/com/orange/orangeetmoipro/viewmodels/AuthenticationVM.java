package com.orange.orangeetmoipro.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.orange.orangeetmoipro.datamanager.retrofit.RestService;
import com.orange.orangeetmoipro.models.cgu.CGUData;
import com.orange.orangeetmoipro.models.login.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationVM extends AndroidViewModel {

    private MutableLiveData<LoginData> loginMutableLiveData;
    private MutableLiveData<CGUData> cguMutableLiveData;

    public AuthenticationVM(@NonNull Application application) {
        super(application);

        init();
    }

    public MutableLiveData<LoginData> getLoginMutableLiveData() {
        return loginMutableLiveData;
    }

    public MutableLiveData<CGUData> getCguMutableLiveData() {
        return cguMutableLiveData;
    }

    private void init() {
        loginMutableLiveData = new MutableLiveData<>();
        cguMutableLiveData = new MutableLiveData<>();
    }

    public void login(String login, String password, String lang) {
        Call<LoginData> call = RestService.getInstance().endpoint().login(login, password, lang);
        call.enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                loginMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {
                loginMutableLiveData.setValue(null);
            }
        });
    }

    public void getCGU(String lang) {
        Call<CGUData> call = RestService.getInstance().endpoint().getCGU("cgu", lang);
        call.enqueue(new Callback<CGUData>() {
            @Override
            public void onResponse(Call<CGUData> call, Response<CGUData> response) {
                cguMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<CGUData> call, Throwable t) {
                cguMutableLiveData.setValue(null);
            }
        });
    }
}
