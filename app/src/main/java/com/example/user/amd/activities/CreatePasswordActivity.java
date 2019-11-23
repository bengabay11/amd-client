package com.example.user.amd.activities;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.amd.Config;
import com.example.user.amd.Utils;
import com.example.user.amd.R;
import com.example.user.amd.handlers.ButtonVisibilityHandler;
import com.example.user.amd.tasks.SocketTask;
import com.example.user.amd.watchers.EmptyTextWatcher;

import java.util.Arrays;
import java.util.List;


public class CreatePasswordActivity extends AppCompatActivity {
    SocketTask socketTask;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private Button createPasswordButton;
    private Button crossXButtonConfirmPassword;
    private Button crossXButtonPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        Intent intent = getIntent();
        socketTask = (SocketTask) intent.getSerializableExtra(Config.SOCKET_TASk_KEY);
        editTextPassword = (EditText) findViewById(R.id.new_password);
        editTextConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        createPasswordButton = (Button) findViewById(R.id.change_password_button);
        crossXButtonPassword = (Button) findViewById(R.id.cross_x_button_new_password);
        crossXButtonConfirmPassword = (Button) findViewById(R.id.cross_x_button_confirm_password);

        initUI();
    }

    public void initUI()
    {
        // TODO: check if there is a way to do it in the xml file
        createPasswordButton.setTextColor(Color.parseColor("#808080"));
        createPasswordButton.setEnabled(false);

        List<EditText> editTexts = Arrays.asList(editTextPassword, editTextConfirmPassword);
        TextWatcher emptyTextWatcher = new EmptyTextWatcher(editTexts, createPasswordButton);
        editTextPassword.addTextChangedListener(emptyTextWatcher);
        editTextConfirmPassword.addTextChangedListener(emptyTextWatcher);

        ButtonVisibilityHandler passwordButtonVisibilityHandler = new ButtonVisibilityHandler(crossXButtonPassword);
        ButtonVisibilityHandler confirmPasswordButtonVisibilityHandler = new ButtonVisibilityHandler(crossXButtonConfirmPassword);
        editTextPassword.setOnFocusChangeListener(passwordButtonVisibilityHandler::handleFocus);
        editTextConfirmPassword.setOnFocusChangeListener(confirmPasswordButtonVisibilityHandler::handleFocus);
    }

    @Override
    public void onBackPressed() {}

    public void onChangePassword(View view){
        String newPassword = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        if(!newPassword.equals(confirmPassword))
        {
            editTextConfirmPassword.setText("");
            Utils.CreateDialog("Invalid Password Confirmation", "You" +
                    " have confirmed the password incorrectly. please try again.", CreatePasswordActivity.this);
        }
        else
        {
            socketTask.send("ChangeTemporaryPassword," + newPassword);
        }
    }

    public void resetPasswordText(View view) {
        editTextPassword.setText("");
    }

    public void resetConfirmPasswordText(View view) {
        editTextConfirmPassword.setText("");
    }
}