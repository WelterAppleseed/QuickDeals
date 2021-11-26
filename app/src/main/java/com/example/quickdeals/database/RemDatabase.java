package com.example.quickdeals.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.quickdeals.database.dao.ReminderDao;
import com.example.quickdeals.database.entity.ReminderEntity;

@Database(entities = {ReminderEntity.class}, version = 1)
public abstract class RemDatabase extends RoomDatabase {
    public abstract ReminderDao reminderDao();
}
