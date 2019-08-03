package com.example.user.amd.handlers;

import android.view.View;
import android.widget.Button;

import com.example.user.amd.interfaces.ItemFocusHandler;


public class ButtonVisibilityHandler implements ItemFocusHandler {
    private Button button;

    public ButtonVisibilityHandler(Button button) {
        this.button = button;
    }

    public void handleFocus(View view, boolean hasFocus) {
        if (!hasFocus)
            this.button.setVisibility(View.GONE);
        else
            this.button.setVisibility(View.VISIBLE);
    }
}
