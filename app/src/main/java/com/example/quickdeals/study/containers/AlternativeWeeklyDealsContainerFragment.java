package com.example.quickdeals.study.containers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdeals.R;
import com.example.quickdeals.database.timeless.TimelessRemDatabase;
import com.example.quickdeals.database.timeless.dao.TimelessReminderDao;
import com.example.quickdeals.database.timeless.entity.TimelessReminderEntity;
import com.example.quickdeals.study.TimelessReminderDCC;
import com.example.quickdeals.utils.Listeners;
import com.example.quickdeals.utils.reminders.AlternativeRecyclerItemAdapter;
import com.example.quickdeals.utils.reminders.AlternativeWeeklyRecyclerItemAdapter;

import org.threeten.bp.LocalDate;

import java.util.List;

public class AlternativeWeeklyDealsContainerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Context context;
    public RecyclerView rList;
    private TimelessReminderDao tdao;
    private static TimelessRemDatabase tdb;
    private static FragmentManager parent;
    private List<String> titles;
    private List<TimelessReminderEntity> timelessReminderEntities;
    private AlternativeWeeklyRecyclerItemAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AlternativeWeeklyDealsContainerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment AlternativeDailyDealsRemindersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlternativeWeeklyDealsContainerFragment newInstance() {
        return new AlternativeWeeklyDealsContainerFragment();
    }
    public static void setTdb(TimelessRemDatabase tdb) {
        AlternativeWeeklyDealsContainerFragment.tdb = tdb;
    }

    public static void setParent(FragmentManager parent) {
        AlternativeWeeklyDealsContainerFragment.parent = parent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        tdao = tdb.timelessReminderDao();
        if (titles != TimelessReminderEntity.getTitlesFromDatabase(tdao)) {
            titles = TimelessReminderEntity.getTitlesFromDatabase(tdao);
            timelessReminderEntities = TimelessReminderEntity.getAll(tdao);
        }
    }
    public static void setContext(Context context) {
        AlternativeWeeklyDealsContainerFragment.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alternative_daily_deals_reminders, container, false);
        adapter = new AlternativeWeeklyRecyclerItemAdapter(context, tdao, titles, timelessReminderEntities, LocalDate.now());
        rList = (RecyclerView) view.findViewById(R.id.item_layout);
        rList.setLayoutManager(new LinearLayoutManager(context));
        rList.setAdapter(adapter);
        TimelessReminderDCC.setAltWeekAdapter(adapter);
        Listeners.setAltWeekAdapter(adapter);
        return view;
    }
}
