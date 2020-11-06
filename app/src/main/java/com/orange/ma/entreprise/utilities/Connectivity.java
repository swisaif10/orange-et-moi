package com.orange.ma.entreprise.utilities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

import java.util.ArrayList;

public class Connectivity implements LifecycleObserver {

    private static final String TAG = Connectivity.class.getSimpleName();

    private ConnectivityManager connectivityManager;
    private Context context;
    private Runtime runtime;
    private ConnectivityReceiver connectivityReceiver;

    public Connectivity(@NonNull Context context, @Nullable LifecycleOwner lifecycleOwner) {
        if (context == null) {
            throw new IllegalArgumentException("Context cannot be null!");
        }
        this.context = context.getApplicationContext();
        connectivityManager = (ConnectivityManager) this.context.getSystemService(Context.CONNECTIVITY_SERVICE);
        runtime = Runtime.getRuntime();

        if (lifecycleOwner != null) {
            lifecycleOwner.getLifecycle().addObserver(this);
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    void onStop() {
        unregisterConnectivityChangeReceiver();
    }


    public ConnectivityManager getConnectivityManager() {
        return connectivityManager;
    }

    @Nullable
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public NetworkInfo getActiveNetworkInfo() {
        NetworkInfo networkInfo = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            networkInfo = connectivityManager.getNetworkInfo(connectivityManager.getActiveNetwork());
        } else {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        return networkInfo;
    }

    public boolean isConnected() {
        NetworkInfo info = getActiveNetworkInfo();
        return (info != null && info.isConnected());
    }

    public boolean isConnectedOnWifi() {
        NetworkInfo info = getActiveNetworkInfo();
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
    }

    public boolean isConnectedOnMobile() {
        NetworkInfo info = getActiveNetworkInfo();
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * Checks if there is a real internet connection, means there is data sent and received.
     * Note: use with caution because this method executes a ping system command
     * against Google DNS (8.8.8.8)
     *
     * @return true if there is a real internet connection.
     */
    public boolean isReallyConnected() {
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");  // 8.8.8.8 is Google DNS
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @return connection status information about all network types supported/tracked by the device/framework.
     */
    @RequiresPermission(Manifest.permission.ACCESS_NETWORK_STATE)
    public NetworkInfo[] getAllNetworkInfo() {
        NetworkInfo[] networkInfoArray = new NetworkInfo[0];
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ArrayList<NetworkInfo> networkInfoArrayList = new ArrayList<>();
            Network[] networks = connectivityManager.getAllNetworks();
            for (Network network : networks) {
                networkInfoArrayList.add(connectivityManager.getNetworkInfo(network));
            }
            networkInfoArray = networkInfoArrayList.toArray(networkInfoArray);
        } else {
            networkInfoArray = connectivityManager.getAllNetworkInfo();
        }
        return networkInfoArray;
    }

    public void registerConnectivityChangeReceiver(@NonNull ConnectivityChangeCallback connectivityChangeCallback) {
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        if (connectivityReceiver == null) {
            connectivityReceiver = new ConnectivityReceiver(connectivityChangeCallback);
        }
        context.registerReceiver(connectivityReceiver, intentFilter);
    }

    public void unregisterConnectivityChangeReceiver() {
        if (connectivityReceiver != null) {
            try {
                context.unregisterReceiver(connectivityReceiver);
            } catch (Exception e) {
            }
        }
    }

    // ****************************************************************************************
    // ****************************************************************************************
    // ****************************************************************************************

    private class ConnectivityReceiver extends BroadcastReceiver {

        private ConnectivityChangeCallback connectivityChangeCallback;

        private ConnectivityReceiver(@NonNull ConnectivityChangeCallback connectivityChangeCallback) {
            this.connectivityChangeCallback = connectivityChangeCallback;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            connectivityChangeCallback.onConnectivityChanged();
        }
    }

    // ****************************************** Connectivity interfaces
    public interface ConnectivityChangeCallback {
        void onConnectivityChanged();
    }

}
