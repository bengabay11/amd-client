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


public class SignUpActivity extends AppCompatActivity {

    private SocketTask socketTask;
    private static String username;
    private EditText editTextUsername;
    private EditText editTextPassword;
    private EditText editTextConfirmPassword;
    private EditText editTextEmail;
    private Button signUpButton;
    private Button crossXButtonUsername;
    private Button crossXButtonPassword;
    private Button crossXButtonConfirmPassword;
    private Button crossXButtonEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();
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
        signUpButton.setEnabled(false);
        signUpButton.setTextColor(Color.parseColor("#808080"));

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

    @Override
    public void onBackPressed() {}

    public static String getUsername() {
        return username;
    }

    public void OnSignUp(View view)
    {
        username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        String email = editTextEmail.getText().toString();
        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setText("");
            AlertDialog.Builder builder = Utils.CreateDialog("Invalid Password Confirmation", "You have " +
                    "confirmed the password incorrectly. please try again.", SignUpActivity.this);
            builder.show();
        }
        else
        {
            this.socketTask = Utils.runSocketTask(this);
            this.socketTask.send("SignUpActivity," + username + "," + password + "," + email);
        }
    }

    public void resetUsernameText(View view) {
        editTextUsername.setText("");
    }

    public void resetPasswordText(View view) {
        editTextPassword.setText("");
    }

    public void resetConfirmPasswordText(View view) {
        editTextConfirmPassword.setText("");
    }

    public void resetEmailText(View view) {
        editTextEmail.setText("");
    }

    public void backLogin(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("activity","SignUpActivity");
        startActivity(intent);
    }
}
