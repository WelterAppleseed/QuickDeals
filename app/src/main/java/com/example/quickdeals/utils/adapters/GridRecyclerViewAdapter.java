package com.example.quickdeals.utils.adapters;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdeals.ContainerFragment;
import com.example.quickdeals.R;
import com.example.quickdeals.database.timeless.TimelessReminderData;
import com.example.quickdeals.database.timeless.dao.TimelessReminderDao;
import com.example.quickdeals.database.timeless.entity.TimelessReminderEntity;
import com.example.quickdeals.utils.Utils;
import com.example.quickdeals.weekly.WeeklyRemindersFragment;
import com.example.quickdeals.weekly.WeeklyReminderReview;

import java.util.ArrayList;
import java.util.List;

public class GridRecyclerViewAdapter extends RecyclerView.Adapter<GridRecyclerViewAdapter.RecyclerViewHolder> {

    private List<TimelessReminderEntity> courseDataArrayList;
    private int purposeCode;
    private List<String> items;
    private TimelessReminderDao timelessReminderDao;
    private int count, newDataInt, next, restorePosition, changeInt;
    private static ContainerFragment containerFragment;
    private static WeeklyReminderReview weeklyReminderReview;
    private static FragmentManager fragmentManager;

    public GridRecyclerViewAdapter(List<TimelessReminderEntity> recyclerDataArrayList, TimelessReminderDao timelessReminderDao, List<String> items) {
        count = 0;
        this.courseDataArrayList = recyclerDataArrayList;
        this.items = items;
        this.timelessReminderDao = timelessReminderDao;
        if (getItemCount() == 0) {
            WeeklyRemindersFragment.removeProgressBar();
            WeeklyRemindersFragment.setEmptyContainerImage(true);
        }
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate Layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        return new RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Set the data to textview and imageview.
        switch (purposeCode) {
            case 1: {
                holder.currentEntity = courseDataArrayList.get(next);
                next++;
                Log.i("State", "Removing. Initializing title is " + holder.currentEntity.title + " with purposeCode " + purposeCode);
                break;
            }
            case 2: {
                holder.currentEntity = courseDataArrayList.get(newDataInt);
                Log.i("State", "Adding. Initializing title is " + holder.currentEntity.title + " with purposeCode " + purposeCode);
                break;
            }
            case 3: {
                holder.currentEntity = courseDataArrayList.get(restorePosition);
                Log.i("State", "Restoring. Initializing title is " + holder.currentEntity.title + " with purposeCode " + purposeCode);
                break;
            }
            case 4: {
                holder.currentEntity = courseDataArrayList.get(changeInt);
                changeInt++;
                Log.i("State", "Changing. Initializing title is " + holder.currentEntity.title + " with purposeCode " + purposeCode);
                break;
            }
            default: {
                break;
            }
        }
        holder.courseTV.setText(holder.currentEntity.title);
        holder.courseIV.setImageResource(Utils.getIcon(holder.currentEntity.icon));
        if (position == getItemCount()-1) {
            WeeklyRemindersFragment.removeProgressBar();
            WeeklyRemindersFragment.setEmptyContainerImage(items.size() == 0);
        }
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return courseDataArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView courseTV;
        private ImageView courseIV;
        private TimelessReminderEntity currentEntity;
        RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            if (purposeCode == 0) {
                currentEntity = courseDataArrayList.get(count);//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO
                Log.i("State", "Restoring. Initializing title is " + currentEntity.title + " with purposeCode " + purposeCode);
            }
            courseTV = itemView.findViewById(R.id.idTVCourse);
            courseIV = itemView.findViewById(R.id.idIVcourseIV);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView titleTextView = v.findViewById(R.id.idTVCourse);
                    weeklyReminderReview.setNewArgs(getBundlesFrom(titleTextView.getText().toString()), items.indexOf(titleTextView.getText().toString()));
                    ContainerFragment.showOrHideFragment(fragmentManager, weeklyReminderReview, containerFragment,true);
                }
            });
            count++;
        }
    }

    public void setCourseDataArrayList(List<TimelessReminderEntity> courseDataArrayList) {
        this.courseDataArrayList = courseDataArrayList;
    }

    public void add(String title) {
        purposeCode = 2;
        count = items.size();
        items.add(title);
        newDataInt = items.size() - 1;
        notifyItemInserted(items.size() - 1);
        notifyItemRangeInserted(items.size() - 1, items.size());
        Log.i("AddItem: ", String.valueOf(items));
    }
    void removeAt(int position) {
        purposeCode = 1;
        count = 0;
        String title = items.get(position);
        next = position;
        TimelessReminderData data = TimelessReminderEntity.getEntity(timelessReminderDao, title);
        TimelessReminderEntity.deleteFromDatabase(data, timelessReminderDao);
        items.remove(title);
        courseDataArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeRemoved(position, items.size());
        if (items.isEmpty()) {
            WeeklyRemindersFragment.setEmptyContainerImage(true);
        }
        Log.i("RemoveItem: ", String.valueOf(items));
    }
    void restore(TimelessReminderData item, int position) {
        purposeCode = 3;
        restorePosition = position;
        items.add(position, item.getTitle());
        TimelessReminderEntity.addToDatabase(item, timelessReminderDao);
        courseDataArrayList.add(position, item.castToEntity(item));
        notifyItemInserted(position);
        Log.i("RestoreItem: ", items + " in position " + position);
    }

    public void change(TimelessReminderData item, int position) {
        purposeCode = 4;
        changeInt = 0;
        items.set(position, item.getTitle());
        TimelessReminderEntity.update(timelessReminderDao, item);
        courseDataArrayList.set(position, item.castToEntity(item));
        notifyDataSetChanged();
        Log.i("ChangeItem: ", items + " in position " + position);
    }
    private Bundle getBundlesFrom(String title) {
        TimelessReminderData remData = TimelessReminderEntity.getEntity(timelessReminderDao, title);
        ArrayList<Integer> time = new ArrayList<>();
        time.add(remData.getHours());
        time.add(remData.getMinutes());
        boolean[] weekdays = TimelessReminderEntity.getSelectedWeekDays(remData);
        Bundle remBundles = new Bundle();
        remBundles.putBooleanArray("selected_week_days", weekdays);
        remBundles.putInt("icon", remData.getIcon());
        remBundles.putString("title", remData.getTitle());
        remBundles.putString("note", remData.getDesc());
        remBundles.putIntegerArrayList("time", time);
        return remBundles;
    }

    public static void setFragmentManager(FragmentManager fragmentManager) {
        GridRecyclerViewAdapter.fragmentManager = fragmentManager;
    }

    public static void setWeeklyReminderReview(WeeklyReminderReview weeklyReminderReview) {
        GridRecyclerViewAdapter.weeklyReminderReview = weeklyReminderReview;
    }

    public static void setContainerFragment(ContainerFragment containerFragment) {
        GridRecyclerViewAdapter.containerFragment = containerFragment;
    }
}
