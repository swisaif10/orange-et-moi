package com.orange.orangeetmoipro.datamanager.retrofit;

import com.orange.orangeetmoipro.models.cgu.CGUData;
import com.orange.orangeetmoipro.models.controlversion.ControlVersionData;
import com.orange.orangeetmoipro.models.dashboard.DashboardData;
import com.orange.orangeetmoipro.models.login.LoginData;
import com.orange.orangeetmoipro.models.settings.SettingsData;
import com.orange.orangeetmoipro.models.tabmenu.TabMenuData;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RestEndpoint {

    @POST(ApiUrls.CHECK_VERSION_URL)
    Call<ControlVersionData> getVersionCheck(@Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiUrls.GET_CGU_URL)
    Call<CGUData> getCGU(@Field("tag") String tag, @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiUrls.SIGN_IN_URL)
    Call<LoginData> signIn(@Field("identifier") String id, @Field("cinPassport") String cin,
                           @Field("email") String email, @Field("password") String password, @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiUrls.LOGIN_URL)
    Call<LoginData> login(@Field("login") String login, @Field("password") String password, @Path("locale") String lang);

    @POST(ApiUrls.GET_TAB_MENU_URL)
    Call<TabMenuData> getTabMenu(@Path("locale") String lang);

    @POST(ApiUrls.GET_DASHBOARD_LIST_URL)
    Call<DashboardData> getDashboardList();

    @POST(ApiUrls.GET_SETTINGS_LIST_URL)
    Call<SettingsData> getSettingsList(@Path("locale") String lang);

}
