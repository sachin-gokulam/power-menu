package me.sachinmuralig.powermenu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

public class MainActivity extends Activity {

    private static final String LOG_TAG = "MainActivity";
    LinearLayout viewMainElements, viewFooterElements;
    View shutdown, reboot, flightMode;
    View silent, vibrate, normal;
    MainElementGenerator elementGenerator;
    FooterElementGenerator footerElementGenerator;
    Actions actions;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate( savedInstanceState );
        this.getWindow().requestFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_main );

        actions = new Actions( this );

        addMainElements();
        addFooterElements();

    }

    @Override
    protected void onStart() {
        super.onStart();

        try {

            switch ( this.getIntent().getAction() ) {
                case WidgetProvider.INTENT_RINGER_SILENT:
                    setRingerModeSilentAndFinish();
                    break;
                case WidgetProvider.INTENT_RINGER_VIBRATE:
                    setRingerModeVibrateAndFinish();
                    break;
                case WidgetProvider.INTENT_RINGER_NORMAL:
                    setRingerModeNormalAndFinish();
                    break;
                default:
                    Log.i( LOG_TAG, "No ringer action to be performed found" );

            }
        }
        catch ( Exception e ) {
            Log.d( LOG_TAG, "No intent action found" );
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult( int requestCode, int resultCode, Intent data ) {

        if ( requestCode == Actions.CALLBACK_ID_DND_PERMISSION ) {
            int ringerMode = data.getIntExtra( Actions.KEY_RINGER_MODE, AudioManager.RINGER_MODE_VIBRATE );

            switch ( ringerMode ) {
                case AudioManager.RINGER_MODE_SILENT:
                    setRingerModeSilentAndFinish();
                    break;
                case AudioManager.RINGER_MODE_VIBRATE:
                    setRingerModeVibrateAndFinish();
                    break;
                case AudioManager.RINGER_MODE_NORMAL:
                    setRingerModeNormalAndFinish();
            }

        }
    }

    private void addMainElements() {

        viewMainElements = ( LinearLayout ) findViewById( R.id.viewMainElements );

        elementGenerator = new MainElementGenerator( this );

        Drawable shutDownIcon = ContextCompat.getDrawable( this, R.drawable.ic_power_settings_new_black_24dp );
        shutdown = elementGenerator.getView( shutDownIcon, "Shutdown" );
        shutdown.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                actions.shutdown();
            }
        } );

        Drawable rebootIcon = ContextCompat.getDrawable( this, R.drawable.ic_loop_black_24dp );
        reboot = elementGenerator.getView( rebootIcon, "Reboot" );
        reboot.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                actions.reboot();
            }
        } );

        Drawable flightOffIcon = ContextCompat.getDrawable( this, R.drawable.ic_airplanemode_inactive_black_24dp );
        Drawable flightOnIcon = ContextCompat.getDrawable( this, R.drawable.ic_airplanemode_active_black_24dp );

        if ( actions.isFlightModeOn() ) {

            flightMode = elementGenerator.getView( flightOffIcon, "Exit Flight Mode" );
            flightMode.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    actions.flightModeToggle();
                    MainActivity.this.finish();
                }
            } );


        }
        else {

            flightMode = elementGenerator.getView( flightOnIcon, "Enter Flight Mode" );
            flightMode.setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View v ) {
                    actions.flightModeToggle();
                    MainActivity.this.finish();
                }
            } );

        }

        viewMainElements.addView( shutdown );
        viewMainElements.addView( reboot );
        viewMainElements.addView( flightMode );


    }

    private void addFooterElements() {

        viewFooterElements = ( LinearLayout ) findViewById( R.id.viewFooterElements );
        footerElementGenerator = new FooterElementGenerator( this );

        Drawable silentIcon = ContextCompat.getDrawable( this, R.drawable.ic_volume_off_black_24dp );
        Drawable vibrateIcon = ContextCompat.getDrawable( this, R.drawable.ic_vibration_black_24dp );
        Drawable normalIcon = ContextCompat.getDrawable( this, R.drawable.ic_volume_up_black_24dp );

        silent = footerElementGenerator.getView( silentIcon );
        silent.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                setRingerModeSilentAndFinish();
            }
        } );

        vibrate = footerElementGenerator.getView( vibrateIcon );
        vibrate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                setRingerModeVibrateAndFinish();
            }
        } );

        normal = footerElementGenerator.getView( normalIcon );
        normal.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View v ) {
                setRingerModeNormalAndFinish();
            }
        } );

        viewFooterElements.addView( silent );
        viewFooterElements.addView( vibrate );
        viewFooterElements.addView( normal );
    }

    private void setRingerModeSilentAndFinish() {
        actions.setRingerMode( AudioManager.RINGER_MODE_SILENT );
        MainActivity.this.finish();
    }

    private void setRingerModeVibrateAndFinish() {
        actions.setRingerMode( AudioManager.RINGER_MODE_VIBRATE );
        MainActivity.this.finish();
    }

    private void setRingerModeNormalAndFinish() {
        actions.setRingerMode( AudioManager.RINGER_MODE_NORMAL );
        MainActivity.this.finish();
    }


}
