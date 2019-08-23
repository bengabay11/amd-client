package com.example.user.amd.scanners;

import android.util.Log;

import com.example.user.amd.tasks.SocketTask;
import com.example.user.amd.Utils;


// Thread that sends all the processes that run on the device every minute.
public class Processes implements Runnable
{
    private boolean continueSend;
    private SocketTask socketTask;

    public Processes(SocketTask socketTask)
    {
        this.socketTask = socketTask;
    }

    public void run()
    {
        while (true)
        {
//            socketTask.getCheckProcessesClass(this);
            Utils.sleep(2);
            // Execute command ps, that will return all the processes that running on the device.
            String result = Utils.executeCommand("ps");
            Log.d(Apps.class.getSimpleName(), "ps Result: " + result);
            Log.d(Processes.class.getSimpleName(), "Processes length: " + result.length());
            while (result.length() > 900) {
                socketTask.send("CheckProcessesData," + result.substring(0, 900));
                result = result.substring(900, result.length());
//                Utils.sleep(2);
                stopSend();
            }
            socketTask.send("CheckProcessesData," + result);
//            Utils.sleep(2);
            stopSend();
            socketTask.send("Processes");
            Utils.sleep(120);
        }
    }

    // The SocketTask thread inform the Apps thread that he can send another
    // part of the data.
    public void continueSend()
    {
        Log.d(Apps.class.getSimpleName(), "Continue sending!");
        continueSend = true;
    }

    // The function stops the thread from sending data until he gets response from the server.
    private void stopSend()
    {
        Log.d(Apps.class.getSimpleName(), "Stop sending!");
        continueSend = false;
        boolean printOnce = true;
        while(!continueSend){
            if(printOnce){
                printOnce = false;
                Log.d(Apps.class.getSimpleName(), "Stuck in while loop...");
            }
        }
        Log.d(Apps.class.getSimpleName(), "Real Continue sending!");
    }
}