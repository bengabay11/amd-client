package com.example.user.amd.server_data_handlers;

import android.app.Activity;
import android.content.Intent;

import com.example.user.amd.Config;
import com.example.user.amd.DTOs.ServerData;
import com.example.user.amd.activities.ConnectedActivity;
import com.example.user.amd.interfaces.IServerDataHandler;
import com.example.user.amd.tasks.SocketTask;

public class LoadingCompleteHandler implements IServerDataHandler {
    @Override
    public void handle(ServerData data, Activity currentActivity, SocketTask socketTask) {
        Intent intent = new Intent(currentActivity, ConnectedActivity.class); //use context here
        String username = intent.getStringExtra(Config.USERNAME_KEY);
        intent.putExtra(Config.USERNAME_KEY, username);
        currentActivity.startActivity(intent);
    }
}
