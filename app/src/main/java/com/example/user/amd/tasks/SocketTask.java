package com.example.user.amd.tasks;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.example.user.amd.Config;
import com.example.user.amd.Utils;
import com.example.user.amd.scanners.CheckProcesses;
import com.example.user.amd.activities.ConnectedActivity;
import com.example.user.amd.activities.CreatePasswordActivity;
import com.example.user.amd.activities.LoadingActivity;
import com.example.user.amd.activities.MainActivity;
import com.example.user.amd.activities.SignUpActivity;
import com.example.user.amd.scanners.SmishingDetect;
import com.example.user.amd.scanners.SuspiciousApps;
import com.example.user.amd.scanners.UnknownSources;
import com.example.user.amd.encryption.AESCipher;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;


public class SocketTask extends AsyncTask<String,String ,String >
{
    public static final String USERNAME_KEY = "com.example.user.java.com.amd.USERNAME";
    private String dataToSend = "";
    private String response = "";
    private Activity currentActivity;
    private SuspiciousApps sa1;
    private boolean socketError= false;
    private boolean send = false;
    private boolean finish = false;
    private ConnectedActivity connected_activity;
    private UnknownSources unknownSources;
    private ArrayList listNotifications;
    private SmishingDetect sm1;
    private AESCipher aesCipher;
    private String AESKey;
    private CheckProcesses cp1;

    public SocketTask(MainActivity activity)
    {
        currentActivity = activity;
        listNotifications = new ArrayList();
    }

    // Set the builder according to the current activity.
    public void setBuilder(Activity activity) {
        currentActivity = activity;
    }

    // Set the connected activity as the context.
    public void setContextConnected(ConnectedActivity activity) {
        connected_activity = activity;
        for(Object notification : listNotifications){
            connected_activity.addNotification((View)null, notification.toString());
        }
    }

    // The actual thread that runs.
    @Override
    protected String doInBackground(String... voids)
    {
        Log.d(SocketTask.class.getSimpleName(), "trying to connect to " + Config.SERVER_IP +
                " on port " + Config.SERVER_PORT + "...");
        try
        {
            // Try to connect to the server
            Socket s = new Socket();
            s.connect(new InetSocketAddress(Config.SERVER_IP, Config.SERVER_PORT), 3*1000);
            PrintWriter writer = new PrintWriter(s.getOutputStream());
            InputStream is = s.getInputStream();

            // Receive the AES key from the server.
            byte[] buffer = new byte[1024];
            int red = is.read(buffer);
            byte[] redData = new byte[red];
            System.arraycopy(buffer, 0, redData, 0, red);
            response = new String(redData, "UTF-8");
            if(response.split(",")[0].equals("AESKey"))
                AESKey = response.split(",")[1];
            Log.d(SocketTask.class.getSimpleName(), "AES Key:" + AESKey);
            aesCipher = new AESCipher(AESKey);

            // Start the receive and send threads.
            Thread writeThread = new Thread(new WriteData(writer));
            Thread receiveThread = new Thread(new ReceiveData(is));
            writeThread.start();
            receiveThread.start();
        }
        catch (IOException e)
        {
            Log.d(SocketTask.class.getSimpleName(), "Socket Error");
            socketError = true;
            publishProgress();
            dataToSend = "exit";
        }

        return null;
    }



