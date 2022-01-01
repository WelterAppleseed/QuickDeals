package com.example.quickdeals.database.temporary.entity;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.quickdeals.database.temporary.ReminderData;
import com.example.quickdeals.database.temporary.dao.ReminderDao;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Entity
public class ReminderEntity {
    @PrimaryKey(autoGenerate = true)
    public int count;
    @NonNull
    @ColumnInfo(name = "title")
    public String title;
    @ColumnInfo(name = "desc")
    public String desc;
    @ColumnInfo(name = "icon")
    public int icon;
    @ColumnInfo(name = "year")
    public int year;

    @ColumnInfo(name = "month")
    public int month;

    @ColumnInfo(name = "day")
    public int day;

    @ColumnInfo(name = "hours")
    public int hours;

    @ColumnInfo(name = "minutes")
    public int minutes;

    @ColumnInfo(name = "repeat")
    public boolean repeat;

    @ColumnInfo(name = "alarm")
    public boolean alarm;

    public static void addToDatabase(final ReminderData entity, final ReminderDao dao) {
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
    public static List<String> getTitlesFromDatabase(final ReminderDao dao) {
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
    public static ReminderData getEntity(final ReminderDao dao, final String title) {
        final ReminderData[] entity = {null};
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
    public static void deleteFromDatabase(final ReminderData entity, final ReminderDao dao) {
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
    public static List<ReminderEntity> getAll(final ReminderDao dao) {
        final List<ReminderEntity>[] list = new List[]{new ArrayList<>()};
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
    public static void update(final ReminderDao dao, final ReminderData data) {
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
    public static void clearAll(final ReminderDao dao) {
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
