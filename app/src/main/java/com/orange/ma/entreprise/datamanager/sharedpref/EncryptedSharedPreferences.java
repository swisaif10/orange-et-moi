package com.orange.ma.entreprise.datamanager.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

public class EncryptedSharedPreferences {

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;


    public SharedPreferences getEncryptedSharedPreferences(Context context) {

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                MasterKey masterKeyAlias = new MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build();//.getOrCreate(MasterKeys.AES256_GCM_SPEC);
                sharedPreferences = androidx.security.crypto.EncryptedSharedPreferences.create(
                        context,
                        "secret_shared_prefs",
                        masterKeyAlias,
                        androidx.security.crypto.EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        androidx.security.crypto.EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );

                editor = sharedPreferences.edit();

            } catch (GeneralSecurityException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return sharedPreferences;
    }


    public String getValue(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public Boolean getValue(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public int getValue(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public float getValue(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public long getValue(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public void clearValue(@NonNull @Size(min = 1) String key) {
        try {
            editor.remove(key).apply();
        } catch (Exception e) {
        }
    }

    public void putValue(@NonNull String key, @NonNull Object value) {
        if (!key.isEmpty()) {
            try {
                if (value instanceof String) {
                    editor.putString(key, value.toString()).apply();
                    return;
                }
                if (value instanceof Boolean) {
                    editor.putBoolean(key, (boolean) value).apply();
                    return;
                }
                if (value instanceof Integer) {
                    editor.putInt(key, (Integer) value).apply();
                    return;
                }
                if (value instanceof Float) {
                    editor.putFloat(key, (Float) value).apply();
                    return;
                }
                if (value instanceof Long) {
                    editor.putLong(key, (Long) value).apply();
                }
            } catch (Exception e) {
            }
        } else {
        }
    }
}
