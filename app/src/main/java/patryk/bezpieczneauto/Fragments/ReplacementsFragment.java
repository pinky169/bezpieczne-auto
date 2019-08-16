package patryk.bezpieczneauto.Fragments;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import patryk.bezpieczneauto.Adapters.ExpandableListAdapter;
import patryk.bezpieczneauto.Database.DBHelper;
import patryk.bezpieczneauto.Interfaces.mDialogInterface;
import patryk.bezpieczneauto.Objects.Car;
import patryk.bezpieczneauto.Objects.CarPart;
import patryk.bezpieczneauto.R;

public class ReplacementsFragment extends Fragment implements mDialogInterface {

    private ArrayList<Car> carData;
    private ArrayList<String> allCars;
    private HashMap<Car, List<CarPart>> carParts;
    private ExpandableListAdapter adapter;
    private DBHelper dbHelper;
    private Spinner chooseCarSpinner;
    private ArrayAdapter<String> spinnerAdapter;
    private int spinnerSelectedItemPosition;
    private ArrayList<CarPart> partsList;
    private mDialogInterface dialogInterface;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        dialogInterface = (mDialogInterface) this;
        dbHelper = new DBHelper(getContext());

        // Lista aut (obiektów Car)
        carData = dbHelper.getAllCars();

        if (carData != null)
        {
            carParts = new HashMap<>();

            for(int i=0; i < carData.size(); i++) {
                partsList = dbHelper.getSpecficCarParts(i+1);
                carParts.put(carData.get(i), partsList);
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_replacements, container, false);
        ExpandableListView expandableListView = rootView.findViewById(R.id.expandableList);
        adapter = new ExpandableListAdapter(getContext(), dialogInterface, R.layout.list_group, R.layout.list_child, carData, carParts);
        expandableListView.setAdapter(adapter);

        // Przycisk dodawania nowych części
        FloatingActionButton fabAddPart = rootView.findViewById(R.id.add_new_part_fab_id);
        fabAddPart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPartDialog();
            }
        });

        return rootView;
    }

    // Dodawanie nowej części dla wybranego auta
    public void addPartDialog() {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_add_part, null);

        final AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(Objects.requireNonNull(getContext()));
        alertDialogBuilderUserInput.setView(view);

        final EditText partNew = view.findViewById(R.id.dialog_new_part_input);
        final EditText partAdditionalInfo = view.findViewById(R.id.dialog_additional_info_input);
        final EditText partReplacementDate = view.findViewById(R.id.dialog_replacement_date_input);
        final EditText partPrice = view.findViewById(R.id.dialog_part_price_input);

        // Ustawiam proponowaną datę jako dzisiejszą w EditText
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault());
        String dateString = getResources().getString(R.string.date_hint, dateFormat.format(currentDate));
        partReplacementDate.setText(dateString);

        // Lista z samymi markami
        allCars = dbHelper.getCarsNames();

        // Adapter spinnera wypełniany jest markami aut z listy allCars
        spinnerAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_spinner_item, allCars);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Spinner z markami aut
        chooseCarSpinner = view.findViewById(R.id.choose_car_spinner_id);
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
                        if (TextUtils.isEmpty(partNew.getText().toString()) ||
                                TextUtils.isEmpty(partReplacementDate.getText().toString()) ||
                                TextUtils.isEmpty(partPrice.getText().toString()) ||
                                chooseCarSpinner.getSelectedItem() == null) {
                            Toast.makeText(getContext(), "Musisz wypełnić każde pole oraz wybrać auto!", Toast.LENGTH_SHORT).show();
                            return;
                        } else {

                            if (!allCars.isEmpty()) {

                                dbHelper.insertPart(
                                        spinnerSelectedItemPosition+1,
                                        partAdditionalInfo.getText().toString(),
                                        partNew.getText().toString(),
                                        partReplacementDate.getText().toString(),
                                        partPrice.getText().toString()
                                );

                                ArrayList<CarPart> partsList = dbHelper.getSpecficCarParts(spinnerSelectedItemPosition+1);
                                carParts.put(carData.get(spinnerSelectedItemPosition), partsList);
                                adapter.notifyDataSetChanged();
                            }

                            Toast.makeText(getContext(), "Dodano nową część dla auta " +
                                    allCars.get(spinnerSelectedItemPosition), Toast.LENGTH_LONG).show();
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

    // Edytowanie danych auta o podanym id
    public void editCarDialog(final int id) {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getContext());
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_add_car, null);

        final EditText carName = view.findViewById(R.id.car_marka_input);
        final EditText carModel = view.findViewById(R.id.car_model_input);
        final EditText carYear = view.findViewById(R.id.car_rok_input);
        final EditText carCapacity = view.findViewById(R.id.car_pojemnosc_input);
        final EditText carPower = view.findViewById(R.id.car_moc_input);
        final CheckBox checkBox = view.findViewById(R.id.main_car_checkbox);

        final Car currentCar = dbHelper.getCar(id);
        carName.setText(currentCar.getMarka());
        carModel.setText(currentCar.getModel());
        carYear.setText(currentCar.getRok_produkcji());
        carCapacity.setText(currentCar.getPojemnosc());
        carPower.setText(currentCar.getMoc());

        if(currentCar.isMainCar() > 0) {
            checkBox.setChecked(true);
        }

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(view);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("ZAPISZ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int dialogID) {

                        // Wyświetl wiadomość jeżeli któreś pole jest puste
                        if (TextUtils.isEmpty(carName.getText().toString()) ||
                                TextUtils.isEmpty(carModel.getText().toString()) ||
                                TextUtils.isEmpty(carYear.getText().toString()) ||
                                TextUtils.isEmpty(carCapacity.getText().toString()) ||
                                TextUtils.isEmpty(carPower.getText().toString())) {
                            Toast.makeText(getContext(), "Musisz wypełnić każde pole!", Toast.LENGTH_SHORT).show();
                            return;
                        } else {

                            if (checkBox.isChecked()) {

                                Car updatedCar = new Car(
                                        carName.getText().toString(),
                                        carModel.getText().toString(),
                                        carYear.getText().toString(),
                                        carCapacity.getText().toString(),
                                        carPower.getText().toString(),
                                        R.drawable.ic_car,
                                        1
                                );

                                dbHelper.updateCar(id, updatedCar);
                                dbHelper.setMainCar(id);
                                carData.set(id-1, updatedCar);
                                carParts.put(carData.get(id-1), dbHelper.getSpecficCarParts(id));
                                adapter.notifyDataSetChanged();

                                Toast.makeText(getContext(), "Zmieniono dane auta " +
                                        currentCar.getMarka() + " (" + currentCar.getModel() +
                                        ") oraz ustawiono jako auto główne", Toast.LENGTH_LONG).show();

                            } else {

                                Car updatedCar = new Car(
                                        carName.getText().toString(),
                                        carModel.getText().toString(),
                                        carYear.getText().toString(),
                                        carCapacity.getText().toString(),
                                        carPower.getText().toString(),
                                        R.drawable.ic_car,
                                        0
                                );

                                dbHelper.updateCar(id, updatedCar);
                                carData.set(id-1, updatedCar);
                                carParts.put(carData.get(id-1), dbHelper.getSpecficCarParts(id));
                                adapter.notifyDataSetChanged();

                                Toast.makeText(getContext(), "Zmieniono dane auta " + currentCar.getMarka() + " (" + currentCar.getModel() + ")", Toast.LENGTH_LONG).show();
                            }

                            dbHelper.close();
                        }
                    }
                })
                .setNegativeButton("ANULUJ",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialogBox, int id) {
                                dialogBox.cancel();
                            }
                        })
                .setNeutralButton("USUŃ",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dbHelper.deletePart(id);
//                                dbHelper.deleteCar(id);
//                                carData = dbHelper.getAllCars();
//                                adapter.notifyDataSetChanged();
//
//                                Toast.makeText(getContext(), "Usunięto auto " +
//                                        currentCar.getMarka() + " (" +
//                                        currentCar.getModel() + ")", Toast.LENGTH_LONG).show();

                                Toast.makeText(getContext(), "TO-DO", Toast.LENGTH_LONG).show();
                            }
                        });

        final AlertDialog alertDialog = alertDialogBuilderUserInput.create();
        alertDialog.show();
    }
}
