package com.orange.orangeetmoipro.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.orange.orangeetmoipro.datamanager.retrofit.RestService;
import com.orange.orangeetmoipro.models.login.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationVM extends AndroidViewModel {

    private MutableLiveData<LoginData> loginMutableLiveData;

    public AuthenticationVM(@NonNull Application application) {
        super(application);

        init();
    }

    public MutableLiveData<LoginData> getLoginMutableLiveData() {
        return loginMutableLiveData;
    }

    private void init() {
        loginMutableLiveData = new MutableLiveData<>();
    }

    public void login(String login, String password) {
        Call<LoginData> call = RestService.getInstance().endpoint().login(login, password);
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
}
