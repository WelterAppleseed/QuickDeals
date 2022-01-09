package com.example.quickdeals.utils.reminders;

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
import com.example.quickdeals.database.temporary.dao.ReminderDao;
import com.example.quickdeals.database.temporary.entity.ReminderEntity;
import com.example.quickdeals.database.timeless.dao.TimelessReminderDao;
import com.example.quickdeals.database.timeless.entity.TimelessReminderEntity;
import com.example.quickdeals.study.StudyFragment;
import com.example.quickdeals.utils.Listeners;
import com.example.quickdeals.utils.states.States;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class AlternativeWeeklyRecyclerItemAdapter extends RecyclerView.Adapter<AlternativeWeeklyRecyclerItemAdapter.AlternativeCustomViewHolder> {
    private Context context;
    public List<String> items;
    public int count, next, changeInt, newDataInt, restorePosition, purposeCode;
    private View item;
    private TimelessReminderDao tdao;
    private static int reminderDataListLength;
    private List<TimelessReminderEntity> reminderDataList;
    private LocalDate localDate;
    private static FragmentManager fragmentManager;
    private static int c = 0;
    private ArrayList<Integer> weekDaysArrayList;

    public AlternativeWeeklyRecyclerItemAdapter(Context context, TimelessReminderDao tdao, List<String> items, List<TimelessReminderEntity> reminderDataList, LocalDate localDate) {
        count = 0;
        c = 7;
        this.localDate = localDate;
        this.context = context;
        this.items = items;
        this.tdao = tdao;
        this.reminderDataList = reminderDataList;
        if (getItemCount() == 0) {
            StudyFragment.removeProgressBar();
            StudyFragment.setEmptyContainerImage(true);
        }
    }

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
        holder.dayOfWeek.setText(States.getWeekDay(position));
        for (TimelessReminderEntity reminder : reminderDataList) {
            boolean [] days = new boolean[] {reminder.mon, reminder.tue, reminder.wed, reminder.thu, reminder.fri, reminder.sat, reminder.sun};
            for (int i = 0; i < 7; i++) {
                if (days[i]) {
                    if (position == i) {
                        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.element_of_alternative_item_view, null, false);
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
        private int[] weekDays, weekValue;
        private String [] dateTitle = new String[7];
        private TextView date, dayOfWeek;
        private LinearLayout itemContainer;
        private FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        private AlternativeCustomViewHolder(final View itemView) {
            super(itemView);
            itemView.setLayoutParams(params);
            date = itemView.findViewById(R.id.rem_title);
            dayOfWeek = itemView.findViewById(R.id.time);
            itemContainer = itemView.findViewById(R.id.item_containter);
            if (weekDays == null) {
                weekDays = new int[7];
                int q = -1;
                weekValue = new int[7];
                monthDay = localDate.getDayOfMonth() - (localDate.getDayOfWeek().getValue()-1);
                month = localDate.getMonthValue();
                for (int i = 0; i < 7; i++) {
                    weekDays[i] = monthDay;
                    weekValue[i] = (localDate.getDayOfWeek().getValue()+q > 6) ? localDate.getDayOfWeek().getValue()+q-7 : localDate.getDayOfWeek().getValue()+q;
                    dateTitle[i] = monthDay + "." + month;
                    if (monthDay + 1 > localDate.getMonth().maxLength()) {
                        System.out.println(localDate.getMonth().maxLength() + " " + month);
                        monthDay = 1;
                        month = (month == 12) ? 1 : month++;
                    } else {
                        monthDay++;
                    }
                    q++;
                }
            }

        }
    }
    public void update(String name, TimelessReminderEntity reminderEntity) {
        reminderDataList.add(reminderEntity);
        items.add(name);
        notifyDataSetChanged();
        Listeners.setAltWeekAdapter(this);
    }
    public void remove(TimelessReminderEntity obj) {
        reminderDataList.remove(items.indexOf(obj.title));
        items.remove(obj.title);
        notifyItemRangeChanged(0, 7, weekDaysArrayList);
    }

}