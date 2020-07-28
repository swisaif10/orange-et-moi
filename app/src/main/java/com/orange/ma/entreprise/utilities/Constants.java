package com.orange.ma.entreprise.utilities;

import java.util.regex.Pattern;

public interface Constants {
    String FCM_PREFS_NAME = "com.orange.orangeetmoipro.fcm";
    String SHARED_PREFS_NAME = "com.orange.orangeetmoipro";
    String FIRST_TIME = "FIRST_TIME";
    String IS_LOGGED_IN = "LOGGED_IN";
    String LANGUAGE_KEY = "LANGUAGE_KEY";
    String LOGIN_KEY = "LOGIN_KEY";
    String PASS_KEY = "PASS_KEY";
    String SAVE_CREDENTIALS_KEY = "SAVE_CREDENTIALS_KEY";
    String TOKEN_KEY = "TOKEN_KEY";
    String X_AUTHORIZATION = "X-Authorization";
    String IS_AUTHENTICATED = "IS_AUTHENTICATED";

    String firstPart = "[A-Z0-9a-z]([A-Z0-9a-z._%+-]{0,30}[A-Z0-9a-z])?";
    String serverPart = "([A-Z0-9a-z]([A-Z0-9a-z-]{0,30}[A-Z0-9a-z])?\\.){1,5}";
    String emailRegex = firstPart + "@" + serverPart + "[A-Za-z]{2,8}";
    Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(emailRegex);

    String VIEW_BILLING = "view_billing";
    String DEEP_LINK = "deep_link";
    String IN_APP_URL = "in_app_url";
    String OUT_APP_URL = "out_app_url";
    String APP_VIEW = "app_view";
    String DEFAULT = "default";
    String INSCRIPTION = "inscription";

    String FIREBASE_LANGUE_KEY = "Langue";
    String FIREBASE_RC_KEY = "RC_entreprise";
    String FIREBASE_ELEMENT_NAME_KEY = "Nom_Element_params";

    String ERROR_MESSAGE = "error_message";
    String ERROR_CODE = "error_code";

    String TITLE = "title";
    String MESSAGE = "body";
    String IMAGE = "image";
    String ENDPOINT = "endpoint";
    String ENDPOINT_TITLE = "endpoint_title";
    String ACTION_TYPE = "action";
    String M_DASHBOARD = "dashboard";
    String M_SETTING = "setting";
    CharSequence EX_SSO_TOKEN = "{sso_token}";
    String DASH_TEMPLATE_HASH = "dash_hash";
    String TAB_MENU_HASH = "tabmenu_hash";
    String VISITOR_TEMPLATE_HASH = "visitor_hash";

    //TAG
    String FORGOT_PASSWORD_TAG = "forgot_password";
    String CHANGE_ACCOUNT_MANAGER_TAG = "change_account_manager";
}
