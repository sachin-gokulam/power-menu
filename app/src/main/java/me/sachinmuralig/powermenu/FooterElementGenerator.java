package me.sachinmuralig.powermenu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

class FooterElementGenerator {

    private Context context;
    private LinearLayout elementView;

    FooterElementGenerator( Context context ) {
        this.context = context;
    }

    View getView( Drawable icon ) {

        elementView = new LinearLayout( context );

        ImageView iconView = new ImageView( context );
        iconView.setImageDrawable( icon );
        iconView.setPaddingRelative( 10, 10, 10, 10 );


        elementView.addView( iconView );
        LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT, 1.0f );
        elementView.setLayoutParams( param );
        elementView.setGravity( Gravity.CENTER );
        elementView.setPadding( 20, 20, 20, 20 );
        elementView.setClickable( true );
        elementView.setBackground( ContextCompat.getDrawable( context, android.R.drawable.list_selector_background ) );

        return elementView;
    }
}
