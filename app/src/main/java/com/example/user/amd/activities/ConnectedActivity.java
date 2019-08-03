package com.example.user.amd.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.camera2.CameraManager;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.user.amd.Utils;
import com.example.user.amd.R;
import com.example.user.amd.scanners.CheckProcesses;
import com.example.user.amd.scanners.SmishingDetect;
import com.example.user.amd.scanners.UnknownSources;
import com.example.user.amd.scanners.osReport;
import com.example.user.amd.tasks.SocketTask;


public class ConnectedActivity extends AppCompatActivity
{
    private static String username;
    private static SocketTask socketTask;
    private TextView textViewWelcomeUser;
    private TextView notificationsTitle;
    private static TextView textViewTitle;
    private Button changeUsernameButton;
    private Button changePasswordButton;
    private Button deleteUserButton;
    private Button logOutButton;
    private ImageView image1;
    private Button backButton;
    private Button newUsernameButton;
    private EditText editTextNewUsername;
    private Button newPasswordButton;
    private EditText editTextNewPassword;
    private EditText editTextConfirmPassword;
    private Button changeEmailButton;
    private Button newEmailButton;
    private EditText editTextNewEmail;
    private Button crossXButtonUsername;
    private Button crossXButtonPassword;
    private Button crossXButtonConfirmPassword;
    private Button crossXButtonEmail;
    private static String newUsername;
    private SmishingDetect sm1;
    private Switch notificationsSwitch;
    private TextView notificationsTextView;
    private AHBottomNavigation bottomNavigation;
    private TextView notifications;
    private Button clearNotificationsButton;
    private int notificationsCount;
    private EditText editTextOldPassword;
    private Button crossXButtonOldPassword;
    private ImageButton helpButton;
    private ScrollView helpScrollView;
    private TextView helpText;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connected);

        Intent intent = getIntent();
        username = intent.getStringExtra(SocketTask.USERNAME_KEY);
        socketTask = MainActivity.socketTask;

        textViewWelcomeUser = (TextView) findViewById(R.id.hello_user_text);
        textViewTitle = (TextView) findViewById(R.id.Title);
        notificationsTitle = (TextView) findViewById(R.id.notifications_title);
        notifications = (TextView) findViewById(R.id.notifications);
        clearNotificationsButton = (Button) findViewById(R.id.clear_notification_button);
        helpButton = (ImageButton) findViewById(R.id.help_button);
        changeUsernameButton = (Button) findViewById(R.id.change_username_button);
        changePasswordButton = (Button) findViewById(R.id.change_password_button);
        changeEmailButton = (Button) findViewById(R.id.change_email_button);
        deleteUserButton = (Button) findViewById(R.id.delete_user);
        logOutButton = (Button) findViewById(R.id.log_out_button);
        image1= (ImageView) findViewById(R.id.secure_image);
        backButton = (Button) findViewById(R.id.back_button);
        newUsernameButton = (Button) findViewById(R.id.change_button);
        editTextNewUsername = (EditText) findViewById(R.id.new_username);
        newPasswordButton = (Button)findViewById(R.id.change_password_button2);
        editTextOldPassword = (EditText)findViewById(R.id.old_password);
        editTextNewPassword = (EditText)findViewById(R.id.new_password);
        editTextConfirmPassword = (EditText)findViewById(R.id.confirm_password);
        editTextNewEmail = (EditText)findViewById(R.id.new_email);
        newEmailButton = (Button) findViewById(R.id.change_email_button2);
        crossXButtonUsername = (Button) findViewById(R.id.cross_x_button_new_username);
        crossXButtonOldPassword = (Button) findViewById(R.id.cross_x_button_old_password);
        crossXButtonPassword = (Button) findViewById(R.id.cross_x_button_new_password);
        crossXButtonConfirmPassword = (Button) findViewById(R.id.cross_x_button_confirm_password);
        crossXButtonEmail = (Button) findViewById(R.id.cross_x_button_new_email);
        notificationsSwitch = (Switch) findViewById(R.id.notifications_switch);
        notificationsTextView = (TextView) findViewById(R.id.get_notifications_text_view);
        helpScrollView = (ScrollView)  findViewById(R.id.help_scroll_view);
        helpText = (TextView) findViewById(R.id.help_text);

        textViewTitle.setText("Hello " + username + "!");
        // Create bottom bar.
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem item1 = new AHBottomNavigationItem("Home Page",
                R.drawable.home);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem("Notifications",
                R.drawable.bell);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem("Settings",
                R.drawable.settings);

        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setAccentColor(Color.WHITE);
        bottomNavigation.setInactiveColor(Color.BLACK);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#1A237E")); // BLUE

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener()
        {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected)
            {
                helpText.setVisibility(View.GONE);
                helpScrollView.setVisibility(View.GONE);
                backButton.setVisibility(View.GONE);
                newUsernameButton.setVisibility(View.GONE);
                editTextNewUsername.setVisibility(View.GONE);
                editTextOldPassword.setVisibility(View.GONE);
                crossXButtonOldPassword.setVisibility(View.GONE);
                newPasswordButton.setVisibility(View.GONE);
                editTextNewPassword.setVisibility(View.GONE);
                editTextConfirmPassword.setVisibility(View.GONE);
                editTextNewEmail.setVisibility(View.GONE);
                newEmailButton.setVisibility(View.GONE);
                if (position == 0)
                {
                    textViewTitle.setVisibility(View.VISIBLE);
                    textViewTitle.setText("Hello " + username + "!");
                    textViewWelcomeUser.setText("As long as you're connected, the AMD server" +
                            " detects attacks on your phone.");
                    textViewWelcomeUser.setVisibility(View.VISIBLE);
                    image1.setVisibility(View.VISIBLE);
                    notifications.setVisibility(View.GONE);
                    clearNotificationsButton.setVisibility(View.GONE);
                    changeUsernameButton.setVisibility(View.GONE);
                    helpButton.setVisibility(View.GONE);
                    changePasswordButton.setVisibility(View.GONE);
                    changeEmailButton.setVisibility(View.GONE);
                    deleteUserButton.setVisibility(View.GONE);
                    logOutButton.setVisibility(View.GONE);
                    notificationsSwitch.setVisibility(View.GONE);
                    notificationsTextView.setVisibility(View.GONE);
                    notificationsTitle.setVisibility(View.GONE);
                }
                if (position == 1)
                {
                    textViewTitle.setVisibility(View.GONE);
                    notificationsTitle.setVisibility(View.VISIBLE);
                    textViewWelcomeUser.setVisibility(View.GONE);
                    image1.setVisibility(View.GONE);
                    clearNotificationsButton.setVisibility(View.GONE);
                    notifications.setVisibility(View.GONE);
                    changeUsernameButton.setVisibility(View.GONE);
                    helpButton.setVisibility(View.GONE);
                    changePasswordButton.setVisibility(View.GONE);
                    changeEmailButton.setVisibility(View.GONE);
                    deleteUserButton.setVisibility(View.GONE);
                    logOutButton.setVisibility(View.GONE);
                    notificationsSwitch.setVisibility(View.GONE);
                    notificationsTextView.setVisibility(View.GONE);
                    bottomNavigation.setNotification("", 1);
                    if(notificationsSwitch.isChecked()){
                        notificationsCount = 0;
                        notifications.setVisibility((View.VISIBLE));
                        clearNotificationsButton.setVisibility(View.VISIBLE);
                    }
                    else {
                        textViewWelcomeUser.setVisibility(View.VISIBLE);
                        textViewWelcomeUser.setText("Receiving notifications is not approved" +
                                " in the settings.");
                    }
                }
                if (position == 2)
                {
                    textViewTitle.setVisibility(View.VISIBLE);
                    textViewTitle.setText("Settings");
                    notificationsTitle.setVisibility(View.GONE);
                    notificationsTextView.setVisibility(View.VISIBLE);
                    notificationsSwitch.setVisibility(View.VISIBLE);
                    changeUsernameButton.setVisibility(View.VISIBLE);
                    helpButton.setVisibility(View.VISIBLE);
                    changePasswordButton.setVisibility(View.VISIBLE);
                    changeEmailButton.setVisibility(View.VISIBLE);
                    deleteUserButton.setVisibility(View.VISIBLE);
                    logOutButton.setVisibility(View.VISIBLE);
                    notifications.setVisibility(View.GONE);
                    clearNotificationsButton.setVisibility(View.GONE);
                    textViewWelcomeUser.setVisibility(View.GONE);
                    image1.setVisibility(View.GONE);
                }
                return true;
            }
        });

        handleUI();
        try{
            socketTask.setBuilder(ConnectedActivity.this);
            socketTask.setContextConnected(this);
        }
        catch (Exception ignored){}
        try {
            startDetects();
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }
    }

    // The function starts the detects.
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startDetects() throws Settings.SettingNotFoundException
    {
        // OS version check
        osReport os1 = new osReport(socketTask);
        Thread osReportThread = new Thread(os1);
        osReportThread.start();

        // Check Processes
        CheckProcesses chp = new CheckProcesses(socketTask);
        Thread checkProcessesThread = new Thread(chp);
        checkProcessesThread.start();

        // Unknown Sources check
        UnknownSources un1 = new UnknownSources(this, socketTask);
        Thread UnknownSourcesThread = new Thread(un1);
        if(socketTask != null)
            socketTask.getUnknownSourcesClass(un1);
        UnknownSourcesThread.start();

        // Check suspicious sms
//        sm1 = new SmishingDetect(socketTask, ConnectedActivity.this);
//        Thread smishingDetectThread = new Thread(sm1);
//        smishingDetectThread.start();

        // Check if camera opened
        cameraOn();
    }

    // The function checks if the camera was opened.
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void cameraOn()
    {
        Handler mHandler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {}
        };

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            manager.registerAvailabilityCallback(new CameraManager.AvailabilityCallback() {
                @Override
                public void onCameraAvailable(@NonNull String cameraId) {
                    super.onCameraAvailable(cameraId);
                }

                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onCameraUnavailable(@NonNull String cameraId) {
                    super.onCameraUnavailable(cameraId);
                    AlertDialog.Builder builder = Utils.CreateDialog("Camera On", "You are not allowed to" +
                            " open the camera.", ConnectedActivity.this);
                    if(!builder.create().isShowing()){
                        builder.show();
                    }
                    String currentTime = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format
                            (Calendar.getInstance().getTime());
                    socketTask.send("CameraOn," + currentTime);
                    addNotification((View)null, "Camera opened at " + currentTime);
                }
            }, mHandler);
        }
    }

    // The function present the help page.
    public void onHelp(View view)
    {
        helpScrollView.setVisibility(View.VISIBLE);
        helpText.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
        textViewTitle.setText("Help");
        textViewTitle.setVisibility(View.VISIBLE);
        changeUsernameButton.setVisibility(View.GONE);
        helpButton.setVisibility(View.GONE);
        changePasswordButton.setVisibility(View.GONE);
        changeEmailButton.setVisibility(View.GONE);
        notificationsTextView.setVisibility(View.GONE);
        notificationsSwitch.setVisibility(View.GONE);
        deleteUserButton.setVisibility(View.GONE);
        logOutButton.setVisibility(View.GONE);
    }

    // The function present the change username page.
    public void onChangeUsername(View view)
    {
        newUsernameButton.setVisibility(View.VISIBLE);
        editTextNewUsername.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
        textViewTitle.setText("Change Username");
        changeUsernameButton.setVisibility(View.GONE);
        helpButton.setVisibility(View.GONE);
        changePasswordButton.setVisibility(View.GONE);
        changeEmailButton.setVisibility(View.GONE);
        notificationsTextView.setVisibility(View.GONE);
        notificationsSwitch.setVisibility(View.GONE);
        deleteUserButton.setVisibility(View.GONE);
        logOutButton.setVisibility(View.GONE);
    }

    // The function check if the username is valid and send it to the server.
    public void onChangeUsernameButton(View view)
    {
        newUsername = editTextNewUsername.getText().toString();
        if (newUsername.contains(",")){
            editTextNewUsername.setText("");
            AlertDialog.Builder builder = Utils.CreateDialog("Invalid Username", "You should not enter the" +
                    " username with the letter: ,", ConnectedActivity.this);
            builder.show();
        }
        else
        {
            socketTask.send("ChangeUsername," + newUsername);
        }
    }

    // The function update the new username.
    public static void usernameChanged()
    {
        username = newUsername;
        textViewTitle.setText("Hello " + username + "!");
    }

    // The function present the change password page.
    public void onChangePassword(View view)
    {
        editTextOldPassword.setVisibility(View.VISIBLE);
        newPasswordButton.setVisibility(View.VISIBLE);
        editTextNewPassword.setVisibility(View.VISIBLE);
        editTextConfirmPassword.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
        textViewTitle.setText("Change Password");
        changeUsernameButton.setVisibility(View.GONE);
        helpButton.setVisibility(View.GONE);
        changePasswordButton.setVisibility(View.GONE);
        changeEmailButton.setVisibility(View.GONE);
        notificationsTextView.setVisibility(View.GONE);
        notificationsSwitch.setVisibility(View.GONE);
        deleteUserButton.setVisibility(View.GONE);
        logOutButton.setVisibility(View.GONE);
    }

    // The function check if the password confirmed correctly and send it to the server.
    public void onChangePasswordButton(View view)
    {
        String oldPassword = editTextOldPassword.getText().toString();
        String newPassword = editTextNewPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        if(!newPassword.equals(confirmPassword) || newPassword.contains(",") || oldPassword.contains(","))
        {
            if (oldPassword.contains(",")){
                editTextOldPassword.setText("");
                AlertDialog.Builder builder = Utils.CreateDialog("Invalid Old Password", "You should not enter the" +
                        " password with the letter: ,", ConnectedActivity.this);
                builder.show();
            }
            if (newPassword.contains(",")){
                editTextNewPassword.setText("");
                AlertDialog.Builder builder = Utils.CreateDialog("Invalid Password", "You should not enter the" +
                        " password with the letter: ,", ConnectedActivity.this);
                builder.show();
            }
            if(!newPassword.equals(confirmPassword))
            {
                editTextConfirmPassword.setText("");
                AlertDialog.Builder builder = Utils.CreateDialog("Invalid Password Confirmation", "You" +
                        " have confirmed the password incorrectly. please try again.", ConnectedActivity.this);
                builder.show();
            }
        }
        else
        {
            socketTask.send("ChangePassword," + oldPassword + "," + newPassword);
        }
    }

    // The function present the change email page.
    public void onChangeEmail(View view)
    {
        newEmailButton.setVisibility(View.VISIBLE);
        editTextNewEmail.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.VISIBLE);
        textViewTitle.setText("Change Email");
        changeUsernameButton.setVisibility(View.GONE);
        helpButton.setVisibility(View.GONE);
        changePasswordButton.setVisibility(View.GONE);
        changeEmailButton.setVisibility(View.GONE);
        notificationsTextView.setVisibility(View.GONE);
        notificationsSwitch.setVisibility(View.GONE);
        deleteUserButton.setVisibility(View.GONE);
        logOutButton.setVisibility(View.GONE);
    }

    // The function check if the new email is valid and send it to the server.
    public void onChangeEmailButton(View view)
    {
        String newEmail = editTextNewEmail.getText().toString();
        if (newEmail.contains(",")){
            editTextNewEmail.setText("");
            AlertDialog.Builder builder = Utils.CreateDialog("Invalid Email", "You should not enter the" +
                    " email with the letter: ,", ConnectedActivity.this);
            builder.show();
        }
        else {
            socketTask.send("ChangeEmail," + newEmail);
        }
    }

    // The function shows a verify dialog to delete the user, before she send delete user
    // request to the server.
    public void onDeleteUser(View view)
    {
        AlertDialog.Builder deletePopUp = new AlertDialog.Builder(ConnectedActivity.this);
        deletePopUp.setTitle("Confirm Delete");
        deletePopUp.setMessage("Are you sure you want to delete your account?");
        deletePopUp.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {}});
        deletePopUp.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                socketTask.send("DeleteUser");
            }});
        deletePopUp.show();
    }

    // The function shows a verify dialog to logout, before she send logout request to the server.
    public void onLogOut(View view)
    {
        AlertDialog.Builder exitVerificationPopUp = new AlertDialog.Builder(ConnectedActivity.this);
        exitVerificationPopUp.setTitle("Confirm Exit");
        exitVerificationPopUp.setMessage("Are you sure you want to exit your account?");
        exitVerificationPopUp.setPositiveButton("No", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int id) {}});
        exitVerificationPopUp.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id)
            {
                socketTask.send("LogOut");
            }});
        exitVerificationPopUp.show();
    }

    // The function gets notification and present it in the notifications page.
    public void addNotification(View view, String notification)
    {
        Log.d(ConnectedActivity.class.getSimpleName(), "Get notification: " + notification);
        notificationsCount++;
        if (notificationsSwitch.isChecked())
            bottomNavigation.setNotification(Integer.toString(notificationsCount), 1);
        else
            bottomNavigation.setNotification("", 1);

        String input  = notifications.getText().toString();
        String[] lines = input.split("\\n");

        notifications.setText("* " + notification + "\n\n");
        for(String currentNotification : lines)
        {
            notifications.append(currentNotification + "\n");
        }
    }

    // The function shows the settings page.
    public void backSettings(View view)
    {
        editTextNewUsername.setText("");
        editTextNewEmail.setText("");
        editTextOldPassword.setText("");
        editTextNewPassword.setText("");
        editTextConfirmPassword.setText("");
        helpButton.setVisibility(View.VISIBLE);
        textViewTitle.setVisibility(View.VISIBLE);
        textViewTitle.setText("Settings");
        changeEmailButton.setVisibility(View.VISIBLE);
        changeUsernameButton.setVisibility(View.VISIBLE);
        changePasswordButton.setVisibility(View.VISIBLE);
        deleteUserButton.setVisibility(View.VISIBLE);
        logOutButton.setVisibility(View.VISIBLE);
        notificationsSwitch.setVisibility(View.VISIBLE);
        notificationsTextView.setVisibility(View.VISIBLE);
        backButton.setVisibility(View.GONE);
        newUsernameButton.setVisibility(View.GONE);
        editTextNewUsername.setVisibility(View.GONE);
        editTextOldPassword.setVisibility(View.GONE);
        crossXButtonOldPassword.setVisibility(View.GONE);
        newPasswordButton.setVisibility(View.GONE);
        editTextNewPassword.setVisibility(View.GONE);
        editTextConfirmPassword.setVisibility(View.GONE);
        editTextNewEmail.setVisibility(View.GONE);
        newEmailButton.setVisibility(View.GONE);
        helpText.setVisibility(View.GONE);
        helpScrollView.setVisibility(View.GONE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        Log.d(SmishingDetect.class.getSimpleName(), "check permission...");
        // BEGIN_INCLUDE(onRequestPermissionsResult)
        if (requestCode == 0)
        {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                sm1.setPermission();
            }
        }
    }

    // The function delete the text from the editTextOldPassword, when the cross x button is pressed.
    public void resetOldPasswordText(View view) {editTextOldPassword.setText("");}

    // The function delete the text from the editTextNewEmail, when the cross x button is pressed.
    public void resetNewEmailText(View view)
    {
        editTextNewEmail.setText("");
    }

    // The function delete the text from editTextConfirmPassword, when the cross x button is pressed.
    public void resetConfirmPasswordText(View view)
    {
        editTextConfirmPassword.setText("");
    }

    // The function delete the text from the editTextPassword, when the cross x button is pressed.
    public void resetPasswordText(View view)
    {
        editTextNewPassword.setText("");
    }

    // The function delete the text from the editTextNewUsername, when the cross x button is pressed.
    public void resetUsernameText(View view)
    {
        editTextNewUsername.setText("");
    }

    //The function override the original function, in order for when the user click on the back
    // button, nothing will happen.
    @Override
    public void onBackPressed() {}

    // The function clear the notifications.
    public void onClearNotifications(View view) {
        notifications.setText("");
        notificationsCount = 0;
        bottomNavigation.setNotification("", 1);
    }

    // The function handle the UI according to the screen size.
    // The function also take care of the buttons visibility in the UI, and other important things.
    private void handleUI()
    {
        int[] screenSize = Utils.getScreenSize(ConnectedActivity.this);
        int width = screenSize[0], height=screenSize[1];
        if(width == 1440 && height == 2560)
        {
            changeUsernameButton.setTextSize(20);
            changePasswordButton.setTextSize(20);
            changeEmailButton.setTextSize(20);
            deleteUserButton.setTextSize(20);
            logOutButton.setTextSize(20);
            backButton.setTextSize(15);

            // act according to notifications switch.
            notificationsSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (buttonView.isChecked() && notificationsCount > 0) {
                        bottomNavigation.setNotification(Integer.toString(notificationsCount), 1);
                    }
                    else{
                        bottomNavigation.setNotification("", 1);
                    }
                }});

            // handle buttons availability
            newUsernameButton.setEnabled(false);
            newUsernameButton.setTextColor(Color.parseColor("#808080"));
            newPasswordButton.setEnabled(false);
            newPasswordButton.setTextColor(Color.parseColor("#808080"));
            newEmailButton.setEnabled(false);
            newEmailButton.setTextColor(Color.parseColor("#808080"));

            // handle the cross x buttons
            editTextNewUsername.setOnFocusChangeListener(new View.OnFocusChangeListener()
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
            editTextOldPassword.setOnFocusChangeListener(new View.OnFocusChangeListener()
            {
                @Override
                public void onFocusChange(View v, boolean hasFocus)
                {
                    if (!hasFocus)
                        crossXButtonOldPassword.setVisibility(View.GONE);
                    else
                        crossXButtonOldPassword.setVisibility(View.VISIBLE);
                }
            });
            editTextNewPassword.setOnFocusChangeListener(new View.OnFocusChangeListener()
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
            editTextNewEmail.setOnFocusChangeListener(new View.OnFocusChangeListener()
            {
                @Override
                public void onFocusChange(View v, boolean hasFocus)
                {
                    if (!hasFocus)
                        crossXButtonEmail.setVisibility(View.GONE);
                    else
                        crossXButtonEmail.setVisibility(View.VISIBLE);
                }});
            // handle the buttons visibility according to the edit texts.
            editTextNewUsername.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String username = s.toString();
                    if (username.equals(""))
                    {
                        newUsernameButton.setEnabled(false);
                        newUsernameButton.setTextColor(Color.parseColor("#808080"));
                    } else {
                        newUsernameButton.setEnabled(true);
                        newUsernameButton.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            editTextNewEmail.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String email = s.toString();
                    if (email.equals("")) {
                        newEmailButton.setEnabled(false);
                        newEmailButton.setTextColor(Color.parseColor("#808080"));
                    } else {
                        newEmailButton.setEnabled(true);
                        newEmailButton.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            editTextOldPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String oldPassword = s.toString();
                    String password = editTextNewPassword.getText().toString();
                    String confirmPassword = editTextConfirmPassword.getText().toString();
                    if (password.equals("") || confirmPassword.equals("") ||
                            oldPassword.equals("")) {
                        newPasswordButton.setEnabled(false);
                        newPasswordButton.setTextColor(Color.parseColor("#808080"));
                    } else {
                        newPasswordButton.setEnabled(true);
                        newPasswordButton.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
            editTextNewPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }
                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    String password = s.toString();
                    String oldPassword = editTextOldPassword.getText().toString();
                    String confirmPassword = editTextConfirmPassword.getText().toString();
                    if (password.equals("") || confirmPassword.equals("") ||
                            oldPassword.equals("")) {
                        newPasswordButton.setEnabled(false);
                        newPasswordButton.setTextColor(Color.parseColor("#808080"));
                    } else {
                        newPasswordButton.setEnabled(true);
                        newPasswordButton.setTextColor(Color.parseColor("#FFFFFF"));
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
                    String password = editTextConfirmPassword.getText().toString();
                    String confirmPassword = s.toString();
                    String oldPassword = editTextOldPassword.getText().toString();
                    if (password.equals("") || confirmPassword.equals("") ||
                            oldPassword.equals("")) {
                        newPasswordButton.setEnabled(false);
                        newPasswordButton.setTextColor(Color.parseColor("#808080"));
                    } else {
                        newPasswordButton.setEnabled(true);
                        newPasswordButton.setTextColor(Color.parseColor("#FFFFFF"));
                    }
                }
                @Override
                public void afterTextChanged(Editable s) {
                }
            });
        }
    }
}