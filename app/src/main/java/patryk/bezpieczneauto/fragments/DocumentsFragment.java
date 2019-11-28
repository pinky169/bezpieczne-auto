package patryk.bezpieczneauto.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import patryk.bezpieczneauto.R;
import patryk.bezpieczneauto.adapters.CustomPagerAdapter;

public class DocumentsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_documents, container, false);

        final ViewPager viewPager = rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(getContext(), getChildFragmentManager()));

        TabLayout tabLayout = rootView.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        return rootView;
    }
}