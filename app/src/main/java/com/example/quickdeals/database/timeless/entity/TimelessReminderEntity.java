package com.example.quickdeals.database.timeless.entity;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.quickdeals.database.timeless.TimelessReminderData;
import com.example.quickdeals.database.timeless.dao.TimelessReminderDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Entity
public class TimelessReminderEntity {
    @PrimaryKey(autoGenerate = true)
    public int count;
    @NonNull
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "desc")
    public String desc;
    @ColumnInfo(name = "icon")
    public int icon;
    @ColumnInfo(name = "monday")
    public boolean mon;
    @ColumnInfo(name = "tuesday")
    public boolean tue;
    @ColumnInfo(name = "wednesday")
    public boolean wed;
    @ColumnInfo(name = "thursday")
    public boolean thu;
    @ColumnInfo(name = "friday")
    public boolean fri;
    @ColumnInfo(name = "saturday")
    public boolean sat;
    @ColumnInfo(name = "sunday")
    public boolean sun;
    @ColumnInfo(name = "hours")
    public int hours;
    @ColumnInfo(name = "minutes")
    public int minutes;

    public static void addToDatabase(final TimelessReminderData entity, final TimelessReminderDao dao) {
        System.out.println(dao);
        Runnable r = new Runnable() {
            @Override
            public void run() {
                System.out.println("232111");
                dao.insert(entity);
                Log.i("Titles", "Adding is over. New list of titles is: " + String.valueOf(dao.getTitles()));
            }
        };
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future future = executorService.submit(r);
        while (!future.isDone()) {
        }
        Log.i("State:", "Complete.");
        executorService.shutdown();
    }
    public static boolean[] getSelectedWeekDays(final TimelessReminderData entity) {
        final boolean [] weekPosition = new boolean[7];
        Runnable r = new Runnable() {
            @Override
            public void run() {
                weekPosition[0] = (entity.isMonday());
                weekPosition[1] = (entity.isTuesday());
                weekPosition[2] = (entity.isWednesday());
                weekPosition[3] = (entity.isThursday());
                weekPosition[4] = (entity.isFriday());
                weekPosition[5] = (entity.isSaturday());
                weekPosition[6] = (entity.isSunday());
            }
        };
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future future = executorService.submit(r);
        while (!future.isDone()) {
        }
        Log.i("State:", "Complete.");
        executorService.shutdown();
        return weekPosition;
    }
    public static List<String> getTitlesFromDatabase(final TimelessReminderDao dao) {
        final List<String> titleList = new ArrayList<>();
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (dao.getTitles() != null) {
                    List<String> titles = dao.getTitles();
                    titleList.addAll(titles);
                    Log.i("Titles", "Gotten list of titles is: " + titles);
                } else {
                    Log.e("Titles", "List of titles is null.");
                }
            }
        };
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future future = executorService.submit(r);
        while (!future.isDone()) {
        }
        Log.i("State:", "Complete.");
        executorService.shutdown();
        return titleList;
    }
    public static TimelessReminderData getEntity(final TimelessReminderDao dao, final String title) {
        final TimelessReminderData[] entity = {null};
        Runnable r = new Runnable() {
            @Override
            public void run() {
                if (title!= null) {
                    entity[0] = dao.getInfo(title);
                    Log.i("Title", "Gotten entity is: " + String.valueOf(entity[0].getTitle()));
                } else {

                }
            }
        };
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future future = executorService.submit(r);
        while (!future.isDone()) {
        }
        Log.i("State:", "Complete.");
        executorService.shutdown();
        return entity[0];
    }
    public static void deleteFromDatabase(final TimelessReminderData entity, final TimelessReminderDao dao) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                dao.delete(entity);
                Log.i("Titles", "Deleting is over. New list of titles is: " + String.valueOf(dao.getTitles()));
            }
        };
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future future = executorService.submit(r);
        while (!future.isDone()) {
        }
        executorService.shutdown();
    }
    public static List<TimelessReminderEntity> getAll(final TimelessReminderDao dao) {
        final List<TimelessReminderEntity>[] list = new List[]{new ArrayList<>()};
        Runnable r = new Runnable() {
            @Override
            public void run() {
                list[0] = dao.getAll();
                Log.i("Titles", "List of titles is: " + String.valueOf(dao.getTitles()));
            }
        };
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future future = executorService.submit(r);
        while (!future.isDone()) {
        }
        Log.i("State:", "Complete.");
        executorService.shutdown();
        return list[0];
    }
    public static void update(final TimelessReminderDao dao, final TimelessReminderData data) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                dao.update(data);
                Log.i("Titles", "List of titles is: " + String.valueOf(dao.getTitles()));
            }
        };
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future future = executorService.submit(r);
        while (!future.isDone()) {
        }
        Log.i("State:", "Complete.");
        executorService.shutdown();
    }
    public static void clearAll(final TimelessReminderDao dao) {
        Runnable r = new Runnable() {
            @Override
            public void run() {
                dao.clearAll();
                Log.i("Titles", "List of titles is cleared");
            }
        };
        ExecutorService executorService = Executors.newCachedThreadPool();
        Future future = executorService.submit(r);
        while (!future.isDone()) {
        }
        Log.i("State:", "Complete.");
        executorService.shutdown();
    }
}
