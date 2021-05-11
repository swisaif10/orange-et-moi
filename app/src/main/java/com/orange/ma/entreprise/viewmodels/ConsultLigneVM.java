package com.orange.ma.entreprise.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.orange.ma.entreprise.datamanager.retrofit.ApiUrls;
import com.orange.ma.entreprise.datamanager.retrofit.RestService;
import com.orange.ma.entreprise.datamanager.sharedpref.EncryptedSharedPreferences;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.models.cgu.CGUData;
import com.orange.ma.entreprise.models.guest.GuestLoginData;
import com.orange.ma.entreprise.models.listmsisdn.ListMsisdnData;
import com.orange.ma.entreprise.models.login.LoginData;
import com.orange.ma.entreprise.models.login.SettingTagData;
import com.orange.ma.entreprise.utilities.Constants;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultLigneVM extends AndroidViewModel {

    private MutableLiveData<ListMsisdnData> listMsisdnMutableLiveData;


    public ConsultLigneVM(@NonNull Application application) {
        super(application);

        init();
    }

    public MutableLiveData<ListMsisdnData> getListMsisdnMutableLiveData() {
        return listMsisdnMutableLiveData;
    }


    private void init() {
        listMsisdnMutableLiveData = new MutableLiveData<>();
    }

    public void getListNum(String lang, String token) {

        System.out.println("");
        Call<ListMsisdnData> call = RestService.getInstance().endpoint().getListNum(lang,token);
        call.enqueue(new Callback<ListMsisdnData>() {
            @Override
            public void onResponse(Call<ListMsisdnData> call, Response<ListMsisdnData> response) {
                listMsisdnMutableLiveData.setValue(response.body());
            }

            @Override
            public void onFailure(Call<ListMsisdnData> call, Throwable t) {
                listMsisdnMutableLiveData.setValue(null);
            }
        });
    }
}
