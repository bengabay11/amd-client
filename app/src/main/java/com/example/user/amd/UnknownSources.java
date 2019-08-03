package com.example.user.amd;

import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.view.View;


// Thread that checks "Unknown Sources" permission in settings.
class UnknownSources implements Runnable
{
    private SocketTask socketTask;
    private boolean finish = false;
    private Connected currentActivity;

    UnknownSources(Connected activity, SocketTask socketTask){
        this.currentActivity = activity;
        this.socketTask = socketTask;
    }

    public void run()
    {
        boolean runOnce = true;
        boolean isNonPlayAppAllowed = false;
        String notification = "Unknown sources permission is allowed in settings.";
        // Create dialog
        final AlertDialog.Builder builder = Functions.onCreateDialog("Unknown Sources",
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
            Functions.sleep(60);
        }
    }

    // To finish thread
    void finish(){
        finish = true;
    }
}