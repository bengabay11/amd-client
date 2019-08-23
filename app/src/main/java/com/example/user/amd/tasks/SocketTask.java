package com.example.user.amd.tasks;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.example.user.amd.Config;
import com.example.user.amd.DTOs.ServerData;
import com.example.user.amd.Utils;
import com.example.user.amd.enums.ServerDataType;
import com.example.user.amd.interfaces.IServerDataHandler;
import com.example.user.amd.activities.ConnectedActivity;
import com.example.user.amd.activities.MainActivity;
import com.example.user.amd.encryption.AESICipher;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Dictionary;


public class SocketTask extends AsyncTask<String,String ,String> implements Serializable {
    private String dataToSend = "";
    private String response = "";
    private String encryptedSerializedServerData;
    private Activity currentActivity;
    private boolean socketError= false;
    private boolean send = false;
    private boolean finish = false;
    private ArrayList listNotifications;
    private AESICipher aesCipher;
    private String AESKey;
    private Dictionary<ServerDataType, IServerDataHandler> serverDataHandlers;

    public ConnectedActivity connected_activity;

    public SocketTask(MainActivity activity, Dictionary<ServerDataType,
            IServerDataHandler> serverDataHandlers)
    {
        currentActivity = activity;
        this.serverDataHandlers = serverDataHandlers;
        listNotifications = new ArrayList();
    }

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

    private class ReceiveData implements Runnable {
        private InputStream inputStream;

        ReceiveData(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        public void run()
        {
            while (!finish)
            {
                try {
                    byte[] buffer = new byte[1024];
                    int red = inputStream.read(buffer);
                    byte[] redData = new byte[red];
                    System.arraycopy(buffer, 0, redData, 0, red);
                    encryptedSerializedServerData = new String(redData, StandardCharsets.UTF_8);
                } catch (Exception e)
                {
                    e.printStackTrace();
                    socketError = true;
                }
                publishProgress();
            }
        }
    }

    private void startCommunicationThreads(PrintWriter writer, InputStream inputStream) {
        Thread writeThread = new Thread(new WriteData(writer));
        Thread receiveThread = new Thread(new ReceiveData(inputStream));
        writeThread.start();
        receiveThread.start();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void receiveAESKey(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int red = inputStream.read(buffer);
        byte[] redData = new byte[red];
        System.arraycopy(buffer, 0, redData, 0, red);
        response = new String(redData, StandardCharsets.UTF_8);
        if(response.split(",")[0].equals("AESKey"))
            AESKey = response.split(",")[1];
        Log.d(SocketTask.class.getSimpleName(), "AES Key:" + AESKey);
        aesCipher = new AESICipher(AESKey);
    }


    private ServerData deserializeServerData(String serializedServerData) {
        return new ServerData(ServerDataType.EmailChanged, response);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected String doInBackground(String... voids)
    {
        try
        {
            Socket s = new Socket();
            s.connect(new InetSocketAddress(Config.SERVER_IP, Config.SERVER_PORT), 3*1000);
            PrintWriter writer = new PrintWriter(s.getOutputStream());
            InputStream inputStream = s.getInputStream();

            this.receiveAESKey(inputStream);
            this.startCommunicationThreads(writer, inputStream);
        }
        catch (IOException e)
        {
            socketError = true;
            publishProgress();
        }

        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onProgressUpdate(String... s)
    {
        super.onProgressUpdate(s);

        if(!socketError)
        {
            try {
                String serializedServerData = aesCipher.decrypt(encryptedSerializedServerData);
                ServerData serverData = this.deserializeServerData(serializedServerData);
                this.serverDataHandlers.get(serverData.dataType).handle(serverData, currentActivity, this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            finish = true;
            String title = "Connection Error";
            String body = "The AMD server is not available at this time. Please try again later.";
            AlertDialog.Builder builder = Utils.CreateDialog(title, body, currentActivity);
            builder.setPositiveButton("OK", (dialog, id) -> System.exit(0));
            builder.show();
            socketError = false;
        }
    }

    public void setBuilder(Activity activity) {
        currentActivity = activity;
    }

    public void setContextConnected(ConnectedActivity activity) {
        connected_activity = activity;
        for(Object notification : listNotifications){
            connected_activity.addNotification(null, notification.toString());
        }
    }

    public void send(String data, boolean encrypt, boolean delimiter)
    {
        Log.d(SocketTask.class.getSimpleName(), "Sending to server: " + data);
        if (encrypt) {
            try {
                data = aesCipher.encrypt(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if(delimiter){
            data = data + Config.sendDelimiter;
        }
        dataToSend = data;
        send = true;
    }

    public void send(String data) {
        this.send(data, true, true);
    }

    public void finish() {
        this.finish = true;
    }
}