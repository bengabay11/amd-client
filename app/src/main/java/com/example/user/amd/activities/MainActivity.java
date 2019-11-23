package com.example.user.amd.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.amd.Config;
import com.example.user.amd.R;
import com.example.user.amd.Utils;
import com.example.user.amd.handlers.ButtonVisibilityHandler;
import com.example.user.amd.tasks.SocketTask;
import com.example.user.amd.watchers.EmptyTextWatcher;


import java.util.Arrays;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button crossXButtonUsername;
    private Button crossXButtonPassword;
    private Button loginButton;
    private String username;

    public SocketTask socketTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = (EditText) findViewById(R.id.edit_text_username);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);
        loginButton = (Button) findViewById((R.id.login_button));
        crossXButtonUsername = (Button) findViewById((R.id.cross_x_button1));
        crossXButtonPassword = (Button) findViewById((R.id.cross_x_button2));

        initUI();
    }

    private void initUI()
    {
        loginButton.setEnabled(false);
        loginButton.setTextColor(Color.parseColor("#808080"));
        List<EditText> editTexts = Arrays.asList(editTextUsername, editTextPassword);
        TextWatcher emptyTextWatcher = new EmptyTextWatcher(editTexts, loginButton);
        editTextUsername.addTextChangedListener(emptyTextWatcher);
        editTextPassword.addTextChangedListener(emptyTextWatcher);
        ButtonVisibilityHandler crossXUsernameButtonVisibility = new ButtonVisibilityHandler(crossXButtonUsername);
        ButtonVisibilityHandler crossXPasswordButtonVisibility = new ButtonVisibilityHandler(crossXButtonPassword);
        editTextUsername.setOnFocusChangeListener(crossXUsernameButtonVisibility::handleFocus);
        editTextPassword.setOnFocusChangeListener(crossXPasswordButtonVisibility::handleFocus);
    }

    public void onLogin(View view)
    {
        this.socketTask = Utils.runSocketTask(this);
        username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        socketTask.send("Login," + username + "," + password);
    }

    public void onSignUp(View view)
    {
        username = editTextUsername.getText().toString();
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra(Config.USERNAME_KEY, username);
        startActivity(intent);
    }

    public void onForgotPassword(View view)
    {
        username = editTextUsername.getText().toString();
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        intent.putExtra(Config.USERNAME_KEY, username);
        startActivity(intent);
    }

    public void resetUsernameText(View view)
    {
        editTextUsername.setText("");
    }

    public void resetPasswordText(View view)
    {
        editTextPassword.setText("");
    }
}
