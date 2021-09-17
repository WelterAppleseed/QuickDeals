package com.example.quickdeals;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.chauthai.swipereveallayout.SwipeRevealLayout;
import com.example.quickdeals.daily.adapter_and_activity.ListAdapter;
import com.example.quickdeals.utils.AlarmManagerBroadcastReceiver;
import com.example.quickdeals.utils.Listeners;
import com.example.quickdeals.utils.NotificationCreator;
import com.example.quickdeals.utils.ReminderDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.zerobranch.layout.SwipeLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FloatingActionButton addItem;
    private ListAdapter adapter;
    private LinearLayout itemLayout, defLayout;
    private Dialog addItemDialog;
    private NotificationManager mNotificationManager;
    private AlarmManagerBroadcastReceiver alarmManager;
    private Listeners listeners;
    private ReminderDialog dialogCreator;
    private Dialog dialog;
    private LayoutInflater inflater;
    private static final int NOTIFY_ID = 101;
    private static String CHANNEL_ID = "Cat channel";
    private PendingIntent pendingIntent;
    private NotificationCreator notificationCreator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.daily_activity_layout);
        inflater = LayoutInflater.from(MainActivity.this);
        contentCreate();

}

    private void contentCreate() {
        listeners = new Listeners();
        mNotificationManager =
                (NotificationManager) MainActivity.this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationCreator = new NotificationCreator(MainActivity.this, mNotificationManager);
        itemLayout = (LinearLayout) findViewById(R.id.item_layout);
        defLayout = (LinearLayout) findViewById(R.id.def_layout);
        addItem = (FloatingActionButton) findViewById(R.id.add_item);
        dialogCreator = new ReminderDialog(MainActivity.this, listeners, notificationCreator, itemLayout, defLayout);
        dialog = dialogCreator.getCustomizingDialog();
        addItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        System.out.println(System.currentTimeMillis());
    }

    public void layoutTwoOnClick(View v) {
        Toast.makeText(MainActivity.this, "Layout 2 clicked", Toast.LENGTH_SHORT).show();
    }

    public void layoutThreeOnClick(View v) {
        Toast.makeText(MainActivity.this, "Layout 3 clicked", Toast.LENGTH_SHORT).show();
    }

    public void layoutFourOnClick(View v) {
        Toast.makeText(MainActivity.this, "Layout 4 clicked", Toast.LENGTH_SHORT).show();
    }

    public void moreOnClick(View v) {
        Toast.makeText(MainActivity.this, "More clicked", Toast.LENGTH_SHORT).show();
    }
    public void deleteItem(View view, LinearLayout itemLayout, LinearLayout defLayout) {
        if (view.getParent() instanceof SwipeLayout) {
            try {
                itemLayout.removeView((View) view.getParent());
                if (itemLayout.getChildCount() == 1) {
                    defLayout.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void archiveOnClick(View v) {
        Toast.makeText(MainActivity.this, "Archive clicked", Toast.LENGTH_SHORT).show();
    }

    public void helpOnClick(View v) {
        Toast.makeText(MainActivity.this, "Help clicked", Toast.LENGTH_SHORT).show();
    }

    public void searchOnClick(View v) {
        Toast.makeText(MainActivity.this, "Search clicked", Toast.LENGTH_SHORT).show();
    }

    public void starOnClick(View v) {
        Toast.makeText(MainActivity.this, "Star clicked", Toast.LENGTH_SHORT).show();
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
}
