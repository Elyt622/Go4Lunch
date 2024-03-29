package com.elytevolution.go4lunch.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Build;
import android.util.Log;
import com.elytevolution.go4lunch.R;
import com.elytevolution.go4lunch.view.activity.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

public class NotificationsService extends FirebaseMessagingService {

    private static final String TAG = "NOTIFICATIONS";
    private static final String NOTIFICATION_TAG = "FIREBASE_OC";
    private static final int NOTIFICATION_ID = 1;

    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getNotification() != null) {
            String message = remoteMessage.getNotification().getBody();
            Log.d(TAG, "Message Notification Body: " + message);
        }
        sendVisualNotification(remoteMessage.getNotification().getBody());
    }

    private void sendVisualNotification(String messageBody) {

        // 1 - Create an Intent that will be shown when user will click on the Notification
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        // 2 - Create a Style for the Notification
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        inboxStyle.setBigContentTitle(getString(R.string.notification_title));
        inboxStyle.addLine(messageBody);

        // 3 - Create a Channel (Android 8)
        String channelId = getString(R.string.default_notification_channel_id);

        // 4 - Build a Notification object
        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.ic_circle_notifications_black_24dp)
                        .setContentTitle(getString(R.string.app_name))
                        .setContentText(getString(R.string.notification_title))
                        .setAutoCancel(true)
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                        .setContentIntent(pendingIntent)
                        .setStyle(inboxStyle);

        // 5 - Add the Notification to the Notification Manager and show it.
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // 6 - Support Version >= Android 8
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence channelName = "Message from Firebase";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // 7 - Show notification
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_TAG, NOTIFICATION_ID, notificationBuilder.build());
        }
    }
}