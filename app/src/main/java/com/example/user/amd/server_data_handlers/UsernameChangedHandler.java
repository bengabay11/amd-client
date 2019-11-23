package com.example.user.amd.server_data_handlers;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.example.user.amd.DTOs.ServerData;
import com.example.user.amd.Utils;
import com.example.user.amd.activities.ConnectedActivity;
import com.example.user.amd.interfaces.IServerDataHandler;
import com.example.user.amd.tasks.SocketTask;

public class UsernameChangedHandler implements IServerDataHandler {
    @Override
    public void handle(ServerData data, Activity currentActivity, SocketTask socketTask) {
        Utils.CreateDialog("Successfully " +
                "Changed Username", "The AMD server has saved the new username." +
                " please try again.", currentActivity);
        ConnectedActivity.usernameChanged();
        socketTask.connected_activity.backSettings(null);
    }
}
