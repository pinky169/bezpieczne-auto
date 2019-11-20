package patryk.bezpieczneauto.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

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

        ImageButton swipeLeft = rootView.findViewById(R.id.left_nav);
        ImageButton swipeRight = rootView.findViewById(R.id.right_nav);

        swipeLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.arrowScroll(View.FOCUS_LEFT);
                // Toast.makeText(getContext(), "PRZEGLĄDY", Toast.LENGTH_LONG).show();
            }
        });

        swipeRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.arrowScroll(View.FOCUS_RIGHT);
                // Toast.makeText(getContext(), "UBEZPIECZENIA", Toast.LENGTH_LONG).show();
            }
        });

        return rootView;
    }
}