package com.example.quickdeals.utils;

import android.content.res.Resources;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.example.quickdeals.R;
import com.example.quickdeals.utils.states.States;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.LocalDate;

public class Utils {

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    static void onFirstFieldChecking(CharSequence s, EditText currentField, EditText nextField, TextWatcher textWatcher) {
        int ffValue = Integer.parseInt(s.toString());
        if (ffValue > 2) {
            currentField.removeTextChangedListener(textWatcher);
            nextField.removeTextChangedListener(textWatcher);
            String time = "0" + ffValue;
            currentField.setText(time);
            currentField.addTextChangedListener(textWatcher);
            nextField.addTextChangedListener(textWatcher);
        } else {
            nextField.requestFocus();
        }
    }
    public static StringBuilder updateLabel(TextView label, CalendarDay date, String previousLabelValue) {
        int yearV = date.getYear();
        String month = States.getMonth(date.getMonth(), false);
        int dayV = date.getDay();
        int lastFullDateLength = previousLabelValue.length();
        String newLabel = yearV + ", " + month + " " + dayV + ", ";
        Log.i("UpdateLabel", "Updating previous label - " + previousLabelValue + " - with new label - " + newLabel + ".");
        return new StringBuilder(label.getText()).replace(0, lastFullDateLength, newLabel);

    }
}
