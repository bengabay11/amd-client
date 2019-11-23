package com.example.user.amd;

import android.app.Activity;
import android.graphics.Point;
import android.support.v7.app.AlertDialog;
import android.view.Display;

import com.example.user.amd.enums.ServerDataType;
import com.example.user.amd.interfaces.IServerDataHandler;
import com.example.user.amd.server_data_handlers.EmailChangedHandler;
import com.example.user.amd.server_data_handlers.ChangePasswordNeededHandler;
import com.example.user.amd.server_data_handlers.EmailSentHandler;
import com.example.user.amd.server_data_handlers.FailedSendEmailHandler;
import com.example.user.amd.server_data_handlers.IncorrectPasswordHandler;
import com.example.user.amd.server_data_handlers.IncorrectUsernameHandler;
import com.example.user.amd.server_data_handlers.InvalidEmailHandler;
import com.example.user.amd.server_data_handlers.LoadingCompleteHandler;
import com.example.user.amd.server_data_handlers.LoginCompletedHandler;
import com.example.user.amd.server_data_handlers.LogoutCompletedHandler;
import com.example.user.amd.server_data_handlers.PasswordAcceptedHandler;
import com.example.user.amd.server_data_handlers.PasswordChangedHandler;
import com.example.user.amd.server_data_handlers.UsernameAcceptedHandler;
import com.example.user.amd.server_data_handlers.UsernameChangedHandler;
import com.example.user.amd.server_data_handlers.UsernameDoesntExistHandler;
import com.example.user.amd.server_data_handlers.UsernameExistHandler;
import com.example.user.amd.tasks.SocketTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;


public class Utils {
    public static void sleep(int seconds){
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void CreateDialog(String title, String body, Activity activity)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(body);
        builder.setPositiveButton(Config.DEADULT_DIALOG_POSITIVE_BUTTON_TEXT, (dialog, id) -> {});
        builder.show();
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
        Map<ServerDataType, IServerDataHandler> serverDataHandlers =
                new HashMap<ServerDataType, IServerDataHandler>(){{
            put(ServerDataType.ChangePasswordNeeded, new ChangePasswordNeededHandler());
            put(ServerDataType.EmailChanged, new EmailChangedHandler());
            put(ServerDataType.EmailSent, new EmailSentHandler());
            put(ServerDataType.FailedSendingEmail, new FailedSendEmailHandler());
            put(ServerDataType.IncorrectPassword, new IncorrectPasswordHandler());
            put(ServerDataType.IncorrectUsername, new IncorrectUsernameHandler());
            put(ServerDataType.InvalidEmail, new InvalidEmailHandler());
            put(ServerDataType.LoadingComplete, new LoadingCompleteHandler());
            put(ServerDataType.LoginCompleted, new LoginCompletedHandler());
            put(ServerDataType.LogoutCompleted, new LogoutCompletedHandler());
            put(ServerDataType.PasswordAccepted, new PasswordAcceptedHandler());
            put(ServerDataType.PasswordChanged, new PasswordChangedHandler());
            put(ServerDataType.UsernameAccepted, new UsernameAcceptedHandler());
            put(ServerDataType.UsernameChanged, new UsernameChangedHandler());
            put(ServerDataType.UsernameDoesntExist, new UsernameDoesntExistHandler());
            put(ServerDataType.UsernameExist, new UsernameExistHandler());
        }};
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