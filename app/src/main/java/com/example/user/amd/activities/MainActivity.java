package com.example.user.amd.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.amd.Utils;
import com.example.user.amd.R;
import com.example.user.amd.tasks.SocketTask;

import java.io.*;


// The activity that the application runs when it starts.
public class MainActivity extends AppCompatActivity
{
    public static final String USERNAME_KEY = "com.example.user.java.com.amd.USERNAME";
    private static boolean runOnce = true;
    private EditText editTextUsername;
    private EditText editTextPassword;
    static String username;
    static String password;
    public static SocketTask socketTask;
    private Button crossXButtonUsername;
    private Button crossXButtonPassword;
    private Button loginButton;
    private Button signUpButton;
    private Button forgotPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextUsername = (EditText) findViewById(R.id.edit_text_username);
        editTextPassword = (EditText) findViewById(R.id.edit_text_password);
        loginButton = (Button) findViewById((R.id.login_button));
        signUpButton = (Button) findViewById((R.id.sign_up_button1));
        crossXButtonUsername = (Button) findViewById((R.id.cross_x_button1));
        crossXButtonPassword = (Button) findViewById((R.id.cross_x_button2));
        forgotPasswordButton = (Button) findViewById((R.id.forgot_password_button));

        handleUI();
        connectOnce();
        loginButton.setEnabled(false);
        loginButton.setTextColor(Color.parseColor("#808080"));
        socketTask.setBuilder(MainActivity.this);
    }

    // The function handle the UI according to the screen size.
    // The function also take care of the buttons visibility in the UI.
    private void handleUI()
    {
        int[] screenSize = Utils.getScreenSize(MainActivity.this);
        int width = screenSize[0], height=screenSize[1];
        if(width == 1440 && height == 2560)
        {
            loginButton.setTextSize(20);
            signUpButton.setTextSize(20);
            editTextUsername.setTextSize(28);
            editTextPassword.setTextSize(28);
            forgotPasswordButton.setTextSize(15);
        }
        // disable the login button if the username field is empty
        editTextUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String password = editTextPassword.getText().toString();
                String username = s.toString();
                if (username.equals("") || password.equals("")) {
                    loginButton.setEnabled(false);
                    loginButton.setTextColor(Color.parseColor("#808080"));
                } else {
                    loginButton.setEnabled(true);
                    loginButton.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // disable the login button if the password field is empty
        editTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String username = editTextUsername.getText().toString();
                String password = s.toString();
                if (username.equals("") || password.equals("")) {
                    loginButton.setEnabled(false);
                    loginButton.setTextColor(Color.parseColor("#808080"));
                } else {
                    loginButton.setEnabled(true);
                    loginButton.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        // hide the cross x button of the username if the focus is not on the username field
        editTextUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    crossXButtonUsername.setVisibility(View.GONE);
                else
                    crossXButtonUsername.setVisibility(View.VISIBLE);
            }
        });
        // hide the cross x button of the password if the focus is not on the password field
        editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus)
                    crossXButtonPassword.setVisibility(View.GONE);
                else
                    crossXButtonPassword.setVisibility(View.VISIBLE);
            }
        });
    }

    // The function restart the connection if it needed.
    private void connectOnce()
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
            String activity = intent.getStringExtra("activity");
            Log.d(MainActivity.class.getSimpleName(), "Previous activity: " + activity);
            if(activity.equals("ConnectedActivity"))
            {
                Log.d(MainActivity.class.getSimpleName(), "Set bt1");
                socketTask = new SocketTask(MainActivity.this);
                socketTask.execute();
            }
        }
    }

    // The function check if the username and the password are valid and send login request
    // to the server.
    public void onLogin(View view) throws IOException
    {
        username = editTextUsername.getText().toString();
        password = editTextPassword.getText().toString();
        if (username.contains(",") || password.contains(","))
        {
            if(username.contains(","))
            {
                editTextUsername.setText("");
                AlertDialog.Builder builder = Utils.CreateDialog("Invalid Username", "You should not enter username" +
                        " with the letter: ,", MainActivity.this);
                builder.show();
            }
            if (password.contains(",")){
                editTextPassword.setText("");
                AlertDialog.Builder builder = Utils.CreateDialog("Invalid Password", "You should not enter password" +
                        " with the letter: ,", MainActivity.this);
                builder.show();
            }
        }
        else {
            socketTask.send("Login," + username + "," + password);
        }
    }

    // The function start the SignUpActivity activity.
    public void onSignUp(View view)
    {
        username = editTextUsername.getText().toString();
        Intent intent = new Intent(this, SignUpActivity.class);
        intent.putExtra(USERNAME_KEY, username);
        startActivity(intent);
    }

    // The function start the ForgotPasswordActivity activity.
    public void onForgotPassword(View view)
    {
        username = editTextUsername.getText().toString();
        Intent intent = new Intent(this, ForgotPasswordActivity.class);
        intent.putExtra(USERNAME_KEY, username);
        startActivity(intent);
    }

    // The function delete the text from the editTextUsername, when the cross x button is pressed.
    public void resetUsernameText(View view) {editTextUsername.setText("");}

    // The function delete the text from the editTextPassword, when the cross x button is pressed.
    public void resetPasswordText(View view) {editTextPassword.setText("");}

    //The function override the original function, in order for when the user click on the back
    // button, nothing will happen.
    @Override
    public void onBackPressed() {}

    // The function return the current username and password.
    public static String getUsername()
    {
        return username;
    }
}


