package Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import Fragments.Fragment1;
import Fragments.Fragment2;
import Fragments.Fragment3;

/**
 * Created by Deon-Mass on 18/02/2018.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    int NbrOfTabs;
    public ViewPagerAdapter(FragmentManager fm, int NbrOfTabs) {
        super(fm);
        this.NbrOfTabs = NbrOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                Fragment1 fragment1 = new Fragment1();
                return fragment1;
            case 1:
                Fragment2 fragment2 = new Fragment2();
                return fragment2;
            case 2:
                Fragment3 fragment3 = new Fragment3();
                return fragment3;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return NbrOfTabs;
    }
}
