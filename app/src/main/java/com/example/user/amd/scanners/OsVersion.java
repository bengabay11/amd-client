package com.example.user.amd.scanners;

import android.os.Build;

import com.example.user.amd.tasks.SocketTask;


// The thread will send the current OS version to the server.
public class OsVersion implements Runnable
{
    private SocketTask socketTask;

    public OsVersion(SocketTask socketTask)
    {
        this.socketTask = socketTask;
    }

    public void run()
    {
        socketTask.send("CheckVersion," + Build.VERSION.RELEASE);
    }
}
