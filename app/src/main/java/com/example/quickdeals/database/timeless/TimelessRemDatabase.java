package com.example.quickdeals.database.timeless;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.quickdeals.database.timeless.dao.TimelessReminderDao;
import com.example.quickdeals.database.temporary.entity.ReminderEntity;
import com.example.quickdeals.database.timeless.entity.TimelessReminderEntity;

@Database(entities = {TimelessReminderEntity.class}, version = 1)
public abstract class TimelessRemDatabase extends RoomDatabase {
    public abstract TimelessReminderDao timelessReminderDao();
}
