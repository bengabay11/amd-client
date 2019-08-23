package com.example.user.amd.server_data_handlers;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import com.example.user.amd.DTOs.ServerData;
import com.example.user.amd.Utils;
import com.example.user.amd.interfaces.IServerDataHandler;
import com.example.user.amd.tasks.SocketTask;

public class PasswordChangedHandler implements IServerDataHandler {
    @Override
    public void handle(ServerData data, Activity currentActivity, SocketTask socketTask) {
        String title = "Successfully Changed Password";
        String body = "The AMD server has saved the new password.";
        AlertDialog.Builder builder = Utils.CreateDialog(title, body, currentActivity);
        builder.setPositiveButton("OK", (dialog, id) -> socketTask.connected_activity.backSettings(null));
        builder.show();
    }
}
