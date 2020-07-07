package com.orange.orangeetmoipro.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.orange.orangeetmoipro.datamanager.retrofit.RestService;
import com.orange.orangeetmoipro.models.dashboard.DashboardData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardVM extends AndroidViewModel {

    private MutableLiveData<DashboardData> dashboardMutableLiveData;

    public DashboardVM(@NonNull Application application) {
        super(application);

        init();
    }

    public MutableLiveData<DashboardData> getDashboardMutableLiveData() {
        return dashboardMutableLiveData;
    }

    private void init() {
        dashboardMutableLiveData = new MutableLiveData<>();
    }


    public void getDashboardList(String lang) {
        Call<DashboardData> call = RestService.getInstance().endpoint().getDashboardList(lang);
        call.enqueue(new Callback<DashboardData>() {
            @Override
            public void onResponse(Call<DashboardData> call, Response<DashboardData> response) {
                dashboardMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<DashboardData> call, Throwable t) {
                dashboardMutableLiveData.setValue(null);
            }
        });
    }
}
