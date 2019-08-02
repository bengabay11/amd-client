package com.example.user.testapp;

import android.util.Log;


// Thread that sends all the processes that run on the device every minute.
class CheckProcesses implements Runnable
{
    private boolean continueSend;
    private SocketTask socketTask;

    CheckProcesses(SocketTask socketTask)
    {
        this.socketTask = socketTask;
    }

    public void run()
    {
        while (true)
        {
            socketTask.getCheckProcessesClass(this);
            Functions.sleep(2);
            // Execute command ps, that will return all the processes that running on the device.
            String result = Functions.executeCommand("ps");
            Log.d(SuspiciousApps.class.getSimpleName(), "ps Result: " + result);
            Log.d(CheckProcesses.class.getSimpleName(), "CheckProcesses length: " + result.length());
            while (result.length() > 900) {
                socketTask.send("CheckProcessesData," + result.substring(0, 900));
                result = result.substring(900, result.length());
//                Functions.sleep(2);
                stopSend();
            }
            socketTask.send("CheckProcessesData," + result);
//            Functions.sleep(2);
            stopSend();
            socketTask.send("CheckProcesses");
            Functions.sleep(120);
        }
    }

    // The SocketTask thread inform the SuspiciousApps thread that he can send another
    // part of the data.
    public void continueSend()
    {
        Log.d(SuspiciousApps.class.getSimpleName(), "Continue sending!");
        continueSend = true;
    }

    // The function stops the thread from sending data until he gets response from the server.
    private void stopSend()
    {
        Log.d(SuspiciousApps.class.getSimpleName(), "Stop sending!");
        continueSend = false;
        boolean printOnce = true;
        while(!continueSend){
            if(printOnce){
                printOnce = false;
                Log.d(SuspiciousApps.class.getSimpleName(), "Stuck in while loop...");
            }
        }
        Log.d(SuspiciousApps.class.getSimpleName(), "Real Continue sending!");
    }
}