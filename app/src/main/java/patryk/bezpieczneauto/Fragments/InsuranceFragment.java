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
import patryk.bezpieczneauto.Database.DBHelper;
import patryk.bezpieczneauto.Interfaces.Documents;
import patryk.bezpieczneauto.Objects.Document;
import patryk.bezpieczneauto.R;

public class InsuranceFragment extends Fragment implements Documents, DocumentsListAdapter.OnDocumentListener {

    private ArrayList<Document> documents = new ArrayList<>();
    private RecyclerView recyclerView;
    private DocumentsListAdapter documentsListAdapter;
    private FloatingActionButton fab;
    private DBHelper dbHelper;
    private ArrayList<String> allCars;
    private Spinner chooseCarSpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private int spinnerSelectedItemPosition;

    public static InsuranceFragment newInstance(String text) {

        InsuranceFragment f = new InsuranceFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);

        return f;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        dbHelper = new DBHelper(getContext());
        documents = dbHelper.getInsurances();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_insurances, container, false);
        documentsListAdapter = new DocumentsListAdapter(getContext(), R.layout.document_listview_item, this, documents);
        recyclerView = rootView.findViewById(R.id.insurance_recyclerview_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(documentsListAdapter);

        fab = rootView.findViewById(R.id.insuurance_fab);
        fab.setOnClickListener(v -> newDocumentDialog());


        return rootView;
    }

    public void newDocumentDialog() {
        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_add_insurance, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(view);

        final EditText policy = view.findViewById(R.id.insurance_policy_input);
        final EditText additionalInfo = view.findViewById(R.id.insurance_additional_info_input);
        final EditText dateFrom = view.findViewById(R.id.insurance_date_input);
        final EditText dateTo = view.findViewById(R.id.insurance_expiry_date_input);

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
        chooseCarSpinner = view.findViewById(R.id.insurance_choose_car_spinner);
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
                        if (TextUtils.isEmpty(policy.getText().toString()) ||
                                TextUtils.isEmpty(additionalInfo.getText().toString()) ||
                                TextUtils.isEmpty(dateFrom.getText().toString()) ||
                                TextUtils.isEmpty(dateTo.getText().toString())) {
                            Toast.makeText(getContext(), "Musisz wypełnić każde pole!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if (!allCars.isEmpty()) {
                            Document mDocument = new Document(
                                    dbHelper.getCar(spinnerSelectedItemPosition + 1).getMarka() + " " + dbHelper.getCar(spinnerSelectedItemPosition + 1).getModel(),
                                    "Numer polisy: " + policy.getText().toString(),
                                    additionalInfo.getText().toString(),
                                    dateFrom.getText().toString(),
                                    dateTo.getText().toString()
                            );

                            dbHelper.insertInsurance(spinnerSelectedItemPosition + 1, mDocument);
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
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_add_insurance, null);

        final EditText policy = view.findViewById(R.id.insurance_policy_input);
        final EditText additionalInfo = view.findViewById(R.id.insurance_additional_info_input);
        final EditText dateFrom = view.findViewById(R.id.insurance_date_input);
        final EditText dateTo = view.findViewById(R.id.insurance_expiry_date_input);

        final Document currentDocument = dbHelper.getInsurance(id);

        allCars = new ArrayList<>();
        allCars.add(currentDocument.getAuto().toUpperCase());
        spinnerAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, allCars);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        chooseCarSpinner = view.findViewById(R.id.insurance_choose_car_spinner);
        chooseCarSpinner.setAdapter(spinnerAdapter);

        policy.setText(currentDocument.getInfo());
        additionalInfo.setText(currentDocument.getAdditionalInfo());
        dateFrom.setText(currentDocument.getDate());
        dateTo.setText(currentDocument.getExpiryDate());

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        alertDialogBuilderUserInput.setView(view);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("ZAPISZ", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int dialogID) {

                                // Wyświetl wiadomość jeżeli któreś pole jest puste
                                if (TextUtils.isEmpty(policy.getText().toString()) ||
                                        TextUtils.isEmpty(additionalInfo.getText().toString()) ||
                                        TextUtils.isEmpty(dateFrom.getText().toString()) ||
                                        TextUtils.isEmpty(dateTo.getText().toString())) {
                                    Toast.makeText(getContext(), "Musisz wypełnić każde pole!", Toast.LENGTH_SHORT).show();
                                } else {


                                    Document updatedDoc = new Document(
                                            currentDocument.getAuto(),
                                            policy.getText().toString(),
                                            additionalInfo.getText().toString(),
                                            dateFrom.getText().toString(),
                                            dateTo.getText().toString()
                                    );


                                    dbHelper.updateInsurance(id, updatedDoc);
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
