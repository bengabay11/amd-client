package com.example.user.amd.scanners;

import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.example.user.amd.activities.ConnectedActivity;
import com.example.user.amd.tasks.SocketTask;
import com.example.user.amd.Utils;


public class UnknownSources implements Runnable
{
    private SocketTask socketTask;
    private boolean finish = false;
    private ConnectedActivity currentActivity;

    public UnknownSources(ConnectedActivity activity, SocketTask socketTask){
        this.currentActivity = activity;
        this.socketTask = socketTask;
    }

    public void run()
    {
        boolean runOnce = true;
        boolean isNonPlayAppAllowed = false;
        String notification = "Unknown sources permission is allowed in settings.";
        final AlertDialog.Builder builder = Utils.CreateDialog("Unknown Sources",
                "Unknown sources permission is allowed in your settings. turn it off.", currentActivity);

        while(!finish)
        {
            // check if unknown sources permission is allowed in settings.
            try {
                isNonPlayAppAllowed = Settings.Secure.getInt(currentActivity.getContentResolver(),
                        Settings.Secure.INSTALL_NON_MARKET_APPS) == 1;
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            if(isNonPlayAppAllowed){
                // Send to server
                socketTask.send("UnknownSources,Allowed");

                // Show dialog
                currentActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    builder.show();
                    }
                });
                if(runOnce){
                    // Update the UI with the new notification.
                    runOnce = false;
                    final String finalNotification = notification;
                    currentActivity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            currentActivity.addNotification((View)null, finalNotification);
                        }
                    });
                }
            }
            else{
                runOnce = true;
                socketTask.send("UnknownSources,Not Allowed");
            }
            Utils.sleep(60);
        }
    }

    // To finish thread
    public void finish(){
        finish = true;
    }
}