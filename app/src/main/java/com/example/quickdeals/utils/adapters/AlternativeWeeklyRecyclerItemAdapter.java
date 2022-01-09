package com.example.quickdeals.utils.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdeals.R;
import com.example.quickdeals.database.timeless.dao.TimelessReminderDao;
import com.example.quickdeals.database.timeless.entity.TimelessReminderEntity;
import com.example.quickdeals.utils.Utils;
import com.example.quickdeals.weekly.WeeklyRemindersFragment;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class AlternativeWeeklyRecyclerItemAdapter extends RecyclerView.Adapter<AlternativeWeeklyRecyclerItemAdapter.AlternativeCustomViewHolder> {
    private Context context;
    private List<String> items;
    public int count;
    private List<TimelessReminderEntity> reminderDataList;
    private LocalDate localDate;
    private static int c = 0;
    private ArrayList<Integer> weekDaysArrayList;

    public AlternativeWeeklyRecyclerItemAdapter(Context context, List<String> items, List<TimelessReminderEntity> reminderDataList, LocalDate localDate) {
        count = 0;
        c = 7;
        this.localDate = localDate;
        this.context = context;
        this.items = items;
        this.reminderDataList = reminderDataList;
        if (getItemCount() == 0) {
            WeeklyRemindersFragment.removeProgressBar();
            WeeklyRemindersFragment.setEmptyContainerImage(true);
        }
    }

    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public AlternativeWeeklyRecyclerItemAdapter.AlternativeCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlternativeWeeklyRecyclerItemAdapter.AlternativeCustomViewHolder(LayoutInflater.from(context).inflate(R.layout.daily_alternative_item_view, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlternativeCustomViewHolder holder, int position, @NonNull List<Object> payloads) {
        holder.itemContainer.removeAllViews();
        onBindViewHolder(holder, position);
    }

    @Override
    public void onBindViewHolder(@NonNull AlternativeWeeklyRecyclerItemAdapter.AlternativeCustomViewHolder holder, final int position) {
        holder.date.setText(holder.dateTitle[holder.getAbsoluteAdapterPosition()]);
        holder.dayOfWeek.setText(Utils.getWeekDay(position));
        for (TimelessReminderEntity reminder : reminderDataList) {
            boolean [] days = new boolean[] {reminder.mon, reminder.tue, reminder.wed, reminder.thu, reminder.fri, reminder.sat, reminder.sun};
            for (int i = 0; i < 7; i++) {
                if (days[i]) {
                    if (position == i) {
                        @SuppressLint("InflateParams") FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.element_of_alternative_item_view, null, false);
                        frameLayout.setId(reminder.count);
                        TextView title = frameLayout.findViewById(R.id.item);
                        title.setText(reminder.title);
                        holder.itemContainer.addView(frameLayout);
                        break;
                    }
                }
            }
        }
    }
    @Override
    public int getItemCount() {
        return c;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    class AlternativeCustomViewHolder extends RecyclerView.ViewHolder {
        private int monthDay, month;
        private int[] weekDays;
        private String [] dateTitle = new String[7];
        private TextView date, dayOfWeek;
        private LinearLayout itemContainer;

        private AlternativeCustomViewHolder(final View itemView) {
            super(itemView);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            itemView.setLayoutParams(params);
            date = itemView.findViewById(R.id.rem_title);
            dayOfWeek = itemView.findViewById(R.id.time);
            itemContainer = itemView.findViewById(R.id.item_containter);
            if (weekDays == null) {
                weekDays = new int[7];
                monthDay = localDate.getDayOfMonth() - (localDate.getDayOfWeek().getValue()-1);
                month = localDate.getMonthValue();
                for (int i = 0; i < 7; i++) {
                    weekDays[i] = monthDay;
                    dateTitle[i] = monthDay + "." + month;
                    if (monthDay + 1 > localDate.getMonth().maxLength()) {
                        System.out.println(localDate.getMonth().maxLength() + " " + month);
                        monthDay = 1;
                        month = (month == 12) ? 1 : month++;
                    } else {
                        monthDay++;
                    }
                }
            }

        }
    }
    public void update(String name, TimelessReminderEntity reminderEntity) {
        reminderDataList.add(reminderEntity);
        items.add(name);
        notifyDataSetChanged();
        ItemTouchCallbacks.setAltWeekAdapter(this);
    }
    void remove(TimelessReminderEntity obj) {
        System.out.println(items);
        reminderDataList.remove(items.indexOf(obj.title));
        items.remove(obj.title);
        notifyItemRangeChanged(0, 7, weekDaysArrayList);
    }

}