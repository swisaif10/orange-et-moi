package com.orange.orangeetmoipro.utilities;


import java.util.regex.Pattern;

public class Constants {
    public static final String SHARED_PREFS_NAME = "com.orange.orangeetmoipro";
    public static final String FIRST_TIME = "FIRST_TIME";
    public static final String IS_LOGGED_IN = "LOGGED_IN";
    public static final String LANGUAGE_KEY = "LANGUAGE_KEY";


    public static final String __firstpart = "[A-Z0-9a-z]([A-Z0-9a-z._%+-]{0,30}[A-Z0-9a-z])?";
    public static final String __serverpart = "([A-Z0-9a-z]([A-Z0-9a-z-]{0,30}[A-Z0-9a-z])?\\.){1,5}";
    public static final String __emailRegex = __firstpart + "@" + __serverpart + "[A-Za-z]{2,8}";
    public static final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(__emailRegex);
}
