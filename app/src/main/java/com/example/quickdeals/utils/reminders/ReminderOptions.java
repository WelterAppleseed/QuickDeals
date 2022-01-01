package com.example.quickdeals.utils.reminders;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.quickdeals.R;
import com.example.quickdeals.daily.actions.ReminderActions;
import com.example.quickdeals.daily.adapter_and_activity.TypeOfActivityAdapter;
import com.example.quickdeals.daily.dialog.notifications.AlarmManagerBroadcastReceiver;
import com.example.quickdeals.database.temporary.ReminderData;
import com.example.quickdeals.database.temporary.dao.ReminderDao;
import com.example.quickdeals.database.temporary.entity.ReminderEntity;
import com.example.quickdeals.utils.CompoundListeners;
import com.example.quickdeals.utils.states.States;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.DateTimeUtils;
import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class ReminderOptions extends Activity {
    private static final int NOTIFICATION_REMINDER = 01;
    public FloatingActionButton backButton;
    private Spinner spinner;
    private TypeOfActivityAdapter typeOfActivityAdapter;
    private TimePicker timePicker;
    private TextView timeAndDateTextView;
    private EditText h, m,  reminderTitle, reminderDescription;;
    private Button cancelB, okB;
    private String month, fullDate;
    private int year, day;
    private CompoundListeners listeners;
    private static Context context;
    private Switch alarmSwitch, repeatSwitch;
    private ReminderActions reminderActions;
    private MaterialCalendarView calendarView;
    private Vibrator v;
    public static boolean isFirstItem;
    private static ReminderDao dao;
    private static RecyclerItemAdapter adapter;
    private static int iconNumber = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_reminder);
        initializeContent();
      /*  h = (EditText) findViewById(R.id.hours_picker);
        m = (EditText) findViewById(R.id.minutes_picker);*/
      /*  listeners.setClickTimeListener(h, m);
        listeners.setClickTimeListener(m, null);*/

    }

    public static void setIsFirstItem(boolean isFirstItem) {
        ReminderOptions.isFirstItem = isFirstItem;
    }

    public static void setDao(ReminderDao dao) {
        ReminderOptions.dao = dao;
    }

    public static void setContext(Context context) {
        ReminderOptions.context = context;
    }

    public static void setAdapter(RecyclerItemAdapter adapter) {
        ReminderOptions.adapter = adapter;
    }

    private void initializeContent() {
        Calendar c = Calendar.getInstance();
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        reminderActions = new ReminderActions(context);
        listeners = new CompoundListeners();
        spinner = (Spinner) findViewById(R.id.notif_time_sp);
        typeOfActivityAdapter = new TypeOfActivityAdapter(getApplicationContext());
        spinner.setAdapter(typeOfActivityAdapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                iconNumber = position==0? 0: position-1;
                System.out.println(iconNumber);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        alarmSwitch = findViewById(R.id.alarm_sw);
        repeatSwitch = findViewById(R.id.rep_sw);
        reminderTitle = findViewById(R.id.reminder_title);
        reminderDescription = findViewById(R.id.reminder_desc);
        timeAndDateTextView = (TextView) findViewById(R.id.time_and_date_textview);
        year = c.get(Calendar.YEAR);
        month = States.getMonth(c.get(Calendar.MONTH), false);
        day = c.get(Calendar.DAY_OF_MONTH);
        fullDate = year + ", " + month + " " + day + ", ";
        calendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        calendarInit(calendarView);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                year = date.getYear();
                month = States.getMonth(date.getMonth()-1, false);
                day = date.getDay();
                fullDate = year + ", " + month + " " + day;
                StringBuilder stringBuilder = new StringBuilder(timeAndDateTextView.getText()).replace(0, timeAndDateTextView.getText().toString().lastIndexOf(", "), fullDate);
                timeAndDateTextView.setText(stringBuilder);
            }
        });
        calendarView.setSelectedDate(calendarView.getMinimumDate());
        timePicker = (TimePicker) findViewById(R.id.time_picker);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timeAndDateTextView.setText(fullDate + ", " +String.format("%02d", timePicker.getHour()) + ":" + String.format("%02d", timePicker.getMinute()));
            }
        });
        timePicker.setIs24HourView(true);
        timeAndDateTextView.setText(fullDate + String.format("%02d", timePicker.getHour()) + ":" + String.format("%02d", timePicker.getMinute()));
        backButton = (FloatingActionButton) findViewById(R.id.back_fl_b);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        okB = (Button) findViewById(R.id.ok_b);
        okB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               addItem(reminderTitle, reminderDescription, iconNumber,  year, calendarView.getSelectedDate().getMonth(), day, timePicker.getHour(), timePicker.getMinute(), repeatSwitch.isChecked(), alarmSwitch.isChecked());
            }
        });

    }
    private void addItem(EditText text, EditText desc, int iconNumber, int year, int month, int day, int hours, int minutes, boolean repeat, boolean alarm) {
        String title = text.getText().toString();
        if (ReminderEntity.getEntity(dao, title) != null) {
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            Log.e("NamingError: ", "This title already exist.");
        }else {
            Log.i("AddItem: ", "Create item container.");
            ReminderEntity.addToDatabase(new ReminderData(null, title, desc.getText().toString(),iconNumber, year, month, day, hours, minutes, repeat, alarm), dao);
            Log.i("AddItem: ", "Adding item to adapter list.");
            List<ReminderEntity> reminderEntityList = ReminderEntity.getAll(dao);
            adapter.setReminderDataList(reminderEntityList);
            adapter.add(title);
            Log.i("AddItem: ", "Item is added.");
            addNotification(title, alarmSwitch.isChecked());
            finish();

        }
        /*Log.i("AddItem: ", "Start adding item");
        String gottenText = text.getText().toString();
        if (!prf.getString(gottenText + " parameters", "").equals("") || gottenText.equals("")) {
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            Log.e("NamingError: ", "This title already exist.");
        } else {
            final ConstraintLayout layout = reminderActions.createContainer(context);
            Log.i("AddItem: ", "Setting layout params");
            final TextView itemTitle = (TextView) layout.findViewById(R.id.drag_item);
            itemTitle.setText(gottenText);
            List<String> titles = Paper.book("reminders_titles").getAllKeys();
            recItAdapter.count = 0;
            recItAdapter.notifyItemInserted(titles.size());
            SavedReminder reminder = new SavedReminder(context, titles, gottenText, (SwipeLayout) layout.findViewById(R.id.swipe_layout), alarmSwitch.isChecked(), repeatSwitch.isChecked(), timePicker.getHour(), timePicker.getMinute(), (calendarView.getSelectedDate() != null) ? calendarView.getSelectedDate() : calendarView.getMinimumDate());
            reminder.save();
            Log.i("AddItem: ", "Item is added.");
            Log.i("AddItem: ", "Item list: " + titles);
            addNotification(gottenText, alarmSwitch.isChecked());
            finish();
        }*/
    }
    private ArrayList<Long> convertDate(int hours, int minutes, CalendarDay calendarDay) {
        Log.i("ConvertDate: ", "Starting convertDate");
        String stringDate = calendarDay.getDate().toString() + " " + String.format("%02d", hours) + " " + String.format("%02d", minutes);
        long dateToLong = 0;
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(stringDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH mm"));
            dateToLong = localDateTime
                    .atZone(ZoneId.systemDefault())
                    .toInstant().toEpochMilli();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("ConvertDate: ", "Start adding dates to list");
        ArrayList<Long> longList = new ArrayList<>();
        longList.add(0, dateToLong - 10800);
        longList.add(1, dateToLong - 3600);
        longList.add(2, dateToLong);
        Log.i("ConvertDate: ", "Returning list");
        return longList;
    }

    private void addNotification(String contentTitle, boolean setAlarmIsChecked) {
        int hours = timePicker.getHour();
        int minutes = timePicker.getMinute();
        CalendarDay selectedDay = calendarView.getSelectedDate();
        Log.i("AddNotif: ", "Start set alarms to notifications");
        ArrayList<Long> longList = convertDate(hours, minutes, (selectedDay != null) ? selectedDay : CalendarDay.today());
        Intent notificationIntent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        notificationIntent.putExtra("notif_time_arr", longList);
        notificationIntent.putExtra("content_title", contentTitle);
        notificationIntent.putExtra("alarm_b", setAlarmIsChecked);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_REMINDER, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, longList.get(2), pendingIntent);
        Log.i("AddNotif: ", "Notification is added");
    }
    public void calendarInit(MaterialCalendarView calendarView) {
        Log.i("CalendarInit: ", "Start initializing a calendar");
        calendarView.setTopbarVisible(false);
        int endYear = CalendarDay.today().getYear();
        int endMonth = CalendarDay.today().getMonth();
        int endDay = CalendarDay.today().getDay() + 6;
        CalendarDay toDay = CalendarDay.today();
        Calendar c = Calendar.getInstance();
        Date date = DateTimeUtils.toDate(toDay.getDate().atStartOfDay(ZoneId.systemDefault()).toInstant());
        c.setTime(date);
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);
        Calendar mycal = new GregorianCalendar(LocalDate.now().getYear(), LocalDate.now().getMonthValue(), CalendarDay.today().getDay());
        Log.i("CalendarInit: ", "Start checking on OutOfBounce");
        if (endDay > LocalDate.now().lengthOfMonth()) {
            if (CalendarDay.today().getMonth() + 1 == Calendar.JANUARY) {
                endYear = CalendarDay.today().getYear() + 1;
            }
            endMonth = CalendarDay.today().getMonth() + 1;
            endDay = CalendarDay.today().getDay() + 6 - LocalDate.now().lengthOfMonth();
        }
        Log.i("CalendarInit: ", "Start set properties to calendar");
        CalendarDay endDate = CalendarDay.from(endYear, endMonth, endDay);
        calendarView.state().edit()
                .setMinimumDate(toDay)
                .setMaximumDate(endDate)
                .setFirstDayOfWeek((DayOfWeek.of((dayOfWeek-1) == 0? 7: dayOfWeek-1)))
                .commit();
        calendarView.setTopbarVisible(false);
        Log.i("CalendarInit: ", "Calendar is initialized");
    }
}
