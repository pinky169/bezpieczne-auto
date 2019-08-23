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

public class UbezpieczeniaFragment extends Fragment {

    private ArrayList<Document> documents = new ArrayList<>();
    private ListView listView;
    private DocumentsListAdapter documentsListAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Document doc = new Document(
                "Seat Ibiza III",
                "UBEZPIECZENIE 2019",
                "PZU UBEZPIECZENIA",
                "16.08.2019",
                "15.08.2020"
        );

        for (int i=0; i<10; i++)
            documents.add(doc);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ubezpieczenia, container, false);
        documentsListAdapter = new DocumentsListAdapter(getContext(), R.layout.listview_item, documents);
        listView = rootView.findViewById(R.id.ubezpieczenia_listview_id);
        listView.setAdapter(documentsListAdapter);

        return rootView;
    }

    public static UbezpieczeniaFragment newInstance(String text) {

        UbezpieczeniaFragment f = new UbezpieczeniaFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }
}
