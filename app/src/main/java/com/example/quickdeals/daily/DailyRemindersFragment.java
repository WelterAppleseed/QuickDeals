package com.example.quickdeals.daily;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quickdeals.R;
import com.example.quickdeals.ReminderDCC;
import com.example.quickdeals.ReminderReview;
import com.example.quickdeals.ShablonFragment;
import com.example.quickdeals.database.RemDatabase;
import com.example.quickdeals.database.dao.ReminderDao;
import com.example.quickdeals.database.entity.ReminderEntity;
import com.example.quickdeals.utils.Listeners;
import com.example.quickdeals.utils.reminders.RecyclerItemAdapter;
import com.example.quickdeals.utils.reminders.ReminderOptions;
import com.example.quickdeals.utils.reminders.SavedReminder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DailyRemindersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DailyRemindersFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
   /* private ImageButton toStudyFragmentButton;
    private ImageButton toCarWashFragmentButton;
    private ImageButton toActivitiesFragmentButton;
    private ImageButton toDailyFragmentButton;*/
    private FloatingActionButton addButton;
    public static RecyclerView rList;
    private List<String> titles;
    private SharedPreferences preferences;
    private SavedReminder savedReminder;
    private static RecyclerItemAdapter adapter;
    private ReminderOptions reminderDCC;
    private static Context context;
    private RemDatabase db;
    private ReminderDao dao;
    private static ItemTouchHelper itemTouchHelper;
    public static View parentView;
    private ReminderDCC dccFragment;
    private static ReminderReview reviewFragment;

    public static void setParentView(View parentView) {
        DailyRemindersFragment.parentView = parentView;
    }

    public DailyRemindersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DailyRemindersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DailyRemindersFragment newInstance(String param1, String param2) {
        DailyRemindersFragment fragment = new DailyRemindersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static void setContext(Context context) {
        DailyRemindersFragment.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        restoreReminders();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_daily_reminders, container, false);
        createAndInitContent(view);
        return view;
    }

    private void createAndInitContent(final View view) {
        context = view.getContext();
        dccFragment = new ReminderDCC();
        reviewFragment = new ReminderReview();
        RecyclerItemAdapter.setReviewFragment(reviewFragment);
        ShablonFragment.addFragments(getParentFragmentManager(), dccFragment, reviewFragment);
        RecyclerItemAdapter.setFragmentManager(getParentFragmentManager());
        /*LinearLayout navigationLayout = view.findViewById(R.id.navigationLayout);
        toDailyFragmentButton = navigationLayout.findViewById(R.id.dailyFragmentButton);
        toStudyFragmentButton = navigationLayout.findViewById(R.id.studyFragmentButton);
        toActivitiesFragmentButton = navigationLayout.findViewById(R.id.activitiesFragmentButton);
        toCarWashFragmentButton = navigationLayout.findViewById(R.id.carWashFragmentButton);
        toStudyFragmentButton.setOnClickListener(this);
        toActivitiesFragmentButton.setOnClickListener(this);
        toCarWashFragmentButton.setOnClickListener(this);*/
        rList = (RecyclerView) view.findViewById(R.id.item_layout);
        rList.setLayoutManager(new LinearLayoutManager(context));
        rList.setAdapter(adapter);
        itemTouchHelper = Listeners.setSimpleItemTouchCallback(context, getParentFragment().getView(), adapter, titles, dao);
        itemTouchHelper.attachToRecyclerView(rList);
        addButton = (FloatingActionButton) view.findViewById(R.id.add_item);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("2");
                ShablonFragment.showOrHideFragment(getParentFragmentManager(), dccFragment, true);
            }
        });

    }

    private void restoreReminders() {
        db = Room.databaseBuilder(context, RemDatabase.class, "remind_database").build();
        dao = db.reminderDao();
        titles = ReminderEntity.getTitlesFromDatabase(dao);
        List<ReminderEntity> reminderEntityList = ReminderEntity.getAll(dao);
        System.out.println(reminderEntityList);
        savedReminder = new SavedReminder(context);
        adapter = new RecyclerItemAdapter(context, dao, titles, reminderEntityList);
        ReminderReview.setContext(context);
        ReminderReview.setAdapter(adapter);
        ReminderReview.setDao(dao);
        ReminderReview.setIsFirstItem(titles.size()==0);
        ReminderDCC.setContext(context);
        ReminderDCC.setAdapter(adapter);
        ReminderDCC.setDao(dao);
        ReminderDCC.setIsFirstItem(titles.size()==0);
       /* ReminderOptions.setContext(context);
        ReminderOptions.setAdapter(adapter);
        ReminderOptions.setDao(dao);
        ReminderOptions.setIsFirstItem(titles.size()==0);*/
    }


    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public RecyclerItemAdapter getAdapter() {
        return adapter;
    }

    @Override
    public void onClick(View v) {
       /* if (v == toStudyFragmentButton) {
            Navigation.findNavController(getView()).navigate(R.id.action_dailyRemindersFragment_to_studyFragment2);
        }
        if (v == toCarWashFragmentButton) {
            Navigation.findNavController(getView()).navigate(R.id.action_dailyRemindersFragment_to_carWashFragment2);
        }
        if (v == toActivitiesFragmentButton) {
            Navigation.findNavController(getView()).navigate(R.id.action_dailyRemindersFragment_to_activitiesFragment2);
        }*/
    }
}
