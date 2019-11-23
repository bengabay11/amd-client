package com.example.user.amd.activities;

import android.content.res.Resources;
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


public class SignUpActivity extends AppCompatActivity {
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextEmail;
    private Button signUpButton;
    private Button crossXButtonUsername;
    private Button crossXButtonPassword;
    private Button crossXButtonConfirmPassword;
    private Button crossXButtonEmail;

    private static String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        editTextUsername = (EditText)findViewById(R.id.edit_text_username_sign_up);
        editTextPassword = (EditText)findViewById(R.id.edit_text_password_sign_up);
        editTextConfirmPassword = (EditText)findViewById(R.id.edit_text_confirm_password_sign_up);
        editTextEmail = (EditText)findViewById(R.id.edit_text_email);
        signUpButton = (Button)findViewById((R.id.sign_up_button));
        crossXButtonUsername = (Button)findViewById((R.id.cross_x_button1));
        crossXButtonPassword = (Button)findViewById((R.id.cross_x_button2));
        crossXButtonConfirmPassword = (Button)findViewById((R.id.cross_x_button3));
        crossXButtonEmail = (Button)findViewById((R.id.cross_x_button4));
        initUI();
    }

    private void initUI()
    {
        Utils.disableButton(signUpButton);
        List<EditText> editTexts = Arrays.asList(editTextUsername, editTextPassword,
                editTextConfirmPassword, editTextEmail);
        TextWatcher emptyTextWatcher = new EmptyTextWatcher(editTexts, signUpButton);
        editTextUsername.addTextChangedListener(emptyTextWatcher);
        editTextPassword.addTextChangedListener(emptyTextWatcher);
        editTextConfirmPassword.addTextChangedListener(emptyTextWatcher);
        editTextEmail.addTextChangedListener(emptyTextWatcher);

        ButtonVisibilityHandler buttonVisibilityHandlerUsername = new ButtonVisibilityHandler(crossXButtonUsername);
        ButtonVisibilityHandler buttonVisibilityHandlerPassword = new ButtonVisibilityHandler(crossXButtonPassword);
        ButtonVisibilityHandler buttonVisibilityHandlerConfirmPassword = new ButtonVisibilityHandler(crossXButtonConfirmPassword);
        ButtonVisibilityHandler buttonVisibilityHandlerEmail = new ButtonVisibilityHandler(crossXButtonEmail);

        editTextUsername.setOnFocusChangeListener(buttonVisibilityHandlerUsername::handleFocus);
        editTextPassword.setOnFocusChangeListener(buttonVisibilityHandlerPassword::handleFocus);
        editTextConfirmPassword.setOnFocusChangeListener(buttonVisibilityHandlerConfirmPassword::handleFocus);
        editTextEmail.setOnFocusChangeListener(buttonVisibilityHandlerEmail::handleFocus);
    }

    public static String getUsername()
    {
        return username;
    }

    public void OnSignUp(View view)
    {
        username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        String email = editTextEmail.getText().toString();
        if (password.equals(confirmPassword)) {
            SocketTask socketTask = Utils.runSocketTask(this);
            String activityName = this.getClass().getSimpleName();
            socketTask.send(activityName + "," + username + "," + password + "," + email);
        }
        else
        {
            editTextConfirmPassword.setText(Config.EMPTY_STRING);
            Resources resource = SignUpActivity.this.getResources();
            Utils.showAlertDialog(
                    resource.getString(R.string.invalid_password_confirmation_title),
                    resource.getString(R.string.invalid_password_confirmation_body),
                    SignUpActivity.this
            );
        }
    }

    public void resetUsernameText(View view)
    {
        editTextUsername.setText(Config.EMPTY_STRING);
    }

    public void resetPasswordText(View view)
    {
        editTextPassword.setText(Config.EMPTY_STRING);
    }

    public void resetConfirmPasswordText(View view)
    {
        editTextConfirmPassword.setText(Config.EMPTY_STRING);
    }

    public void resetEmailText(View view)
    {
        editTextEmail.setText(Config.EMPTY_STRING);
    }

    public void backLogin(View view)
    {
        this.onBackPressed();
    }
}
