package patryk.bezpieczneauto.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import patryk.bezpieczneauto.R;
import patryk.bezpieczneauto.database.DBHelper;
import patryk.bezpieczneauto.interfaces.Cars;
import patryk.bezpieczneauto.model.Car;

public class CarDataFragment extends Fragment implements Cars {

    private static final int CAMERA_REQUEST = 1337;
    private static final int CAMERA_PERMISSION_CODE = 111;

    private TextView name, model, year, capacity, power;
    private FloatingActionButton fab;
    private DBHelper dbHelper;
    private ImageView img;
    private ImageButton imgButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_car_data, container, false);

        name = rootView.findViewById(R.id.car_name);
        model = rootView.findViewById(R.id.car_model);
        year = rootView.findViewById(R.id.car_prod_date);
        capacity = rootView.findViewById(R.id.car_engine_capacity);
        power = rootView.findViewById(R.id.car_power);
        img = rootView.findViewById(R.id.car_image);
        imgButton = rootView.findViewById(R.id.car_image_button);

        // Listeners
        img.setOnClickListener(requestCarImage);
        imgButton.setOnClickListener(requestCarImage);

        Car car = dbHelper.getMainCar();
        if (car != null) {
            name.setText(car.getMarka());
            model.setText(car.getModel());
            year.setText(car.getRok_produkcji());
            capacity.setText(car.getPojemnosc());
            power.setText(car.getMoc());
            dbHelper.close();
        } else {
            Toast.makeText(getContext(), "Dodaj nowe auto główne przyciskiem po prawej stronie :)", Toast.LENGTH_LONG).show();
        }

        fab = rootView.findViewById(R.id.fab);
        fab.setOnClickListener(view -> addDialog());

        // Załaduj zdjęcie auta z bazy danych
        String photoString = dbHelper.getCarPhoto();
        if(photoString != null) {
            Uri photoUri = Uri.parse(photoString);
            Glide.with(CarDataFragment.this)
                    .load(photoUri)
                    .fitCenter()
                    .into(img);
            img.setTag("img-from-phone");
            imgButton.setTag("img-from-phone");
        } else {
            img.setImageResource(R.drawable.ic_camera_add);
            img.setTag("R.drawable.ic_camera_add");
            imgButton.setVisibility(View.GONE);
        }

        if (img.getTag() != imgButton.getTag())
            imgButton.setVisibility(View.GONE);
        else {
            img.setOnClickListener(null);
            imgButton.setVisibility(View.VISIBLE);
        }

        return rootView;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Uzyskano dostęp do multimediów", Toast.LENGTH_LONG).show();
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, CAMERA_REQUEST);
            } else {
                Toast.makeText(getContext(), "Odmówiono dostępu do multimediów", Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
            if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK)
            {
                final Uri imageUri = data.getData();
                Glide.with(CarDataFragment.this)
                        .load(imageUri)
                        .fitCenter()
                        .into(img);
                img.setOnClickListener(null);
                imgButton.setVisibility(View.VISIBLE);
                dbHelper.insertCarPhoto(imageUri);
            }
    }

    private View.OnClickListener requestCarImage = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, CAMERA_PERMISSION_CODE);
            }
            else
            {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, CAMERA_REQUEST);
            }
        }
    };

    public void addDialog() {

        LayoutInflater layoutInflaterAndroid = LayoutInflater.from(getActivity());
        View view = layoutInflaterAndroid.inflate(R.layout.dialog_add_car, null);

        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getContext());
        alertDialogBuilderUserInput.setView(view);

        final EditText carName = view.findViewById(R.id.car_marka_input);
        final EditText carModel = view.findViewById(R.id.car_model_input);
        final EditText carYear = view.findViewById(R.id.car_rok_input);
        final EditText carCapacity = view.findViewById(R.id.car_pojemnosc_input);
        final EditText carPower = view.findViewById(R.id.car_moc_input);

        final CheckBox checkBox = view.findViewById(R.id.main_car_checkbox);

        alertDialogBuilderUserInput
                .setCancelable(false)
                .setPositiveButton("DODAJ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogBox, int id) {

                        // Wyświetl wiadomość jeżeli któreś pole jest puste
                        if (TextUtils.isEmpty(carName.getText().toString()) ||
                                TextUtils.isEmpty(carModel.getText().toString()) ||
                                TextUtils.isEmpty(carYear.getText().toString()) ||
                                TextUtils.isEmpty(carCapacity.getText().toString()) ||
                                TextUtils.isEmpty(carPower.getText().toString())) {
                            Toast.makeText(getContext(), "Musisz wypełnić każde pole!", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        if(checkBox.isChecked()) {

                            // SetMainCar przyjmuje ID,
                            // a insertCar zwraca ID aktualnie dodanej wartości
                            dbHelper.setMainCar(
                                    dbHelper.insertCar(
                                            carName.getText().toString(),
                                            carModel.getText().toString(),
                                            carYear.getText().toString(),
                                            carCapacity.getText().toString(),
                                            carPower.getText().toString(),
                                            R.drawable.ic_car,
                                            1
                                    )
                            );

                            dbHelper.close();

                            // Odśwież view z nowym główny autem
                            FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.detach(CarDataFragment.this).attach(CarDataFragment.this).commit();

                            Toast.makeText(getContext(), "Dodano nowe główne auto!", Toast.LENGTH_LONG).show();

                        } else {

                            dbHelper.insertCar(
                                    carName.getText().toString(),
                                    carModel.getText().toString(),
                                    carYear.getText().toString(),
                                    carCapacity.getText().toString(),
                                    carPower.getText().toString(),
                                    R.drawable.ic_car,
                                    0
                            );

                            dbHelper.close();
                            Toast.makeText(getContext(), "Dodano nowe auto!", Toast.LENGTH_LONG).show();
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
    public void editDialog(int id) {
        // TO-DO
    }
}
