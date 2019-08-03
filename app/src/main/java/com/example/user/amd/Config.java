package com.example.user.amd;

import android.text.TextUtils;
import java.util.Collections;


public class Config
{
    public static String sendDelimiter = TextUtils.join("", Collections.nCopies(3, Character.toString ((char) 31)));
    public static String receiveDelimiter = Character.toString ((char) 30);

    public static String SMS_URI_INBOX = "content://sms/inbox";
    public static String SMS_URI_ALL = "content://sms/";

    public static final int SERVER_PORT = 6000;
    public static final String SERVER_IP = "172.16.1.95";
}
