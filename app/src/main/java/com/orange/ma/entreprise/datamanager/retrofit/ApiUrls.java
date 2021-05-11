package com.orange.ma.entreprise.datamanager.retrofit;


import com.orange.ma.entreprise.BuildConfig;

import okhttp3.Credentials;

public interface ApiUrls {

    String CHECK_VERSION_URL = "{locale}/api/version-control/check";
    String GET_CGU_URL = "{locale}/api/html-description/show";
    String SIGN_IN_URL = "{locale}/api/account/register";
    String LOGIN_URL = "{locale}/api/account/login";
    String GET_TAB_MENU_URL = "{locale}/api/tab-menu/list";
    String GET_SETTINGS_LIST_URL = "{locale}/api/setting/list";
    String GET_DASHBOARD_LIST_URL = "{locale}/api/account/dashboard";
    String LOGOUT_URL = "{locale}/api/account/logout";
    String GUEST_LOGIN_URL = "{locale}/api/account/guest-login";
    String CONSULT_LINE = "{locale}/api/profile/my-lines";
    String AUTHORIZATION = Credentials.basic(BuildConfig.ID, BuildConfig.DOMAIN);

    String SETTING_BY_TAG = "{locale}/api/setting/by-tag";
}
