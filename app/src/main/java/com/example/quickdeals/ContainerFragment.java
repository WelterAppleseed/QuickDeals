package com.example.quickdeals;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.room.Room;
import androidx.viewpager2.widget.ViewPager2;

import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.quickdeals.daily.DailyRemindersFragment;
import com.example.quickdeals.daily.ReminderDCC;
import com.example.quickdeals.daily.ReminderReview;
import com.example.quickdeals.daily.container.AlternativeDailyDealsContainerFragment;
import com.example.quickdeals.daily.container.DefaultDailyDealsContainerFragment;
import com.example.quickdeals.database.temporary.RemDatabase;
import com.example.quickdeals.database.timeless.TimelessRemDatabase;
import com.example.quickdeals.navigation.ViewPagerAdapter;
import com.example.quickdeals.weekly.WeeklyRemindersFragment;
import com.example.quickdeals.weekly.WeeklyReminderDCC;
import com.example.quickdeals.weekly.WeeklyReminderReview;
import com.example.quickdeals.utils.adapters.GridRecyclerViewAdapter;
import com.example.quickdeals.weekly.containers.AlternativeWeeklyDealsContainerFragment;
import com.example.quickdeals.weekly.containers.DefaultWeeklyDealsContainerFragment;
import com.example.quickdeals.utils.adapters.ItemTouchCallbacks;
import com.example.quickdeals.utils.adapters.RecyclerItemAdapter;
import com.google.android.material.snackbar.Snackbar;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContainerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContainerFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private static Drawable dFC;
    private ImageButton toStudyFragmentButton;
    private ImageButton toDailyFragmentButton;
    private ViewPager2 viewPager;
    private LinearLayout navigationLayout;
    private static FragmentManager parentManager;
    private static boolean dailyIsUpdated, weeklyIsUpdated;
    private static Context context;
    private Menu dynamicalMenu;

    public ContainerFragment() {
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
    public static ContainerFragment newInstance(String param1, String param2) {
        ContainerFragment fragment = new ContainerFragment();
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
        try {
            ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setHasOptionsMenu(true);
        setMenuVisibility(true);
        initDatabases();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragments
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_container, container, false);
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
        RecyclerItemAdapter.setContainerFragment(this);
        GridRecyclerViewAdapter.setContainerFragment(this);
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
        dynamicalMenu = menu;
        inflater.inflate(R.menu.menu, menu);
        final MenuItem item = menu.findItem(R.id.action_edit);
        try {
            Thread.sleep(300);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Drawable drawable = item.getIcon();
        if (drawable instanceof Animatable) {
            ((Animatable) drawable).start();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        if (item.getItemId() == R.id.action_edit) {
            if (viewPager.getCurrentItem() == 0) {
                if (ItemTouchCallbacks.snackbar != null && ItemTouchCallbacks.snackbar.isShown()) {
                    ItemTouchCallbacks.snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).dismiss();
                }
                DailyRemindersFragment.setAlternativeView(dailyIsUpdated, parentManager, this);
                dailyIsUpdated = !dailyIsUpdated;
                toStudyFragmentButton.setClickable(!dailyIsUpdated);
                Log.i("Menu", "Edit.");
                return true;
            } else {
                if (ItemTouchCallbacks.snackbar != null && ItemTouchCallbacks.snackbar.isShown()) {
                    ItemTouchCallbacks.snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).dismiss();
                }
                WeeklyRemindersFragment.setAlternativeView(weeklyIsUpdated, parentManager, this);
                weeklyIsUpdated = !weeklyIsUpdated;
                toDailyFragmentButton.setClickable(!weeklyIsUpdated);
                Log.i("Menu", "Edit.");
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int numOfFragment;
        if (v == toDailyFragmentButton) {
            numOfFragment = 0;
            toDailyFragmentButton.getDrawable().setTint(getResources().getColor(R.color.colorSelectedViewGreen));
            toStudyFragmentButton.getDrawable().setTint(getResources().getColor(R.color.colorBlack));
        } else {
            numOfFragment = 1;
            toDailyFragmentButton.getDrawable().setTint(getResources().getColor(R.color.colorBlack));
            toStudyFragmentButton.getDrawable().setTint(getResources().getColor(R.color.colorSelectedViewGreen));
            if (ItemTouchCallbacks.snackbar != null && ItemTouchCallbacks.snackbar.isShown()) {
                ItemTouchCallbacks.snackbar.setAnimationMode(Snackbar.ANIMATION_MODE_SLIDE).dismiss();
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
        AlternativeDailyDealsContainerFragment aFragment = AlternativeDailyDealsContainerFragment.newInstance();
        DefaultDailyDealsContainerFragment dFragment = DefaultDailyDealsContainerFragment.newInstance();
        DailyRemindersFragment.setDefaultFragment(dFragment);
        DailyRemindersFragment.setAlternativeFragment(aFragment);

        TimelessRemDatabase tdb = Room.databaseBuilder(context, TimelessRemDatabase.class, "timeless_remind_database").build();
        AlternativeWeeklyDealsContainerFragment.setTdb(tdb);
        AlternativeWeeklyDealsContainerFragment.setParent(getChildFragmentManager());
        DefaultWeeklyDealsContainerFragment.setTdb(tdb);
        DefaultWeeklyDealsContainerFragment.setParent(getChildFragmentManager());
        AlternativeWeeklyDealsContainerFragment atFragment = AlternativeWeeklyDealsContainerFragment.newInstance();
        DefaultWeeklyDealsContainerFragment dtFragment = DefaultWeeklyDealsContainerFragment.newInstance();
        WeeklyRemindersFragment.setDefaultFragment(dtFragment);
        WeeklyRemindersFragment.setAlternativeFragment(atFragment);
        ContainerFragment.addAlternativeRListFragments(getChildFragmentManager(), aFragment, atFragment);
    }

    public static void addFragments(FragmentManager parentManager, ReminderDCC fragment1, ReminderReview fragment2) {
        FragmentTransaction transaction = parentManager.beginTransaction();
        transaction.replace(R.id.dcc_container, fragment1, "dcc_fragment").hide(fragment1).replace(R.id.review_container, fragment2, "review_edit_fragment").hide(fragment2).commit();
    }

    public static void addTimelessFragments(FragmentManager parentManager, WeeklyReminderDCC tdccFragment, WeeklyReminderReview treviewFragment) {
        FragmentTransaction transaction = parentManager.beginTransaction();
        transaction.replace(R.id.tdcc_container, tdccFragment, "tdcc_fragment").hide(tdccFragment).replace(R.id.treview_container, treviewFragment, "treview_edit_fragment").hide(treviewFragment).commit();
    }

    private static void addAlternativeRListFragments(FragmentManager parentManager, AlternativeDailyDealsContainerFragment dAltFragment, AlternativeWeeklyDealsContainerFragment wAltFragment) {
        FragmentTransaction transaction = parentManager.beginTransaction();
        transaction.replace(R.id.d_alt_container, dAltFragment, "d_alt_fragment").hide(dAltFragment).replace(R.id.w_alt_container, wAltFragment, "w_alt_fragment").hide(wAltFragment).commit();
    }

    public static void showOrHideFragment(FragmentManager parentManager, Fragment fragment, ContainerFragment containerFragment, boolean show) {
        FragmentTransaction transaction = parentManager.beginTransaction();
        containerFragment.setSharedElementReturnTransition(TransitionInflater.from(containerFragment.getActivity()).inflateTransition(R.transition.change_image_transform));
        fragment.setSharedElementEnterTransition(TransitionInflater.from(containerFragment.getActivity()).inflateTransition(R.transition.change_image_transform));
        transaction.setCustomAnimations(0, R.anim.exit, R.anim.enter, 0);
        if (show) {
            transaction.show(fragment).commit();
        } else {
            transaction.hide(fragment).commit();
        }
    }
    public void start(ReminderReview dcc) {
        Intent intent = new Intent(dcc.getContext(), MainActivity.class);
        startActivity(intent);
    }

    public static void setParentManager(FragmentManager parentManager) {
        ContainerFragment.parentManager = parentManager;
    }

    public static void setContext(Context context) {
        ContainerFragment.context = context;
    }
}
