package com.example.user.amd.server_data_handlers;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.example.user.amd.DTOs.ServerData;
import com.example.user.amd.Utils;
import com.example.user.amd.activities.MainActivity;
import com.example.user.amd.interfaces.IServerDataHandler;
import com.example.user.amd.tasks.SocketTask;

public class EmailSentHandler implements IServerDataHandler {
    @Override
    public void handle(ServerData data, Activity currentActivity, SocketTask socketTask) {
        String title = "Email Sent";
        String body = "Your password sent to your email. if you don't get anything, " +
                "send the request again.";
        Utils.CreateDialog(title, body, currentActivity);
        Intent i = new Intent(currentActivity, MainActivity.class);
        i.putExtra("activity", "ForgotPasswordActivity");
        currentActivity.startActivity(i);
    }
}
