package com.example.quickdeals.database.timeless.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quickdeals.database.timeless.TimelessReminderData;
import com.example.quickdeals.database.timeless.entity.TimelessReminderEntity;

import java.util.List;

@Dao
public interface TimelessReminderDao {
    @Query("SELECT * FROM timelessreminderentity")
    List<TimelessReminderEntity> getAll();
    @Query("SELECT title FROM timelessreminderentity ORDER BY count")
    List<String> getTitles();
    @Query("SELECT * FROM timelessreminderentity WHERE title LIKE :title LIMIT 1")
    TimelessReminderData getInfo(String title);
    @Query("DELETE FROM timelessreminderentity")
    void clearAll();
    @Insert(entity = TimelessReminderEntity.class)
    void insert(TimelessReminderData reminderData);
    @Update
    void update(TimelessReminderEntity reminder);
    @Update(entity = TimelessReminderEntity.class)
    void update(TimelessReminderData reminder);
    @Delete(entity = TimelessReminderEntity.class)
    void delete(TimelessReminderData reminderData);
}
