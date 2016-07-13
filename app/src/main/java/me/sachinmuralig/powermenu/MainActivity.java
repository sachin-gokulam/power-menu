package me.sachinmuralig.powermenu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.AudioManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.Toast;

public class MainActivity extends Activity {

    LinearLayout viewMainElements,viewFooterElements;
    View shutdown,reboot,recovery,bootloader,flightMode;
    View silent,vibrate,normal;

    MainElementGenerator elementGenerator;
    FooterElementGenerator footerElementGenerator;

    Actions actions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        actions = new Actions(this);

        addMainElements();
        addFooterElements();

    }

    private void addMainElements(){

        viewMainElements = (LinearLayout) findViewById(R.id.viewMainElements);

        elementGenerator = new MainElementGenerator(this);

        Drawable shutDownIcon = ContextCompat.getDrawable(this, R.drawable.ic_power_settings_new_black_24dp);
        shutdown = elementGenerator.getView(shutDownIcon,"Shutdown");
        shutdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.shutdown();
            }
        });

        Drawable rebootIcon = ContextCompat.getDrawable(this, R.drawable.ic_loop_black_24dp);
        reboot = elementGenerator.getView(rebootIcon,"Reboot");
        reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.reboot();
            }
        });

        Drawable flightOffIcon = ContextCompat.getDrawable(this, R.drawable.ic_airplanemode_inactive_black_24dp);
        Drawable flightOnIcon = ContextCompat.getDrawable(this, R.drawable.ic_airplanemode_active_black_24dp);

        if(actions.isFlightModeOn()){

            flightMode = elementGenerator.getView(flightOffIcon,"Exit Flight Mode");
            flightMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actions.flightModeToggle();
                    MainActivity.this.finish();
                }
            });


        }
        else{

            flightMode = elementGenerator.getView(flightOnIcon,"Enter Flight Mode");
            flightMode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    actions.flightModeToggle();
                    MainActivity.this.finish();
                }
            });

        }

        viewMainElements.addView(shutdown);
        viewMainElements.addView(reboot);
        viewMainElements.addView(flightMode);


    }
    private void addFooterElements(){
        viewFooterElements = (LinearLayout) findViewById(R.id.viewFooterElements);
        footerElementGenerator = new FooterElementGenerator(this);

        Drawable silentIcon = ContextCompat.getDrawable(this,R.drawable.ic_volume_off_black_24dp);
        Drawable vibrateIcon = ContextCompat.getDrawable(this,R.drawable.ic_vibration_black_24dp);
        Drawable normalIcon = ContextCompat.getDrawable(this,R.drawable.ic_volume_up_black_24dp);

        silent = footerElementGenerator.getView(silentIcon);
        silent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.ringerSilent();
                MainActivity.this.finish();
            }
        });

        vibrate = footerElementGenerator.getView(vibrateIcon);
        vibrate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.ringerVibrate();
                MainActivity.this.finish();
            }
        });

        normal = footerElementGenerator.getView(normalIcon);
        normal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actions.ringerNormal();
                MainActivity.this.finish();
            }
        });

        viewFooterElements.addView(silent);
        viewFooterElements.addView(vibrate);
        viewFooterElements.addView(normal);
    }


}
