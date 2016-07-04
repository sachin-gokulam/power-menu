package me.sachinmuralig.powermenu;

import android.app.ActionBar;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


public class MainElementGenerator {

    LinearLayout elementView;
    Context context;

    MainElementGenerator(Context context){
        this.context = context;
    }

    public View getView(Drawable icon,String title){

        elementView = new LinearLayout(context);

        ImageView iconView = new ImageView(context);
        iconView.setImageDrawable(icon);
        iconView.setPaddingRelative(10, 10, 10, 10);
        //iconView.setBackground(ContextCompat.getDrawable(context, R.drawable.circle_bg));



        TextView mainLabel = new TextView(context);

        mainLabel.setText(title);
        mainLabel.setGravity(Gravity.CENTER);
        mainLabel.setPaddingRelative(30, 5, 10, 10);

        if (Build.VERSION.SDK_INT < 23) {

            mainLabel.setTextAppearance(context,android.R.style.TextAppearance_DeviceDefault_Large);

        } else {
            mainLabel.setTextAppearance(android.R.style.TextAppearance_DeviceDefault_Large);
        }
        mainLabel.setTextColor(Color.parseColor("#FF000000"));
        mainLabel.setBackgroundColor(Color.parseColor("#00000000"));

        elementView.addView(iconView);
        elementView.addView(mainLabel);

        elementView.setGravity(Gravity.START);
        elementView.setPadding(10, 20, 10, 20);

        elementView.setClickable(true);

        elementView.setBackground(ContextCompat.getDrawable(context, android.R.drawable.list_selector_background));
        //elementView.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        //elementView.setDividerPadding(5);

        return elementView;
    }

}
