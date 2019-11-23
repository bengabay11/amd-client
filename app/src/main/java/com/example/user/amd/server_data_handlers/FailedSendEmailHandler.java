package com.example.user.amd.server_data_handlers;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import com.example.user.amd.DTOs.ServerData;
import com.example.user.amd.Utils;
import com.example.user.amd.interfaces.IServerDataHandler;
import com.example.user.amd.tasks.SocketTask;

public class FailedSendEmailHandler implements IServerDataHandler {
    @Override
    public void handle(ServerData data, Activity currentActivity, SocketTask socketTask) {
        String title = "Fail Sending Email";
        String body = "The server" +
                " couldn't send to the requested email. maybe the address " +
                "does'nt exist or the server is not connected to the internet." +
                " try again with a different username or email";
        Utils.CreateDialog(title, body, currentActivity);
    }
}
