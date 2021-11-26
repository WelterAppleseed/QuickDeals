package com.example.quickdeals.car;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.example.quickdeals.R;
import com.example.quickdeals.navigation.NavigationLayout;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CarWashFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CarWashFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    /*private ImageButton toStudyFragmentButton;
    private ImageButton toCarWashFragmentButton;
    private ImageButton toActivitiesFragmentButton;
    private ImageButton toDailyFragmentButton;*/

    public CarWashFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CarWashFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CarWashFragment newInstance(String param1, String param2) {
        CarWashFragment fragment = new CarWashFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_car_wash, container, false);
        /*LinearLayout navigationLayout = view.findViewById(R.id.navigationLayout);
        toDailyFragmentButton = navigationLayout.findViewById(R.id.dailyFragmentButton);
        toStudyFragmentButton = navigationLayout.findViewById(R.id.studyFragmentButton);
        toActivitiesFragmentButton = navigationLayout.findViewById(R.id.activitiesFragmentButton);
        toCarWashFragmentButton = navigationLayout.findViewById(R.id.carWashFragmentButton);
        toDailyFragmentButton.setOnClickListener(this);
        toActivitiesFragmentButton.setOnClickListener(this);
        toStudyFragmentButton.setOnClickListener(this);*/
        return view;
    }

    @Override
    public void onClick(View v) {
       /* if (v == toStudyFragmentButton) {
            Navigation.findNavController(getView()).navigate(R.id.action_carWashFragment2_to_studyFragment2);
        }
        if (v == toActivitiesFragmentButton) {
            Navigation.findNavController(getView()).navigate(R.id.action_carWashFragment2_to_activitiesFragment2);
        }
        if (v == toDailyFragmentButton) {
            Navigation.findNavController(getView()).navigate(R.id.action_carWashFragment2_to_dailyRemindersFragment);
        }*/
    }
}
