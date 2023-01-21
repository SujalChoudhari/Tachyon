package com.sujal.tachyon;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_SPEECH_INPUT = 1;

    BluetoothManager mBluetoothManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);


        mBluetoothManager = new BluetoothManager(this);
        if (mBluetoothManager.activateBluetooth()) {
            mBluetoothManager.getDevice();
        }
    }


    public void onForwardPressed(View view) {
        Toast.makeText(this, "FOR", Toast.LENGTH_SHORT).show();
        if (mBluetoothManager != null)
            mBluetoothManager.sendCharacter('w');
    }

    public void onReversePressed(View view) {
        Toast.makeText(this, "BCK", Toast.LENGTH_SHORT).show();
        if (mBluetoothManager != null)
            mBluetoothManager.sendCharacter('s');
    }

    public void onLeftPressed(View view) {
        Toast.makeText(this, "LFT", Toast.LENGTH_SHORT).show();
        if (mBluetoothManager != null)
            mBluetoothManager.sendCharacter('a');
    }

    public void onRightPressed(View view) {
        Toast.makeText(this, "RGT", Toast.LENGTH_SHORT).show();
        if (mBluetoothManager != null)
            mBluetoothManager.sendCharacter('d');
    }

    public void onStopPressed(View view) {
        Toast.makeText(this, "STP", Toast.LENGTH_SHORT).show();
        if (mBluetoothManager != null)
            mBluetoothManager.sendCharacter('o');
    }

    public void onSpeakPressed(View view) {
        Toast.makeText(this, "SPK", Toast.LENGTH_SHORT).show();

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
            Toast.makeText(MainActivity.this, " " + e.getMessage(),
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
                Toast.makeText(this, result.get(0), Toast.LENGTH_SHORT).show();
            }
        }
    }
}