package com.example.user.amd.handlers;

import android.graphics.Color;
import android.widget.Button;

import com.example.user.amd.interfaces.lambdaFunctions.loginButtonHandler;

public class handleLoginButtonAvailability implements loginButtonHandler {
    @Override
    public void handle(Button loginButton, String username, String password) {
        if (username.isEmpty() || password.isEmpty()) {
            loginButton.setEnabled(false);
            loginButton.setTextColor(Color.parseColor("#808080"));
        } else {
            loginButton.setEnabled(true);
            loginButton.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }
}
