package com.example.quickdeals.utils.reminders;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.quickdeals.R;
import com.example.quickdeals.daily.actions.ReminderActions;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.zerobranch.layout.SwipeLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import io.paperdb.Paper;

public class SavedReminder {
    private static SharedPreferences itemSP, textSP;
    private static SharedPreferences.Editor itemSPE;
    private SwipeLayout item;
    private List<String> remindersTitles;
    private String itemText;
    private boolean withAlarm, repeatable;
    private int hours, minutes;
    private CalendarDay day;
    private static Context mainCon, swipeCon;
    private static final String SHARED_PREFERENCES_NAME = "items";
    private ReminderActions actions;
    public SavedReminder(Context mainCon, List<String> remindersTitles, String itemText, SwipeLayout item, boolean withAlarm, boolean repeatable, int hours, int minutes, CalendarDay day) {
        this.mainCon = mainCon;
        this.item = item;
        this.withAlarm = withAlarm;
        this.repeatable = repeatable;
        this.hours = hours;
        this.minutes = minutes;
        this.day = day;
        this.remindersTitles = remindersTitles;
        this.itemText = itemText;
        itemSP = mainCon.getSharedPreferences("all_parameters", Context.MODE_PRIVATE);
    }
    public SavedReminder(Context mainCon) {
        this.mainCon = mainCon;
        itemSP = mainCon.getSharedPreferences("all_parameters", Context.MODE_PRIVATE);
    }

    public void save() {
        Paper.book("reminders_titles").write(itemText, itemText);
        itemSP.edit()
                .putString(itemText + " title", itemText)
                .putBoolean(itemText + " with_alarm", withAlarm)
                .putBoolean(itemText + " repeatable", repeatable)
                .putInt(itemText + " hours", hours)
                .putInt(itemText + " minutes", minutes)
                .putInt(itemText + " day", day.getDay()).apply();
    }
    public void delete(String title) {
        Paper.book("reminders_titles").delete(title);
        itemSP.edit()
                .remove(title + " title")
                .remove(title + " with_alarm")
                .remove(title + " repeatable")
                .remove(title + " hours")
                .remove(title + " minutes")
                .remove(title + " day").apply();
    }
}
