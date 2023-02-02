package com.sujal.tachyon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;

public class ApplicationActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    private static boolean mIsGyroscopeOn = false;

    BluetoothManager mBluetoothManager;
    private Gyroscope mGyroscope;
    private Accelerometer accelerometer;


    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 0.2F);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);

        mBluetoothManager = new BluetoothManager(this);
        mGyroscope = new Gyroscope(this);
        accelerometer = new Accelerometer(this);

        mBluetoothManager.activateBluetooth();

    }

    @Override
    protected void onResume() {
        super.onResume();
        mGyroscope.register();
        accelerometer.register();
    }

    // create on pause method
    @Override
    protected void onPause() {
        super.onPause();
        mGyroscope.unregister();
        accelerometer.unregister();
    }


    public void onForwardPressed(View view) {
//        Toast.makeText(this, "FOR", Toast.LENGTH_SHORT).show();
        view.startAnimation(buttonClick);
        if (mBluetoothManager != null)
            mBluetoothManager.sendCharacter('w');
    }

    public void onReversePressed(View view) {
//        Toast.makeText(this, "BCK", Toast.LENGTH_SHORT).show();
        view.startAnimation(buttonClick);
        if (mBluetoothManager != null)
            mBluetoothManager.sendCharacter('s');
    }

    public void onLeftPressed(View view) {
//        Toast.makeText(this, "LFT", Toast.LENGTH_SHORT).show();
        view.startAnimation(buttonClick);
        if (mBluetoothManager != null)
            mBluetoothManager.sendCharacter('a');
    }

    public void onRightPressed(View view) {
//        Toast.makeText(this, "RGT", Toast.LENGTH_SHORT).show();
        view.startAnimation(buttonClick);
        if (mBluetoothManager != null)
            mBluetoothManager.sendCharacter('d');
    }

    public void onStopPressed(View view) {
//        Toast.makeText(this, "STP", Toast.LENGTH_SHORT).show();
        view.startAnimation(buttonClick);
        if (mBluetoothManager != null)
            mBluetoothManager.sendCharacter('o');
    }

    public void onConnectPressed(View view){
        if(mBluetoothManager != null){
            mBluetoothManager.connectDevice();
        }

        Toast.makeText(this, "Connected!", Toast.LENGTH_SHORT).show();
    }

    public void onDisconnectPressed(View view){
        if(mBluetoothManager != null){
            mBluetoothManager.disconnectDevice();
        }

        Toast.makeText(this, "Disconnected!", Toast.LENGTH_SHORT).show();
    }





    public void onGyroscopePressed(View view) {
        Switch button = (Switch) view;
        mIsGyroscopeOn = button.isChecked();
        if (mIsGyroscopeOn) {
            mGyroscope.setListener(new Gyroscope.Listener() {
                // on rotation method of gyroscope
                @Override
                public void onRotation(float rx, float ry, float rz) {
                    if (!mIsGyroscopeOn) return;
                    // set the color green if the device rotates on positive z axis
                    if (rz > 1.3f) {
                        getWindow().getDecorView().setBackgroundColor(Color.rgb(100, 11, 11));
                        mBluetoothManager.sendCharacter('a');
                        // Left
                    } else if (rz < -1.3f) {
                        getWindow().getDecorView().setBackgroundColor(Color.rgb(11, 11, 100));
                        mBluetoothManager.sendCharacter('d');
                        //Right
                    }
                }

            });

            accelerometer.setListener(new Accelerometer.Listener() {
                //on translation method of accelerometer
                @Override
                public void onTranslation(float tx, float ty, float ts) {
                    // set the color red if the device moves in positive x axis
                    if (ts > 2.0f) {
                        getWindow().getDecorView().setBackgroundColor(Color.rgb(11,100,11));
                        mBluetoothManager.sendCharacter('s');
                    }
                    // set the color blue if the device moves in negative x axis
                    else if (ts < -2.0f) {
                        getWindow().getDecorView().setBackgroundColor(Color.rgb(100,11,100));
                        mBluetoothManager.sendCharacter('w');
                    }
                }
            });
        }
    }

    public void onSpeakPressed(View view) {
//        Toast.makeText(this, "SPK", Toast.LENGTH_SHORT).show();

        Intent intent
                = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

        try {
            startActivityForResult(intent, REQUEST_CODE_SPEECH_INPUT);
        } catch (Exception e) {
            Toast.makeText(ApplicationActivity.this, " " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);

                ArrayList<Character> output = Command.processRawInput(result.get(0));


                // Send Characters
                for (Character c : output) {
                    mBluetoothManager.sendCharacter(c);
                    Toast.makeText(this,c, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}