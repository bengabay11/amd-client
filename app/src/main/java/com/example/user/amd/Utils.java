package com.example.user.amd;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;

import com.example.user.amd.activities.ConnectedActivity;
import com.example.user.amd.activities.MainActivity;
import com.example.user.amd.enums.ServerDataType;
import com.example.user.amd.interfaces.IServerDataHandler;
import com.example.user.amd.tasks.SocketTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Dictionary;
import java.util.concurrent.TimeUnit;


public class Utils {
    public static void sleep(int seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static AlertDialog.Builder CreateDialog(String title, String body, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(body);
        builder.setPositiveButton("OK", (dialog, id) -> {
        });
        return builder;
    }

    public static String executeCommand(String command) {

        try {
            Process process = Runtime.getRuntime().exec(command);
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            int read;
            char[] buffer = new char[4096];
            StringBuilder output = new StringBuilder();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();
            process.waitFor();
            return output.toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static SocketTask runSocketTask(Activity currentActivity)
    {
        // TODO: add init for the server data handlers
        Dictionary<ServerDataType, IServerDataHandler> serverDataHandlers = null;
        SocketTask socketTask = new SocketTask(currentActivity, serverDataHandlers);
        socketTask.execute();
        return socketTask;
    }

    public static int[] getScreenSize(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int[] screenSizeArr = new int[2];
        screenSizeArr[0] = width;
        screenSizeArr[1] = height;
        return screenSizeArr;
    }
}