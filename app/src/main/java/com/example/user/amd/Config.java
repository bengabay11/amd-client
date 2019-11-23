package com.example.user.amd;

import android.text.TextUtils;
import java.util.Collections;


public class Config
{
    public static final String EMPTY_STRING = "";
    public static final String DEADULT_DIALOG_POSITIVE_BUTTON_TEXT = "OK";

    public static final String GRAY = "#808080";

    public static String sendDelimiter = TextUtils.join("", Collections.nCopies(3, Character.toString ((char) 31)));
    public static String receiveDelimiter = Character.toString ((char) 30);

    public static String SMS_URI_INBOX = "content://sms/inbox";
    public static String SMS_URI_ALL = "content://sms/";

    public static final int SERVER_PORT = 6000;
    public static final String SERVER_IP = "192.168.1.46";

    public static final String USERNAME_KEY = "com.example.user.java.com.amd.USERNAME";
    public static final String SOCKET_TASk_KEY = "com.example.user.java.com.amd.SOCKET_TASK";
}
