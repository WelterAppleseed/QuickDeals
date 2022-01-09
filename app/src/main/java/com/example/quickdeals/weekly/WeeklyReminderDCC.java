package com.example.quickdeals.weekly;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.quickdeals.R;
import com.example.quickdeals.ContainerFragment;
import com.example.quickdeals.adapter_and_activity.TypeOfActivityAdapter;
import com.example.quickdeals.notifications.AlarmManagerBroadcastReceiver;
import com.example.quickdeals.database.timeless.TimelessReminderData;
import com.example.quickdeals.database.timeless.dao.TimelessReminderDao;
import com.example.quickdeals.database.timeless.entity.TimelessReminderEntity;
import com.example.quickdeals.utils.adapters.GridRecyclerViewAdapter;
import com.example.quickdeals.utils.adapters.AlternativeWeeklyRecyclerItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WeeklyReminderDCC#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WeeklyReminderDCC extends Fragment implements View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static final int NOTIFICATION_REMINDER = 01;
    public FloatingActionButton backButton;
    private Spinner spinner;
    private TypeOfActivityAdapter typeOfActivityAdapter;
    private TimePicker timePicker;
    private TextView timeAndDateTextView, titleTVState;
    private EditText h, m, reminderTitle, reminderDescription;
    private ImageView calendarImgV;
    private Button cancelB, okB;
    private String month, fullDate;
    private int yearV, monthV, dayV, yearCV, monthCV, dayCV;
    private static Context context;
    private Switch alarmSwitch, repeatSwitch;
    private MaterialCalendarView calendarView;
    private Vibrator v;
    private static boolean isFirstItem;
    private static GridRecyclerViewAdapter adapter;
    private static int iconNumber = 0;
    private StringBuilder stringBuilder;
    private Calendar c;
    private ScrollView scrollView;
    private LocalDate localDate;
    private static AlarmManager alarmManager;
    private TextView monTV, tueTV, wedTV, thuTV, friTV, satTV, sunTV;
    private static TimelessReminderDao timelessReminderDao;
    private ContainerFragment containerFragment;
    private static AlternativeWeeklyRecyclerItemAdapter altWeekAdapter;


    public WeeklyReminderDCC() {
        // Required empty public constructor
    }

    public static void setContext(Context context) {
        WeeklyReminderDCC.context = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TimelessReminderDCC.
     */
    // TODO: Rename and change types and number of parameters
    public static WeeklyReminderDCC newInstance() {
        WeeklyReminderDCC fragment = new WeeklyReminderDCC();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public static void setAltWeekAdapter(AlternativeWeeklyRecyclerItemAdapter altWeekAdapter) {
        WeeklyReminderDCC.altWeekAdapter = altWeekAdapter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeless_reminder_d_c_c, container, false);
        c = Calendar.getInstance();
        containerFragment = (ContainerFragment) getParentFragment();
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        scrollView = view.findViewById(R.id.scroll_rem_t);
        spinner = (Spinner) view.findViewById(R.id.notif_time_sp);
        TypeOfActivityAdapter typeOfActivityAdapter = new TypeOfActivityAdapter(context);
        spinner.setAdapter(typeOfActivityAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iconNumber = position == 0 ? 0 : position - 1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        reminderTitle = view.findViewById(R.id.reminder_title);
        reminderDescription = view.findViewById(R.id.reminder_desc);
        initWeek(view);
        timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        backButton = (FloatingActionButton) view.findViewById(R.id.back_fl_b);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContainerFragment.showOrHideFragment(getParentFragmentManager(), WeeklyReminderDCC.this, containerFragment,  false);
            }
        });
        okB = (Button) view.findViewById(R.id.ok_b);
        okB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(reminderTitle, reminderDescription, iconNumber, monTV.getBackground() != null, tueTV.getBackground() != null, wedTV.getBackground() != null, thuTV.getBackground() != null, friTV.getBackground() != null, satTV.getBackground() != null, sunTV.getBackground() != null, timePicker.getHour(), timePicker.getMinute());
            }
        });
        cancelB = (Button) view.findViewById(R.id.cancel_b);
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContainerFragment.showOrHideFragment(getParentFragmentManager(), WeeklyReminderDCC.this, containerFragment, false);
            }
        });
    return view;
    }

    @Override
    public void onClick(View v) {
        v.setBackground(v.getBackground() == null ? (Drawable) getResources().getDrawable(R.drawable.week_day_rounded_background)  : null);
    }
    private void initWeek(View view) {
        monTV = view.findViewById(R.id.monday_textview);
        monTV.setOnClickListener(this);
        tueTV = view.findViewById(R.id.tuesday_textview);
        tueTV.setOnClickListener(this);
        wedTV = view.findViewById(R.id.wednesday_textview);
        wedTV.setOnClickListener(this);
        thuTV = view.findViewById(R.id.thursday_textview);
        thuTV.setOnClickListener(this);
        friTV = view.findViewById(R.id.friday_textview);
        friTV.setOnClickListener(this);
        satTV = view.findViewById(R.id.saturday_textview);
        satTV.setOnClickListener(this);
        sunTV = view.findViewById(R.id.sunday_textview);
        sunTV.setOnClickListener(this);
    }
    private void clearWeek() {
        monTV.setBackground(null);
        tueTV.setBackground(null);
        wedTV.setBackground(null);
        thuTV.setBackground(null);
        friTV.setBackground(null);
        satTV.setBackground(null);
        sunTV.setBackground(null);
    }
    private void addItem(EditText titleET, EditText descriptionET, int iconNumber, boolean isMonday, boolean isTuesday, boolean isWednesday, boolean isThursday, boolean isFriday, boolean isSaturday, boolean isSunday, int hours, int minutes) {
        String title = titleET.getText().toString();
        String description = descriptionET.getText().toString();
        boolean [] days = new boolean[] {isMonday, isTuesday, isWednesday, isThursday, isFriday, isSaturday, isSunday};
        if (TimelessReminderEntity.getEntity(timelessReminderDao, title) != null | title.equals("") | (!days[0] & !days[1] & !days[2] & !days[3] & !days[4] & !days[5] & !days[6])) {
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            Log.e("NamingError: ", "This title already exist.");
        } else {
            Log.i("AddItem: ", "Create item container.");
            TimelessReminderEntity.addToDatabase(new TimelessReminderData(null, title, description, iconNumber, isMonday, isTuesday, isWednesday, isThursday, isFriday, isSaturday, isSunday, hours, minutes), timelessReminderDao);
            Log.i("AddItem: ", "Adding item to adapter list.");
            List<TimelessReminderEntity> reminderEntityList = TimelessReminderEntity.getAll(timelessReminderDao);
            adapter.setCourseDataArrayList(reminderEntityList);
            adapter.add(title);
            altWeekAdapter.update(title, reminderEntityList.get(reminderEntityList.size()-1));
            Log.i("AddItem: ", "Item is added.");
            //addNotification(ReminderEntity.getEntity(dao, title).getCount(), title, alarmSwitch.isChecked(), repeatSwitch.isChecked());
            addTimelessNotification(TimelessReminderEntity.getEntity(timelessReminderDao, title).getCount(), iconNumber, title, days);
            ContainerFragment.showOrHideFragment(getParentFragmentManager(), WeeklyReminderDCC.this, containerFragment,  false);
        }
    }
    private void addTimelessNotification(int id, int iconId, String contentTitle, boolean [] selectedWeekDays) {
        int hours = timePicker.getHour();
        int minutes = timePicker.getMinute();
        ArrayList<Long> days = getWeekDaysInMillis(selectedWeekDays, hours, minutes);
        Log.i("AddNotification: ", "Start set alarms to notifications");
        Intent notificationIntent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        notificationIntent.putExtra("selected_week_days", selectedWeekDays);
        notificationIntent.putExtra("content_title", contentTitle);
        notificationIntent.putExtra("notification_id", id);
        notificationIntent.putExtra("icon_id", iconId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for (long day : days) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, day, (long) 604800000, pendingIntent);
        }
        Log.i("AddNotification: ", "Notification is added");
    }
    public static void setTimelessReminderDao(TimelessReminderDao timelessReminderDao) {
        WeeklyReminderDCC.timelessReminderDao = timelessReminderDao;
    }

    public static void setAdapter(GridRecyclerViewAdapter adapter) {
        WeeklyReminderDCC.adapter = adapter;
    }
    private ArrayList<Long> getWeekDaysInMillis(boolean[] selectedDays, int hours, int minutes) {
        ArrayList<Long> dates = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        Calendar c = Calendar.getInstance();
        int today = localDate.getDayOfWeek().getValue();
        for (int i = 0; i < 7; i++) {
            if (selectedDays[i]) {
                int difference;
                long hoursAndMinutes;
                if (today > i+1 || today == i+1 & c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE) >= hours * 60 + minutes) {
                    difference = (i+1) + 7 - today;
                } else {
                    difference = (i+1) - today;
                }
                if (hours * 60 + minutes < c.get(Calendar.HOUR_OF_DAY) * 60 + minutes) {
                    difference-=1;
                    hoursAndMinutes = ((24 * 60) - (c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE)) + hours * 60 + minutes) * 1000 * 60;
                    System.out.println(hoursAndMinutes + " " + difference);
                } else {
                    hoursAndMinutes = Math.abs(c.get(Calendar.HOUR_OF_DAY) * 1000 * 60 * 60 + c.get(Calendar.MINUTE) * 1000 * 60 - (1000 * 60 * 60 * hours + 1000 * 60 * minutes));
                }
                System.out.println("today is " + today + " and selected day is " + (i+1) + " difference in days " + difference + " with hours " + hours + " with minutes " + minutes);
                long date = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * difference + hoursAndMinutes - 60000;
                System.out.println( System.currentTimeMillis() + " and different is " + (System.currentTimeMillis() - date));
                dates.add(date);
            }
        }
        return dates;
    }
    public static void dismissNotification(PendingIntent pendingIntent) {
        if (WeeklyReminderDCC.alarmManager != null) {
            WeeklyReminderDCC.alarmManager.cancel(pendingIntent);
        }
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            scrollView.scrollTo(0, 0);
            reminderTitle.setText("");
            reminderDescription.setText("");
            c = Calendar.getInstance();
            fullDate = yearV + ", " + month + " " + dayV + ", ";
            timePicker.setHour(c.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(c.get(Calendar.MINUTE));
            clearWeek();
        }
    }
}
