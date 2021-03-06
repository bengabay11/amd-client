package com.example.user.testapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


// The activity present loading page until all the relevant data will send to the server.
public class LoadingActivity extends AppCompatActivity
{
    public static SocketTask socketTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        socketTask = MainActivity.socketTask;
        socketTask.setBuilder(LoadingActivity.this);

        // Suspicious Apps check
        SuspiciousApps sa1 = new SuspiciousApps(getPackageManager(), socketTask);
        Thread suspiciousAppsThread = new Thread(sa1);
        suspiciousAppsThread.start();
//        socketTask.getSuspiciousAppsClass(sa1);
    }
}
