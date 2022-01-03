package com.example.quickdeals.study;

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
import com.example.quickdeals.ShablonFragment;
import com.example.quickdeals.daily.actions.ReminderActions;
import com.example.quickdeals.daily.adapter_and_activity.TypeOfActivityAdapter;
import com.example.quickdeals.daily.dialog.notifications.AlarmManagerBroadcastReceiver;
import com.example.quickdeals.database.timeless.TimelessReminderData;
import com.example.quickdeals.database.timeless.dao.TimelessReminderDao;
import com.example.quickdeals.database.timeless.entity.TimelessReminderEntity;
import com.example.quickdeals.study.adapter.GridRecyclerViewAdapter;
import com.example.quickdeals.utils.CompoundListeners;
import com.example.quickdeals.utils.reminders.RecyclerItemAdapter;
import com.example.quickdeals.utils.states.States;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import org.threeten.bp.LocalDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.example.quickdeals.daily.dialog.notifications.NotificationService.context;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimelessReminderDCC#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelessReminderReview extends Fragment implements View.OnClickListener{
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
    private TextView remSTT;
    private EditText h, m, reminderTitle, reminderDescription;
    private ImageView iconView;
    private Button cancelB, okB;
    private String month, fullDate;
    private int yearV, monthV, dayV, yearCV, monthCV, dayCV;
    private CompoundListeners listeners;
    private static Context context;
    private Switch alarmSwitch, repeatSwitch;
    private ReminderActions reminderActions;
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
    private Bundle bundle;
    private int position;
    private boolean [] weekdays;
    private ArrayList<Integer> time;


    public TimelessReminderReview() {
        // Required empty public constructor
    }

    public static void setContext(Context context) {
        TimelessReminderReview.context = context;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TimelessReminderDCC.
     */
    // TODO: Rename and change types and number of parameters
    public static TimelessReminderReview newInstance() {
        TimelessReminderReview fragment = new TimelessReminderReview();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }
    public void setNewArgs(Bundle bundle, int position) {
        this.position = position;
        this.bundle = bundle;
        Log.i("TimelessReminderReview", "New args are inputed...");
        remSTT.setText(bundle.getString("title"));
        weekdays = bundle.getBooleanArray("selected_week_days");
        setSelectedWeekDays();
        iconView.setImageResource(States.getIcon(bundle.getInt("icon")));
        reminderTitle.setText(bundle.getString("title"));
        reminderDescription.setText(bundle.getString("note"));
        time = bundle.getIntegerArrayList("time");
        timePicker.setHour(time.get(0));
        timePicker.setMinute(time.get(1));
        scrollView.scrollTo(0, 0);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeless_reminder_review, container, false);
        c = Calendar.getInstance();
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        remSTT = view.findViewById(R.id.rem_st_t);
        iconView = view.findViewById(R.id.notif_type_ic);
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
                ShablonFragment.showOrHideFragment(getParentFragmentManager(), TimelessReminderReview.this, (ShablonFragment) getParentFragment(), false);
            }
        });
        okB = (Button) view.findViewById(R.id.ok_b);
        okB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateItem(reminderTitle, reminderDescription, iconNumber, monTV.getBackground() != null, tueTV.getBackground() != null, wedTV.getBackground() != null, thuTV.getBackground() != null, friTV.getBackground() != null, satTV.getBackground() != null, sunTV.getBackground() != null, timePicker.getHour(), timePicker.getMinute());
            }
        });
        cancelB = (Button) view.findViewById(R.id.cancel_b);
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShablonFragment.showOrHideFragment(getParentFragmentManager(), TimelessReminderReview.this, (ShablonFragment) getParentFragment(), false);
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
    private void updateItem(EditText titleET, EditText descriptionET, int iconNumber, boolean isMonday, boolean isTuesday, boolean isWednesday, boolean isThursday, boolean isFriday, boolean isSaturday, boolean isSunday, int hours, int minutes) {
        String title = titleET.getText().toString();
        boolean [] days =  new boolean[] {isMonday, isTuesday, isWednesday, isThursday, isFriday, isSaturday, isSunday};
        if (title.equals("")| title.equals("") | (!days[0] & !days[1] & !days[2] & !days[3] & !days[4] & !days[5] & !days[6])) {
            Log.e("NamingError:", "This title already exist or forbidden.");
        } else {
            Log.i("UpdateItem:", "Get item from database.");
            TimelessReminderData item = TimelessReminderEntity.getEntity(timelessReminderDao, bundle.getString("title"));
            Log.i("UpdateItem:", "Update item.");
            item.setTitle(title);
            item.setDesc(descriptionET.getText().toString());
            item.setIcon(iconNumber);
            item.setHours(hours);
            item.setMinutes(minutes);
            item.setMonday(isMonday);
            item.setTuesday(isTuesday);
            item.setWednesday(isWednesday);
            item.setThursday(isThursday);
            item.setFriday(isFriday);
            item.setSaturday(isSaturday);
            item.setSunday(isSunday);
            Log.i("UpdateItem:", "Send item to database back.");
            adapter.change(item, position);
            Log.i("UpdateItem:", "Item is updated.");
            ShablonFragment.showOrHideFragment(getParentFragmentManager(), TimelessReminderReview.this, (ShablonFragment) getParentFragment(),  false);
            updateTimelessNotification(item.getCount(), title, days);
        }
    }

    private void setSelectedWeekDays() {
        monTV.setBackground(weekdays[0] ?  getResources().getDrawable(R.drawable.week_day_rounded_background) : null);
        tueTV.setBackground(weekdays[1] ?  getResources().getDrawable(R.drawable.week_day_rounded_background) : null);
        wedTV.setBackground(weekdays[2] ?  getResources().getDrawable(R.drawable.week_day_rounded_background) : null);
        thuTV.setBackground(weekdays[3] ?  getResources().getDrawable(R.drawable.week_day_rounded_background) : null);
        friTV.setBackground(weekdays[4] ?  getResources().getDrawable(R.drawable.week_day_rounded_background) : null);
        satTV.setBackground(weekdays[5] ?  getResources().getDrawable(R.drawable.week_day_rounded_background) : null);
        sunTV.setBackground(weekdays[6] ?  getResources().getDrawable(R.drawable.week_day_rounded_background) : null);
    }
    private void updateTimelessNotification(int id, String contentTitle, boolean [] selectedWeekDays) {
        int hours = timePicker.getHour();
        int minutes = timePicker.getMinute();
        ArrayList<Long> days = getWeekDaysInMillis(selectedWeekDays, hours, minutes);
        Log.i("AddNotification: ", "Start set alarms to notifications");
        Intent notificationIntent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        notificationIntent.putExtra("selected_week_days", selectedWeekDays);
        notificationIntent.putExtra("content_title", contentTitle);
        notificationIntent.putExtra("notification_id", id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        for (long day : days) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, day, (long) 604800000, pendingIntent);
        }
        Log.i("AddNotification: ", "Notification is added");
    }

    private ArrayList<Long> getWeekDaysInMillis(boolean[] selectedDays, int hours, int minutes) {
        ArrayList<Long> dates = new ArrayList<>();
        LocalDate localDate = LocalDate.now();
        Calendar c = Calendar.getInstance();
        int today = localDate.getDayOfWeek().getValue();
        for (int i = 0; i < 7; i++) {
            if (selectedDays[i]) {
                int difference, hoursAndMinutes;
                if (today > i+1 || today == i+1 & c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE) >= hours * 60 + minutes) {
                    difference = (i+1) + 7 - today;
                    if (hours * 60 + minutes < c.get(Calendar.HOUR_OF_DAY) * 60 + minutes) {
                        difference-=1;
                        hoursAndMinutes = (24 * 60) - (c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE)) + hours * 60 + minutes;
                        System.out.println(hoursAndMinutes + " " + difference);
                    }
                } else {
                    difference = (i+1) - today;
                    if (hours * 60 + minutes < c.get(Calendar.HOUR_OF_DAY) * 60 + minutes) {
                        difference-=1;
                        hoursAndMinutes = (24 * 60) - (c.get(Calendar.HOUR_OF_DAY) * 60 + c.get(Calendar.MINUTE)) + hours * 60 + minutes;
                        System.out.println(hoursAndMinutes + " " + difference);
                    }
                }
                System.out.println("today is " + today + " and selected day is " + (i+1) + " difference in days " + difference + " with hours " + hours + " with minutes " + minutes);
                long date = System.currentTimeMillis() + 1000 * 60 * 60 * 24 * difference + Math.abs(c.get(Calendar.HOUR_OF_DAY) * 1000 * 60 * 60 + c.get(Calendar.MINUTE) * 1000 * 60 - (1000 * 60 * 60 * hours + 1000 * 60 * minutes)) - 60000;
                System.out.println( System.currentTimeMillis() + " and different is " + (System.currentTimeMillis() - date));
                dates.add(date);
            }
        }
        return dates;
    }

    public static void setTimelessReminderDao(TimelessReminderDao timelessReminderDao) {
        TimelessReminderReview.timelessReminderDao = timelessReminderDao;
    }

    public static void setAdapter(GridRecyclerViewAdapter adapter) {
        TimelessReminderReview.adapter = adapter;
    }
    public static void dismissNotification(PendingIntent pendingIntent) {
        if (TimelessReminderReview.alarmManager != null) {
            TimelessReminderReview.alarmManager.cancel(pendingIntent);
        }
    }
}
