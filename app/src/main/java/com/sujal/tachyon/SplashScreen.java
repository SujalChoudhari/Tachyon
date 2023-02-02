package com.sujal.tachyon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);



        progressBar = findViewById(R.id.progressBar);

        progressBar.setProgress(50,true);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                // on below line we are
                // creating a new intent
                Intent i = new Intent(SplashScreen.this, ApplicationActivity.class);

                progressBar.setProgress(100,true);
                // on below line we are
                // starting a new activity.
                startActivity(i);

                // on the below line we are finishing
                // our current activity.
                finish();
            }
        }, 3000);
    }
}