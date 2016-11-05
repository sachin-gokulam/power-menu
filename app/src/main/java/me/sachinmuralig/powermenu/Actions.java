package me.sachinmuralig.powermenu;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import eu.chainfire.libsuperuser.Shell;

public class Actions {
    Context context;
    AudioManager am;

    protected static final String COMMAND_SHUTDOWN_BROADCAST = "am broadcast android.intent.action.ACTION_SHUTDOWN";
    protected static final String COMMAND_SHUTDOWN = "reboot -p";
    protected static final String COMMAND_REBOOT = "reboot";
    protected static final String COMMAND_SLEEP = "sleep 2";
    protected static final String COMMAND_FLIGHT_MODE_ON = "settings put global airplane_mode_on 1";
    protected static final String COMMAND_FLIGHT_MODE_OFF = "settings put global airplane_mode_on 0";
    protected static final String COMMAND_FLIGHT_MODE_BROADCAST_ON = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state 1";
    protected static final String COMMAND_FLIGHT_MODE_BROADCAST_OFF = "am broadcast -a android.intent.action.AIRPLANE_MODE --ez state 0";


    Actions(Context context){
        this.context = context;
        am = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
    }

    public static boolean isRooted(){
        return Shell.SU.available();
    }

    public boolean isFlightModeOn() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return Settings.System.getInt(context.getContentResolver(),
                    Settings.System.AIRPLANE_MODE_ON, 0) != 0;
        } else {
            return Settings.Global.getInt(context.getContentResolver(),
                    Settings.Global.AIRPLANE_MODE_ON, 0) != 0;
        }
    }

    private void showProgress(){
        ProgressDialog pgdlg = new ProgressDialog(context,android.R.style.Theme_DeviceDefault_Dialog_Alert);
        pgdlg.setIndeterminate(true);
        pgdlg.setMessage("Shutting Down...");
        pgdlg.show();
    }

    private void showToast(String msg,int duration){
        Toast toast = Toast.makeText(context,msg,duration);

        View view = toast.getView();
        view.setBackgroundResource(android.R.drawable.toast_frame);

        TextView toastMessage = (TextView) view.findViewById(android.R.id.message);

        toastMessage.setBackgroundColor((Color.parseColor("#00000000")));

        toast.show();
    }

    public void shutdown(){

        if(!isRooted()){
            showToast("This Action requires ROOT permissions",Toast.LENGTH_LONG);
            return;
        }

        showProgress();

        (new BackgroundTask(new String[]{COMMAND_SHUTDOWN_BROADCAST,COMMAND_SLEEP,COMMAND_SHUTDOWN})).execute();


    }

    public void reboot(){
        if(!isRooted()){
            showToast("This Action requires ROOT permissions", Toast.LENGTH_LONG);
            return;
        }
        showProgress();
        (new BackgroundTask(new String[]{COMMAND_SHUTDOWN_BROADCAST,COMMAND_SLEEP,COMMAND_REBOOT})).execute();
    }

    public void flightModeToggle(){

        if(!isRooted()){
            showToast("This Action requires ROOT permissions", Toast.LENGTH_LONG);
            return;
        }
        if(isFlightModeOn()) {
            showToast("Exiting Flight Mode...",Toast.LENGTH_SHORT);
            (new BackgroundTask(new String[]{COMMAND_FLIGHT_MODE_OFF,COMMAND_FLIGHT_MODE_BROADCAST_OFF})).execute();
        }
        else{
            showToast("Entering Flight Mode...",Toast.LENGTH_SHORT);
            (new BackgroundTask(new String[]{COMMAND_FLIGHT_MODE_ON,COMMAND_FLIGHT_MODE_BROADCAST_ON})).execute();
        }
    }

    public void ringerSilent(){
        am.setRingerMode(AudioManager.RINGER_MODE_SILENT);
        //showToast("Silent Mode Activated",Toast.LENGTH_SHORT);
    }
    public void ringerVibrate(){
        am.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        //showToast("Vibration Mode Activated",Toast.LENGTH_SHORT);
    }
    public void ringerNormal(){
        am.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        //showToast("Normal Mode Activated",Toast.LENGTH_SHORT);
    }


    private class BackgroundTask extends AsyncTask<Void,Void,List<String>>{

        String commands[];

        BackgroundTask(String[] commands){
            this.commands = commands;
        }


        @Override
        protected List<String> doInBackground(Void... params) {

            return Shell.SU.run(commands);

        }

        @Override
        protected void onPostExecute(List<String> result){
            if(result==null){
                showToast("Failed to Execute command - Root Permission was denied",Toast.LENGTH_LONG);
            }
        }
    }

}
