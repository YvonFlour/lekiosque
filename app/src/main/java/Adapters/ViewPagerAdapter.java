package Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import Fragments.Fragment_Articles;
import Fragments.Fragment_MesArticles;
import Fragments.Fragment_favoris;

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
                Fragment_Articles fragmentArticles = new Fragment_Articles();
                return fragmentArticles;
            case 1:
                Fragment_MesArticles fragmentMesArticles = new Fragment_MesArticles();
                return fragmentMesArticles;
            case 2:
                Fragment_favoris fragment3 = new Fragment_favoris();
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