    // The function gets the data and set the dataToSend parameter. the WriteData thread will
    // send it.
    public void send(String data, boolean encrypt, boolean delimiter)
    {
        Log.d(SocketTask.class.getSimpleName(), "Sending to server: " + data);
        if (encrypt) {
            // Encrypt the data
            try {
                data = aesCipher.encrypt(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(delimiter){
            // Adds the delimiter to the data
            data = data + Config.sendDelimiter;
        }
        dataToSend = data;
        send = true;
    }

    // Optional function that sets default values to the encrypt and delimiter parameters in the send function.
    public void send(String data) {
        send(data, true, true);
    }

    // Thread that runs in while loop and send data to the server when the "send" parameter
    // becomes true.
    private class WriteData implements Runnable {
        PrintWriter writer;

        WriteData(PrintWriter writer) {
            this.writer = writer;
        }

        public void run() {
            while (!finish) {
                if (send)
                {
                    writer.write(dataToSend);
                    writer.flush();
                    send = false;
                }
            }
        }
    }

    // Thread that runs in while loop and receive data from the server.
    private class ReceiveData implements Runnable {

        private InputStream is;

        ReceiveData(InputStream is) {
            this.is = is;
        }

        public void run()
        {
            while (!finish)
            {
                try {
                    byte[] buffer = new byte[1024];
                    int red = is.read(buffer);
                    byte[] redData = new byte[red];
                    System.arraycopy(buffer, 0, redData, 0, red);
                    response = new String(redData, "UTF-8");
                } catch (Exception e)
                {
                    e.printStackTrace();
                    socketError = true;
                }
                publishProgress();
            }
        }
    }

    public void getSuspiciousAppsClass(SuspiciousApps sa1) {
        this.sa1 = sa1;
    }

    public void getCheckProcessesClass(CheckProcesses cp1) {
        this.cp1 = cp1;
    }

    public void getUnknownSourcesClass(UnknownSources un1) {
        this.unknownSources = un1;
    }

    public void getSmishingDetectClass(SmishingDetect sm1) {
        this.sm1 = sm1;
    }

    // The function called meanwhile the doInBackground and handle the UI according to the
    // response from the server.
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onProgressUpdate(String... s)
    {
        super.onProgressUpdate(s);
        // Check if there is any socket error.
        if(socketError && !dataToSend.startsWith("LogOut"))
        {
            if(currentActivity.getClass().getName().equals("com.example.user.java.com.amd.ConnectedActivity"))
                unknownSources.finish();
            finish = true;
            AlertDialog.Builder builder = Utils.CreateDialog("Connection Error", "The AMD" +
                    " server is not available at this time. Please try again later.", currentActivity);
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id)
                {
                    System.exit(0);
                }
            });
            builder.show();
            socketError = false;
        }

        // Handle the response from the server.
        else {
            for (String currentResponse : response.split(Config.receiveDelimiter)) {
                if (!currentResponse.equals("")) {
                    // decrypt the response from server
                    try {
                        currentResponse = aesCipher.decrypt(currentResponse);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // Handle the current response
                    Log.d(SocketTask.class.getSimpleName(), "current response: " + currentResponse);
                    if (currentResponse.equals("Incorrect Username")) {
                        AlertDialog.Builder builder = Utils.CreateDialog("Username Does'nt" +
                                " exist", "The username you entered does'nt exist." +
                                " please try again.", currentActivity);
                        builder.show();
                    }
                    if (currentResponse.equals("Incorrect Password")) {
                        AlertDialog.Builder builder = Utils.CreateDialog("Incorrect Password",
                                "The The password you entered is incorrect. please try again.", currentActivity);
                        builder.show();
                    }
                    if (currentResponse.equals("Login Complete")) {
                        String username = MainActivity.getUsername();
                        Intent i = new Intent(currentActivity, LoadingActivity.class);
                        i.putExtra(USERNAME_KEY, username);
                        currentActivity.startActivity(i);
                    }

                    if (currentResponse.equals("Login Complete, need to change password")) {
                        String username = SignUpActivity.getUsername();
                        if (username == null) {
                            username = MainActivity.getUsername();
                        }
                        Intent i = new Intent(currentActivity, CreatePasswordActivity.class); //use context here
                        i.putExtra(USERNAME_KEY, username);
                        currentActivity.startActivity(i);
                    }

                    if (currentResponse.equals("Username Exist")) {
                        AlertDialog.Builder builder = Utils.CreateDialog("Username Already" +
                                " Exist", "There is already AMD user with this username." +
                                " please try again.", currentActivity);
                        builder.show();
                    }

                    if (currentResponse.equals("Username Accepted")) {
                        String username = SignUpActivity.getUsername();
                        Intent i = new Intent(currentActivity, LoadingActivity.class);
                        i.putExtra(USERNAME_KEY, username);
                        currentActivity.startActivity(i);
                    }

                    if (currentResponse.equals("Email Sent")) {
                        AlertDialog.Builder builder = Utils.CreateDialog("Email Sent",
                                "Your password sent to your email. if you don't get anything, " +
                                        "send the request again.", currentActivity);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent i = new Intent(currentActivity, MainActivity.class);
                                i.putExtra("activity", "ForgotPasswordActivity");
                                currentActivity.startActivity(i);
                            }
                        });
                        builder.show();
                    }

                    if (currentResponse.equals("Fail Sending Email")) {
                        AlertDialog.Builder builder = Utils.CreateDialog("Fail Sending Email", "The server" +
                                " couldn't send to the requested email. maybe the address " +
                                "does'nt exist or the server is not connected to the internet." +
                                " try again with a different username or email", currentActivity);
                        builder.show();
                    }

                    if (currentResponse.contains("Loading Complete")) {
                        String username = SignUpActivity.getUsername();
                        if (username == null) {
                            username = MainActivity.getUsername();
                        }
                        Intent i = new Intent(currentActivity, ConnectedActivity.class); //use context here
                        i.putExtra(USERNAME_KEY, username);
                        currentActivity.startActivity(i);
                    }

                    if (currentResponse.equals("Password Accepted")) {
                        String username = SignUpActivity.getUsername();
                        Intent i = new Intent(currentActivity, LoadingActivity.class);
                        i.putExtra(USERNAME_KEY, username);
                        currentActivity.startActivity(i);
                    }

                    if (currentResponse.equals("Username Does'nt Exist")) {
                        AlertDialog.Builder builder = Utils.CreateDialog("Username Does'nt " +
                                "Exist", "There is no user that has this username or email." +
                                " please try again.", currentActivity);
                        builder.show();
                    }
                    if (currentResponse.equals("Username Changed")) {
                        AlertDialog.Builder builder = Utils.CreateDialog("Successfully " +
                                "Changed Username", "The AMD server has saved the new username." +
                                " please try again.", currentActivity);
                        ConnectedActivity.usernameChanged();
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                connected_activity.backSettings((View) null);
                            }
                        });
                        builder.show();
                    }
                    if (currentResponse.equals("Email Changed")) {
                        AlertDialog.Builder builder = Utils.CreateDialog("Successfully " +
                                "Changed Email", "The AMD server has saved the new email.", currentActivity);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                connected_activity.backSettings((View) null);
                            }
                        });
                        builder.show();
                    }
                    if (currentResponse.equals("Password Changed")) {
                        AlertDialog.Builder builder = Utils.CreateDialog("Successfully " +
                                "Changed Password", "The AMD server has saved the new password.", currentActivity);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                connected_activity.backSettings((View) null);
                            }
                        });
                        builder.show();

                    }
                    if (currentResponse.equals("Invalid Email")) {
                        AlertDialog.Builder builder = Utils.CreateDialog("Invalid Email",
                                "You entered invalid email. try again.", currentActivity);
                        builder.show();
                    }
                    if(currentResponse.equals("CheckApps part accepted")){
                        sa1.continueSend();
                    }
                    if(currentResponse.equals("CheckProcesses part accepted")){
                        cp1.continueSend();
                    }
                    if (currentResponse.split(",")[0].equals("Notification")) {
                        final String notification = currentResponse.split(",")[1];
                        try {
                            connected_activity.runOnUiThread(new Runnable() {

                                @Override
                                public void run() {
                                    connected_activity.addNotification((View) null, notification);
                                }
                            });
                        } catch (Exception ignored) {
                            listNotifications.add(notification);
                        }
                    }
                    if (currentResponse.equals("Logout Successfully") || currentResponse.equals("Delete Complete")) {
                        if(currentActivity.getClass().getName().equals("com.example.user.java.com.amd.ConnectedActivity"))
                            unknownSources.finish();

                        finish = true;
                        Intent i = new Intent(currentActivity, MainActivity.class);
                        i.putExtra("activity", "ConnectedActivity");
                        currentActivity.startActivity(i);
                    }
                }
            }
        }
    }
}