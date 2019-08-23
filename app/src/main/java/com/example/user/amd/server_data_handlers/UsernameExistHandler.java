package com.example.user.amd.server_data_handlers;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import com.example.user.amd.DTOs.ServerData;
import com.example.user.amd.Utils;
import com.example.user.amd.interfaces.IServerDataHandler;
import com.example.user.amd.tasks.SocketTask;

public class UsernameExistHandler implements IServerDataHandler {
    @Override
    public void handle(ServerData data, Activity currentActivity, SocketTask socketTask) {
        String title = "Username Already Exist";
        String body = "There is already AMD user with this username. please try again.";
        AlertDialog.Builder builder = Utils.CreateDialog(title, body, currentActivity);
        builder.show();
    }
}
