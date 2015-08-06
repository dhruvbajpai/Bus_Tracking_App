package com.example.dhruv.bus_tracking_app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;


public class startpage extends Activity{

    ProgressDialog progress;
    private static int SPLASH_TIME_OUT = 5000;
    ImageView iv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startpage);

        //progress = ProgressDialog.show(getApplicationContext(), "Fetching Information","Loading..", true);

        iv = (ImageView)findViewById(R.id.iv);
        Animation ar = AnimationUtils.loadAnimation(this, R.anim.rotation);
        iv.startAnimation(ar);

        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slidefromleft, R.anim.slidetoleft);
            }
        }, SPLASH_TIME_OUT);
    }



}
