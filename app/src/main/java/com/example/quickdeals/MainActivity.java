package com.example.quickdeals;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdeals.daily.DailyRemindersFragment;
import com.example.quickdeals.daily.actions.ReminderActions;
import com.example.quickdeals.daily.container.AlternativeDailyDealsContainerFragment;
import com.example.quickdeals.daily.container.DefaultDailyDealsContainerFragment;
import com.example.quickdeals.daily.dialog.notifications.AlarmManagerBroadcastReceiver;
import com.example.quickdeals.daily.dialog.notifications.NotificationService;
import com.example.quickdeals.study.containers.AlternativeWeeklyDealsContainerFragment;
import com.example.quickdeals.study.containers.DefaultWeeklyDealsContainerFragment;
import com.example.quickdeals.utils.Listeners;
import com.example.quickdeals.daily.dialog.notifications.NotificationCreator;
import com.example.quickdeals.daily.ReminderInitializer;
import com.example.quickdeals.utils.Utils;
import com.example.quickdeals.utils.reminders.SavedReminder;
import com.example.quickdeals.utils.states.States;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zerobranch.layout.SwipeLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addItem;
    private LinearLayout defLayout;
    private RecyclerView itemLayout;
    private List<SwipeLayout> itemList;
    private Dialog addItemDialog;
    private NotificationManager mNotificationManager;
    private Listeners listeners;
    private ReminderInitializer dialogCreator;
    private ReminderActions actions;
    private Dialog dialog;
    private LayoutInflater inflater;
    private static final int NOTIFY_ID = 101;
    private PendingIntent pendingIntent;
    private NotificationCreator notificationCreator;
    private static final String IS_WORKING = "is_working";
    private ConstraintLayout layout;
    private SharedPreferences.Editor editor;
    private SavedReminder reminders;
    private List<String> items;
    private Intent changeReminder;
    private static DefaultDailyDealsContainerFragment frag;
    public static boolean working;
    ArrayList<States> states;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        DailyRemindersFragment.setContext(MainActivity.this);
        DefaultDailyDealsContainerFragment.setContext(MainActivity.this);
        DefaultWeeklyDealsContainerFragment.setContext(MainActivity.this);
        AlternativeDailyDealsContainerFragment.setContext(MainActivity.this);
        AlternativeWeeklyDealsContainerFragment.setContext(MainActivity.this);
        NotificationService.setActivity(MainActivity.this);
        ShablonFragment.setContext(MainActivity.this);
        //setInitialData();
        //inflater = LayoutInflater.from(MainActivity.this);
        //restoreReminders();
        //contentCreate();

    }
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        AlarmManagerBroadcastReceiver.isWorking = hasFocus;
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onBackPressed() {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory( Intent.CATEGORY_HOME );
        homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}