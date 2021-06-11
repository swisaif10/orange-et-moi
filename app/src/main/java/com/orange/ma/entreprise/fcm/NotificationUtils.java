package com.orange.ma.entreprise.fcm;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;

import androidx.core.app.NotificationCompat;

import com.orange.ma.entreprise.R;
import com.orange.ma.entreprise.datamanager.sharedpref.EncryptedSharedPreferences;
import com.orange.ma.entreprise.datamanager.sharedpref.PreferenceManager;
import com.orange.ma.entreprise.utilities.Constants;
import com.orange.ma.entreprise.utilities.Utilities;
import com.orange.ma.entreprise.views.authentication.AuthenticationActivity;
import com.orange.ma.entreprise.views.main.MainActivity;
import com.orange.ma.entreprise.views.splashscreen.SplashScreenActivity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.orange.ma.entreprise.utilities.Constants.ACTION_TYPE;
import static com.orange.ma.entreprise.utilities.Constants.APP_VIEW;
import static com.orange.ma.entreprise.utilities.Constants.DEEP_LINK;
import static com.orange.ma.entreprise.utilities.Constants.DEFAULT;
import static com.orange.ma.entreprise.utilities.Constants.ENDPOINT;
import static com.orange.ma.entreprise.utilities.Constants.ENDPOINT_TITLE;
import static com.orange.ma.entreprise.utilities.Constants.INSCRIPTION;
import static com.orange.ma.entreprise.utilities.Constants.IN_APP_URL;
import static com.orange.ma.entreprise.utilities.Constants.OUT_APP_URL;

public class NotificationUtils {

    private static final String CHANNEL_ID = "myChannel";
    private static final String CHANNEL_NAME = "myChannelName";
    private static final String URL = "url";
    private final EncryptedSharedPreferences encryptedSharedPreferences;
    private Map<String, Class> activities = new HashMap<>();
    private Context mContext;
    private Intent intent;
    private PendingIntent resultPendingIntent;
    private PreferenceManager preferenceManager;

    public NotificationUtils(Context context) {
        this.mContext = context;
        // activites to open via notifications
        activities.put("splash", SplashScreenActivity.class);
        preferenceManager = new PreferenceManager.Builder(context, Context.MODE_PRIVATE)
                .name(Constants.SHARED_PREFS_NAME)
                .build();

        encryptedSharedPreferences = new EncryptedSharedPreferences();

        encryptedSharedPreferences.getEncryptedSharedPreferences(context);
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            java.net.URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            return null;
        }
    }

    public void showNotification(NotificationObject notification, Intent resultIntent) {

        Bitmap image = null;
        intent = resultIntent;
        int icon;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.P)
            icon = R.mipmap.ic_launcher;
        else
            icon = R.mipmap.ic_launcher;

        if (notification.getImageUrl() != null && !notification.getImageUrl().isEmpty()) {
            image = getBitmapFromURL(notification.getImageUrl());
        }

        if (Utilities.isNullOrEmpty(encryptedSharedPreferences.getValue(Constants.TOKEN_KEY, ""))) {
            switch (notification.getActionType()) {
                case INSCRIPTION:
                    intent = new Intent(mContext, AuthenticationActivity.class);
                    intent.putExtra("link", notification.getActionType());
                    resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    break;
                default:
                    intent = new Intent(mContext, AuthenticationActivity.class);
                    resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            }
        } else {
            switch (notification.getActionType()) {
                case DEEP_LINK:
                    handleDeepLink(notification.getEndPoint());
                    break;
                case IN_APP_URL:
                    intent = new Intent(mContext, SplashScreenActivity.class);
                    intent.putExtra(ACTION_TYPE, notification.getActionType());
                    intent.putExtra(ENDPOINT, notification.getEndPoint());
                    intent.putExtra(ENDPOINT_TITLE, notification.getEndPointTitle());
                    resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    break;
                case OUT_APP_URL:
                    intent = new Intent(Intent.ACTION_VIEW);
                    intent.putExtra(ACTION_TYPE, notification.getActionType());
                    intent.setData(Uri.parse(notification.getEndPoint()));
                    resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    break;
                case APP_VIEW:
                    intent = new Intent(mContext, MainActivity.class);
                    intent.putExtra(ACTION_TYPE, notification.getActionType());
                    intent.putExtra(ENDPOINT, notification.getEndPoint());
                    resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
                    break;
                case DEFAULT:
                default:
                    resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    resultPendingIntent = PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            }
        }


        final NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                mContext, CHANNEL_ID);

        Notification osNotification;

        if (image == null) {
            //When Inbox Style is applied, user can expand the notification
            NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

            inboxStyle.addLine(notification.getMessage());
            osNotification = mBuilder.setSmallIcon(icon).setTicker(notification.getTitle()).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(notification.getTitle())
                    .setContentIntent(resultPendingIntent)
                    .setStyle(inboxStyle)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setContentText(notification.getMessage())
                    .build();

        } else {
            //If Bitmap is created from URL, show big icon
            NotificationCompat.BigPictureStyle bigPictureStyle = new NotificationCompat.BigPictureStyle();
            bigPictureStyle.setBigContentTitle(notification.getTitle());
            bigPictureStyle.setSummaryText(Html.fromHtml(notification.getMessage()).toString());
            bigPictureStyle.bigPicture(image);
            osNotification = mBuilder.setSmallIcon(icon).setTicker(notification.getTitle()).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(notification.getTitle())
                    .setContentIntent(resultPendingIntent)
                    .setStyle(bigPictureStyle)
                    .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(), icon))
                    .setLargeIcon(image)
                    .setContentText(notification.getMessage())
                    .build();
        }

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        //All notifications should go through NotificationChannel on Android 26 & above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        }
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        notificationManager.notify(m, osNotification);
    }

    private void handleDeepLink(String endPoint) {
        intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra(ACTION_TYPE, DEEP_LINK);
        intent.putExtra(ENDPOINT, endPoint);
        intent.setData(Uri.parse(endPoint));
        resultPendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public void playNotificationSound() {
        try {
            Uri notifSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(mContext, notifSound);
            r.play();
        } catch (Exception e) {
        }
    }

}
