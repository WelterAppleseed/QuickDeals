package com.example.quickdeals.daily;

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

import com.example.quickdeals.R;
import com.example.quickdeals.ContainerFragment;
import com.example.quickdeals.adapter_and_activity.TypeOfActivityAdapter;
import com.example.quickdeals.notifications.AlarmManagerBroadcastReceiver;
import com.example.quickdeals.database.temporary.ReminderData;
import com.example.quickdeals.database.temporary.dao.ReminderDao;
import com.example.quickdeals.database.temporary.entity.ReminderEntity;
import com.example.quickdeals.utils.Utils;
import com.example.quickdeals.utils.adapters.AlternativeRecyclerItemAdapter;
import com.example.quickdeals.utils.adapters.RecyclerItemAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;
import org.threeten.bp.ZoneId;
import org.threeten.bp.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReminderReview#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReminderDCC extends Fragment {
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
    private static RecyclerItemAdapter adapter;
    private static AlternativeRecyclerItemAdapter altAdapter;
    private static int iconNumber = 0;
    private StringBuilder stringBuilder;
    private Calendar c;
    private ScrollView scrollView;
    private LocalDate localDate;
    private static AlarmManager alarmManager;
    private ContainerFragment containerFragment;

    private static int changeInt;
    private static View itemView;
    public static ReminderDao dao;

    private static boolean isExist = false;

    public ReminderDCC() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment ReminderDCC.
     */
    // TODO: Rename and change types and number of parameters
    public static ReminderDCC newInstance(@Nullable String title, @Nullable int changeInt, @Nullable ReminderDao dao, @Nullable View itemView, @Nullable RecyclerItemAdapter adapter, boolean isExist) {
        ReminderDCC fragment = new ReminderDCC();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initializeContent(view);
    }

    public static void setDao(ReminderDao dao) {
        ReminderDCC.dao = dao;
    }

    public static void setContext(Context context) {
        ReminderDCC.context = context;
    }

    public static void setAdapter(RecyclerItemAdapter adapter) {
        ReminderDCC.adapter = adapter;
    }

    public static void setAltAdapter(AlternativeRecyclerItemAdapter altAdapter) {
        ReminderDCC.altAdapter = altAdapter;
    }

    private void initializeContent(final View view) {
        c = Calendar.getInstance();
        containerFragment = (ContainerFragment) getParentFragment();
        v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        scrollView = view.findViewById(R.id.scroll_rem_t);
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
        localDate = LocalDate.now();
        yearV = localDate.getYear();
        yearCV = yearV;
        monthV = localDate.getMonthValue();
        monthCV = monthV - 1;
        month = Utils.getMonth(monthV, false);
        dayV = localDate.getDayOfMonth();
        dayCV = dayV;
        fullDate = yearV + ", " + month + " " + dayV + ", ";
        calendarView = (MaterialCalendarView) view.findViewById(R.id.calendarView);
        calendarInit(calendarView, localDate);
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                yearV = date.getYear();
                month = Utils.getMonth(date.getMonth(), false);
                dayV = date.getDay();
                yearCV = date.getYear();
                monthCV = date.getMonth() - 1;
                dayCV = date.getDay();
                int lastFullDateLength = fullDate.length();
                fullDate = yearV + ", " + month + " " + dayV + ", ";
                stringBuilder = new StringBuilder(timeAndDateTextView.getText()).replace(0, lastFullDateLength, fullDate);
                timeAndDateTextView.setText(stringBuilder);

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
                                localDate = LocalDate.of(year, monthV, day);
                                if (day <= calendarView.getMaximumDate().getDay() && day > calendarView.getSelectedDate().getDay() && monthV == calendarView.getSelectedDate().getMonth() && year == calendarView.getSelectedDate().getYear()) {
                                    calendarView.setSelectedDate(CalendarDay.from(year, monthV, day));
                                    StringBuilder newLabelValue = Utils.updateLabel(timeAndDateTextView, CalendarDay.from(localDate), fullDate);
                                    fullDate = newLabelValue.substring(0, newLabelValue.length() - 5);
                                    timeAndDateTextView.setText(newLabelValue);

                                } else {
                                    calendarInit(calendarView, localDate);
                                    StringBuilder newLabelValue = Utils.updateLabel(timeAndDateTextView, CalendarDay.from(localDate), fullDate);
                                    fullDate = newLabelValue.substring(0, newLabelValue.length() - 5);
                                    timeAndDateTextView.setText(newLabelValue);
                                }
                                yearCV = year;
                                monthCV = month;
                                dayCV = day;
                            }
                        }, yearCV, monthCV, dayCV).show();

            }
        });
        timePicker = (TimePicker) view.findViewById(R.id.time_picker);
        timePicker.setIs24HourView(true);
        timeAndDateTextView.setText(fullDate + String.format("%02d", timePicker.getHour()) + ":" + String.format("%02d", timePicker.getMinute()));
        System.out.println(timeAndDateTextView.getText().toString());
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                String hoursAndMinutes = String.format("%02d", timePicker.getHour()) + ":" + String.format("%02d", timePicker.getMinute());
                System.out.println(fullDate.length() + " 2 " + timeAndDateTextView.getText() + " 3 " + hoursAndMinutes.length());
                stringBuilder = new StringBuilder(timeAndDateTextView.getText()).replace(fullDate.length(), timeAndDateTextView.getText().length(), hoursAndMinutes);
                timeAndDateTextView.setText(stringBuilder);
            }
        });
        backButton = (FloatingActionButton) view.findViewById(R.id.back_fl_b);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContainerFragment.showOrHideFragment(getParentFragmentManager(), ReminderDCC.this, containerFragment,   false);
            }
        });
        okB = (Button) view.findViewById(R.id.ok_b);
        okB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItem(reminderTitle, reminderDescription, iconNumber, yearV, calendarView.getSelectedDate().getMonth(), dayV, timePicker.getHour(), timePicker.getMinute(), repeatSwitch.isChecked(), alarmSwitch.isChecked());
            }
        });
        cancelB = (Button) view.findViewById(R.id.cancel_b);
        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContainerFragment.showOrHideFragment(getParentFragmentManager(), ReminderDCC.this, containerFragment, false);
            }
        });

    }

    private void addItem(EditText text, EditText desc, int iconNumber, int year, int month, int day, int hours, int minutes, boolean repeat, boolean alarm) {
        String title = text.getText().toString();
        Calendar calendar = Calendar.getInstance();
        Calendar calendar1 = Calendar.getInstance();
        calendar1.set(year, month-1, day, hours, minutes);
        if (ReminderEntity.getEntity(dao, title) != null | (calendar.getTimeInMillis() > calendar1.getTimeInMillis()) | title.equals("")) {
            v.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE));
            Log.e("NamingError: ", "This title already exist.");
        } else {
            Log.i("AddItem: ", "Create item container.");
            ReminderEntity.addToDatabase(new ReminderData(null, title, desc.getText().toString(), iconNumber, year, month, day, hours, minutes, repeat, alarm), dao);
            Log.i("AddItem: ", "Adding item to adapter list.");
            List<ReminderEntity> reminderEntityList = ReminderEntity.getAll(dao);
            adapter.setReminderDataList(reminderEntityList);
            adapter.add(title);
            altAdapter.update(title, reminderEntityList.get(reminderEntityList.size()-1));
            Log.i("AddItem: ", "Item is added.");
            addNotification(ReminderEntity.getEntity(dao, title).getCount(), iconNumber, title);
            ContainerFragment.showOrHideFragment(getParentFragmentManager(), ReminderDCC.this, containerFragment,  false);
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
            System.out.println(System.currentTimeMillis() + " with different " + (dateToLong - System.currentTimeMillis()));
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

    private void addNotification(int id, int iconId, String contentTitle) {
        int hours = timePicker.getHour();
        int minutes = timePicker.getMinute();
        CalendarDay selectedDay = calendarView.getSelectedDate();
        Log.i("AddNotification: ", "Start set alarms to notifications");
        ArrayList<Long> longList = convertDate(hours, minutes, (selectedDay != null) ? selectedDay : CalendarDay.today());
        Intent notificationIntent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        notificationIntent.putExtra("notif_time_arr", longList);
        notificationIntent.putExtra("content_title", contentTitle);
        notificationIntent.putExtra("notification_id", id);
        notificationIntent.putExtra("icon_id", iconId);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_REMINDER, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, longList.get(2), pendingIntent);
        Log.i("AddNotification: ", "Notification is added");
    }

    public void calendarInit(MaterialCalendarView calendarView, LocalDate localDate) {
        Log.i("CalendarInit: ", "Start initializing a calendar");
        calendarView.setTopbarVisible(false);
        int endYear = localDate.getYear();
        int endMonth = localDate.getMonthValue();
        int endDay = localDate.getDayOfMonth() + 6;
        Log.i("CalendarInit: ", "Start checking on OutOfBounce");
        if (endDay > localDate.lengthOfMonth()) {
            if (endMonth - 1 == Calendar.DECEMBER) {
                endMonth = 1;
                endYear = localDate.getYear() + 1;
            } else {
                endMonth += 1;
            }
            endDay -= localDate.lengthOfMonth();
        }
        Log.i("CalendarInit: ", "Start set properties to calendar");
        CalendarDay selectedDay = CalendarDay.from(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth());
        CalendarDay endDate = CalendarDay.from(endYear, endMonth, endDay);
        calendarView.state().edit()
                .setMinimumDate(selectedDay)
                .setMaximumDate(endDate)
                .setFirstDayOfWeek(localDate.getDayOfWeek())
                .commit();
        calendarView.setSelectedDate(localDate);
        Log.i("CalendarInit: ", "Calendar is initialized");
    }
    public static void dismissNotification(PendingIntent pendingIntent) {
        assert ReminderDCC.alarmManager != null;
        ReminderDCC.alarmManager.cancel(pendingIntent);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (!hidden) {
            alarmSwitch.setChecked(false);
            repeatSwitch.setChecked(false);
            reminderTitle.setText("");
            reminderDescription.setText("");
            scrollView.scrollTo(0, 0);
            LocalDate localDate = LocalDate.now();
            c = Calendar.getInstance();
            yearV = localDate.getYear();
            month = Utils.getMonth(localDate.getMonthValue(), false);
            dayV = localDate.getDayOfMonth();
            fullDate = yearV + ", " + month + " " + dayV + ", ";
            timeAndDateTextView.setText(fullDate + String.format("%02d", timePicker.getHour()) + ":" + String.format("%02d", timePicker.getMinute()));
            timePicker.setHour(c.get(Calendar.HOUR_OF_DAY));
            timePicker.setMinute(c.get(Calendar.MINUTE));
            if (this.localDate != localDate) {
                calendarInit(calendarView, localDate);
            }
        }
    }
}