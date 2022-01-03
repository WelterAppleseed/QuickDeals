package com.example.quickdeals;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.appcompat.widget.Toolbar;

import com.example.quickdeals.daily.DailyRemindersFragment;
import com.example.quickdeals.daily.container.AlternativeDailyDealsContainerFragment;
import com.example.quickdeals.daily.container.DefaultDailyDealsContainerFragment;
import com.example.quickdeals.database.temporary.RemDatabase;
import com.example.quickdeals.database.timeless.TimelessRemDatabase;
import com.example.quickdeals.navigation.ViewPagerAdapter;
import com.example.quickdeals.study.StudyFragment;
import com.example.quickdeals.study.TimelessReminderDCC;
import com.example.quickdeals.study.TimelessReminderReview;
import com.example.quickdeals.study.adapter.GridRecyclerViewAdapter;
import com.example.quickdeals.utils.Listeners;
import com.example.quickdeals.utils.reminders.RecyclerItemAdapter;
import com.google.android.material.snackbar.Snackbar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShablonFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShablonFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static Drawable dFC;
    private ImageButton toStudyFragmentButton;
    private ImageButton toDailyFragmentButton;
    private ViewPager2 viewPager;
    private static boolean itemIsTouched;
    private Toolbar toolbar;
    private LinearLayout navigationLayout;
    private FrameLayout deletePane;
    private View view;
    private static FragmentManager parentManager;
    private static boolean isUpdated;
    private static Context context;
    private DefaultDailyDealsContainerFragment dFragment;
    private AlternativeDailyDealsContainerFragment aFragment;

    public ShablonFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShablonFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShablonFragment newInstance(String param1, String param2) {
        ShablonFragment fragment = new ShablonFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("Menu", "Menu is built in onCreate");
        setHasOptionsMenu(true);
        setMenuVisibility(true);
        initDatabases();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragments
        setHasOptionsMenu(true);
        view = inflater.inflate(R.layout.fragment_shablon, container, false);
        dFC = ContextCompat.getDrawable(getContext(), R.drawable.view_on_delete_borders);
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setUserInputEnabled(false);
        /*toolbar = view.findViewById(R.id.toolbar);
        toolbar.setBackgroundColor(getResources().getColor(R.color.colorTopbar));
        //((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);*/
        navigationLayout = view.findViewById(R.id.navigationLayout);
        initNavigationLayout();
        viewPager.setAdapter(new ViewPagerAdapter(this));
        DailyRemindersFragment.setParentView(view);
        RecyclerItemAdapter.setShablonFragment(this);

        return view;
    }

    private void initNavigationLayout() {
        toDailyFragmentButton = navigationLayout.findViewById(R.id.dailyFragmentButton);
        toStudyFragmentButton = navigationLayout.findViewById(R.id.studyFragmentButton);
        toDailyFragmentButton.setOnClickListener(this);
        toStudyFragmentButton.setOnClickListener(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Log.i("Menu", "Inflating menu.");
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_edit) {
            if (viewPager.getCurrentItem() == 0) {
                if (Listeners.snackbar != null && Listeners.snackbar.isShown()) {
                    Listeners.snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).dismiss();
                }
                DailyRemindersFragment.setAlternativeView(isUpdated, parentManager);
                isUpdated = !isUpdated;
                Log.i("Menu", "Edit.");
                return true;
            } else {
                if (Listeners.snackbar != null && Listeners.snackbar.isShown()) {
                    Listeners.snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).dismiss();
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int numOfFragment;
        if (v == toDailyFragmentButton) {
            numOfFragment = 0;
        } else {
            numOfFragment = 1;
            if (Listeners.snackbar != null && Listeners.snackbar.isShown()) {
                Listeners.snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).dismiss();
            }
        }
        viewPager.setCurrentItem(numOfFragment, false);
    }
    private void initDatabases() {
        RemDatabase db = Room.databaseBuilder(context, RemDatabase.class, "remind_database").build();
        AlternativeDailyDealsContainerFragment.setDb(db);
        AlternativeDailyDealsContainerFragment.setParent(getChildFragmentManager());
        DefaultDailyDealsContainerFragment.setDb(db);
        DefaultDailyDealsContainerFragment.setParent(getChildFragmentManager());
        aFragment = AlternativeDailyDealsContainerFragment.newInstance();
        dFragment = DefaultDailyDealsContainerFragment.newInstance();
        DailyRemindersFragment.setDefaultFragment(dFragment);
        DailyRemindersFragment.setAlternativeFragment(aFragment);

        TimelessRemDatabase tdb = Room.databaseBuilder(context, TimelessRemDatabase.class, "timeless_remind_database").build();
        TimelessReminderDCC.setContext(context);
        TimelessReminderDCC.setTimelessReminderDao(tdb.timelessReminderDao());
        TimelessReminderReview.setContext(context);
        TimelessReminderReview.setTimelessReminderDao(tdb.timelessReminderDao());
        GridRecyclerViewAdapter.setShablonFragment(this);
        StudyFragment.initRecyclerList(tdb);
    }

    public static void addFragments(FragmentManager parentManager, ReminderDCC fragment1, ReminderReview fragment2) {
        FragmentTransaction transaction = parentManager.beginTransaction();
        transaction.replace(R.id.dcc_container, fragment1, "dcc_fragment").hide(fragment1).replace(R.id.review_container, fragment2, "review_edit_fragment").hide(fragment2).commit();
    }
    public static void addTimelessFragments(FragmentManager parentManager, TimelessReminderDCC tdccFragment, TimelessReminderReview treviewFragment) {
        FragmentTransaction transaction = parentManager.beginTransaction();
        transaction.replace(R.id.tdcc_container, tdccFragment, "tdcc_fragment").hide(tdccFragment).replace(R.id.treview_container, treviewFragment, "treview_edit_fragment").hide(treviewFragment).commit();
    }

    public static void showOrHideFragment(FragmentManager parentManager, Fragment fragment, ShablonFragment shablonFragment, boolean show) {
        FragmentTransaction transaction = parentManager.beginTransaction();
        transaction.setCustomAnimations(0, R.anim.exit, R.anim.enter, 0);
        if (show) {
            transaction.show(fragment).commit();
        } else {
            transaction.hide(fragment).commit();
        }
    }

    public static void moveToFragment(FragmentManager parentManager, Fragment fragment) {
        FragmentTransaction transaction = parentManager.beginTransaction();
        transaction.replace(R.id.item_containter, fragment, "alt_daily_deals_view").commit();
    }

    public void start(ReminderReview dcc) {
        Intent intent = new Intent(dcc.getContext(), MainActivity.class);
        startActivity(intent);
    }

    public static void setParentManager(FragmentManager parentManager) {
        ShablonFragment.parentManager = parentManager;
    }

    public static void setContext(Context context) {
        ShablonFragment.context = context;
    }
}
