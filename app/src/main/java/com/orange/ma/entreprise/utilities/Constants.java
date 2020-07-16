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
}
