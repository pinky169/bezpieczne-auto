package patryk.bezpieczneauto.Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import patryk.bezpieczneauto.Adapters.DocumentsListAdapter;
import patryk.bezpieczneauto.Objects.Document;
import patryk.bezpieczneauto.R;

public class PrzegladyFragment extends Fragment {

    private ArrayList<Document> documents = new ArrayList<>();
    private ListView listView;
    private DocumentsListAdapter documentsListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Document doc = new Document(
                "Seat Ibiza III",
                "PRZEGLĄD 2019",
                "Stan licznika podczas badania: 309745km",
                "18.08.2019",
                "17.08.2020"
        );

        documents.add(doc);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_przeglady, container, false);
        documentsListAdapter = new DocumentsListAdapter(getContext(), R.layout.listview_item, documents);
        listView = rootView.findViewById(R.id.przeglady_listview_id);
        listView.setAdapter(documentsListAdapter);

        return rootView;
    }

    public static PrzegladyFragment newInstance(String text) {

        PrzegladyFragment f = new PrzegladyFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
