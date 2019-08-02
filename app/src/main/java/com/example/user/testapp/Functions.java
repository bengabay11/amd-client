package com.example.user.testapp;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Display;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;


// class of static methods, that relevant to the other classes of the application.
public class Functions
{

    // The function gets number of seconds and wait
    public static void sleep(int seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // The function gets title and body and create dialog.
    public static AlertDialog.Builder onCreateDialog(String title, String body, Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(body);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        return builder;
    }

    // The function execute command in the android shell.
    public static String executeCommand(String command) {

        try {
            // Executes the command.
            Process process = Runtime.getRuntime().exec(command);

            // Reads output.
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            int read;
            char[] buffer = new char[4096];
            StringBuilder output = new StringBuilder();
            while ((read = reader.read(buffer)) > 0) {
                output.append(buffer, 0, read);
            }
            reader.close();

            // Waits for the command to finish.
            process.waitFor();

            return output.toString();
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    // The function returns the coordinates of the device
    public static int[] getCoordinates(Activity activity){
        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;
        int[] screenSizeArr = new int[2];
        screenSizeArr[0] = width;
        screenSizeArr[1] = height;
        Log.d(Connected.class.getSimpleName(), "Screen Size: (" + width + "," + height + ")");
        return screenSizeArr;
    }
}