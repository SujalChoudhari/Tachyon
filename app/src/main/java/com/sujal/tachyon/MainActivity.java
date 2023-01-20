package com.sujal.tachyon;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    static final UUID UNIVERSAL_UNIQUE_IDENTIFIER = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mBluetoothSocket;
    private BluetoothDevice mHC05;

    private int mConnectionTryCount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE);

        if(savedInstanceState == null){
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

            activateBluetooth();
            getDevice();
//            connectDevice();
        }
    }


    @SuppressLint("MissingPermission")
    protected void activateBluetooth() {

        // Check if Bluetooth is available or not
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, "Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
        } else {
            // if bluetooth is enabled (status on/off)
            mBluetoothAdapter.enable();
            if (mBluetoothAdapter.isEnabled()) {
                Toast.makeText(this, "Bluetooth Activated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    protected void getDevice() {
        mHC05 = mBluetoothAdapter.getRemoteDevice("00:22:04:00:E3:E8");
        Toast.makeText(this, "Device Found (00:22:04:00:E3:E8)", Toast.LENGTH_SHORT).show();

    }

    @SuppressLint("MissingPermission")
    protected void connectDevice() {
        try {
            mBluetoothSocket = mHC05.createRfcommSocketToServiceRecord(UNIVERSAL_UNIQUE_IDENTIFIER);
            mBluetoothSocket.connect();
        } catch (IOException e) {
            Toast.makeText(this, "Failed connection, retrying 3 times (" + mConnectionTryCount + ")", Toast.LENGTH_LONG).show();
            mConnectionTryCount++;

            if (mConnectionTryCount <= 3 && !mBluetoothSocket.isConnected())
                connectDevice();

        }
    }
}