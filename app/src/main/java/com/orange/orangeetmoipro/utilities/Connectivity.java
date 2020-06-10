package com.orange.orangeetmoipro.utilities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
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

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    void onStart() {
        // ToDo register receivers , or may be not
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
     * @return true if there is a real internet connection.
     */
    public boolean isReallyConnected() {
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");  // 8.8.8.8 is Google DNS
            int exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
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
                Log.e(TAG, e.getMessage());
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
            ConnectivityChangeDataModel connectivityChangeDataModel = new ConnectivityChangeDataModel();
            try {
                connectivityChangeDataModel.intent = intent;
                connectivityChangeDataModel.networkInfo = getActiveNetworkInfo();
                connectivityChangeDataModel.extraExtras = intent.getStringExtra(ConnectivityManager.EXTRA_EXTRA_INFO);
                connectivityChangeDataModel.networkType = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) ? intent.getIntExtra(ConnectivityManager.EXTRA_NETWORK_TYPE, ConnectivityManager.TYPE_DUMMY) : ConnectivityManager.TYPE_DUMMY;
                connectivityChangeDataModel.noConnectivity = intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false);
                connectivityChangeDataModel.otherNetworkInfo = intent.getParcelableExtra(ConnectivityManager.EXTRA_OTHER_NETWORK_INFO);
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }

            connectivityChangeCallback.onConnectivityChanged(connectivityChangeDataModel);
        }
    }

    public static class Utils {

        @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
        public static NetworkRequest newNetworkRequest(@NonNull int[] networkCapabilities, @NonNull int[] networkTransportTypes) {
            NetworkRequest.Builder networkBuilder = new NetworkRequest.Builder();
            if (networkCapabilities.length > 0) {
                for (int capability : networkCapabilities) {
                    networkBuilder.addCapability(capability);
                }
            }
            if (networkTransportTypes.length > 0) {
                for (int type : networkTransportTypes) {
                    networkBuilder.addTransportType(type);
                }
            }

            return networkBuilder.build();
        }
    }

    public class ConnectivityChangeDataModel {

        public Intent intent;
        public NetworkInfo networkInfo;
        public String extraExtras;
        public int networkType;
        public boolean noConnectivity;
        public NetworkInfo otherNetworkInfo;


        private ConnectivityChangeDataModel() {}
    }

    // ****************************************** Connectivity interfaces
    public interface ConnectivityChangeCallback {
        void onConnectivityChanged(ConnectivityChangeDataModel connectivityChangeDataModel);
    }

}
