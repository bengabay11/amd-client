package com.example.user.amd.server_data_handlers;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import com.example.user.amd.DTOs.ServerData;
import com.example.user.amd.Utils;
import com.example.user.amd.interfaces.IServerDataHandler;
import com.example.user.amd.tasks.SocketTask;

public class InvalidEmailHandler implements IServerDataHandler {
    @Override
    public void handle(ServerData data, Activity currentActivity, SocketTask socketTask) {
        String title = "Invalid Email";
        String body = "You entered invalid email. try again.";
        Utils.CreateDialog(title, body, currentActivity);
    }
}
