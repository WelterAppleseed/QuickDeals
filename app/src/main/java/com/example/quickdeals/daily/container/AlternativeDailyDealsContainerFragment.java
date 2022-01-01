package com.example.quickdeals.daily.container;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.quickdeals.R;
import com.example.quickdeals.database.temporary.RemDatabase;
import com.example.quickdeals.database.temporary.dao.ReminderDao;
import com.example.quickdeals.database.temporary.entity.ReminderEntity;
import com.example.quickdeals.utils.reminders.AlternativeRecyclerItemAdapter;

import org.threeten.bp.LocalDate;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AlternativeDailyDealsContainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AlternativeDailyDealsContainerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Context context;
    public RecyclerView rList;
    private ReminderDao dao;
    private static RemDatabase db;
    private static FragmentManager parent;
    private List<String> titles;
    private List<ReminderEntity> reminderEntityList;
    private AlternativeRecyclerItemAdapter adapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AlternativeDailyDealsContainerFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.

     * @return A new instance of fragment AlternativeDailyDealsRemindersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AlternativeDailyDealsContainerFragment newInstance() {
        return new AlternativeDailyDealsContainerFragment();
    }
    public static void setDb(RemDatabase db) {
        AlternativeDailyDealsContainerFragment.db = db;
    }

    public static void setParent(FragmentManager parent) {
        AlternativeDailyDealsContainerFragment.parent = parent;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        dao = db.reminderDao();
        if (titles != ReminderEntity.getTitlesFromDatabase(dao)) {
            titles = ReminderEntity.getTitlesFromDatabase(dao);
            reminderEntityList = ReminderEntity.getAll(dao);
        }
    }
    public static void setContext(Context context) {
        AlternativeDailyDealsContainerFragment.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_alternative_daily_deals_reminders, container, false);
        adapter = new AlternativeRecyclerItemAdapter(context, dao, titles, reminderEntityList, LocalDate.now());
        rList = (RecyclerView) view.findViewById(R.id.item_layout);
        rList.setLayoutManager(new LinearLayoutManager(context));
        rList.setAdapter(adapter);
        return view;
    }
}
