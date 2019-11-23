package com.example.user.amd.watchers;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.example.user.amd.Utils;

import java.util.List;


public class EmptyTextWatcher implements TextWatcher {
    private List<EditText> editTexts;
    private Button button;

    public EmptyTextWatcher(List<EditText> editTexts, Button button) {
        this.editTexts = editTexts;
        this.button = button;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        for (EditText editText: editTexts) {
            String editTextValue = editText.getText().toString();
            if (editTextValue.isEmpty())
            {
                Utils.disableButton(button);
            }
            else {
                Utils.enableButton(button);
            }
        }
    }

    @Override
    public void afterTextChanged(Editable s) {}
}
