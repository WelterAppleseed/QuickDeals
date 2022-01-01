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
import com.example.quickdeals.utils.states.States;

import org.threeten.bp.LocalDate;

import java.util.List;


public class AlternativeRecyclerItemAdapter extends RecyclerView.Adapter<AlternativeRecyclerItemAdapter.AlternativeCustomViewHolder> {
    private Context context;
    public List<String> items;
    public int count, next, changeInt, newDataInt, restorePosition, purposeCode;
    private View item;
    private ReminderDao dao;
    private static int reminderDataListLength;
    private  AlternativeRecyclerItemAdapter.AlternativeCustomViewHolder holdere;
    private List<ReminderEntity> reminderDataList;
    private LocalDate localDate;
    private static FragmentManager fragmentManager;
    private static int c = 0;

    public AlternativeRecyclerItemAdapter(Context context, ReminderDao dao, List<String> items, List<ReminderEntity> reminderDataList, LocalDate localDate) {
        count = 0;
        c = 7;
        this.localDate = localDate;
        this.context = context;
        this.items = items;
        this.dao = dao;
        this.reminderDataList = reminderDataList;
    }

    @NonNull
    @Override
    public AlternativeRecyclerItemAdapter.AlternativeCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new AlternativeCustomViewHolder(LayoutInflater.from(context).inflate(R.layout.daily_alternative_item_view, null, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlternativeRecyclerItemAdapter.AlternativeCustomViewHolder holder, final int position) {
        holder.date.setText(holder.dateTitle[holder.getAbsoluteAdapterPosition()]);
        holder.dayOfWeek.setText(States.getWeekDay(holder.weekValue[holder.getAbsoluteAdapterPosition()]));
        for (ReminderEntity reminder : reminderDataList) {
            System.out.println(holder.weekDays[holder.getAbsoluteAdapterPosition()] + " " + reminder.day);
            if (holder.weekDays[holder.getAbsoluteAdapterPosition()] == reminder.day && holder.itemContainer.findViewById(reminder.count) == null) {
                FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(context).inflate(R.layout.element_of_alternative_item_view, null, false);
                frameLayout.setId(reminder.count);
                TextView title = frameLayout.findViewById(R.id.item);
                title.setText(reminder.title);
                holder.itemContainer.addView(frameLayout);
            }
        }
        if(holder.getAbsoluteAdapterPosition() == 6) {
            holder.setIsRecyclable(false);
        }
    }
    @Override
    public int getItemCount() {
        return c;
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
                monthDay = localDate.getDayOfMonth();
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
            c++;
        }
    }

}