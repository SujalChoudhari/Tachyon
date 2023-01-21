package com.sujal.tachyon;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class BluetoothManager {

    static final UUID UNIVERSAL_UNIQUE_IDENTIFIER = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private Activity mContext;
    private BluetoothAdapter mBluetoothAdapter;
    private BluetoothSocket mBluetoothSocket;
    private BluetoothDevice mHC05;

    private int mConnectionTryCount = 1;
    BluetoothManager(Activity context){
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @SuppressLint("MissingPermission")
    protected boolean activateBluetooth() {

        // Check if Bluetooth is available or not
        if (mBluetoothAdapter == null) {
            Toast.makeText(mContext, "Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            // if bluetooth is enabled (status on/off)

            mBluetoothAdapter.enable();
            if (mBluetoothAdapter.isEnabled()) {
                Toast.makeText(mContext, "Bluetooth Activated", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    @SuppressLint("MissingPermission")
    protected void getDevice() {
        mHC05 = mBluetoothAdapter.getRemoteDevice("00:22:04:00:E3:E8");
        Toast.makeText(mContext, "Device Found (00:22:04:00:E3:E8)", Toast.LENGTH_SHORT).show();
    }

    @SuppressLint("MissingPermission")
    protected boolean connectDevice() {
        try {
            mBluetoothSocket = mHC05.createRfcommSocketToServiceRecord(UNIVERSAL_UNIQUE_IDENTIFIER);
            mBluetoothSocket.connect();
            return true;
        } catch (IOException e) {
            Toast.makeText(mContext, "Failed connection, Retrying (" + mConnectionTryCount + "/3)", Toast.LENGTH_LONG).show();
            mConnectionTryCount++;

            if (mConnectionTryCount <= 3 && !mBluetoothSocket.isConnected())
                connectDevice();

        }
        return false;
    }

    protected boolean sendCharacter(char data) {
        if (mBluetoothSocket != null) {
            try {
                OutputStream outStream = mBluetoothSocket.getOutputStream();
                outStream.write(data);
                return true;
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        System.out.println(data);
        return false;
    }

    protected String getCharacters(){
        String result = "";
        if(mBluetoothSocket != null){
            try {
                InputStream inputStream = mBluetoothSocket.getInputStream();
                inputStream.skip(inputStream.available());
                for(int i=0;i <24;i++){
                    byte b = (byte) inputStream.read();
                    result = result + (char) b;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return result;
    }

    protected void disconnectDevice() {
        try {
            mBluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Failed to disconnect", Toast.LENGTH_LONG).show();
        }
    }
}
