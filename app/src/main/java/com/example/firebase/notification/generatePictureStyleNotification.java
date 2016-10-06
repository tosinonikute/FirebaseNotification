package com.example.firebase.notification;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import com.example.tosin.myapplication.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tosin on 9/13/2016.
 */
public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

    private Context mContext;
    private String title, message, imageUrl;

    public generatePictureStyleNotification(Context context, String title, String message, String imageUrl) {
        super();
        this.mContext = context;
        this.title = title;
        this.message = message;
        this.imageUrl = imageUrl;
    }

    @Override
    protected Bitmap doInBackground(String... params) {

        InputStream in;
        try {
            URL url = new URL(this.imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            in = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(in);
            return myBitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onPostExecute(Bitmap result) {
        super.onPostExecute(result);

        Intent intent = new Intent(mContext, TestActivity.class);
        intent.putExtra("key", "value");
        PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 100, intent, PendingIntent.FLAG_ONE_SHOT);

        //added
        long[] pattern = {500,500,500,500,500};
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notif = new Notification.Builder(mContext)
                .setContentIntent(pendingIntent)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.notify_me)
                .setLargeIcon(result)
                .setStyle(new Notification.BigPictureStyle()
                        .bigPicture(result)
                        .setSummaryText(message))
                .setVibrate(pattern)
                .setLights(Color.BLUE,1,1)
                .setSound(defaultSoundUri)
                .setAutoCancel(true)
                .build();

        notif.flags |= Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notif);

    }

}
