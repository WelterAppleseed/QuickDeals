package com.example.quickdeals.study;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.quickdeals.R;
import com.example.quickdeals.ShablonFragment;
import com.example.quickdeals.daily.DailyRemindersFragment;
import com.example.quickdeals.database.timeless.TimelessRemDatabase;
import com.example.quickdeals.database.timeless.dao.TimelessReminderDao;
import com.example.quickdeals.database.timeless.entity.TimelessReminderEntity;
import com.example.quickdeals.navigation.NavigationLayout;
import com.example.quickdeals.start.StartFragment;
import com.example.quickdeals.study.adapter.GridRecyclerViewAdapter;
import com.example.quickdeals.study.item_data.GridItemData;
import com.example.quickdeals.utils.Listeners;
import com.example.quickdeals.utils.states.States;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StudyFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StudyFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static List<TimelessReminderEntity> recyclerDataArrayList;
    private static GridRecyclerViewAdapter adapter;
    private TimelessReminderDCC timelessReminderDCC;
    private TimelessReminderReview timelessReminderReview;
    private static GridLayoutManager layoutManager;
    private FloatingActionButton addButton;
    private static List<String> items;
    private Context context;
    private ItemTouchHelper itemTouchHelper;
    private static TimelessReminderDao timelessReminderDao;

    // TODO: Rename and change types of parameters
   /* private ImageButton toStudyFragmentButton;
    private ImageButton toCarWashFragmentButton;
    private ImageButton toActivitiesFragmentButton;
    private ImageButton toDailyFragmentButton;*/

    public StudyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment StudyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StudyFragment newInstance() {
        StudyFragment fragment = new StudyFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study, container, false);
        RecyclerView recyclerView= view.findViewById(R.id.grid_recycler_view);
        timelessReminderDCC = new TimelessReminderDCC();
        timelessReminderReview = new TimelessReminderReview();
        GridRecyclerViewAdapter.setTimelessReminderReview(timelessReminderReview);
        GridRecyclerViewAdapter.setFragmentManager(getParentFragmentManager());
        ShablonFragment.addTimelessFragments(getParentFragmentManager(), timelessReminderDCC, timelessReminderReview);
        adapter = new GridRecyclerViewAdapter(recyclerDataArrayList, items, context);
        layoutManager = new GridLayoutManager(this.getContext(),2);
        TimelessReminderDCC.setAdapter(adapter);
        TimelessReminderReview.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        itemTouchHelper = Listeners.setSimpleItemTouchCallback(getParentFragment().getView(), adapter, items, timelessReminderDao);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        addButton = view.findViewById(R.id.add_grid_item);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShablonFragment.showOrHideFragment(getParentFragmentManager(), timelessReminderDCC, (ShablonFragment) getParentFragment(),true);
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
       /* if (v == toCarWashFragmentButton) {
            Navigation.findNavController(getView()).navigate(R.id.action_studyFragment2_to_carWashFragment2);
        }
        if (v == toActivitiesFragmentButton) {
            Navigation.findNavController(getView()).navigate(R.id.action_studyFragment_to_activitiesFragment);
        }
        if (v == toDailyFragmentButton) {
            Navigation.findNavController(getView()).navigate(R.id.action_studyFragment2_to_dailyRemindersFragment);
        }*/
    }
    public static void initRecyclerList(TimelessRemDatabase tdb) {
        TimelessReminderDao tdao = tdb.timelessReminderDao();
        List<String> titles = TimelessReminderEntity.getTitlesFromDatabase(tdao);
        System.out.println(titles);
        StudyFragment.recyclerDataArrayList =  TimelessReminderEntity.getAll(tdao);
        StudyFragment.items = titles;
        StudyFragment.timelessReminderDao = tdao;
        GridRecyclerViewAdapter.setTimelessReminderDao(tdao);
    }
    public void setContext(Context context) {
        this.context = context;
    }

    public static void setAlternativeView(boolean isUpdated, FragmentManager parentManager) {
        FragmentTransaction parentTransaction = parentManager.beginTransaction();
        if (isUpdated) {
            parentTransaction.replace(R.id.updated_view_container, DailyRemindersFragment.defaultFragment).commit();
        } else {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    DailyRemindersFragment.defaultFragment.initDefaultRV();
                }
            };
            ExecutorService executorService = Executors.newCachedThreadPool();
            executorService.submit(r);
            executorService.shutdown();
            parentTransaction.replace(R.id.updated_view_container, DailyRemindersFragment.alternativeFragment).commit();
        }
    }
}
