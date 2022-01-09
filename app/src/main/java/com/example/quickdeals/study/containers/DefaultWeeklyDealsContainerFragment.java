package com.example.quickdeals.study.containers;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.quickdeals.R;
import com.example.quickdeals.ShablonFragment;
import com.example.quickdeals.database.timeless.TimelessRemDatabase;
import com.example.quickdeals.database.timeless.dao.TimelessReminderDao;
import com.example.quickdeals.database.timeless.entity.TimelessReminderEntity;
import com.example.quickdeals.study.StudyFragment;
import com.example.quickdeals.study.TimelessReminderDCC;
import com.example.quickdeals.study.TimelessReminderReview;
import com.example.quickdeals.study.adapter.GridRecyclerViewAdapter;
import com.example.quickdeals.utils.Listeners;

import java.util.List;

public class DefaultWeeklyDealsContainerFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static Context context;
    public RecyclerView rList;
    private TimelessReminderDao tdao;
    private static TimelessReminderDCC timelessReminderDCC;
    private static TimelessReminderReview timelessReminderReview;
    private static TimelessRemDatabase tdb;
    private static List<String> titles;
    private static List<TimelessReminderEntity> timelessReminderEntities;
    private static GridRecyclerViewAdapter adapter;
    private static ItemTouchHelper itemTouchHelper;
    private static boolean f;
    private static FragmentManager parent;
    private FrameLayout dailyDealsContainer;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DefaultWeeklyDealsContainerFragment() {
        // Required empty public constructor
    }

    public static void setParent(FragmentManager parent) {
        DefaultWeeklyDealsContainerFragment.parent = parent;
    }

    public void setDailyDealsContainer(FrameLayout dailyDealsContainer) {
        this.dailyDealsContainer = dailyDealsContainer;
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
        adapter = new GridRecyclerViewAdapter(timelessReminderEntities, tdao, titles, context);
        TimelessReminderReview.setAdapter(adapter);
        TimelessReminderDCC.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_default_weekly_deals_container, container, false);
        rList = (RecyclerView) view.findViewById(R.id.grid_recycler_view);
        rList.setLayoutManager(new GridLayoutManager(context, 2));
        rList.setAdapter(adapter);
        itemTouchHelper = Listeners.setSimpleItemTouchCallback(getParentFragment().getView(), adapter, titles, tdao);
        itemTouchHelper.attachToRecyclerView(rList);
        StudyFragment.setTimelessDccFragment(timelessReminderDCC);
        return view;
    }
    public void initDefaultRV() {
        if (titles == null || timelessReminderEntities.size() != TimelessReminderEntity.getAll(tdao).size()) {
            titles = TimelessReminderEntity.getTitlesFromDatabase(tdao);
            timelessReminderEntities = TimelessReminderEntity.getAll(tdao);
            TimelessReminderReview.setContext(context);
            TimelessReminderReview.setTimelessReminderDao(tdao);
            TimelessReminderDCC.setContext(context);
            TimelessReminderDCC.setTimelessReminderDao(tdao);
            timelessReminderDCC = new TimelessReminderDCC();
            timelessReminderReview = new TimelessReminderReview();
            GridRecyclerViewAdapter.setTimelessReminderReview(timelessReminderReview);
            GridRecyclerViewAdapter.setFragmentManager(parent);
            ShablonFragment.addTimelessFragments(parent, timelessReminderDCC, timelessReminderReview);
        }
    }

    public static void setAdapter(GridRecyclerViewAdapter adapter) {
        DefaultWeeklyDealsContainerFragment.adapter = adapter;
    }
}
