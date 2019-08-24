package patryk.bezpieczneauto.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import patryk.bezpieczneauto.Adapters.CustomPagerAdapter;
import patryk.bezpieczneauto.R;

public class DocumentsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_documents, container, false);

        final ViewPager viewPager = rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(getChildFragmentManager()));

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