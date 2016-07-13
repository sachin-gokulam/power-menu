package me.sachinmuralig.powermenu;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider{

    Actions actions;
    private static final String TAG = "powermenu/widget";
    public static final String INTENT_SHOW_MENU = "me.sachinmuralig.powermenu.showMenu";
    public static final String INTENT_RINGER_SILENT = "me.sachinmuralig.powermenu.silent";
    public static final String INTENT_RINGER_VIBRATE = "me.sachinmuralig.powermenu.vibrate";
    public static final String INTENT_RINGER_NORMAL = "me.sachinmuralig.powermenu.normal";

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    protected void setRingerButtons(int audioMode,RemoteViews remoteViews){
        switch (audioMode){

            case AudioManager.RINGER_MODE_SILENT:
                remoteViews.setImageViewResource(R.id.btnSilent,R.drawable.ic_volume_off_orange_24dp);
                remoteViews.setImageViewResource(R.id.btnVibrate,R.drawable.ic_vibration_black_24dp);
                remoteViews.setImageViewResource(R.id.btnNormal,R.drawable.ic_volume_up_black_24dp);
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                remoteViews.setImageViewResource(R.id.btnSilent,R.drawable.ic_volume_off_black_24dp);
                remoteViews.setImageViewResource(R.id.btnVibrate,R.drawable.ic_vibration_orange_24dp);
                remoteViews.setImageViewResource(R.id.btnNormal,R.drawable.ic_volume_up_black_24dp);
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                remoteViews.setImageViewResource(R.id.btnSilent,R.drawable.ic_volume_off_black_24dp);
                remoteViews.setImageViewResource(R.id.btnVibrate,R.drawable.ic_vibration_black_24dp);
                remoteViews.setImageViewResource(R.id.btnNormal,R.drawable.ic_volume_up_orange_24dp);
                break;
            default:
                remoteViews.setImageViewResource(R.id.btnSilent,R.drawable.ic_volume_off_black_24dp);
                remoteViews.setImageViewResource(R.id.btnVibrate,R.drawable.ic_vibration_black_24dp);
                remoteViews.setImageViewResource(R.id.btnNormal,R.drawable.ic_volume_up_black_24dp);

        }

    }

    protected void performAction(Context context,String action){
        actions = new Actions(context);


        if(action.equals(WidgetProvider.INTENT_SHOW_MENU)){
            Intent in = new Intent(context,MainActivity.class);
            in.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(in);
            return;
        }
        else if(action.equals(WidgetProvider.INTENT_RINGER_SILENT)){
            actions.ringerSilent();
        }
        else if(action.equals(WidgetProvider.INTENT_RINGER_VIBRATE)){
            actions.ringerVibrate();
        }
        else if(action.equals(WidgetProvider.INTENT_RINGER_NORMAL)){
            actions.ringerNormal();
        }
    }


}
