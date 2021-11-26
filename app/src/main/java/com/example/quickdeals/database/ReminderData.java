package com.example.quickdeals.database;

import androidx.annotation.Nullable;

import com.example.quickdeals.database.entity.ReminderEntity;

public class ReminderData {
    private String title;
    private String desc;
    private int icon, year, month, day, hours, minutes;
    private boolean repeat, alarm;
    private Integer count;

    public ReminderData(@Nullable Integer count, String title, String desc, int icon, int year, int month, int day, int hours, int minutes, boolean repeat, boolean alarm) {
        this.title = title;
        this.desc = desc;
        this.icon = icon;
        this.count= count;
        this.year = year;
        this.month = month;
        this.day = day;
        this.hours = hours;
        this.minutes = minutes;
        this.repeat = repeat;
        this.alarm = alarm;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setAlarm(boolean alarm) {
        this.alarm = alarm;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setHours(int hours) {
        this.hours = hours;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setYear(int year) {
        this.year = year;
    }
    public Integer getCount() {
        return count;
    }

    public ReminderEntity castToEntity(ReminderData data) {
        ReminderEntity entity = new ReminderEntity();
        entity.count = count;
        entity.title = data.title;
        entity.desc = data.desc;
        entity.icon = data.icon;
        entity.year = data.year;
        entity.month = data.month;
        entity.day = data.day;
        entity.hours = data.hours;
        entity.minutes = data.minutes;
        entity.repeat = data.repeat;
        entity.alarm = data.alarm;
        return entity;
    }
    public String getTitle() {
        return title;
    }
    public String getDesc() {
        return desc;
    }

    public int getIcon() {
        return icon;
    }
    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public boolean isAlarm() {
        return alarm;
    }

    public boolean isRepeat() {
        return repeat;
    }
}
