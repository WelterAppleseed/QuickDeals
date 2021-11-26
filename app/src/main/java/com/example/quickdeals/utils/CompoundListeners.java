package com.example.quickdeals.utils;

import android.net.Uri;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.navigation.Navigation;

import com.example.quickdeals.R;

public class CompoundListeners {
    public CompoundListeners() {
    }

    public void setClickTimeListener(final EditText field, @Nullable final EditText nextField) {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Utils.onFirstFieldChecking(s, field, nextField, this);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        field.addTextChangedListener(textWatcher);
    }

}
