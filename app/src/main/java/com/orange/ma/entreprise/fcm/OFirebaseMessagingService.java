package com.orange.ma.entreprise.fcm;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.views.splashscreen.SplashScreenActivity;

import java.util.Map;

import static com.orange.ma.entreprise.utilities.Constants.ACTION_TYPE;
import static com.orange.ma.entreprise.utilities.Constants.ENDPOINT;
import static com.orange.ma.entreprise.utilities.Constants.ENDPOINT_TITLE;
import static com.orange.ma.entreprise.utilities.Constants.IMAGE;
import static com.orange.ma.entreprise.utilities.Constants.MESSAGE;
import static com.orange.ma.entreprise.utilities.Constants.TITLE;

public class OFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (!remoteMessage.getData().isEmpty()) {
            handleData(remoteMessage.getData());
        } else if (remoteMessage.getNotification() != null) {
            handleNotification(remoteMessage.getNotification());
        }
    }

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        getSharedPreferences(Constants.FCM_PREFS_NAME, MODE_PRIVATE).edit().putString("fb_token", s).apply();
        Log.d("TAGFCM", "onNewToken: " + s);
    }

    private void handleNotification(RemoteMessage.Notification notification) {
//        NotificationObject mNotification = new NotificationObject(
//                notification.getTitle(),
//                notification.getBody()
//        );
//        setUserDataType();
//        makeNotification(mNotification);
    }

    private void setUserDataType() {

    }

    private void handleData(Map<String, String> data) {
//        Log.d("TAGNOTIF", "handleData: ");
//        NotificationObject notification = new NotificationObject();
//        notification.setTitle(data.get(TITLE));
//        notification.setMessage(data.get(MESSAGE));
//        notification.setImageUrl(data.get(IMAGE));
//        notification.setActionType(data.get(ACTION_TYPE));
//        notification.setEndPoint(data.get(ENDPOINT));
//        notification.setEndPointTitle(data.get(ENDPOINT_TITLE));
//
//        makeNotification(notification);
    }

    private void makeNotification(NotificationObject notification) {
        Intent resultIntent = new Intent(getApplicationContext(), SplashScreenActivity.class);
        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
        notificationUtils.showNotification(notification, resultIntent);
        notificationUtils.playNotificationSound();
    }
}
