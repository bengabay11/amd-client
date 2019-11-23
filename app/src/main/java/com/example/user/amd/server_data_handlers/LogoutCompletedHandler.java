package com.example.user.amd.server_data_handlers;

import android.app.Activity;
import android.content.Intent;

import com.example.user.amd.DTOs.ServerData;
import com.example.user.amd.activities.MainActivity;
import com.example.user.amd.interfaces.IServerDataHandler;
import com.example.user.amd.tasks.SocketTask;

public class LogoutCompletedHandler implements IServerDataHandler {
    @Override
    public void handle(ServerData data, Activity currentActivity, SocketTask socketTask) {
        socketTask.finish();
        Intent i = new Intent(currentActivity, MainActivity.class);
        i.putExtra("activity", "ConnectedActivity");
        currentActivity.startActivity(i);
    }
}
