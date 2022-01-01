package com.example.quickdeals;

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import com.example.quickdeals.utils.Listeners;
import com.example.quickdeals.daily.dialog.notifications.NotificationCreator;
import com.example.quickdeals.daily.ReminderInitializer;
import com.example.quickdeals.utils.reminders.SavedReminder;
import com.example.quickdeals.utils.states.States;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zerobranch.layout.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        DailyRemindersFragment.setContext(MainActivity.this);
        DefaultDailyDealsContainerFragment.setContext(MainActivity.this);
        AlternativeDailyDealsContainerFragment.setContext(MainActivity.this);
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
    /* private void setInitialData(){
        states = new ArrayList<State>();
        changeReminder = new Intent(MainActivity.this, RemindersOptions.class);

    }
    private void restoreReminders() {
        items = new ArrayList<>();
        preferences = getSharedPreferences("all_titles", Context.MODE_PRIVATE);
        Map<String, ?> allEntries = preferences.getAll();
        for (Map.Entry<String, ?> entry : allEntries.entrySet()) {
            String title = entry.getValue().toString();
            items.add(title);
        }
        ImageButton dailyButton = (ImageButton) findViewById(R.id.first_fragment);
        dailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(changeReminder);
            }
        });


    }


    private void contentCreate() {
    }
    public void start() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Date dat = new Date();
        Calendar cal_alarm = Calendar.getInstance();
        Calendar cal_now = Calendar.getInstance();
        cal_now.setTime(dat);
        cal_alarm.setTime(dat);
        cal_alarm.set(Calendar.HOUR_OF_DAY, 16);
        cal_alarm.set(Calendar.MINUTE, 39);
        cal_alarm.set(Calendar.SECOND, 0);
        if (cal_alarm.before(cal_now)) {
            cal_alarm.add(Calendar.DATE, 1);
        }

        Intent myIntent = new Intent(getApplicationContext(), AlarmManagerBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, myIntent, 0);
        manager.set(AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis(), pendingIntent);
    }
*/
}