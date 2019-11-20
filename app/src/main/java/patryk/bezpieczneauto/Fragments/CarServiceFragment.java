package patryk.bezpieczneauto.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import patryk.bezpieczneauto.Adapters.DocumentsListAdapter;
import patryk.bezpieczneauto.Interfaces.Documents;
import patryk.bezpieczneauto.R;
import patryk.bezpieczneauto.database.DBHelper;
import patryk.bezpieczneauto.model.Document;

public class CarServiceFragment extends Fragment implements Documents, DocumentsListAdapter.OnDocumentListener {

    private ArrayList<Document> documents = new ArrayList<>();
    private RecyclerView recyclerView;
    private DocumentsListAdapter documentsListAdapter;
    private DBHelper dbHelper;
    private FloatingActionButton fab;
    private ArrayList<String> allCars;
    private Spinner chooseCarSpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private int spinnerSelectedItemPosition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(getContext());
        documents = dbHelper.getCarServices();
    }

    public static CarServiceFragment newInstance(String text) {

        CarServiceFragment f = new CarServiceFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_car_service, container, false);
        documentsListAdapter = new DocumentsListAdapter(getContext(), R.layout.document_listview_item, this, documents);
        recyclerView = rootView.findViewById(R.id.car_service_recyclerview_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(documentsListAdapter);

        fab = rootView.findViewById(R.id.car_service_fab);
        fab.setOnClickListener(v -> newDocumentDialog());

        return rootView;
    }

    @Override
    public void newDocumentDialog() {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_add_car_service, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(view);

        final EditText reg_nr = view.findViewById(R.id.car_service_registration_input);
        final EditText mileage = view.findViewById(R.id.car_service_mileage_input);
        final EditText dateFrom = view.findViewById(R.id.car_service_date_input);
        final EditText dateTo = view.findViewById(R.id.car_service_expiry_date_input);

        // Ustawiam proponowaną datę jako dzisiejszą i przyszły rok w EditTextach
        Calendar calendar = Calendar.getInstance();
        Date currentDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateString = getResources().getString(R.string.date_hint, dateFormat.format(currentDate));
        dateFrom.setText(dateString);
        calendar.add(Calendar.YEAR, 1);
        Date nextYear = calendar.getTime();
        String nextYearString = getResources().getString(R.string.date_hint, dateFormat.format(nextYear));
        dateTo.setText(nextYearString);

        // Lista z samymi markami
        allCars = dbHelper.getCarsNames();

        // Adapter spinnera wypełniany jest markami aut z listy allCars
        spinnerAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, allCars);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Spinner z markami aut
        chooseCarSpinner = view.findViewById(R.id.car_service_choose_car_spinner);
        chooseCarSpinner.setAdapter(spinnerAdapter);
        chooseCarSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                // Pozycja wybranego elementu na spinnerze
                spinnerSelectedItemPosition = chooseCarSpinner.getSelectedItemPosition();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

                Toast.makeText(getContext(), "Musisz wybrać auto!", Toast.LENGTH_SHORT).show();

            }
        });

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("DODAJ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                        // Wyświetl wiadomość jeżeli któreś pole jest puste
                        if (TextUtils.isEmpty(reg_nr.getText().toString()) ||
                                TextUtils.isEmpty(mileage.getText().toString()) ||
                                TextUtils.isEmpty(dateFrom.getText().toString()) ||
                                TextUtils.isEmpty(dateTo.getText().toString())) {
                            Toast.makeText(getContext(), "Musisz wypełnić każde pole!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!allCars.isEmpty()) {
                            Document mDocument = new Document(
                                    dbHelper.getCar(spinnerSelectedItemPosition + 1).getMarka() + " " + dbHelper.getCar(spinnerSelectedItemPosition + 1).getModel(),
                                    "Numer rejestracyjny: " + reg_nr.getText().toString(),
                                    "Przebieg podczas badania: " + mileage.getText().toString() + "km",
                                    dateFrom.getText().toString(),
                                    dateTo.getText().toString()
                            );

                            dbHelper.insertCarService(spinnerSelectedItemPosition + 1, mDocument);
                            documents.add(mDocument);
                            documentsListAdapter.notifyDataSetChanged();

                            Toast.makeText(getContext(), "Dodano nowy dokument", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(), "Najpierw dodaj nowe auto w zakładce \nDane pojazdu", Toast.LENGTH_LONG).show();
                        }
                    }
                })
                .setNegativeButton("ANULUJ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();
    }

    @Override
    public void editDocumentDialog(int id) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_add_car_service, null);

        final EditText reg_nr = view.findViewById(R.id.car_service_registration_input);
        final EditText mileage = view.findViewById(R.id.car_service_mileage_input);
        final EditText dateFrom = view.findViewById(R.id.car_service_date_input);
        final EditText dateTo = view.findViewById(R.id.car_service_expiry_date_input);

        final Document currentDocument = dbHelper.getCarService(id);

        allCars = new ArrayList<>();
        allCars.add(currentDocument.getAuto().toUpperCase());
        spinnerAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, allCars);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseCarSpinner = view.findViewById(R.id.car_service_choose_car_spinner);
        chooseCarSpinner.setAdapter(spinnerAdapter);

        reg_nr.setText(currentDocument.getInfo());
        mileage.setText(currentDocument.getAdditionalInfo());
        dateFrom.setText(currentDocument.getDate());
        dateTo.setText(currentDocument.getExpiryDate());

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        alertDialogBuilderUserInput.setView(view);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("ZAPISZ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int dialogID) {

                                // Wyświetl wiadomość jeżeli któreś pole jest puste
                                if (TextUtils.isEmpty(reg_nr.getText().toString()) ||
                                        TextUtils.isEmpty(mileage.getText().toString()) ||
                                        TextUtils.isEmpty(dateFrom.getText().toString()) ||
                                        TextUtils.isEmpty(dateTo.getText().toString())) {
                                    Toast.makeText(getContext(), "Musisz wypełnić każde pole!", Toast.LENGTH_SHORT).show();
                                } else {


                                    Document updatedDoc = new Document(
                                            currentDocument.getAuto(),
                                            reg_nr.getText().toString(),
                                            mileage.getText().toString(),
                                            dateFrom.getText().toString(),
                                            dateTo.getText().toString()
                                    );


                                    dbHelper.updateCarService(id, updatedDoc);
                                    documents.set(id - 1, updatedDoc);
                                    documentsListAdapter.notifyDataSetChanged();
                                    dbHelper.close();

                                    Toast.makeText(getContext(), "Zapisano dane ubezpieczenia dla auta " + currentDocument.getAuto(), Toast.LENGTH_LONG).show();
                                }

                            }
                        }
                )
                .setNegativeButton("ANULUJ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();
    }

    @Override
    public void onDocumentClick(int position) {
        editDocumentDialog(position + 1);
    }
}
