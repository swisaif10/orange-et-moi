package com.orange.ma.entreprise.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.orange.ma.entreprise.datamanager.retrofit.RestService;
import com.orange.ma.entreprise.models.cgu.CGUData;
import com.orange.ma.entreprise.models.commons.ResponseData;
import com.orange.ma.entreprise.models.login.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationVM extends AndroidViewModel {

    private MutableLiveData<LoginData> loginMutableLiveData;
    private MutableLiveData<ResponseData> signInMutableLiveData;
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

    public MutableLiveData<ResponseData> getSignInMutableLiveData() {
        return signInMutableLiveData;
    }

    private void init() {
        loginMutableLiveData = new MutableLiveData<>();
        cguMutableLiveData = new MutableLiveData<>();
        signInMutableLiveData = new MutableLiveData<>();
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

    public void signIn(String id, String cin, String email, String password, String lang) {
        Call<ResponseData> call = RestService.getInstance().endpoint().signIn(id, cin, email, password, lang);
        call.enqueue(new Callback<ResponseData>() {
            @Override
            public void onResponse(Call<ResponseData> call, Response<ResponseData> response) {
                signInMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ResponseData> call, Throwable t) {
                signInMutableLiveData.setValue(null);
            }
        });
    }

}