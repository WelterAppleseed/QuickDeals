package com.example.quickdeals.study;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

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
import com.example.quickdeals.daily.DailyRemindersFragment;
import com.example.quickdeals.navigation.NavigationLayout;


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
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment StudyFragment.
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
        /*LinearLayout navigationLayout = view.findViewById(R.id.navigationLayout);
        toDailyFragmentButton = navigationLayout.findViewById(R.id.dailyFragmentButton);
        toStudyFragmentButton = navigationLayout.findViewById(R.id.studyFragmentButton);
        toActivitiesFragmentButton = navigationLayout.findViewById(R.id.activitiesFragmentButton);
        toCarWashFragmentButton = navigationLayout.findViewById(R.id.carWashFragmentButton);
        toDailyFragmentButton.setOnClickListener(this);
        toCarWashFragmentButton.setOnClickListener(this);
        toActivitiesFragmentButton.setOnClickListener(this);*/
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
}
