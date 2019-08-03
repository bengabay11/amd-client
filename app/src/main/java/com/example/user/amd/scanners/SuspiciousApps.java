package com.example.user.amd.scanners;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.example.user.amd.tasks.SocketTask;

import java.util.List;


// Thread that sends all the applications info to the server.
public class SuspiciousApps implements Runnable
{
    private PackageManager pm1;
    private SocketTask socketTask;
    private boolean continueSend = false;

    public SuspiciousApps(PackageManager pm1, SocketTask socketTask)
    {
        this.pm1 = pm1;
        this.socketTask = socketTask;
    }

    // The thread will send to the server all the application that installed on the device,
    // their permissions, and their installer.
    public void run()
    {
        socketTask.getSuspiciousAppsClass(this);
        String dataToSend = getApplicationsInfo();
        while (dataToSend.length() > 1024) {
            socketTask.send("CheckAppsData," + dataToSend.substring(0, 1024));
            dataToSend = dataToSend.substring(1024, dataToSend.length());
//            Utils.sleep(500, true);
            stopSend();
        }
        socketTask.send("CheckAppsData," + dataToSend);
//        Utils.sleep(500, true);
        stopSend();
        socketTask.send("CheckApps");
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

    // The function gets all the data of the apps that installed on the device: their name, their
    // installer, and their permissions, and create on big string with the data.
    private String getApplicationsInfo(){
        String dataToSend = "";
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List pkgAppsList = this.pm1.queryIntentActivities(mainIntent, 0);
        for (Object obj : pkgAppsList)
        {
            ResolveInfo resolveInfo = (ResolveInfo) obj;
            PackageInfo packageInfo = null;
            try
            {
                packageInfo = pm1.getPackageInfo(resolveInfo.activityInfo.packageName,
                        PackageManager.GET_PERMISSIONS | PackageManager.GET_ACTIVITIES | PackageManager.GET_PROVIDERS);
            }
            catch (PackageManager.NameNotFoundException e)
            {
                e.printStackTrace();
            }
            assert packageInfo != null;
            String[] requestedPermissions = packageInfo.requestedPermissions;
            String appName = packageInfo.applicationInfo.loadLabel(pm1).toString();
            String packageName = packageInfo.applicationInfo.packageName;
            String installer = this.pm1.getInstallerPackageName(packageInfo.packageName);
            if(installer == null || isSystemPackage(packageInfo))
                installer = "System App";
            dataToSend += appName + ":" + packageName + ":" + installer + ":";
            if (requestedPermissions != null)
            {
                for(String permissionInfo : requestedPermissions)
                {
                    String[] permissionList = permissionInfo.split("\\.");
                    String permission = permissionList[permissionList.length-1];
                    dataToSend += permission + "/";
                }
                dataToSend = dataToSend.substring(0, dataToSend.length()-1);
            }
            dataToSend += "&";
        }
        dataToSend = dataToSend.substring(0, dataToSend.length()-1);
        Log.d(SuspiciousApps.class.getSimpleName(), "CheckApps Data Length: " + dataToSend.length());
        return dataToSend;
    }

    // The function gets Package Info and checks if the package is package system or not.
    private boolean isSystemPackage(PackageInfo pkgInfo)
    {
        return ((pkgInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }
}
