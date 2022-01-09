package com.example.quickdeals.weekly;

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
import com.example.quickdeals.ContainerFragment;
import com.example.quickdeals.utils.adapters.GridRecyclerViewAdapter;
import com.example.quickdeals.weekly.containers.AlternativeWeeklyDealsContainerFragment;
import com.example.quickdeals.weekly.containers.DefaultWeeklyDealsContainerFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class WeeklyRemindersFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private FloatingActionButton addButton;
    private static GridRecyclerViewAdapter adapter;
    private static Context context;
    private static WeeklyReminderDCC dccFragment;
    public static FrameLayout r;
    private static FrameLayout frameLayout;
    private static ImageView img;
    private static ProgressBar progressBar;
    private static DefaultWeeklyDealsContainerFragment defaultFragment;
    private static AlternativeWeeklyDealsContainerFragment alternativeFragment;
    private ContainerFragment containerFragment;

    public WeeklyRemindersFragment() {
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
    public static WeeklyRemindersFragment newInstance(String param1, String param2) {
        WeeklyRemindersFragment fragment = new WeeklyRemindersFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static void setContext(Context context) {
        WeeklyRemindersFragment.context = context;
    }

    public static void setAdapter(GridRecyclerViewAdapter adapter) {
        WeeklyRemindersFragment.adapter = adapter;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        containerFragment = (ContainerFragment) getParentFragment();
        frameLayout = view.findViewById(R.id.weekly_updated_view_container);
        img = view.findViewById(R.id.empty_imageView);
        progressBar = view.findViewById(R.id.default_d_d_progress_bar);
        progressBar.animate();
        addButton = view.findViewById(R.id.add_item);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ContainerFragment.showOrHideFragment(getParentFragmentManager(), dccFragment, containerFragment, true);
            }
        });
    }

    public static void setAlternativeView(boolean isUpdated, FragmentManager parentManager, ContainerFragment containerFragment) {
        FragmentTransaction transaction = parentManager.beginTransaction();
        containerFragment.setSharedElementReturnTransition(TransitionInflater.from(containerFragment.getActivity()).inflateTransition(R.transition.change_image_transform));
        WeeklyRemindersFragment.alternativeFragment.setEnterTransition(TransitionInflater.from(containerFragment.getActivity()).inflateTransition(android.R.transition.slide_top));
        transaction.setCustomAnimations(0, R.anim.exit, R.anim.enter, 0);
        if (!isUpdated) {
            alternativeFragment.rList.scrollToPosition(0);
            transaction.show(WeeklyRemindersFragment.alternativeFragment).commit();
        } else {
            transaction.hide(WeeklyRemindersFragment.alternativeFragment).commit();
        }
    }

    @Nullable
    @Override
    public Context getContext() {
        return context;
    }

    public GridRecyclerViewAdapter getAdapter() {
        return adapter;
    }

    public static void setTimelessDccFragment(WeeklyReminderDCC dccFragment) {
        WeeklyRemindersFragment.dccFragment = dccFragment;
    }

    public static void setDefaultFragment(DefaultWeeklyDealsContainerFragment defaultFragment) {
        WeeklyRemindersFragment.defaultFragment = defaultFragment;
    }

    public static void setAlternativeFragment(AlternativeWeeklyDealsContainerFragment alternativeFragment) {
       WeeklyRemindersFragment.alternativeFragment = alternativeFragment;
    }

    public static void removeProgressBar() {
        WeeklyRemindersFragment.frameLayout.removeView(WeeklyRemindersFragment.progressBar);
    }

    public static void setEmptyContainerImage(boolean isVisible) {
        if (isVisible) {
            WeeklyRemindersFragment.img.animate().alpha(1).setDuration(500).start();
        } else {
            WeeklyRemindersFragment.img.animate().alpha(0).setDuration(500).start();
        }
    }
}
