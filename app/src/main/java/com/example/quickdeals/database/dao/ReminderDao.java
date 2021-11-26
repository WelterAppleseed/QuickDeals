package com.example.quickdeals.database.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.quickdeals.database.ReminderData;
import com.example.quickdeals.database.entity.ReminderEntity;

import java.util.List;

@Dao
public interface ReminderDao {
    @Query("SELECT * FROM reminderentity")
    List<ReminderEntity> getAll();
    @Query("SELECT title FROM reminderentity ORDER BY count")
    List<String> getTitles();
    @Query("SELECT * FROM reminderentity WHERE title LIKE :title LIMIT 1")
    ReminderData getInfo(String title);
    @Query("DELETE FROM reminderentity")
    void clearAll();
    @Insert(entity = ReminderEntity.class)
    void insert(ReminderData reminderData);
    @Update
    void update(ReminderEntity reminder);
    @Update(entity = ReminderEntity.class)
    void update(ReminderData reminder);
    @Delete(entity = ReminderEntity.class)
    void delete(ReminderData reminderData);
}
