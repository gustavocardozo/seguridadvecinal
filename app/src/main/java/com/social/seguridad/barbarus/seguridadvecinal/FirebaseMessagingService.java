package com.social.seguridad.barbarus.seguridadvecinal;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by retconadmin on 11/07/16.
 */
public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
             sendNotification(remoteMessage.getNotification().getBody()
                    , remoteMessage.getNotification().getTitle()
                    , remoteMessage.getData());
        } catch (Exception e) {
        }
    }

    //This method is only generating push notification
    //It is same as we did in earlier posts
    private void sendNotification(String messageBody, String title, Map<String, String> data) {

        Intent intent = new Intent(this, Notificacion.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        Iterator it = data.keySet().iterator();
        while(it.hasNext()){
            String key = (String) it.next();
            intent.putExtra(key,data.get(key));
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setColor(Color.YELLOW);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());
    }

}
