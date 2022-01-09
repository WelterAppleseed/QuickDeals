package com.example.quickdeals.utils.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdeals.ContainerFragment;
import com.example.quickdeals.R;
import com.example.quickdeals.daily.ReminderReview;
import com.example.quickdeals.daily.DailyRemindersFragment;
import com.example.quickdeals.daily.container.DefaultDailyDealsContainerFragment;
import com.example.quickdeals.database.temporary.ReminderData;
import com.example.quickdeals.database.temporary.dao.ReminderDao;
import com.example.quickdeals.database.temporary.entity.ReminderEntity;
import com.example.quickdeals.utils.Utils;

import java.util.ArrayList;
import java.util.List;


public class RecyclerItemAdapter extends RecyclerView.Adapter<RecyclerItemAdapter.CustomViewHolder> {
    private Context context;
    private List<String> items;
    private int count, next, changeInt, newDataInt, restorePosition, purposeCode;
    private View item;
    private ReminderDao dao;
    private static ContainerFragment containerFragment;
    private static ReminderReview reminderReview;
    private List<ReminderEntity> reminderDataList;
    private static FragmentManager fragmentManager;

    public RecyclerItemAdapter(Context context, ReminderDao dao, List<String> items, List<ReminderEntity> reminderDataList) {
        count = 0;
        this.context = context;
        this.items = items;
        this.dao = dao;
        this.reminderDataList = reminderDataList;
        if (getItemCount() == 0) {
            DailyRemindersFragment.removeProgressBar();
            DailyRemindersFragment.setEmptyContainerImage(true);
        }
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        item = LayoutInflater.from(context).inflate(R.layout.rem_shablon, null, false);
        return new CustomViewHolder(item);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, final int position) {
        switch (purposeCode) {
            case 1: {
                holder.currentReminderData = reminderDataList.get(next);
                next++;
                Log.i("State", "Removing. Initializing title is " + holder.currentReminderData.title + " with purposeCode " + purposeCode);
                break;
            }
            case 2: {
                holder.currentReminderData = reminderDataList.get(newDataInt);
                Log.i("State", "Adding. Initializing title is " + holder.currentReminderData.title + " with purposeCode " + purposeCode);
                break;
            }
            case 3: {
                holder.currentReminderData = reminderDataList.get(restorePosition);
                Log.i("State", "Restoring. Initializing title is " + holder.currentReminderData.title + " with purposeCode " + purposeCode);
                break;
            }
            case 4: {
                holder.currentReminderData = reminderDataList.get(changeInt);
                changeInt++;
                Log.i("State", "Changing. Initializing title is " + holder.currentReminderData.title + " with purposeCode " + purposeCode);
                break;
            }
            default: {
                break;
            }
        }
        System.out.println(getItemCount() + " " + position);
        holder.title.setText(holder.currentReminderData.title);
        holder.desc.setText(holder.currentReminderData.desc);
        holder.icon.setImageResource(Utils.getIcon(holder.currentReminderData.icon));
        holder.time.setText(Utils.getTime(null, holder.currentReminderData.day, Utils.getMonth(holder.currentReminderData.month, true), holder.currentReminderData.hours, holder.currentReminderData.minutes, false));
        if (position == getItemCount()-1) {
            DailyRemindersFragment.removeProgressBar();
            DailyRemindersFragment.setEmptyContainerImage(items.isEmpty()   );
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        private ReminderEntity currentReminderData;
        private ImageView icon;
        private TextView title, desc, time;
        RelativeLayout viewBackground, viewForeground;

        private CustomViewHolder(final View itemView) {
            super(itemView);
            ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 20, 0, 0);
            itemView.setLayoutParams(params);
            title = itemView.findViewById(R.id.rem_title);
            desc = itemView.findViewById(R.id.rem_desc);
            icon = itemView.findViewById(R.id.icon_of_notif);
            time = itemView.findViewById(R.id.time);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
            if (purposeCode == 0) {
                currentReminderData = reminderDataList.get(count);//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO
                Log.i("State", "Restoring. Initializing title is " + currentReminderData.title + " with purposeCode " + purposeCode);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView titleTextView = v.findViewById(R.id.rem_title);
                    reminderReview.setNewArgs(getBundlesFrom(titleTextView.getText().toString()), items.indexOf(titleTextView.getText().toString()));
                    ContainerFragment.showOrHideFragment(fragmentManager, reminderReview, containerFragment, true);
                }
            });
            count++;
        }
    }

    void removeAt(int position) {
        purposeCode = 1;
        count = 0;
        String title = items.get(position);
        next = position;
        ReminderData data = ReminderEntity.getEntity(dao, title);
        ReminderEntity.deleteFromDatabase(data, dao);
        items.remove(title);
        reminderDataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, items.size());
        if (items.isEmpty()) {
            DailyRemindersFragment.setEmptyContainerImage(true);
        }
        Log.i("RemoveItem: ", String.valueOf(items));
    }
    public void removeFromList(String title) {
        purposeCode = 1;
        count = 0;
        int position = items.indexOf(title);
        next = position;
        items.remove(title);
        reminderDataList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, items.size());
        if (items.isEmpty()) {
            DailyRemindersFragment.setEmptyContainerImage(true);
        }
        Log.i("RemoveItem: ", String.valueOf(items));
    }
    public void add(String title) {
        purposeCode = 2;
        count = items.size();
        items.add(title);
        newDataInt = items.size() - 1;
        notifyItemInserted(items.size() - 1);
        notifyItemRangeInserted(items.size() - 1, items.size());
        DefaultDailyDealsContainerFragment.setAdapter(this);
        Log.i("AddItem: ", String.valueOf(items));
    }

    void restore(ReminderData item, int position) {
        purposeCode = 3;
        restorePosition = position;
        items.add(position, item.getTitle());
        ReminderEntity.addToDatabase(item, dao);
        reminderDataList.add(position, item.castToEntity(item));
        notifyItemInserted(position);
        Log.i("RestoreItem: ", items + " in position " + position);
    }

    public void change(ReminderData item, int position) {
        purposeCode = 4;
        changeInt = 0;
        items.set(position, item.getTitle());
        ReminderEntity.update(dao, item);
        reminderDataList.set(position, item.castToEntity(item));
        notifyDataSetChanged();
        Log.i("ChangeItem: ", items + " in position " + position);
    }

    public void setReminderDataList(List<ReminderEntity> reminderDataList) {
        this.reminderDataList = reminderDataList;
    }

    public static void setFragmentManager(FragmentManager fragmentManager) {
        RecyclerItemAdapter.fragmentManager = fragmentManager;
    }

    public static void setContainerFragment(ContainerFragment containerFragment) {
        RecyclerItemAdapter.containerFragment = containerFragment;
    }
    public static void setReviewFragment(ReminderReview reviewFragment) {
        RecyclerItemAdapter.reminderReview = reviewFragment;
    }

    private Bundle getBundlesFrom(String title) {
        ReminderData remData = ReminderEntity.getEntity(dao, title);
        ArrayList<Integer> time = new ArrayList<>();
        time.add(remData.getYear());
        time.add(remData.getMonth());
        time.add(remData.getDay());
        time.add(remData.getHours());
        time.add(remData.getMinutes());
        Bundle remBundles = new Bundle();
        remBundles.putInt("icon", remData.getIcon());
        remBundles.putString("title", remData.getTitle());
        remBundles.putString("note", remData.getDesc());
        remBundles.putIntegerArrayList("time", time);
        remBundles.putBoolean("repeat", remData.isRepeat());
        remBundles.putBoolean("alarm", remData.isAlarm());
        return remBundles;
    }
}