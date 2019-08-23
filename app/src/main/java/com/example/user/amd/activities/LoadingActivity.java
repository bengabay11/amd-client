package com.example.user.amd.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.user.amd.Config;
import com.example.user.amd.R;
import com.example.user.amd.scanners.Apps;
import com.example.user.amd.tasks.SocketTask;


public class LoadingActivity extends AppCompatActivity
{
    public static SocketTask socketTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        Intent intent = getIntent();
        socketTask = (SocketTask) intent.getSerializableExtra(Config.SOCKET_TASk_KEY);
        socketTask.setBuilder(LoadingActivity.this);

        Apps sa1 = new Apps(getPackageManager(), socketTask);
        Thread suspiciousAppsThread = new Thread(sa1);
        suspiciousAppsThread.start();
    }
}
