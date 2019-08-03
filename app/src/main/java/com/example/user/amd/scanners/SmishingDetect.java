package com.example.user.amd.scanners;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.user.amd.Config;
import com.example.user.amd.tasks.SocketTask;

import java.util.concurrent.TimeUnit;


// Thread that sends all the SMS inbox to the server.
public class SmishingDetect implements Runnable
{
    private SocketTask socketTask;
    private Context mContext;
    private boolean permission;

    SmishingDetect(SocketTask socketTask, Context mContext) {
        this.socketTask = socketTask;
        this.mContext = mContext;
    }

    public void run()
    {
        try{
            sendInbox();
        }
        catch (RuntimeException e){
            e.printStackTrace();
            permission = false;
            handlePermission();
            while (!permission){}
            sendInbox();
        }
    }

    // The function send to the server all the sms inbox of the device.
    private void sendInbox(){
        socketTask.getSmishingDetectClass(this);
        String dataToSend = "";
        Log.d(SmishingDetect.class.getSimpleName(), "Starting read SMS inbox...");
        Uri uriSMSURI = Uri.parse(Config.SMS_URI_INBOX);
        Cursor cur = this.mContext.getContentResolver().query(uriSMSURI, null, null, null, null);
        if (cur != null) {
            while (cur.moveToNext()) {
                String address = cur.getString(cur.getColumnIndex("address"));
                String body = cur.getString(cur.getColumnIndexOrThrow("body"));
                Log.d(SmishingDetect.class.getSimpleName(), "Number: " + address + " .Message: " + body);
                dataToSend += address + "#^%" + body + "&*(";
                dataToSend = dataToSend.substring(0, dataToSend.length()-3);
            }
        }

        while (dataToSend.length() > 900) {
            Log.d(CheckProcesses.class.getSimpleName(), "Length: " + dataToSend.length());
            socketTask.send("CheckSmishingData," + dataToSend.substring(0, 900));
            dataToSend = dataToSend.substring(900, dataToSend.length());
            sleep(10);
        }
        socketTask.send("CheckSmishingData," + dataToSend);
        sleep(2);
        socketTask.send("CheckSmishing");
    }

    // The function gets number of seconds and wait
    private void sleep(int seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // When permission is allowed by the client.
    public void setPermission()
    {
        Log.d(SmishingDetect.class.getSimpleName(), "permission allowed!");
        permission = true;
    }

    // The function requests from the client permission to send and view sms.
    private void handlePermission()
    {
        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.RECEIVE_SMS)
                != PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity)mContext,
                    Manifest.permission.RECEIVE_SMS)) {}
            else
            {
                ActivityCompat.requestPermissions((Activity)mContext,
                        new String[]{Manifest.permission.RECEIVE_SMS}, 0);
            }
        }
    }
}
