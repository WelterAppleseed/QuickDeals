package com.example.quickdeals.database.temporary;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.quickdeals.database.temporary.dao.ReminderDao;
import com.example.quickdeals.database.temporary.entity.ReminderEntity;

@Database(entities = {ReminderEntity.class}, version = 1)
public abstract class RemDatabase extends RoomDatabase {
    public abstract ReminderDao reminderDao();
}
