package com.example.user.amd.watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.amd.interfaces.lambdaFunctions.loginButtonHandler;


public class passwordTextWatcher implements TextWatcher {
    private EditText editTextUsername;
    private Button loginButton;
    private loginButtonHandler loginButtonHandler;

    public passwordTextWatcher(EditText currenteditTextUsername, Button loginButton,
                               loginButtonHandler loginButtonHandler) {
        this.editTextUsername = currenteditTextUsername;
        this.loginButton = loginButton;
        this.loginButtonHandler = loginButtonHandler;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        String username = editTextUsername.getText().toString();
        String password = s.toString();
        loginButtonHandler.handle(loginButton, username, password);
    }
    @Override
    public void afterTextChanged(Editable s) {
    }
}
