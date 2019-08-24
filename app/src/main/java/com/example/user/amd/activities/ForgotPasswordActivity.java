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

import java.util.Collections;
import java.util.List;


public class ForgotPasswordActivity extends AppCompatActivity
{
    SocketTask socketTask;
    private EditText editTextUsername;
    private Button crossXButtonUsername;
    private Button findPasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Intent intent = getIntent();
        String username = intent.getStringExtra(Config.USERNAME_KEY);

        editTextUsername = (EditText)findViewById(R.id.edit_text_username);
        editTextUsername.setText(username);
        crossXButtonUsername = (Button)findViewById(R.id.cross_x_button1);
        findPasswordButton = (Button)findViewById((R.id.lets_get_your_password_button));

        initUI();
    }

    private void initUI()
    {
        // TODO: check if there is a way to do it in the xml file
        if (editTextUsername.getText().toString().isEmpty())
        {
            findPasswordButton.setEnabled(false);
            findPasswordButton.setTextColor(Color.parseColor("#808080"));
        }

        List<EditText> editTexts = Collections.singletonList(editTextUsername);
        TextWatcher emptyTextWatcher = new EmptyTextWatcher(editTexts, findPasswordButton);
        editTextUsername.addTextChangedListener(emptyTextWatcher);

        ButtonVisibilityHandler buttonVisibilityHandler = new ButtonVisibilityHandler(crossXButtonUsername);
        editTextUsername.setOnFocusChangeListener(buttonVisibilityHandler::handleFocus);
    }

    public void resetUsernameText(View view) {
        editTextUsername.setText("");
    }

    public void backLogin(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("activity","ForgotPasswordActivity");
        startActivity(intent);
    }

    public void OnForgotPassword(View view)
    {
        this.socketTask = Utils.runSocketTask(this);
        String usernameOrEmail = editTextUsername.getText().toString();
        this.socketTask.send("ForgotPasswordActivity," + usernameOrEmail);
    }
}
