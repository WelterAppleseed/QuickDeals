package com.example.quickdeals.study;

import android.content.Context;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.quickdeals.R;
import com.example.quickdeals.ShablonFragment;
import com.example.quickdeals.daily.DailyRemindersFragment;
import com.example.quickdeals.database.timeless.entity.TimelessReminderEntity;
import com.example.quickdeals.study.adapter.GridRecyclerViewAdapter;
import com.example.quickdeals.study.containers.AlternativeWeeklyDealsContainerFragment;
import com.example.quickdeals.study.containers.DefaultWeeklyDealsContainerFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StudyFragment extends Fragment implements View.OnClickListener {
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
    private static GridRecyclerViewAdapter adapter;
    private static Context context;
    public static View parentView;
    private static TimelessReminderDCC dccFragment;
    private static TimelessReminderReview reviewFragment;
    private static boolean updated;
    public static FrameLayout r;
    private static FrameLayout frameLayout;
    private static ImageView img;
    private static ProgressBar progressBar;
    public static DefaultWeeklyDealsContainerFragment defaultFragment;
    public static AlternativeWeeklyDealsContainerFragment alternativeFragment;
    private ShablonFragment shablonFragment;

    public static void setParentView(View parentView) {
        StudyFragment.parentView = parentView;
    }

    public StudyFragment() {
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
    public static StudyFragment newInstance(String param1, String param2) {
        StudyFragment fragment = new StudyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static void setContext(Context context) {
        StudyFragment.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        Runnable r = new Runnable() {
            @Override
            public void run() {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.weekly_updated_view_container, defaultFragment).commit();
            }
        };
        Thread thread = new Thread(r);
        thread.start();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weekly_reminders, container, false);
        createAndInitContent(view);
        return view;
    }

    private void createAndInitContent(final View view) {
        context = view.getContext();
        shablonFragment = (ShablonFragment) getParentFragment();
        frameLayout = (FrameLayout) view.findViewById(R.id.weekly_updated_view_container);
        img = view.findViewById(R.id.empty_imageView);
        progressBar = view.findViewById(R.id.default_d_d_progress_bar);
        progressBar.animate();
        addButton = (FloatingActionButton) view.findViewById(R.id.add_item);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShablonFragment.showOrHideFragment(getParentFragmentManager(), dccFragment,  shablonFragment, true);
            }
        });
    }

    public static void setAlternativeView(boolean isUpdated, FragmentManager parentManager, ShablonFragment shablonFragment) {
        FragmentTransaction transaction = parentManager.beginTransaction();
        shablonFragment.setSharedElementReturnTransition(TransitionInflater.from(shablonFragment.getActivity()).inflateTransition(R.transition.change_image_transform));
        StudyFragment.alternativeFragment.setEnterTransition(TransitionInflater.from(shablonFragment.getActivity()).inflateTransition(android.R.transition.slide_top));
        transaction.setCustomAnimations(0, R.anim.exit, R.anim.enter, 0);
        if (!isUpdated) {
            alternativeFragment.rList.scrollToPosition(0);
            transaction.show(StudyFragment.alternativeFragment).commit();
        } else {
            transaction.hide(StudyFragment.alternativeFragment).commit();
        }
    }
    /*  public void restoreReminders() {
        db = Room.databaseBuilder(context, RemDatabase.class, "remind_database").build();
        dao = db.reminderDao();
        titles = ReminderEntity.getTitlesFromDatabase(dao);
        reminderEntityList = ReminderEntity.getAll(dao);
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
        NotificationService.setAdapter(adapter);
        //ReminderOptions.setContext(context);
        ReminderOptions.setAdapter(adapter);
        ReminderOptions.setDao(dao);
        ReminderOptions.setIsFirstItem(titles.size()==0);
    }
*/

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public GridRecyclerViewAdapter getAdapter() {
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

    public static void setTimelessDccFragment(TimelessReminderDCC dccFragment) {
        StudyFragment.dccFragment = dccFragment;
    }

    public static void setDefaultFragment(DefaultWeeklyDealsContainerFragment defaultFragment) {
        StudyFragment.defaultFragment = defaultFragment;
    }

    public static void setAlternativeFragment(AlternativeWeeklyDealsContainerFragment alternativeFragment) {
       StudyFragment.alternativeFragment = alternativeFragment;
    }

    public static void removeProgressBar() {
        StudyFragment.frameLayout.removeView(StudyFragment.progressBar);
    }

    public static void setEmptyContainerImage(boolean isVisible) {
        if (isVisible) {
            StudyFragment.img.animate().alpha(1).setDuration(500).start();
        } else {
            StudyFragment.img.animate().alpha(0).setDuration(500).start();
        }
    }
}
