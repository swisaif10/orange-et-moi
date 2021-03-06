package com.orange.ma.entreprise.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.orange.ma.entreprise.datamanager.retrofit.RestService;
import com.orange.ma.entreprise.models.tabmenu.TabMenuData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainVM extends AndroidViewModel {

    private MutableLiveData<TabMenuData> tabMenuMutableLiveData;

    public MainVM(@NonNull Application application) {
        super(application);

        init();
    }

    public MutableLiveData<TabMenuData> getTabMenuMutableLiveData() {
        return tabMenuMutableLiveData;
    }

    private void init() {
        tabMenuMutableLiveData = new MutableLiveData<>();
    }

    public void getTabMenu(String lang, String token) {
        Call<TabMenuData> call = RestService.getInstance().endpoint().getTabMenu(lang, token);
        call.enqueue(new Callback<TabMenuData>() {
            @Override
            public void onResponse(Call<TabMenuData> call, Response<TabMenuData> response) {
                tabMenuMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<TabMenuData> call, Throwable t) {
                tabMenuMutableLiveData.setValue(null);
            }
        });
    }
}
