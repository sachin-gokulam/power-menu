package me.sachinmuralig.powermenu;


import android.app.Activity;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;

class Actions {

    static final int CALLBACK_ID_DND_PERMISSION = 9876;
    static final String KEY_RINGER_MODE = "ringer_mode";
    private static final String COMMAND_SHUTDOWN_BROADCAST = "am broadcast android.intent.action.ACTION_SHUTDOWN";
    private static final String COMMAND_SHUTDOWN = "reboot -p";
    private static final String COMMAND_REBOOT = "reboot";
    private static final String COMMAND_SLEEP = "sleep 2";
    private static final String COMMAND_FLIGHT_MODE_ON = "settings put global airplane_mode_on 1";
    private static final String COMMAND_FLIGHT_MODE_OFF = "settings put global airplane_mode_on 0";
    private static final String COMMAND_FLIGHT_MODE_BROADCAST_ON = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state 1";
    private static final String COMMAND_FLIGHT_MODE_BROADCAST_OFF = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state 0";
    private Activity activity;
    private AudioManager am;

    Actions( Activity activity ) {
        this.activity = activity;
        am = ( AudioManager ) activity.getSystemService( Context.AUDIO_SERVICE );
    }

    void shutdown() {

        if ( !isRooted() ) {
            showToast( "This Action requires ROOT permissions", Toast.LENGTH_LONG );
            return;
        }

        showProgress();

        ( new BackgroundTask( new String[]{ COMMAND_SHUTDOWN_BROADCAST, COMMAND_SLEEP, COMMAND_SHUTDOWN } ) ).execute();


    }

    private static boolean isRooted() {
        return Shell.SU.available();
    }

    private void showToast( String msg, int duration ) {
        Toast toast = Toast.makeText( activity, msg, duration );

        View view = toast.getView();
        view.setBackgroundResource( android.R.drawable.toast_frame );

        TextView toastMessage = ( TextView ) view.findViewById( android.R.id.message );

        toastMessage.setBackgroundColor( ( Color.parseColor( "#00000000" ) ) );

        toast.show();
    }

    private void showProgress() {
        if ( android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP_MR1 ) {

            ProgressDialog pgdlg = new ProgressDialog( activity, android.R.style.Theme_DeviceDefault_Dialog_Alert );
            pgdlg.setIndeterminate( true );
            pgdlg.setMessage( "Shutting Down..." );
            pgdlg.show();
        }
        else {
            showToast( "Shutting Down...", Toast.LENGTH_LONG );
        }
    }

    void reboot() {
        if ( !isRooted() ) {
            showToast( "This Action requires ROOT permissions", Toast.LENGTH_LONG );
            return;
        }
        showProgress();
        ( new BackgroundTask( new String[]{ COMMAND_SHUTDOWN_BROADCAST, COMMAND_SLEEP, COMMAND_REBOOT } ) ).execute();
    }

    void flightModeToggle() {

        if ( !isRooted() ) {
            showToast( "This Action requires ROOT permissions", Toast.LENGTH_LONG );
            return;
        }
        if ( isFlightModeOn() ) {
            showToast( "Exiting Flight Mode...", Toast.LENGTH_SHORT );
            ( new BackgroundTask( new String[]{ COMMAND_FLIGHT_MODE_OFF, COMMAND_FLIGHT_MODE_BROADCAST_OFF } ) ).execute();
        }
        else {
            showToast( "Entering Flight Mode...", Toast.LENGTH_SHORT );
            ( new BackgroundTask( new String[]{ COMMAND_FLIGHT_MODE_ON, COMMAND_FLIGHT_MODE_BROADCAST_ON } ) ).execute();
        }
    }

    boolean isFlightModeOn() {
        if ( Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1 ) {
            return Settings.System.getInt( activity.getContentResolver(),
                                           Settings.System.AIRPLANE_MODE_ON, 0 ) != 0;
        }
        else {
            return Settings.Global.getInt( activity.getContentResolver(),
                                           Settings.Global.AIRPLANE_MODE_ON, 0 ) != 0;
        }
    }

    void setRingerMode( int ringerMode ) {

        try {

            if ( ringerMode != AudioManager.RINGER_MODE_SILENT &&
                 ringerMode != AudioManager.RINGER_MODE_VIBRATE &&
                 ringerMode != AudioManager.RINGER_MODE_NORMAL ) {

                throw new IllegalArgumentException( "Invalid Ringer Mode " + ringerMode );
            }

            am.setRingerMode( ringerMode );
        }
        catch ( SecurityException e ) {
            checkPermissionAndSetRingerMode( ringerMode );
        }
        catch ( Exception e ) {
            e.printStackTrace();
            showToast( "Failed to switch Ringer Mode", Toast.LENGTH_SHORT );
        }
    }

    private void checkPermissionAndSetRingerMode( int ringerMode ) {

        if ( Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {

            NotificationManager notificationManager = ( NotificationManager ) activity.getApplicationContext()
                                                                                      .getSystemService( Context.NOTIFICATION_SERVICE );

            assert notificationManager != null;

            if ( notificationManager.isNotificationPolicyAccessGranted() ) {

                am.setRingerMode( ringerMode );

            }
            else {
                Intent intent = new Intent( Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS );
                intent.putExtra( KEY_RINGER_MODE, ringerMode );
                activity.startActivityForResult( intent, CALLBACK_ID_DND_PERMISSION );
            }
        }
    }


    private class BackgroundTask extends AsyncTask< Void, Void, List< String > > {

        String commands[];

        BackgroundTask( String[] commands ) {
            this.commands = commands;
        }


        @Override
        protected List< String > doInBackground( Void... params ) {

            return Shell.SU.run( commands );

        }

        @Override
        protected void onPostExecute( List< String > result ) {
            if ( result == null ) {
                showToast( "Failed to Execute command - Root Permission was denied", Toast.LENGTH_LONG );
            }
        }
    }

}
