package patryk.bezpieczneauto.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import patryk.bezpieczneauto.Fragments.PrzegladyFragment;
import patryk.bezpieczneauto.Fragments.UbezpieczeniaFragment;

public class CustomPagerAdapter extends FragmentPagerAdapter {

    public CustomPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos) {
            case 0: return PrzegladyFragment.newInstance("PrzegladyFragment, 1");
            case 1: return UbezpieczeniaFragment.newInstance("UbezpieczeniaFragment, 2");
            default: return PrzegladyFragment.newInstance("PrzegladyFragment, Default");
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}