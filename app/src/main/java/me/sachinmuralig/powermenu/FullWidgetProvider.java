package me.sachinmuralig.powermenu;


import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.RemoteViews;

public class FullWidgetProvider extends WidgetProvider {
    private final static String TAG = "FullWidget";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        final int count = appWidgetIds.length;

        AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        int audioMode = audioManager.getRingerMode();

        for(int i=0;i<count;i++){

            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_complete);
            Log.e(TAG, "Ringer mode " + audioMode);
            setRingerButtons(audioMode,remoteViews);

            Intent showMenu = new Intent(context, FullWidgetProvider.class);
            showMenu.setAction(WidgetProvider.INTENT_SHOW_MENU);
            remoteViews.setOnClickPendingIntent(R.id.btnShutdown, PendingIntent.getBroadcast(context, i, showMenu, PendingIntent.FLAG_UPDATE_CURRENT));

            Intent silent = new Intent(context,FullWidgetProvider.class);
            silent.setAction(WidgetProvider.INTENT_RINGER_SILENT);
            remoteViews.setOnClickPendingIntent(R.id.btnSilent,PendingIntent.getBroadcast(context,i,silent,0));

            Intent vibrate = new Intent(context,FullWidgetProvider.class);
            vibrate.setAction(WidgetProvider.INTENT_RINGER_VIBRATE);
            remoteViews.setOnClickPendingIntent(R.id.btnVibrate, PendingIntent.getBroadcast(context, i, vibrate, 0));

            Intent normal = new Intent(context,FullWidgetProvider.class);
            normal.setAction(WidgetProvider.INTENT_RINGER_NORMAL);
            remoteViews.setOnClickPendingIntent(R.id.btnNormal,PendingIntent.getBroadcast(context,i,normal,0));

            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);

        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Log.e(TAG, "Rcvd Broadcast " + intent.getAction());
        String action = intent.getAction();

        performAction(context,action);

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context.getApplicationContext());
        ComponentName fullWidget = new ComponentName(context.getApplicationContext(), FullWidgetProvider.class);
        int[] fullAppWidgetIds = appWidgetManager.getAppWidgetIds(fullWidget);
        if (fullAppWidgetIds != null && fullAppWidgetIds.length > 0) {
            Log.e(TAG, "updating widget ");
            onUpdate(context, appWidgetManager, fullAppWidgetIds);
        }
    }
}
