package com.orange.ma.entreprise.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.orange.ma.entreprise.datamanager.retrofit.RestService;
import com.orange.ma.entreprise.models.cgu.CGUData;
import com.orange.ma.entreprise.models.guest.GuestLoginData;
import com.orange.ma.entreprise.models.login.LoginData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthenticationVM extends AndroidViewModel {

    private MutableLiveData<LoginData> loginMutableLiveData;
    private MutableLiveData<LoginData> signInMutableLiveData;
    private MutableLiveData<CGUData> cguMutableLiveData;
    private MutableLiveData<GuestLoginData> guestLoginMutableLiveData;

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

    public MutableLiveData<LoginData> getSignInMutableLiveData() {
        return signInMutableLiveData;
    }

    public MutableLiveData<GuestLoginData> getGuestLoginMutableLiveData() {
        return guestLoginMutableLiveData;
    }

    private void init() {
        loginMutableLiveData = new MutableLiveData<>();
        cguMutableLiveData = new MutableLiveData<>();
        signInMutableLiveData = new MutableLiveData<>();
        guestLoginMutableLiveData = new MutableLiveData<>();
    }

    public void login(String login, String password, Boolean rememberMe, String lang) {
        Call<LoginData> call = RestService.getInstance().endpoint().login(login, password, rememberMe, lang);
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
        Call<LoginData> call = RestService.getInstance().endpoint().signIn(id, cin, email, password, lang);
        call.enqueue(new Callback<LoginData>() {
            @Override
            public void onResponse(Call<LoginData> call, Response<LoginData> response) {
                signInMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<LoginData> call, Throwable t) {
                signInMutableLiveData.setValue(null);
            }
        });
    }

    public void guestLogin(String lang) {
        Call<GuestLoginData> call = RestService.getInstance().endpoint().guestLogin(lang);
        call.enqueue(new Callback<GuestLoginData>() {
            @Override
            public void onResponse(Call<GuestLoginData> call, Response<GuestLoginData> response) {
                guestLoginMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<GuestLoginData> call, Throwable t) {
                guestLoginMutableLiveData.setValue(null);
            }
        });
    }


}
