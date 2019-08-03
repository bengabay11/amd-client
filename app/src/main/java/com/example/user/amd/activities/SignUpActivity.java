package com.example.user.amd.activities;

import android.content.Intent;
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


// Activity of Sign Up page.
public class SignUpActivity extends AppCompatActivity {

    private SocketTask socketTask;
    private static String username;
    private EditText editText1;
    private EditText editText2;
    private EditText editText3;
    private EditText editText4;
    private Button signUpButton;
    private Button crossXButtonUsername;
    private Button crossXButtonPassword;
    private Button crossXButtonConfirmPassword;
    private Button crossXButtonEmail;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        socketTask = MainActivity.socketTask;
        socketTask.setBuilder(SignUpActivity.this);
        editText1 = (EditText)findViewById(R.id.edit_text_username_sign_up);
        editText2 = (EditText)findViewById(R.id.edit_text_password_sign_up);
        editText3 = (EditText)findViewById(R.id.edit_text_confirm_password_sign_up);
        editText4 = (EditText)findViewById(R.id.edit_text_email);
        signUpButton = (Button)findViewById((R.id.sign_up_button));
        backButton = (Button)findViewById((R.id.back_button));
        crossXButtonUsername = (Button)findViewById((R.id.cross_x_button1));
        crossXButtonPassword = (Button)findViewById((R.id.cross_x_button2));
        crossXButtonConfirmPassword = (Button)findViewById((R.id.cross_x_button3));
        crossXButtonEmail = (Button)findViewById((R.id.cross_x_button4));


        signUpButton.setEnabled(false);
        signUpButton.setTextColor(Color.parseColor("#808080"));
        handleUI();
    }

    @Override
    public void onBackPressed() {}

    // The function return the current username and password.
    public static String getUsername() {
        return username;
    }

    // The function checks if the username, password, confirm password and email are valid and send
    // a sign up request to te server.
    public void OnSignUp(View view)
    {
        username = editText1.getText().toString();
        String password = editText2.getText().toString();
        String confirmPassword = editText3.getText().toString();
        String email = editText4.getText().toString();

        if (!password.equals(confirmPassword) || username.contains(",") || password.contains(","))
        {
            if (!password.equals(confirmPassword)) {
                editText3.setText("");
                AlertDialog.Builder builder = Utils.onCreateDialog("Invalid Password Confirmation", "You have " +
                        "confirmed the password incorrectly. please try again.", SignUpActivity.this);
                builder.show();
            }
            if(username.contains(","))
            {
                editText1.setText("");
                AlertDialog.Builder builder = Utils.onCreateDialog("Invalid Username", "You should not enter the " +
                        " username with the letter: ,", SignUpActivity.this);
                builder.show();
            }
            if (password.contains(",")){
                editText2.setText("");
                AlertDialog.Builder builder = Utils.onCreateDialog("Invalid Password", "You should not enter the" +
                        " password with the letter: ,", SignUpActivity.this);
                builder.show();
            }
        }
        else
        {
            socketTask.send("SignUpActivity," + username + "," + password + "," + email);
        }
    }

    public void resetUsernameText(View view) {editText1.setText("");}

    // The function delete the text from the editTextPassword, when the cross x button is pressed.
    public void resetPasswordText(View view) {editText2.setText("");}

    // The function delete the text from the editTextConfirmPassword, when the cross x button is pressed.
    public void resetConfirmPasswordText(View view) {editText3.setText("");}

    // The function delete the text from the editTextEmail, when the cross x button is pressed.
    public void resetEmailText(View view) {editText4.setText("");}

    // The function starts the MainActivity.
    public void backLogin(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("activity","SignUpActivity");
        startActivity(intent);
    }

    // The function handle the UI according to the screen size.
    // The function also take care of the buttons visibility in the UI.
    private void handleUI()
    {
        int[] screenSize = Utils.getScreenSize(SignUpActivity.this);
        int width = screenSize[0], height=screenSize[1];
        if(width == 1440 && height == 2560)
        {
            editText1.setTextSize(28);
            editText2.setTextSize(28);
            editText3.setTextSize(28);
            editText4.setTextSize(28);
            signUpButton.setTextSize(20);
            backButton.setTextSize(15);
        }

        editText1.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String password = editText2.getText().toString();
                String username = s.toString();
                String confirm_password = editText3.getText().toString();
                String email = editText4.getText().toString();
                if (username.equals("") || password.equals("") || confirm_password.equals("") ||
                        email.equals(""))
                {
                    signUpButton.setEnabled(false);
                    signUpButton.setTextColor(Color.parseColor("#808080"));
                }
                else
                {
                    signUpButton.setEnabled(true);
                    signUpButton.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editText2.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String username = editText1.getText().toString();
                String confirm_password = editText3.getText().toString();
                String email = editText4.getText().toString();
                String password = s.toString();
                if (username.equals("") || password.equals("") || confirm_password.equals("")
                        || email.equals(""))
                {
                    signUpButton.setEnabled(false);
                    signUpButton.setTextColor(Color.parseColor("#808080"));
                }
                else
                {
                    signUpButton.setEnabled(true);
                    signUpButton.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editText3.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String username = editText1.getText().toString();
                String password = editText2.getText().toString();
                String confirm_password = s.toString();
                String email = editText4.getText().toString();
                if (username.equals("") || password.equals("") || confirm_password.equals("")
                        || email.equals(""))
                {
                    signUpButton.setEnabled(false);
                    signUpButton.setTextColor(Color.parseColor("#808080"));
                }
                else
                {
                    signUpButton.setEnabled(true);
                    signUpButton.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        editText4.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String username = editText1.getText().toString();
                String password = editText2.getText().toString();
                String email = s.toString();
                String confirm_password = editText3.getText().toString();
                if (username.equals("") || password.equals("") || confirm_password.equals("")
                        || email.equals(""))
                {
                    signUpButton.setEnabled(false);
                    signUpButton.setTextColor(Color.parseColor("#808080"));
                }
                else
                {
                    signUpButton.setEnabled(true);
                    signUpButton.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
        /* hide the cross x button of the username if the focus is not on the username field */
        editText1.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                    crossXButtonUsername.setVisibility(View.GONE);
                else
                    crossXButtonUsername.setVisibility(View.VISIBLE);
            }
        });
        /* hide the cross x button of the password if the focus is not on the password field */
        editText2.setOnFocusChangeListener(new View.OnFocusChangeListener()
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
        /* hide the cross x button of the confirm password if the focus is not on the confirm password field */
        editText3.setOnFocusChangeListener(new View.OnFocusChangeListener()
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
        /* hide the cross x button of the email if the focus is not on the email field */
        editText4.setOnFocusChangeListener(new View.OnFocusChangeListener()
        {
            @Override
            public void onFocusChange(View v, boolean hasFocus)
            {
                if (!hasFocus)
                    crossXButtonEmail.setVisibility(View.GONE);
                else
                    crossXButtonEmail.setVisibility(View.VISIBLE);
            }
        });
    }
}
