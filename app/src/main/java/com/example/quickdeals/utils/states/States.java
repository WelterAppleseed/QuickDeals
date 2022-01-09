package com.example.quickdeals.utils.states;

import androidx.annotation.Nullable;

import com.example.quickdeals.R;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import java.util.ArrayList;
import java.util.Calendar;

public class States {
    private static final int[] icons = {R.drawable.default_icon, R.drawable.sport_icon, R.drawable.fun_icon, R.drawable.work_icon, R.drawable.hobby_icon};
    private static final String[] months = {"Zero Month", "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
    private static final String[] week = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun", "fds"};
    private static final String[] waveColors = {"#1E3216", "#1E4216", "#1E5216", "#1E6216", "#1E7216", "#1E8216", "#1E9216", "1EA216", "#1EB216", "#1EC216", "#1ED216", "#1EE216", "#1EF216", "#1EFF16" };
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
        return waveColors[hours];
    }
}