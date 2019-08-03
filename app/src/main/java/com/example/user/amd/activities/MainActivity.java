package com.example.user.amd.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.amd.R;
import com.example.user.amd.handlers.ButtonVisibilityHandler;
import com.example.user.amd.tasks.SocketTask;
import com.example.user.amd.watchers.EmptyTextWatcher;


import java.util.Arrays;
import java.util.List;

import static com.example.user.amd.tasks.SocketTask.USERNAME_KEY;


public class MainActivity extends AppCompatActivity
{
    private boolean runOnce = true;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private Button crossXButtonUsername;
    private Button loginButton;

    private static String username;

    public static SocketTask socketTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = (EditText) findViewById(R.id.edit_text_username);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);
        loginButton = (Button) findViewById((R.id.login_button));
        crossXButtonUsername = (Button) findViewById((R.id.cross_x_button1));

        initUI();
        runSocketTask();
    }

    private void initUI()
    {
        List<EditText> editTexts = Arrays.asList(editTextUsername, editTextPassword);
        TextWatcher emptyTextWatcher = new EmptyTextWatcher(editTexts, loginButton);
        editTextUsername.addTextChangedListener(emptyTextWatcher);
        editTextPassword.addTextChangedListener(emptyTextWatcher);
        ButtonVisibilityHandler crossXUsernameButtonVisibility = new ButtonVisibilityHandler(crossXButtonUsername);
        ButtonVisibilityHandler crossXPasswordButtonVisibility = new ButtonVisibilityHandler(crossXButtonUsername);
        editTextUsername.setOnFocusChangeListener(crossXUsernameButtonVisibility::handleFocus);
        editTextPassword.setOnFocusChangeListener(crossXPasswordButtonVisibility::handleFocus);
    }

    private void runSocketTask()
    {
        Intent intent = getIntent();
        if(runOnce)
        {
            runOnce = false;
            socketTask = new SocketTask(MainActivity.this);
            socketTask.execute();
        }
        else
        {
            String previousActivity = intent.getStringExtra("activity");
            if(previousActivity.equals("ConnectedActivity"))
            {
                socketTask = new SocketTask(MainActivity.this);
                socketTask.execute();
            }
        }
        socketTask.setBuilder(MainActivity.this);
    }

    public void onLogin(View view)
    {
        username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        socketTask.send("Login," + username + "," + password);
    }

    public void onSignUp(View view)
    {
        username = editTextUsername.getText().toString();
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra(USERNAME_KEY, username);
        startActivity(intent);
    }

    public void onForgotPassword(View view)
    {
        username = editTextUsername.getText().toString();
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        intent.putExtra(USERNAME_KEY, username);
        startActivity(intent);
    }

    public void resetUsernameText(View view) {
        editTextUsername.setText("");
    }

    public void resetPasswordText(View view) {
        editTextPassword.setText("");
    }

    // TODO: fix the bug when the user come back to the previous activity, the activity will
    //  know how to handle it
    @Override
    public void onBackPressed() {}

    public static String getUsername() {
        return username;
    }
}
