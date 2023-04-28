package com.example.fypapplication1.Adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.fypapplication1.CardOptions.TimetableActivity;
import com.example.fypapplication1.Fragments.FridayFragment;
import com.example.fypapplication1.Fragments.MondayFragment;
import com.example.fypapplication1.Fragments.ThursdayFragment;
import com.example.fypapplication1.Fragments.TuesdayFragment;
import com.example.fypapplication1.Fragments.WednesdayFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {

    public ViewPagerAdapter(@NonNull TimetableActivity fragment) {
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new MondayFragment();
            case 1:
                return new TuesdayFragment();
            case 2:
                return new WednesdayFragment();
            case 3:
                return new ThursdayFragment();
            case 4:
                return new FridayFragment();
            default:
                return new MondayFragment();

        }
    }

    @Override
    public int getItemCount() {
        return 5;
    }
}
