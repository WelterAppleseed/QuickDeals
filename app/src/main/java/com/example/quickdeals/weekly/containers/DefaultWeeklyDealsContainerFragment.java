package com.example.quickdeals.weekly.containers;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdeals.ContainerFragment;
import com.example.quickdeals.R;
import com.example.quickdeals.database.timeless.TimelessRemDatabase;
import com.example.quickdeals.database.timeless.dao.TimelessReminderDao;
import com.example.quickdeals.database.timeless.entity.TimelessReminderEntity;
import com.example.quickdeals.weekly.WeeklyRemindersFragment;
import com.example.quickdeals.weekly.WeeklyReminderDCC;
import com.example.quickdeals.weekly.WeeklyReminderReview;
import com.example.quickdeals.utils.adapters.GridRecyclerViewAdapter;
import com.example.quickdeals.utils.adapters.ItemTouchCallbacks;

import java.util.List;

public class DefaultWeeklyDealsContainerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static Context context;
    private TimelessReminderDao tdao;
    private static WeeklyReminderDCC weeklyReminderDCC;
    private static TimelessRemDatabase tdb;
    private static List<String> titles;
    private static List<TimelessReminderEntity> timelessReminderEntities;
    private static GridRecyclerViewAdapter adapter;
    private static boolean f;
    private static FragmentManager parent;

    public DefaultWeeklyDealsContainerFragment() {
    }

    public static void setParent(FragmentManager parent) {
        DefaultWeeklyDealsContainerFragment.parent = parent;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DefaultDailyDealsContainerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DefaultWeeklyDealsContainerFragment newInstance() {
        return new DefaultWeeklyDealsContainerFragment();
    }

    public static void setContext(Context context) {
        DefaultWeeklyDealsContainerFragment.context = context;
    }

    public static void setTdb(TimelessRemDatabase tdb) {
        DefaultWeeklyDealsContainerFragment.tdb = tdb;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tdao = tdb.timelessReminderDao();
        initDefaultRV();
        for (TimelessReminderEntity entity: timelessReminderEntities) {
            System.out.println(entity.title);
        }
        adapter = new GridRecyclerViewAdapter(timelessReminderEntities, tdao, titles);
        WeeklyReminderReview.setAdapter(adapter);
        WeeklyReminderDCC.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_default_weekly_deals_container, container, false);
        assert getParentFragment() != null;
        RecyclerView rList = view.findViewById(R.id.grid_recycler_view);
        rList.setLayoutManager(new GridLayoutManager(context, 2));
        rList.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = ItemTouchCallbacks.setSimpleItemTouchCallback(getParentFragment().getView(), adapter, titles, tdao);
        itemTouchHelper.attachToRecyclerView(rList);
        WeeklyRemindersFragment.setTimelessDccFragment(weeklyReminderDCC);
        return view;
    }
    private void initDefaultRV() {
        if (titles == null || timelessReminderEntities.size() != TimelessReminderEntity.getAll(tdao).size()) {
            titles = TimelessReminderEntity.getTitlesFromDatabase(tdao);
            timelessReminderEntities = TimelessReminderEntity.getAll(tdao);
            WeeklyReminderReview.setContext(context);
            WeeklyReminderReview.setTimelessReminderDao(tdao);
            WeeklyReminderDCC.setContext(context);
            WeeklyReminderDCC.setTimelessReminderDao(tdao);
            weeklyReminderDCC = new WeeklyReminderDCC();
            WeeklyReminderReview weeklyReminderReview = new WeeklyReminderReview();
            GridRecyclerViewAdapter.setWeeklyReminderReview(weeklyReminderReview);
            GridRecyclerViewAdapter.setFragmentManager(parent);
            ContainerFragment.addTimelessFragments(parent, weeklyReminderDCC, weeklyReminderReview);
        }
    }

    public static void setAdapter(GridRecyclerViewAdapter adapter) {
        DefaultWeeklyDealsContainerFragment.adapter = adapter;
    }
}
