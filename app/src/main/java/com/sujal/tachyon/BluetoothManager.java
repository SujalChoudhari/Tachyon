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

    private Character mLastSentCharacter = '-';

    private int mConnectionTryCount = 1;

    BluetoothManager(Activity context) {
        mContext = context;
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    @SuppressLint("MissingPermission")
    protected void activateBluetooth() {

        // Check if Bluetooth is available or not
        if (mBluetoothAdapter == null) {
            Toast.makeText(mContext, "Bluetooth Not Supported", Toast.LENGTH_SHORT).show();
        } else {
            // if bluetooth is enabled (status on/off)
            if (!mBluetoothAdapter.isEnabled())
                mBluetoothAdapter.enable();

            if (mBluetoothAdapter.isEnabled()) {
                Toast.makeText(mContext, "Bluetooth Activated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("MissingPermission")
    protected void getDevice() {
        mHC05 = mBluetoothAdapter.getRemoteDevice("00:22:04:00:E3:E8");
        Toast.makeText(mContext, "Device Found (00:22:04:00:E3:E8)", Toast.LENGTH_SHORT).show();
        System.out.println(mHC05);
    }

    @SuppressLint("MissingPermission")
    protected void connectDevice() {
        try {
            if (!mBluetoothAdapter.isEnabled()) {
                activateBluetooth();
            }
            if (mBluetoothSocket == null) {
                getDevice();
                mBluetoothSocket = mHC05.createRfcommSocketToServiceRecord(UNIVERSAL_UNIQUE_IDENTIFIER);
                mBluetoothSocket.connect();
                mConnectionTryCount = 0;
            }
        } catch (IOException e) {
            Toast.makeText(mContext, "Failed connection, Retrying (" + mConnectionTryCount + "/3)", Toast.LENGTH_LONG).show();
            mConnectionTryCount++;

            if (mConnectionTryCount <= 3 && !mBluetoothSocket.isConnected())
                connectDevice();

        } catch (Exception e) {
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }


    protected void sendCharacter(char data) {
        if (mLastSentCharacter.equals((Character) data)) {
            return;
        }

        if (mBluetoothSocket != null) {
            try {
                OutputStream outStream = mBluetoothSocket.getOutputStream();
                outStream.write(data);

                return;
            } catch (IOException e) {
                e.printStackTrace();

            } catch (Exception e) {
                Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
            }
        }
        mLastSentCharacter = data;
        System.out.println(data);

    }

    protected String getCharacters() {
        String result = "";
        if (mBluetoothSocket != null) {
            try {
                InputStream inputStream = mBluetoothSocket.getInputStream();
                inputStream.skip(inputStream.available());
                for (int i = 0; i < 24; i++) {
                    byte b = (byte) inputStream.read();
                    result = result + (char) b;
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
            }

        }

        return result;
    }

    protected void disconnectDevice() {
        try {
            if (mBluetoothSocket != null)
                mBluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Failed to disconnect", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(mContext, e.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
