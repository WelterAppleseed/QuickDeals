package com.example.quickdeals.utils;

import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.navigation.NavOptions;

import com.example.quickdeals.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.Calendar;

public class Utils {

    private static final int[] icons = {R.drawable.default_icon, R.drawable.sport_icon, R.drawable.fun_icon, R.drawable.work_icon, R.drawable.hobby_icon};
    private static final String[] months = {"Zero Month", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static final String[] week = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "fds"};
    private static final String[] waveColors = {"#1E3216", "#1E4216", "#26471E", "#2B6125", "#32712C", "#377C32", "#3E8E38", "#439F3E", "#48AF43", "#4FBD4A", "#54CF4E", "#5BDD56", "#5FEF59", "#67FB62" };

    public static StringBuilder updateLabel(TextView label, CalendarDay date, String previousLabelValue) {
        int yearV = date.getYear();
        String month = getMonth(date.getMonth(), false);
        int dayV = date.getDay();
        int lastFullDateLength = previousLabelValue.length();
        String newLabel = yearV + ", " + month + " " + dayV + ", ";
        Log.i("UpdateLabel", "Updating previous label - " + previousLabelValue + " - with new label - " + newLabel + ".");
        return new StringBuilder(label.getText()).replace(0, lastFullDateLength, newLabel);

    }

    public static int getIcon(int i) {
        return icons[i];
    }

    public static String getTime(@Nullable Integer year, int day, String month, int hours, int minutes, boolean fullForm) {
        if (fullForm) {
            return String.format("%d, %s %d, %02d:%02d", year, month, day, hours, minutes);
        } else {
            return String.format("%d %s\n %02d:%02d", day, month, hours, minutes);
        }
    }

    public static String getMonth(int i, boolean shorted) {
        if (shorted) {
            return months[i].substring(0, 3);
        } else {
            return months[i];
        }
    }
    public static String getWeekDay(int i) {
        return week[i];
    }

    public static String getWaves(Calendar c) {
        int hours = c.get(Calendar.HOUR_OF_DAY) > 12 ? 12 - (c.get(Calendar.HOUR_OF_DAY) - 12) : c.get(Calendar.HOUR_OF_DAY);
        System.out.println(hours);
        return waveColors[hours];
    }

    public static int getStartActivityText(Calendar c) {
        int image;
        int hours = c.get(Calendar.HOUR_OF_DAY);
        if (hours < 5 | hours > 21) {
           image = R.drawable.start_text_night;
        } else if (hours > 5 & hours < 12) {
            image = R.drawable.start_text_morning;
        } else if (hours > 12 & hours < 17) {
            image = R.drawable.start_text_afternoon;
        } else {
            image = R.drawable.start_text_evening;
        }
        return image;
    }

    public static NavOptions getNavOptions() {
        return new NavOptions.Builder()
                .setEnterAnim(R.anim.enter)
                .setExitAnim(R.anim.exit)
                .setPopEnterAnim(R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(R.anim.nav_default_pop_exit_anim)
                .build();
    }
}
