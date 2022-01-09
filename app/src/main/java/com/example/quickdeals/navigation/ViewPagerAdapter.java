package com.example.quickdeals.navigation;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.quickdeals.ContainerFragment;
import com.example.quickdeals.daily.DailyRemindersFragment;
import com.example.quickdeals.weekly.WeeklyRemindersFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private static final int CARD_ITEM_SIZE = 10;

    public ViewPagerAdapter(@NonNull ContainerFragment fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) {
                return new DailyRemindersFragment();
            } else {
                return new WeeklyRemindersFragment();
            }
        }

    @Override
    public int getItemCount() {
        return CARD_ITEM_SIZE;
    }
}
