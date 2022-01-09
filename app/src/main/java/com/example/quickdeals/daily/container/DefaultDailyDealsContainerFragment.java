package com.example.quickdeals.daily.container;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.quickdeals.R;
import com.example.quickdeals.daily.ReminderDCC;
import com.example.quickdeals.daily.ReminderReview;
import com.example.quickdeals.ContainerFragment;
import com.example.quickdeals.daily.DailyRemindersFragment;
import com.example.quickdeals.notifications.NotificationService;
import com.example.quickdeals.database.temporary.RemDatabase;
import com.example.quickdeals.database.temporary.dao.ReminderDao;
import com.example.quickdeals.database.temporary.entity.ReminderEntity;
import com.example.quickdeals.utils.adapters.ItemTouchCallbacks;
import com.example.quickdeals.utils.adapters.RecyclerItemAdapter;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DefaultDailyDealsContainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DefaultDailyDealsContainerFragment extends Fragment {
    private static Context context;
    private ReminderDao dao;
    private static ReminderDCC dccFragment;
    private static RemDatabase db;
    private static List<String> titles;
    private static List<ReminderEntity> reminderEntityList;
    private static RecyclerItemAdapter adapter;
    private static FragmentManager parent;

    public DefaultDailyDealsContainerFragment() {
    }

    public static void setParent(FragmentManager parent) {
        DefaultDailyDealsContainerFragment.parent = parent;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment DefaultDailyDealsContainerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DefaultDailyDealsContainerFragment newInstance() {
        return new DefaultDailyDealsContainerFragment();
    }

    public static void setContext(Context context) {
        DefaultDailyDealsContainerFragment.context = context;
    }

    public static void setDb(RemDatabase db) {
        DefaultDailyDealsContainerFragment.db = db;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dao = db.reminderDao();
        initDefaultRV();
        for (ReminderEntity entity: reminderEntityList) {
            System.out.println(entity.title);
        }
        adapter = new RecyclerItemAdapter(context, dao, titles, reminderEntityList);
        ReminderReview.setAdapter(adapter);
        ReminderDCC.setAdapter(adapter);
        NotificationService.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        assert getParentFragment() != null;
        View view = inflater.inflate(R.layout.fragment_default_daily_deals_container, container, false);
        RecyclerView rList = view.findViewById(R.id.item_layout);
        rList.setLayoutManager(new LinearLayoutManager(context));
        rList.setAdapter(adapter);
        ItemTouchHelper itemTouchHelper = ItemTouchCallbacks.setSimpleItemTouchCallback(getParentFragment().getView(), adapter, titles, dao);
        itemTouchHelper.attachToRecyclerView(rList);
        DailyRemindersFragment.setDccFragment(dccFragment);
        return view;
    }
    private void initDefaultRV() {
        if (titles == null || reminderEntityList.size() != ReminderEntity.getAll(dao).size()) {
            titles = ReminderEntity.getTitlesFromDatabase(dao);
            reminderEntityList = ReminderEntity.getAll(dao);
            ReminderReview.setContext(context);
            ReminderReview.setDao(dao);
            ReminderDCC.setContext(context);
            ReminderDCC.setDao(dao);
            dccFragment = new ReminderDCC();
            ReminderReview reviewFragment = new ReminderReview();
            RecyclerItemAdapter.setReviewFragment(reviewFragment);
            RecyclerItemAdapter.setFragmentManager(parent);
            ContainerFragment.addFragments(parent, dccFragment, reviewFragment);
        }
    }

    public static void setAdapter(RecyclerItemAdapter adapter) {
        DefaultDailyDealsContainerFragment.adapter = adapter;
    }
}
