package com.example.user.testapp;

import android.os.Build;


// The thread will send the current OS version to the server.
class osReport implements Runnable
{
    private SocketTask socketTask;

    osReport(SocketTask socketTask)
    {
        this.socketTask = socketTask;
    }

    public void run()
    {
        socketTask.send("CheckVersion," + Build.VERSION.RELEASE);
    }
}
