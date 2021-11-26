package com.example.quickdeals;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.quickdeals.daily.actions.ReminderActions;
import com.example.quickdeals.daily.adapter_and_activity.TypeOfActivityAdapter;
import com.example.quickdeals.daily.dialog.notifications.AlarmManagerBroadcastReceiver;
import com.example.quickdeals.database.ReminderData;
import com.example.quickdeals.database.dao.ReminderDao;
import com.example.quickdeals.database.entity.ReminderEntity;
import com.example.quickdeals.utils.CompoundListeners;
import com.example.quickdeals.utils.Utils;
import com.example.quickdeals.utils.reminders.RecyclerItemAdapter;
import com.example.quickdeals.utils.reminders.ReminderOptions;
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


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReminderReview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReminderReview extends Fragment {
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
    private TextView timeAndDateTextView, remSTT;
    private EditText h, m, reminderTitle, reminderDescription;
    private ImageView calendarImgV, iconView;
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
    private static RecyclerItemAdapter adapter;
    private static int iconNumber = 0;
    private StringBuilder stringBuilder;
    private Calendar c;
    private Bundle bundle;
    private int position;
    public static ReminderDao dao;
    private ScrollView scrollView;
    private DatePickerDialog calendarDialog;
    private  ArrayList<Integer> time;
    private LocalDate localDate;

    private static boolean isExist = false;

    public ReminderReview() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReminderDCC.
     */
    // TODO: Rename and change types and number of parameters
    public static ReminderReview newInstance(@Nullable String title, @Nullable int changeInt, @Nullable ReminderDao dao, @Nullable View itemView, @Nullable RecyclerItemAdapter adapter, boolean isExist) {
        ReminderReview fragment = new ReminderReview();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public void setNewArgs(Bundle bundle, int position) {
        this.position = position;
        this.bundle = bundle;
        Log.i("ReminderReview", "New args are inputed...");
        remSTT.setText(bundle.getString("title"));
        iconView.setImageResource(States.getIcon(bundle.getInt("icon")));
        alarmSwitch.setChecked(bundle.getBoolean("alarm"));
        repeatSwitch.setChecked(bundle.getBoolean("repeat"));
        reminderTitle.setText(bundle.getString("title"));
        reminderDescription.setText(bundle.getString("note"));
        time = bundle.getIntegerArrayList("time");
        yearV = time.get(0);
        yearCV = yearV;
        monthV = time.get(1);
        monthCV = monthV-1;
        month = States.getMonth(monthV, false);
        dayV = time.get(2);
        dayCV = dayV;
        fullDate = yearV + ", " + month + " " + dayV + ", ";
        localDate = LocalDate.of(yearV, monthV, dayV);
        calendarView.setSelectedDate(CalendarDay.from(yearV, monthV, dayV));
        timeAndDateTextView.setText(fullDate + String.format("%02d", timePicker.getHour()) + ":" + String.format("%02d", timePicker.getMinute()));
        calendarInit(calendarView, localDate);
        timePicker.setHour(time.get(3));
        timePicker.setMinute(time.get(4));
        timeAndDateTextView.setText(fullDate + String.format("%02d", timePicker.getHour()) + ":" + String.format("%02d", timePicker.getMinute()));
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
        View view = inflater.inflate(R.layout.fragment_reminder_d_c_c, container, false);
        initializeContent(view, isExist);
        return view;
    }

    public static void setIsFirstItem(boolean isFirstItem) {
        ReminderOptions.isFirstItem = isFirstItem;
    }

    public static void setDao(ReminderDao dao) {
        ReminderReview.dao = dao;
    }

    public static void setContext(Context context) {
        ReminderReview.context = context;
    }

    public static void setAdapter(RecyclerItemAdapter adapter) {
        ReminderReview.adapter = adapter;
    }

    private void initializeContent(final View view, boolean isExist) {
        c = Calendar.getInstance();
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        scrollView = view.findViewById(R.id.scroll_rem_t);
        remSTT = view.findViewById(R.id.rem_st_t);
        iconView = view.findViewById(R.id.notif_type_ic);
        reminderActions = new ReminderActions(context);
        listeners = new CompoundListeners();
        spinner = (Spinner) view.findViewById(R.id.notif_time_sp);
        typeOfActivityAdapter = new TypeOfActivityAdapter(context);
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
        alarmSwitch = view.findViewById(R.id.alarm_sw);
        repeatSwitch = view.findViewById(R.id.rep_sw);
        reminderTitle = view.findViewById(R.id.reminder_title);
        reminderDescription = view.findViewById(R.id.reminder_desc);
        timeAndDateTextView = (TextView) view.findViewById(R.id.time_and_date_textview);
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                System.out.println(date.getDate());
                yearV = date.getYear();
                yearCV = date.getYear();
                monthCV = date.getMonth()-1;
                dayV = date.getDay();
                dayCV = date.getDay();
                StringBuilder newLabelValue = Utils.updateLabel(timeAndDateTextView, date, fullDate);
                fullDate = newLabelValue.substring(0, newLabelValue.length()-5);
                timeAndDateTextView.setText(newLabelValue);
            }
        });
        calendarImgV = view.findViewById(R.id.calendar_img_v);
        calendarImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                yearV = year;
                                monthV = month + 1;
                                dayV = day;
                                System.out.println(yearCV + " " + monthCV + " " + dayCV);
                                if (day <= calendarView.getMaximumDate().getDay() && day > calendarView.getSelectedDate().getDay() && monthV == calendarView.getSelectedDate().getMonth() && year == calendarView.getSelectedDate().getYear()) {
                                    calendarView.setSelectedDate(CalendarDay.from(year, monthV, day));
                                    StringBuilder newLabelValue = Utils.updateLabel(timeAndDateTextView, CalendarDay.from(localDate), fullDate);
                                    fullDate = newLabelValue.substring(0, newLabelValue.length()-5);
                                    timeAndDateTextView.setText(newLabelValue);

                                } else {
                                    localDate = LocalDate.of(year, monthV, dayV);
                                    int endYear = localDate.getYear();
                                    int endMonth = localDate.getMonthValue();
                                    int endDay = localDate.getDayOfMonth() + 6;
                                    if (endDay > localDate.lengthOfMonth()) {
                                        if (endMonth - 1 == Calendar.DECEMBER) {
                                            endMonth = 1;
                                            endYear = localDate.getYear() + 1;
                                        } else {
                                            endMonth += 1;
                                        }
                                        endDay -= localDate.lengthOfMonth();
                                    }
                                    CalendarDay selectedDay = CalendarDay.from(year, monthV, dayV);
                                    CalendarDay endDate = CalendarDay.from(endYear, endMonth, endDay);
                                    calendarView.state().edit()
                                            .setMinimumDate(selectedDay)
                                            .setMaximumDate(endDate)
                                            .setFirstDayOfWeek(localDate.getDayOfWeek())
                                            .commit();
                                    calendarView.setSelectedDate(CalendarDay.from(localDate));
                                    StringBuilder newLabelValue = Utils.updateLabel(timeAndDateTextView, CalendarDay.from(localDate), fullDate);
                                    fullDate = newLabelValue.substring(0, newLabelValue.length()-5);
                                    timeAndDateTextView.setText(newLabelValue);
                                    yearCV = year;
                                    monthCV = month;
                                    dayCV = day;
                                }
                            }
                        }, yearCV, monthCV, dayCV).show();

            }
        });
        timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String hoursAndMinutes = String.format("%02d", timePicker.getHour()) + ":" + String.format("%02d", timePicker.getMinute());
                stringBuilder = new StringBuilder(timeAndDateTextView.getText()).replace(fullDate.length(), timeAndDateTextView.getText().length(), hoursAndMinutes);
                timeAndDateTextView.setText(stringBuilder);
            }
        });
        backButton = (FloatingActionButton) view.findViewById(R.id.back_fl_b);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShablonFragment.showOrHideFragment(getParentFragmentManager(), ReminderReview.this, false);
            }
        });
        okB = (Button) view.findViewById(R.id.ok_b);
        okB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeItem(reminderTitle, reminderDescription, iconNumber, yearV, calendarView.getSelectedDate().getMonth(), dayV, timePicker.getHour(), timePicker.getMinute(), repeatSwitch.isChecked(), alarmSwitch.isChecked());
            }
        });
        cancelB = (Button) view.findViewById(R.id.cancel_b);
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShablonFragment.showOrHideFragment(getParentFragmentManager(), ReminderReview.this, false);
            }
        });

    }

    private void changeItem(EditText titleET, EditText descriptionET, int iconNumber, int year, int month, int day, int hours, int minutes, boolean repeat, boolean alarm) {
        String title = titleET.getText().toString();
        if (title.equals("")) {
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            Log.e("NamingError: ", "This title already exist or forbidden.");
        } else {
            Log.i("ChangeItem: ", "Get item from database.");
            ReminderData item = ReminderEntity.getEntity(dao, bundle.getString("title"));
            Log.i("ChangeItem: ", "Update item.");
            item.setTitle(title);
            item.setDesc(descriptionET.getText().toString());
            item.setIcon(iconNumber);
            item.setYear(year);
            item.setMonth(month);
            item.setDay(day);
            item.setHours(hours);
            item.setMinutes(minutes);
            item.setRepeat(repeat);
            item.setAlarm(alarm);
            Log.i("ChangeItem: ", "Send item to database back.");
            adapter.change(item, position);
            Log.i("ChangeItem: ", "Item is changed.");
            ShablonFragment.showOrHideFragment(getParentFragmentManager(), ReminderReview.this, false);
        }
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

    public void calendarInit(MaterialCalendarView calendarView, LocalDate localDate) {
        Log.i("CalendarInit: ", "Start initializing a calendar");
        calendarView.setTopbarVisible(false);
        int endYear = localDate.getYear();
        int endMonth = localDate.getMonthValue();
        int endDay = localDate.getDayOfMonth() + 6;
        Log.i("CalendarInit: ", "Start checking on OutOfBounce");
        if (endDay > localDate.lengthOfMonth()) {
            if (endMonth-1 == Calendar.DECEMBER) {
                endMonth = 1;
                endYear = localDate.getYear() + 1;
            } else {
                endMonth+=1;
            }
            endDay -= localDate.lengthOfMonth();
        }
        Log.i("CalendarInit: ", "Start set properties to calendar");
        CalendarDay selectedDay = CalendarDay.from(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        CalendarDay endDate = CalendarDay.from(endYear, endMonth, endDay);
        calendarView.setSelectedDate(localDate);
        calendarView.state().edit()
                .setMinimumDate(selectedDay)
                .setMaximumDate(endDate)
                .setFirstDayOfWeek(localDate.getDayOfWeek())
                .commit();
        calendarView.setSelectedDate(localDate);
        Log.i("CalendarInit: ", "Calendar is initialized");
    }
}