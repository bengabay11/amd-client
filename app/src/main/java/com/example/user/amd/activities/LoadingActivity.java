package com.example.user.amd.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.amd.R;
import com.example.user.amd.scanners.SuspiciousApps;
import com.example.user.amd.tasks.SocketTask;


public class LoadingActivity extends AppCompatActivity
{
    public static SocketTask socketTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        socketTask = MainActivity.socketTask;
        socketTask.setBuilder(LoadingActivity.this);

        SuspiciousApps sa1 = new SuspiciousApps(getPackageManager(), socketTask);
        Thread suspiciousAppsThread = new Thread(sa1);
        suspiciousAppsThread.start();
    }
}
