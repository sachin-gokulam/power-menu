package me.sachinmuralig.powermenu;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.widget.RemoteViews;

public class WidgetProvider extends AppWidgetProvider {

    public static final String INTENT_SHOW_MENU = "me.sachinmuralig.powermenu.mainmenu";
    public static final String INTENT_RINGER_SILENT = "me.sachinmuralig.powermenu.ringermode.silent";
    public static final String INTENT_RINGER_VIBRATE = "me.sachinmuralig.powermenu.ringermode.vibrate";
    public static final String INTENT_RINGER_NORMAL = "me.sachinmuralig.powermenu.ringermode.normal";
    private static final String TAG = "powermenu/widget";

    @Override
    public void onUpdate( Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds ) {
        super.onUpdate( context, appWidgetManager, appWidgetIds );
    }

    protected void setRingerButtons( int audioMode, RemoteViews remoteViews ) {
        switch ( audioMode ) {

            case AudioManager.RINGER_MODE_SILENT:
                remoteViews.setImageViewResource( R.id.btnSilent, R.drawable.ic_volume_off_orange_24dp );
                remoteViews.setImageViewResource( R.id.btnVibrate, R.drawable.ic_vibration_black_24dp );
                remoteViews.setImageViewResource( R.id.btnNormal, R.drawable.ic_volume_up_black_24dp );
                break;
            case AudioManager.RINGER_MODE_VIBRATE:
                remoteViews.setImageViewResource( R.id.btnSilent, R.drawable.ic_volume_off_black_24dp );
                remoteViews.setImageViewResource( R.id.btnVibrate, R.drawable.ic_vibration_orange_24dp );
                remoteViews.setImageViewResource( R.id.btnNormal, R.drawable.ic_volume_up_black_24dp );
                break;
            case AudioManager.RINGER_MODE_NORMAL:
                remoteViews.setImageViewResource( R.id.btnSilent, R.drawable.ic_volume_off_black_24dp );
                remoteViews.setImageViewResource( R.id.btnVibrate, R.drawable.ic_vibration_black_24dp );
                remoteViews.setImageViewResource( R.id.btnNormal, R.drawable.ic_volume_up_orange_24dp );
                break;
            default:
                remoteViews.setImageViewResource( R.id.btnSilent, R.drawable.ic_volume_off_black_24dp );
                remoteViews.setImageViewResource( R.id.btnVibrate, R.drawable.ic_vibration_black_24dp );
                remoteViews.setImageViewResource( R.id.btnNormal, R.drawable.ic_volume_up_black_24dp );

        }

    }

    protected void performAction( Context context, String action ) {

        Intent in = new Intent( context, MainActivity.class );
        in.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK );

        switch ( action ) {

            case INTENT_RINGER_SILENT:
                in.setAction( INTENT_RINGER_SILENT );
                in.putExtra( Actions.KEY_RINGER_MODE, AudioManager.RINGER_MODE_SILENT );
                break;
            case INTENT_RINGER_VIBRATE:
                in.setAction( INTENT_RINGER_VIBRATE );
                in.putExtra( Actions.KEY_RINGER_MODE, AudioManager.RINGER_MODE_VIBRATE );
                break;
            case INTENT_RINGER_NORMAL:
                in.setAction( INTENT_RINGER_NORMAL );
                in.putExtra( Actions.KEY_RINGER_MODE, AudioManager.RINGER_MODE_NORMAL );
                break;
        }

        context.startActivity( in );


    }


}
