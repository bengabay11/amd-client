package com.example.user.amd.interfaces;

import android.app.Activity;

import com.example.user.amd.DTOs.ServerData;
import com.example.user.amd.tasks.SocketTask;

public interface IServerDataHandler {
    void handle(ServerData data, Activity currentActivity, SocketTask socketTask);
}
