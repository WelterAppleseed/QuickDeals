package com.example.quickdeals.navigation;

import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.navigation.Navigation;

import com.example.quickdeals.R;

public class NavigationLayout{
    private ImageButton toStudyFragmentButton;
    private ImageButton toCarWashFragmentButton;
    private ImageButton toActivitiesFragmentButton;
    private ImageButton toDailyFragmentButton;
    public View view;

    public NavigationLayout(LinearLayout layout, View view) {
        this.view = view;
        System.out.println(view.getId());
        toDailyFragmentButton = layout.findViewById(R.id.dailyFragmentButton);
        toStudyFragmentButton = layout.findViewById(R.id.studyFragmentButton);
        toActivitiesFragmentButton = layout.findViewById(R.id.activitiesFragmentButton);
        toCarWashFragmentButton = layout.findViewById(R.id.carWashFragmentButton);
        initNavigation();
    }

    private void initNavigation() {

    }

}
