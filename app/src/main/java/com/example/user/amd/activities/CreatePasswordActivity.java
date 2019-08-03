package com.example.user.amd.activities;

import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.amd.Utils;
import com.example.user.amd.R;
import com.example.user.amd.tasks.SocketTask;


// Activity of Create New Password after reset the password from the email.
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

        socketTask = MainActivity.socketTask;
        editTextPassword = (EditText) findViewById(R.id.new_password);
        editTextConfirmPassword = (EditText) findViewById(R.id.confirm_password);
        createPasswordButton = (Button) findViewById(R.id.change_password_button);
        crossXButtonPassword = (Button) findViewById(R.id.cross_x_button_new_password);
        crossXButtonConfirmPassword = (Button) findViewById(R.id.cross_x_button_confirm_password);

        handleUI();
    }

    @Override
    public void onBackPressed() {}

    public void onChangePassword(View view){
        String newPassword = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        if(!newPassword.equals(confirmPassword) || newPassword.contains(","))
        {
            if (newPassword.contains(",")){
                editTextPassword.setText("");
                AlertDialog.Builder builder = Utils.CreateDialog("Invalid Password", "You should not enter the" +
                        " password with the letter: ,", CreatePasswordActivity.this);
                builder.show();
            }
            if(!newPassword.equals(confirmPassword))
            {
                editTextConfirmPassword.setText("");
                AlertDialog.Builder builder = Utils.CreateDialog("Invalid Password Confirmation", "You" +
                        " have confirmed the password incorrectly. please try again.", CreatePasswordActivity.this);
                builder.show();
            }
        }
        else
        {
            socketTask.send("ChangeTemporaryPassword," + newPassword);
        }
    }

    public void resetPasswordText(View view)
    {
        editTextPassword.setText("");
    }

    public void resetConfirmPasswordText(View view) {
        editTextConfirmPassword.setText("");
    }

    public void handleUI()
    {
        int[] screenSize = Utils.getScreenSize(CreatePasswordActivity.this);
        int width = screenSize[0], height=screenSize[1];
        if(width == 1440 && height == 2560) {
            createPasswordButton.setTextSize(20);

            // handle buttons availability
            createPasswordButton.setTextColor(Color.parseColor("#808080"));
            createPasswordButton.setEnabled(false);
            editTextPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String newPassword = s.toString();
                    String confirmPassword = editTextConfirmPassword.getText().toString();
                    if (newPassword.equals("") || confirmPassword.equals(""))
                    {
                        createPasswordButton.setEnabled(false);
                        createPasswordButton.setTextColor(Color.parseColor("#808080"));
                    } else {
                        createPasswordButton.setEnabled(true);
                        createPasswordButton.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            editTextConfirmPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String newPassword = editTextPassword.getText().toString();
                    String confirmPassword = s.toString();
                    if (newPassword.equals("") || confirmPassword.equals(""))
                    {
                        createPasswordButton.setEnabled(false);
                        createPasswordButton.setTextColor(Color.parseColor("#808080"));
                    } else {
                        createPasswordButton.setEnabled(true);
                        createPasswordButton.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            // handle the cross x buttons availability
            editTextPassword.setOnFocusChangeListener(new View.OnFocusChangeListener()
            {
                @Override
                public void onFocusChange(View v, boolean hasFocus)
                {
                    if (!hasFocus)
                        crossXButtonPassword.setVisibility(View.GONE);
                    else
                        crossXButtonPassword.setVisibility(View.VISIBLE);
                }
            });
            editTextConfirmPassword.setOnFocusChangeListener(new View.OnFocusChangeListener()
            {
                @Override
                public void onFocusChange(View v, boolean hasFocus)
                {
                    if (!hasFocus)
                        crossXButtonConfirmPassword.setVisibility(View.GONE);
                    else
                        crossXButtonConfirmPassword.setVisibility(View.VISIBLE);
                }
            });
        }
    }
}