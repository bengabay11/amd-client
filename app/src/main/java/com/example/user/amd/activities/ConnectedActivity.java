package com.example.user.amd.activities;

import android.content.Context;
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
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;
import com.example.user.amd.Config;
import com.example.user.amd.Utils;
import com.example.user.amd.R;
import com.example.user.amd.handlers.ButtonVisibilityHandler;
import com.example.user.amd.scanners.Processes;
import com.example.user.amd.scanners.Smishing;
import com.example.user.amd.scanners.UnknownSources;
import com.example.user.amd.scanners.OsVersion;
import com.example.user.amd.tasks.SocketTask;
import com.example.user.amd.watchers.EmptyTextWatcher;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


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
    private Button crossXButtonNewPassword;
    private Button crossXButtonConfirmPassword;
    private Button crossXButtonEmail;
    private static String newUsername;
    private Smishing sm1;
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
        username = intent.getStringExtra(Config.USERNAME_KEY);
        socketTask = (SocketTask) intent.getSerializableExtra(Config.SOCKET_TASk_KEY);

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
        crossXButtonNewPassword = (Button) findViewById(R.id.cross_x_button_new_password);
        crossXButtonConfirmPassword = (Button) findViewById(R.id.cross_x_button_confirm_password);
        crossXButtonEmail = (Button) findViewById(R.id.cross_x_button_new_email);
        notificationsSwitch = (Switch) findViewById(R.id.notifications_switch);
        notificationsTextView = (TextView) findViewById(R.id.get_notifications_text_view);
        helpScrollView = (ScrollView)  findViewById(R.id.help_scroll_view);
        helpText = (TextView) findViewById(R.id.help_text);

        initUI();
        startDetects();
    }

    private void initBottomNavigation() {
        // Create bottom bar.
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        AHBottomNavigationItem homePage = new AHBottomNavigationItem("Home Page",
                R.drawable.home);
        AHBottomNavigationItem notificationsPage = new AHBottomNavigationItem("Notifications",
                R.drawable.bell);
        AHBottomNavigationItem settingsPage = new AHBottomNavigationItem("Settings",
                R.drawable.settings);

        bottomNavigation.addItem(homePage);
        bottomNavigation.addItem(notificationsPage);
        bottomNavigation.addItem(settingsPage);
        bottomNavigation.setTitleState(AHBottomNavigation.TitleState.SHOW_WHEN_ACTIVE);
        bottomNavigation.setAccentColor(Color.WHITE);
        bottomNavigation.setInactiveColor(Color.BLACK);
        bottomNavigation.setDefaultBackgroundColor(Color.parseColor("#1A237E")); // BLUE

        // TODO: create activity for each tab
        bottomNavigation.setOnTabSelectedListener((position, wasSelected) -> {
            if (position == 0)
            {
                showHomePageTab();
            }
            if (position == 1)
            {
                showNotificationsTab();
            }
            if (position == 2)
            {
                showNotificationsTab();
            }
            return true;
        });
    }

    private void showHomePageTab() {
        textViewTitle.setText("Hello " + username + "!");
        textViewWelcomeUser.setText("As long as you're connected, the AMD server detects attacks on your phone.");

        List<View> visibleViews = Arrays.asList(textViewTitle, textViewWelcomeUser, image1);
        List<View> hiddenViews = Arrays.asList(notifications, clearNotificationsButton,
                changeUsernameButton, helpButton, changePasswordButton, changeEmailButton,
                deleteUserButton, logOutButton, notificationsSwitch, notificationsTextView,
                notificationsTitle);
        showViews(visibleViews);
        hideViews(hiddenViews);
    }

    private void showNotificationsTab() {
        textViewTitle.setVisibility(View.GONE);
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
        notificationsTitle.setVisibility(View.VISIBLE);
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

    private void showSettingsTab() {
        textViewTitle.setText("Settings");
        textViewTitle.setVisibility(View.VISIBLE);
        notificationsTextView.setVisibility(View.VISIBLE);
        notificationsSwitch.setVisibility(View.VISIBLE);
        changeUsernameButton.setVisibility(View.VISIBLE);
        helpButton.setVisibility(View.VISIBLE);
        changePasswordButton.setVisibility(View.VISIBLE);
        changeEmailButton.setVisibility(View.VISIBLE);
        deleteUserButton.setVisibility(View.VISIBLE);
        logOutButton.setVisibility(View.VISIBLE);
        notificationsTitle.setVisibility(View.GONE);
        notifications.setVisibility(View.GONE);
        clearNotificationsButton.setVisibility(View.GONE);
        textViewWelcomeUser.setVisibility(View.GONE);
        image1.setVisibility(View.GONE);
    }

    private void initUI()
    {
        textViewTitle.setText("Hello " + username + "!");

        initBottomNavigation();

        notificationsSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (buttonView.isChecked() && notificationsCount > 0) {
                bottomNavigation.setNotification(Integer.toString(notificationsCount), 1);
            }
            else{
                bottomNavigation.setNotification("", 1);
            }
        });

        // TODO: check if it can be done from the xml file
        newUsernameButton.setEnabled(false);
        newUsernameButton.setTextColor(Color.parseColor("#808080"));
        newPasswordButton.setEnabled(false);
        newPasswordButton.setTextColor(Color.parseColor("#808080"));
        newEmailButton.setEnabled(false);
        newEmailButton.setTextColor(Color.parseColor("#808080"));

        List<EditText> newUsernameEditTexts = Collections.singletonList(editTextNewUsername);
        List<EditText> newPasswordEditTexts = Arrays.asList(editTextOldPassword,
                editTextNewPassword, editTextConfirmPassword);
        List<EditText> newEmailEditTexts = Collections.singletonList(editTextNewEmail);

        TextWatcher newUsernameEmptyTextWatcher =
                new EmptyTextWatcher(newUsernameEditTexts, newUsernameButton);
        TextWatcher newPasswordEmptyTextWatcher =
                new EmptyTextWatcher(newPasswordEditTexts, newPasswordButton);
        TextWatcher newEmailEmptyTextWatcher =
                new EmptyTextWatcher(newEmailEditTexts, newEmailButton);

        editTextNewUsername.addTextChangedListener(newUsernameEmptyTextWatcher);
        editTextOldPassword.addTextChangedListener(newPasswordEmptyTextWatcher);
        editTextNewPassword.addTextChangedListener(newPasswordEmptyTextWatcher);
        editTextConfirmPassword.addTextChangedListener(newPasswordEmptyTextWatcher);
        editTextNewEmail.addTextChangedListener(newEmailEmptyTextWatcher);

        ButtonVisibilityHandler crossXUsernameButtonHandler = new ButtonVisibilityHandler(crossXButtonUsername);
        ButtonVisibilityHandler crossXOldPasswordButtonHandler = new ButtonVisibilityHandler(crossXButtonOldPassword);
        ButtonVisibilityHandler crossXNewPasswordButtonHandler = new ButtonVisibilityHandler(crossXButtonNewPassword);
        ButtonVisibilityHandler crossXConfirmPasswordButtonHandler = new ButtonVisibilityHandler(crossXButtonConfirmPassword);
        ButtonVisibilityHandler crossXEmailButtonHandler = new ButtonVisibilityHandler(crossXButtonEmail);

        editTextNewUsername.setOnFocusChangeListener(crossXUsernameButtonHandler::handleFocus);
        editTextOldPassword.setOnFocusChangeListener(crossXOldPasswordButtonHandler::handleFocus);
        editTextNewPassword.setOnFocusChangeListener(crossXNewPasswordButtonHandler::handleFocus);
        editTextConfirmPassword.setOnFocusChangeListener(crossXConfirmPasswordButtonHandler::handleFocus);
        editTextNewEmail.setOnFocusChangeListener(crossXEmailButtonHandler::handleFocus);
    }

    private void hideViews(List<View> views) {
        for (View view: views) {
            view.setVisibility(View.GONE);
        }
    }

    private void showViews(List<View> views) {
        for (View view: views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    // The function starts the detects.
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void startDetects() {
        try{
            socketTask.setBuilder(ConnectedActivity.this);
            socketTask.setContextConnected(this);
        }
        catch (Exception ignored){}

        // OS version check
        OsVersion os1 = new OsVersion(socketTask);
        Thread osReportThread = new Thread(os1);
        osReportThread.start();

        // Check Processes
        Processes chp = new Processes(socketTask);
        Thread checkProcessesThread = new Thread(chp);
        checkProcessesThread.start();

        // Unknown Sources check
        UnknownSources un1 = new UnknownSources(this, socketTask);
        Thread UnknownSourcesThread = new Thread(un1);
//        if(socketTask != null)
//            socketTask.getUnknownSourcesClass(un1);
        UnknownSourcesThread.start();

        // Check suspicious sms
//        sm1 = new Smishing(socketTask, ConnectedActivity.this);
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

    public void onChangePasswordButton(View view)
    {
        String oldPassword = editTextOldPassword.getText().toString();
        String newPassword = editTextNewPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        if(!newPassword.equals(confirmPassword))
        {
            editTextConfirmPassword.setText("");
            AlertDialog.Builder builder = Utils.CreateDialog("Invalid Password Confirmation", "You" +
                    " have confirmed the password incorrectly. please try again.", ConnectedActivity.this);
            builder.show();
        }
        else
        {
            socketTask.send("ChangePassword," + oldPassword + "," + newPassword);
        }
    }

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

    public void onDeleteUser(View view)
    {
        AlertDialog.Builder deletePopUp = new AlertDialog.Builder(ConnectedActivity.this);
        deletePopUp.setTitle("Confirm Delete");
        deletePopUp.setMessage("Are you sure you want to delete your account?");
        deletePopUp.setPositiveButton("No", (dialog, id) -> {});
        deletePopUp.setNegativeButton("Yes", (dialog, id) -> socketTask.send("DeleteUser"));
        deletePopUp.show();
    }

    public void onLogOut(View view)
    {
        AlertDialog.Builder exitVerificationPopUp = new AlertDialog.Builder(ConnectedActivity.this);
        exitVerificationPopUp.setTitle("Confirm Exit");
        exitVerificationPopUp.setMessage("Are you sure you want to exit your account?");
        exitVerificationPopUp.setPositiveButton("No", (dialog, id) -> {});
        exitVerificationPopUp.setNegativeButton("Yes", (dialog, id) -> socketTask.send("LogOut"));
        exitVerificationPopUp.show();
    }

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
                                           @NonNull int[] grantResults) {
        Log.d(Smishing.class.getSimpleName(), "check permission...");
        if (requestCode == 0)
        {
            // Request for camera permission.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission has been granted. Start camera preview Activity.
                sm1.setPermission();
            }
        }
    }

    public void resetOldPasswordText(View view) { editTextOldPassword.setText(""); }

    public void resetNewEmailText(View view) { editTextNewEmail.setText(""); }

    public void resetConfirmPasswordText(View view) { editTextConfirmPassword.setText(""); }

    public void resetPasswordText(View view) { editTextNewPassword.setText(""); }

    public void resetUsernameText(View view) { editTextNewUsername.setText(""); }

    @Override
    public void onBackPressed() {}

    public void onClearNotifications(View view) {
        notifications.setText("");
        notificationsCount = 0;
        bottomNavigation.setNotification("", 1);
    }
}