package com.example.quickdeals.navigation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.quickdeals.ShablonFragment;
import com.example.quickdeals.activities.ActivitiesFragment;
import com.example.quickdeals.car.CarWashFragment;
import com.example.quickdeals.daily.DailyRemindersFragment;
import com.example.quickdeals.study.StudyFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 10;

    public ViewPagerAdapter(@NonNull ShablonFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
                return new DailyRemindersFragment();
            } else {
                return new StudyFragment();
            }
        }

    @Override
    public int getItemCount() {
        return CARD_ITEM_SIZE;
    }
}
