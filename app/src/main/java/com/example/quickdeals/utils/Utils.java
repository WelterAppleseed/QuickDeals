package com.example.quickdeals.utils;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Color;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewParent;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.widget.ActionBarOverlayLayout;
import androidx.navigation.NavOptions;

import com.example.quickdeals.R;
import com.example.quickdeals.utils.states.States;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.threeten.bp.LocalDate;

import java.lang.reflect.Field;
import java.util.Calendar;

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

    public static String getStartActivityText(Calendar c) {
        String text = "";
        int hours = c.get(Calendar.HOUR_OF_DAY);
        if (hours < 5 | hours > 21) {
            text = "Glad to see you at night!";
        } else if (hours > 5 & hours < 12) {
            text = "Good morning!";
        } else if (hours > 12 & hours < 17) {
            text = "Good afternoon!";
        } else {
            text = "Good evening!";
        }
        return text;
    }

    public static NavOptions getNavOptions() {
        return new NavOptions.Builder()
                .setEnterAnim(R.anim.enter)
                .setExitAnim(R.anim.exit)
                .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
                .build();
    }

    @SuppressLint("RestrictedApi")
    public static void getMenuItemsView(View actionBar) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        ViewParent parentOfHome = (ViewParent) actionBar;  //ActionBarView is parent of home ImageView, see layout file in sources
        Log.i("ActionBar", String.valueOf(actionBar.getClass()));
        ActionBarOverlayLayout overlayLayout = (ActionBarOverlayLayout) actionBar.getParent();
        if (!parentOfHome.getClass().getName().contains("ActionBarView")) {
            parentOfHome = parentOfHome.getParent();
            Class absAbv = parentOfHome.getClass().getSuperclass(); //ActionBarView -> AbsActionBarView class
            Log.i("ActionBar", String.valueOf(absAbv.getClass()));
            Field actionMenuPresenterField = absAbv.getDeclaredField("mActionMenuPresenter");
            actionMenuPresenterField.setAccessible(true);
            Object actionMenuPresenter = actionMenuPresenterField.get(parentOfHome);
            Field actionMenuViewField = actionMenuPresenter.getClass().getSuperclass().getDeclaredField("mMenuView");
            actionMenuViewField.setAccessible(true);
            LinearLayout actionMenuView = (LinearLayout) actionMenuViewField.get(actionMenuPresenter);
            Log.i("ActionBar", String.valueOf(actionMenuView.findViewById(R.id.action_edit)));
        }
    }
}
