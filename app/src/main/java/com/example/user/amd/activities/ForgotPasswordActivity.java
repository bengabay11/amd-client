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


// Activity of Forgot Password page.
public class ForgotPasswordActivity extends AppCompatActivity
{
    SocketTask socketTask;
    private EditText editText1;
    private Button crossXButtonUsername;
    private Button b1;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


        socketTask = MainActivity.socketTask;
        socketTask.setBuilder(ForgotPasswordActivity.this);
        Intent intent = getIntent();
        String username = intent.getStringExtra(MainActivity.USERNAME_KEY);
        editText1 = (EditText)findViewById(R.id.edit_text_username);
        editText1.setText(username);
        crossXButtonUsername = (Button)findViewById(R.id.cross_x_button1);
        b1 = (Button)findViewById((R.id.lets_get_your_password_button));
        backButton = (Button)findViewById((R.id.back_button));

        if (username.equals(""))
        {
            b1.setEnabled(false);
            b1.setTextColor(Color.parseColor("#808080"));
        }

        handleUI();
    }

    // The function delete the text from the editText1, when the cross x button is pressed.
    public void resetUsernameText(View view) {editText1.setText("");}

    // The function starts the MainActivity.
    public void backLogin(View view)
    {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("activity","ForgotPasswordActivity");
        startActivity(intent);
    }

    // The function checks if the text is valid and send a forgot password request to the server.
    public void OnForgotPassword(View view)
    {
        String usernameOrEmail = editText1.getText().toString();
        if(usernameOrEmail.contains(",")){
            AlertDialog.Builder dialog = Utils.onCreateDialog("Invalid Username or Email", "You should not enter the " +
                    " username or the email with the letter: ,", ForgotPasswordActivity.this);
            dialog.show();
        }
        socketTask.send("ForgotPasswordActivity," + usernameOrEmail);
    }

    // The function handle the UI according to the screen size.
    // The function also take care of the buttons visibility in the UI.
    private void handleUI()
    {
        int[] screenSize = Utils.getScreenSize(ForgotPasswordActivity.this);
        int width = screenSize[0], height=screenSize[1];
        if(width == 1440 && height == 2560)
        {
            editText1.setTextSize(28);
            backButton.setTextSize(15);
        }

        editText1.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                String username = s.toString();
                if (username.equals(""))
                {
                    b1.setEnabled(false);
                    b1.setTextColor(Color.parseColor("#808080"));
                }
                else
                {
                    b1.setEnabled(true);
                    b1.setTextColor(Color.parseColor("#FFFFFF"));
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // hide the cross x button of the username if the focus is not on the username field
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
    }
}
