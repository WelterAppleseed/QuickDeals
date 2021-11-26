package com.example.quickdeals.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.quickdeals.R;
import com.example.quickdeals.daily.actions.ReminderActions;
import com.example.quickdeals.daily.adapter_and_activity.TypeOfActivityAdapter;
import com.example.quickdeals.database.ReminderData;
import com.example.quickdeals.database.dao.ReminderDao;
import com.example.quickdeals.database.entity.ReminderEntity;
import com.example.quickdeals.utils.reminders.RecyclerItemAdapter;
import com.example.quickdeals.utils.states.States;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.ArrayList;
import java.util.Calendar;

public class ReviewDialogFragment extends DialogFragment implements View.OnClickListener {
    private static final String ICON = "icon";
    private static final String TITLE = "title";
    private static final String DESC = "note";
    private static final String TIME = "time";
    private static final String REPEAT = "repeat";
    private static final String ALARM = "alarm";


    public FloatingActionButton backButton;
    private Spinner spinner;
    private TypeOfActivityAdapter typeOfActivityAdapter;
    private TimePicker timePicker;
    private TextView timeAndDateTextView;
    private EditText h, m, reminderTitle, reminderDescription;
    ;
    private Button cancelB;
    private String month, fullDate;
    private int year, day;
    private CompoundListeners listeners;
    private static Context context;
    private Switch alarmSwitch, repeatSwitch;
    private ReminderActions reminderActions;
    private MaterialCalendarView calendarView;
    private Vibrator v;
    private static boolean isFirstItem;
    private static int iconNumber = 0;
    private StringBuilder stringBuilder;
    private Calendar c;

    private String title, description;
    private int icon;
    private ArrayList<Integer> time;
    private boolean repeat, alarm;
    private ImageView iconImageView;
    private TextView timeTVState, titleTVState;
    private EditText titleEVState, descEVState;
    private Button informOnceTV, informBCTV, alarmYTV, alarmNTV, okB, backB;
    private static View itemView;
    private static RecyclerItemAdapter adapter;
    public static ReminderDao dao;
    private static int changeInt;

    public ReviewDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static ReviewDialogFragment newInstance(String title, int changeInt, ReminderDao dao, View itemView, RecyclerItemAdapter adapter) {
        ReviewDialogFragment.changeInt = changeInt;
        ReviewDialogFragment.dao = dao;
        ReviewDialogFragment.itemView = itemView;
        ReviewDialogFragment.adapter = adapter;
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
        ReviewDialogFragment review = new ReviewDialogFragment();
        review.setArguments(remBundles);
        return review;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reminder_d_c_c, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        iconImageView = view.findViewById(R.id.notif_type_ic);
        titleTVState = view.findViewById(R.id.rem_st_t);
        titleEVState = view.findViewById(R.id.reminder_title);
        descEVState = view.findViewById(R.id.reminder_desc);
        timeTVState = view.findViewById(R.id.time);
        informOnceTV = view.findViewById(R.id.inform_once_tv);
        informBCTV = view.findViewById(R.id.inform_bc_tv);
        alarmNTV = view.findViewById(R.id.alarm_n);
        alarmYTV = view.findViewById(R.id.alarm_y);
        okB = view.findViewById(R.id.ok_b);
        backB = view.findViewById(R.id.back_b);
        //blow variables
        title = getArguments().getString(TITLE);
        description = getArguments().getString(DESC);
        icon = getArguments().getInt(ICON);
        time = getArguments().getIntegerArrayList(TIME);
        repeat = getArguments().getBoolean(REPEAT);
        alarm = getArguments().getBoolean(ALARM);
        iconImageView.setImageResource(States.getIcon(icon));
        titleEVState.setText(title);
        descEVState.setText(description);
        timeTVState.setText(States.getTime(time.get(0), time.get(2), States.getMonth(time.get(1), true), time.get(3), time.get(4), false));
        informOnceTV.setOnClickListener(this);
        informBCTV.setOnClickListener(this);
        alarmYTV.setOnClickListener(this);
        alarmNTV.setOnClickListener(this);
        if (repeat) {
            informBCTV.performClick();
        } else {
            informOnceTV.performClick();
        }
        if (alarm) {
            alarmYTV.performClick();
        } else {
            alarmNTV.performClick();
        }
        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        timeTVState.requestFocus();
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        okB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReminderData changedData = ReminderEntity.getEntity(dao, title);
                changedData.setTitle(titleEVState.getText().toString());
                changedData.setDesc(descEVState.getText().toString());
                adapter.change(changedData, changeInt);
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == informOnceTV) {
            informOnceTV.setTextColor(getResources().getColor(R.color.colorPressedB));
            informBCTV.setTextColor(Color.GRAY);
            informOnceTV.setPressed(true);
            informBCTV.setPressed(false);
        }
        if (v == informBCTV) {
            informOnceTV.setTextColor(Color.GRAY);
            informBCTV.setTextColor(getResources().getColor(R.color.colorPressedB));
            informOnceTV.setPressed(false);
            informBCTV.setPressed(true);
        }
        if (v == alarmYTV) {
            alarmYTV.setTextColor(getResources().getColor(R.color.colorPressedB));
            alarmNTV.setTextColor(Color.GRAY);
            alarmYTV.setPressed(true);
            alarmNTV.setPressed(false);
        }
        if (v == alarmNTV) {
            alarmYTV.setTextColor(Color.GRAY);
            alarmNTV.setTextColor(getResources().getColor(R.color.colorPressedB));
            alarmYTV.setPressed(false);
            alarmNTV.setPressed(true);
        }
    }
}
