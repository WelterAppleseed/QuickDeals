package com.example.quickdeals.study.adapter;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdeals.R;
import com.example.quickdeals.ShablonFragment;
import com.example.quickdeals.daily.container.DefaultDailyDealsContainerFragment;
import com.example.quickdeals.database.temporary.ReminderData;
import com.example.quickdeals.database.temporary.entity.ReminderEntity;
import com.example.quickdeals.database.timeless.TimelessReminderData;
import com.example.quickdeals.database.timeless.dao.TimelessReminderDao;
import com.example.quickdeals.database.timeless.entity.TimelessReminderEntity;
import com.example.quickdeals.study.StudyFragment;
import com.example.quickdeals.study.TimelessReminderDCC;
import com.example.quickdeals.study.TimelessReminderReview;
import com.example.quickdeals.study.item_data.GridItemData;
import com.example.quickdeals.utils.states.States;

import java.util.ArrayList;
import java.util.List;

public class GridRecyclerViewAdapter extends RecyclerView.Adapter<GridRecyclerViewAdapter.RecyclerViewHolder> {

    private List<TimelessReminderEntity> courseDataArrayList;
    private Context mcontext;
    private int purposeCode;
    private List<String> items;
    private TimelessReminderDao timelessReminderDao;
    private int count, newDataInt, next, restorePosition, changeInt;
    private static ShablonFragment shablonFragment;
    private static TimelessReminderReview timelessReminderReview;
    private static FragmentManager fragmentManager;

    public GridRecyclerViewAdapter(List<TimelessReminderEntity> recyclerDataArrayList, TimelessReminderDao timelessReminderDao, List<String> items, Context mcontext) {
        this.courseDataArrayList = recyclerDataArrayList;
        this.mcontext = mcontext;
        this.items = items;
        this.timelessReminderDao = timelessReminderDao;
        count = 0;
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
        System.out.println(getItemCount() + " " + position);
        holder.courseTV.setText(holder.currentEntity.title);
        holder.courseIV.setImageResource(States.getIcon(holder.currentEntity.icon));
        if (position == getItemCount()-1) {
            StudyFragment.removeProgressBar();
            StudyFragment.setEmptyContainerImage(items.isEmpty());
        }
    }

    @Override
    public int getItemCount() {
        // this method returns the size of recyclerview
        return courseDataArrayList.size();
    }

    // View Holder Class to handle Recycler View.
    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private TextView courseTV;
        private ImageView courseIV;
        private TimelessReminderEntity currentEntity;
        private View view;
        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            if (purposeCode == 0) {
                currentEntity = courseDataArrayList.get(count);//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO//TODO
                Log.i("State", "Restoring. Initializing title is " + currentEntity.title + " with purposeCode " + purposeCode);
            }
            courseTV = itemView.findViewById(R.id.idTVCourse);
            courseIV = itemView.findViewById(R.id.idIVcourseIV);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        TextView titleTextView = v.findViewById(R.id.idTVCourse);
                        //shablonFragment.setSharedElementReturnTransition(TransitionInflater.from(shablonFragment.getActivity()).inflateTransition(R.transition.change_image_transform));
                        //shablonFragment.setExitTransition(TransitionInflater.from(shablonFragment.getActivity()).inflateTransition(android.R.transition.slide_left));
                        timelessReminderReview.setNewArgs(getBundlesFrom(titleTextView.getText().toString()), items.indexOf(titleTextView.getText().toString()));
                        //timelessReminderReview.setSharedElementEnterTransition(TransitionInflater.from(shablonFragment.getActivity()).inflateTransition(R.transition.change_image_transform));
                        //timelessReminderReview.setEnterTransition(TransitionInflater.from(shablonFragment.getActivity()).inflateTransition(android.R.transition.slide_right));
                        //ViewCompat.setTransitionName(courseIV, "1");
                       /* fragmentManager.beginTransaction()
                                .setCustomAnimations(R.anim.enter, R.anim.exit)
                                .show(timelessReminderReview)
                                .addToBackStack("transaction")
                                .addSharedElement(courseIV, "1")
                                .commit();*/
                       ShablonFragment.showOrHideFragment(fragmentManager, timelessReminderReview, shablonFragment,true);
                    }
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
    public void removeAt(int position) {
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
        /*if (items.isEmpty()) {
            DailyRemindersFragment.setEmptyContainerImage(true);
        }*/
        Log.i("RemoveItem: ", String.valueOf(items));
    }
    public void restore(TimelessReminderData item, int position) {
        purposeCode = 3;
        restorePosition = position;
        items.add(position, item.getTitle());
        TimelessReminderEntity.addToDatabase(item, timelessReminderDao);
        courseDataArrayList.add(position, item.castToEntity(item));
        notifyItemInserted(position);
        Log.i("RestoreItem: ", String.valueOf(items) + " in position " + position);
    }

    public void change(TimelessReminderData item, int position) {
        purposeCode = 4;
        changeInt = 0;
        items.set(position, item.getTitle());
        TimelessReminderEntity.update(timelessReminderDao, item);
        courseDataArrayList.set(position, item.castToEntity(item));
        notifyDataSetChanged();
        Log.i("ChangeItem: ", String.valueOf(items) + " in position " + position);
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

    public static void setTimelessReminderReview(TimelessReminderReview timelessReminderReview) {
        GridRecyclerViewAdapter.timelessReminderReview = timelessReminderReview;
    }

    public static void setShablonFragment(ShablonFragment shablonFragment) {
        GridRecyclerViewAdapter.shablonFragment = shablonFragment;
    }
}
