package com.orange.orangeetmoipro.utilities;


import java.util.regex.Pattern;

public class Constants {
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}\\@[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}(\\.[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25})+");

    public static final String SHARED_PREFS_NAME = "com.orange.orangeetmoipro";
    public static final String FIRST_TIME = "FIRST_TIME";
    public static final String IS_LOGGED_IN = "LOGGED_IN";
    public static final String LANGUAGE_KEY = "LANGUAGE_KEY";

}
