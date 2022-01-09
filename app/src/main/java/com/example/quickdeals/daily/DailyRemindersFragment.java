package com.example.quickdeals.daily;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.example.quickdeals.ContainerFragment;
import com.example.quickdeals.R;
import com.example.quickdeals.daily.container.AlternativeDailyDealsContainerFragment;
import com.example.quickdeals.daily.container.DefaultDailyDealsContainerFragment;
import com.example.quickdeals.database.temporary.RemDatabase;
import com.example.quickdeals.database.temporary.dao.ReminderDao;
import com.example.quickdeals.database.temporary.entity.ReminderEntity;
import com.example.quickdeals.utils.adapters.RecyclerItemAdapter;
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
    private static RecyclerItemAdapter adapter;
    private static Context context;
    private static List<ReminderEntity> reminderEntityList;
    private RemDatabase db;
    private ReminderDao dao;
    private static ItemTouchHelper itemTouchHelper;
    public static View parentView;
    private static ReminderDCC dccFragment;
    private static ReminderReview reviewFragment;
    private static boolean updated;
    public static FrameLayout r;
    private static FrameLayout frameLayout;
    private static ImageView img;
    private static ProgressBar progressBar;
    public static DefaultDailyDealsContainerFragment defaultFragment;
    public static AlternativeDailyDealsContainerFragment alternativeFragment;
    private ContainerFragment containerFragment;

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
        Runnable r = new Runnable() {
            @Override
            public void run() {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.updated_view_container, defaultFragment).commit();
            }
        };
        Thread thread = new Thread(r);
        thread.start();
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
        containerFragment = (ContainerFragment) getParentFragment();
        frameLayout = (FrameLayout) view.findViewById(R.id.updated_view_container);
        img = view.findViewById(R.id.empty_imageView);
        progressBar = view.findViewById(R.id.default_d_d_progress_bar);
        progressBar.animate();
        addButton = (FloatingActionButton) view.findViewById(R.id.add_item);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContainerFragment.showOrHideFragment(getParentFragmentManager(), dccFragment, containerFragment, true);
            }
        });
        ContainerFragment.setParentManager(getParentFragmentManager());
    }

    public static void setAlternativeView(boolean isUpdated, FragmentManager parentManager, ContainerFragment containerFragment) {
        FragmentTransaction transaction = parentManager.beginTransaction();
        containerFragment.setSharedElementReturnTransition(TransitionInflater.from(containerFragment.getActivity()).inflateTransition(R.transition.change_image_transform));
        DailyRemindersFragment.alternativeFragment.setEnterTransition(TransitionInflater.from(containerFragment.getActivity()).inflateTransition(android.R.transition.slide_top));
        transaction.setCustomAnimations(0, R.anim.exit, R.anim.enter, 0);
        if (!isUpdated) {
            alternativeFragment.rList.scrollToPosition(0);
            transaction.show(DailyRemindersFragment.alternativeFragment).commit();
        } else {
            transaction.hide(DailyRemindersFragment.alternativeFragment).commit();
        }
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

    public static void setDccFragment(ReminderDCC dccFragment) {
        DailyRemindersFragment.dccFragment = dccFragment;
        System.out.println(dccFragment);
    }

    public static void setDefaultFragment(DefaultDailyDealsContainerFragment defaultFragment) {
        DailyRemindersFragment.defaultFragment = defaultFragment;
    }

    public static void setAlternativeFragment(AlternativeDailyDealsContainerFragment alternativeFragment) {
        DailyRemindersFragment.alternativeFragment = alternativeFragment;
    }

    public static void removeProgressBar() {
        DailyRemindersFragment.frameLayout.removeView(DailyRemindersFragment.progressBar);
    }

    public static void setEmptyContainerImage(boolean isVisible) {
        if (isVisible) {
            DailyRemindersFragment.img.animate().alpha(1).setDuration(500).start();
        } else {
            DailyRemindersFragment.img.animate().alpha(0).setDuration(500).start();
        }
    }
}
