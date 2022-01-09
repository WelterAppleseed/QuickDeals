package com.example.quickdeals.database.timeless;

import androidx.annotation.Nullable;

import com.example.quickdeals.database.timeless.entity.TimelessReminderEntity;

import java.util.ArrayList;

public class TimelessReminderData {
    private String title;
    private String desc;
    private int icon, hours, minutes;
    private boolean monday, tuesday, wednesday, thursday, friday, saturday, sunday;
    private Integer count;

    public TimelessReminderData(@Nullable Integer count, String title, String desc, int icon, boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday, boolean saturday, boolean sunday, int hours, int minutes) {
        this.title = title;
        this.desc = desc;
        this.icon = icon;
        this.count= count;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.hours = hours;
        this.minutes = minutes;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getCount() {
        return count;
    }

    public TimelessReminderEntity castToEntity(TimelessReminderData data) {
        TimelessReminderEntity entity = new TimelessReminderEntity();
        entity.count = count;
        entity.title = data.title;
        entity.desc = data.desc;
        entity.icon = data.icon;
        entity.mon = data.monday;
        entity.tue = data.tuesday;
        entity.wed = data.wednesday;
        entity.thu = data.thursday;
        entity.fri = data.friday;
        entity.sat = data.saturday;
        entity.sun = data.sunday;
        entity.hours = data.hours;
        entity.minutes = data.minutes;
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

    public boolean isMonday() {
        return monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }
}
