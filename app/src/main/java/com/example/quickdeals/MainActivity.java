package com.example.quickdeals;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quickdeals.daily.DailyRemindersFragment;
import com.example.quickdeals.daily.container.AlternativeDailyDealsContainerFragment;
import com.example.quickdeals.daily.container.DefaultDailyDealsContainerFragment;
import com.example.quickdeals.notifications.AlarmManagerBroadcastReceiver;
import com.example.quickdeals.notifications.NotificationService;
import com.example.quickdeals.weekly.containers.AlternativeWeeklyDealsContainerFragment;
import com.example.quickdeals.weekly.containers.DefaultWeeklyDealsContainerFragment;

public class MainActivity extends AppCompatActivity {
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
        ContainerFragment.setContext(MainActivity.this);
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