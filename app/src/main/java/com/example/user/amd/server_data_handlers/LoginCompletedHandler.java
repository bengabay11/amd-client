package com.example.user.amd.server_data_handlers;

import android.app.Activity;
import android.content.Intent;

import com.example.user.amd.Config;
import com.example.user.amd.DTOs.ServerData;
import com.example.user.amd.activities.LoadingActivity;
import com.example.user.amd.activities.MainActivity;
import com.example.user.amd.interfaces.IServerDataHandler;
import com.example.user.amd.tasks.SocketTask;

public class LoginCompletedHandler implements IServerDataHandler {

    @Override
    public void handle(ServerData data, Activity currentActivity, SocketTask socketTask) {
        String username = MainActivity.getUsername();
        Intent i = new Intent(currentActivity, LoadingActivity.class);
        i.putExtra(Config.USERNAME_KEY, username);
        currentActivity.startActivity(i);
    }
}