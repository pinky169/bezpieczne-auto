package patryk.bezpieczneauto.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import patryk.bezpieczneauto.Adapters.CustomPagerAdapter;
import patryk.bezpieczneauto.R;

public class DocumentsFragment extends Fragment {

    public DocumentsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_documents, container, false);

        ViewPager viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        viewPager.setAdapter(new CustomPagerAdapter(getContext()));

        return rootView;
    }
}