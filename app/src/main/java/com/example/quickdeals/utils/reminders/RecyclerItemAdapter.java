package com.example.quickdeals.utils.reminders;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdeals.MainActivity;
import com.example.quickdeals.R;
import com.example.quickdeals.ReminderReview;
import com.example.quickdeals.ShablonFragment;
import com.example.quickdeals.daily.DailyRemindersFragment;
import com.example.quickdeals.daily.container.DefaultDailyDealsContainerFragment;
import com.example.quickdeals.database.temporary.ReminderData;
import com.example.quickdeals.database.temporary.dao.ReminderDao;
import com.example.quickdeals.database.temporary.entity.ReminderEntity;
import com.example.quickdeals.utils.states.States;

import java.util.ArrayList;
import java.util.List;


public class RecyclerItemAdapter extends RecyclerView.Adapter<RecyclerItemAdapter.CustomViewHolder> {
    private Context context;
    public List<String> items;
    public int count, next, changeInt, newDataInt, restorePosition, purposeCode;
    private View item;
    private ReminderDao dao;
    private static ShablonFragment shablonFragment;
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
        holder.icon.setImageResource(States.getIcon(holder.currentReminderData.icon));
        holder.time.setText(States.getTime(null, holder.currentReminderData.day, States.getMonth(holder.currentReminderData.month, true), holder.currentReminderData.hours, holder.currentReminderData.minutes, false));
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
        public RelativeLayout viewBackground, viewForeground;
        ;
        private ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        private CustomViewHolder(final View itemView) {
            super(itemView);
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        TextView titleTextView = v.findViewById(R.id.rem_title);
                        shablonFragment.setSharedElementReturnTransition(TransitionInflater.from(shablonFragment.getActivity()).inflateTransition(R.transition.change_image_transform));
                        //shablonFragment.setExitTransition(TransitionInflater.from(shablonFragment.getActivity()).inflateTransition(android.R.transition.slide_top));
                        reminderReview.setNewArgs(getBundlesFrom(titleTextView.getText().toString()), items.indexOf(titleTextView.getText().toString()));
                        reminderReview.setSharedElementEnterTransition(TransitionInflater.from(shablonFragment.getActivity()).inflateTransition(R.transition.change_image_transform));
                        //reminderReview.setEnterTransition(TransitionInflater.from(shablonFragment.getActivity()).inflateTransition(android.R.transition.slide_top));
                        ShablonFragment.showOrHideFragment(fragmentManager, reminderReview, shablonFragment, true);
                       /* fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.enter, R.anim.exit)
                                .show(reminderReview)
                                .addToBackStack("transaction")
                                .commit();*/
                    }
                }
            });
            count++;
        }
    }

    public void removeAt(int position) {
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

    public void restore(ReminderData item, int position) {
        purposeCode = 3;
        restorePosition = position;
        items.add(position, item.getTitle());
        ReminderEntity.addToDatabase(item, dao);
        reminderDataList.add(position, item.castToEntity(item));
        notifyItemInserted(position);
        Log.i("RestoreItem: ", String.valueOf(items) + " in position " + position);
    }

    public void change(ReminderData item, int position) {
        purposeCode = 4;
        changeInt = 0;
        items.set(position, item.getTitle());
        ReminderEntity.update(dao, item);
        reminderDataList.set(position, item.castToEntity(item));
        notifyDataSetChanged();
        Log.i("ChangeItem: ", String.valueOf(items) + " in position " + position);
    }

    public void setReminderDataList(List<ReminderEntity> reminderDataList) {
        this.reminderDataList = reminderDataList;
    }

    public static void setFragmentManager(FragmentManager fragmentManager) {
        RecyclerItemAdapter.fragmentManager = fragmentManager;
    }

    public static void setShablonFragment(ShablonFragment shablonFragment) {
        RecyclerItemAdapter.shablonFragment = shablonFragment;
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