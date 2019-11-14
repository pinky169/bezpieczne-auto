package patryk.bezpieczneauto.Adapters;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import patryk.bezpieczneauto.Fragments.PrzegladyFragment;
import patryk.bezpieczneauto.Fragments.UbezpieczeniaFragment;
import patryk.bezpieczneauto.R;

public class CustomPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES =
            new int[]{R.string.insurance, R.string.car_service};
    private final Context mContext;

    public CustomPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int pos) {
        switch(pos) {
            case 0:
                return UbezpieczeniaFragment.newInstance("UbezpieczeniaFragment, 1");
            case 1:
                return PrzegladyFragment.newInstance("PrzegladyFragment, 2");
            default:
                return UbezpieczeniaFragment.newInstance("UbezpieczeniaFragment, Default");
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        return 2;
    }
}