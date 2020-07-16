package com.orange.ma.entreprise.datamanager.retrofit;

import com.orange.ma.entreprise.models.cgu.CGUData;
import com.orange.ma.entreprise.models.commons.ResponseData;
import com.orange.ma.entreprise.models.controlversion.ControlVersionData;
import com.orange.ma.entreprise.models.dashboard.DashboardData;
import com.orange.ma.entreprise.models.login.LoginData;
import com.orange.ma.entreprise.models.settings.SettingsData;
import com.orange.ma.entreprise.models.tabmenu.TabMenuData;

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
    Call<ResponseData> signIn(@Field("identifier") String id, @Field("cinPassport") String cin,
                              @Field("email") String email, @Field("password") String password, @Path("locale") String lang);

    @FormUrlEncoded
    @POST(ApiUrls.LOGIN_URL)
    Call<LoginData> login(@Field("login") String login, @Field("password") String password, @Path("locale") String lang);

    @POST(ApiUrls.GET_TAB_MENU_URL)
    Call<TabMenuData> getTabMenu(@Path("locale") String lang);

    @POST(ApiUrls.GET_DASHBOARD_LIST_URL)
    Call<DashboardData> getDashboardList(@Path("locale") String lang);

    @POST(ApiUrls.GET_SETTINGS_LIST_URL)
    Call<SettingsData> getSettingsList(@Path("locale") String lang);

    @POST(ApiUrls.LOGOUT_URL)
    Call<ResponseData> logout(@Path("locale") String lang);

}
