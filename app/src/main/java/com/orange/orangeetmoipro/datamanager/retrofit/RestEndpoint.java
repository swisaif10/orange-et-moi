package com.orange.orangeetmoipro.datamanager.retrofit;

import com.orange.orangeetmoipro.models.controlversion.ControlVersionData;
import com.orange.orangeetmoipro.models.login.LoginData;
import com.orange.orangeetmoipro.models.tabmenu.TabMenuData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface RestEndpoint {

    @POST(ApiUrls.CHECK_VERSION_URL)
    Call<ControlVersionData> getVersionCheck();

    @POST(ApiUrls.GET_TABMENU_URL)
    Call<TabMenuData> getTabMenu();

    @FormUrlEncoded
    @POST(ApiUrls.LOGIN_URL)
    Call<LoginData> login(@Field("login") String login, @Field("password") String password);
}
