package patryk.bezpieczneauto.Fragments;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;
import java.io.InputStream;

import patryk.bezpieczneauto.Database.DBHelper;
import patryk.bezpieczneauto.Interfaces.Cars;
import patryk.bezpieczneauto.Objects.Car;
import patryk.bezpieczneauto.R;

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

        // Do testów
        // wstawAutka(dbHelper);
        // wstawCzesci(dbHelper);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_car_data, container, false);

        name = view.findViewById(R.id.car_name);
        model = view.findViewById(R.id.car_model);
        year = view.findViewById(R.id.car_prod_date);
        capacity = view.findViewById(R.id.car_engine_capacity);
        power = view.findViewById(R.id.car_power);
        img = view.findViewById(R.id.car_image);
        imgButton = view.findViewById(R.id.car_image_button);

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

        fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDialog();
            }
        });

        // Załaduj zdjęcie auta z bazy danych
        String photoString = dbHelper.getCarPhoto();
        if(photoString != null) {
            Uri photoUri = Uri.parse(photoString);
            Bitmap carImage = rotateImage(photoUri);
            img.setImageBitmap(carImage);
        } else {
            img.setImageResource(R.drawable.ic_camera_add);
            imgButton.setVisibility(View.GONE);
        }

        if(img.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.ic_camera_add).getConstantState())
            imgButton.setVisibility(View.GONE);
        else {
            img.setOnClickListener(null);
            imgButton.setVisibility(View.VISIBLE);
        }

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Uzyskano dostęp do multimediów", Toast.LENGTH_LONG).show();
                Intent photoPickerIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
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
                Bitmap selectedImage = rotateImage(imageUri);
                img.setImageBitmap(selectedImage);
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

    public Bitmap rotateImage(Uri imgUri) {
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), imgUri);
            final InputStream imageStream = getContext().getContentResolver().openInputStream(imgUri);
            ExifInterface exif = new ExifInterface(imageStream);
            int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            }
            else if (orientation == 3) {
                matrix.postRotate(180);
            }
            else if (orientation == 8) {
                matrix.postRotate(270);
            }
            return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        } catch (OutOfMemoryError | IOException e) {
            e.printStackTrace();
            Toast.makeText(getContext(), "Coś poszło nie tak :(", Toast.LENGTH_LONG).show();
        }

        return null;
    }

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

    private void wstawAutka(DBHelper dbHelper) {
        dbHelper.insertCar("Seat", "Ibiza III", "2001", "1.9", "90", R.drawable.ic_car, 1);
        dbHelper.insertCar("Ford", "Mustang", "2019", "3.0", "399", R.drawable.ic_car, 0);
        dbHelper.insertCar("Nissan", "Micra", "2003", "1.5", "66", R.drawable.ic_car, 0);
        dbHelper.insertCar("Opel", "Signum", "2006", "3.0", "199", R.drawable.ic_car, 0);
        dbHelper.insertCar("Smart", "Fortwo", "2004", "1.5", "55", R.drawable.ic_car, 0);
        dbHelper.insertCar("Ford", "Mondeo", "2014", "2.0", "145", R.drawable.ic_car, 0);
        dbHelper.insertCar("Renault", "Clio", "2005", "2.0", "85", R.drawable.ic_car, 0);
        dbHelper.insertCar("Kia", "Stinger", "2018", "3.0", "299", R.drawable.ic_car, 0);
    }

    private void wstawCzesci(DBHelper dbHelper) {
        dbHelper.insertPart(1, "Część", "Dodatkowe info", "16.08.2019", "125");
        dbHelper.insertPart(2, "Część", "Dodatkowe info", "16.08.2019", "125");
        dbHelper.insertPart(3, "Część", "Dodatkowe info", "16.08.2019", "125");
        dbHelper.insertPart(4, "Część", "Dodatkowe info", "16.08.2019", "125");
        dbHelper.insertPart(5, "Część", "Dodatkowe info", "16.08.2019", "125");
        dbHelper.insertPart(6, "Część", "Dodatkowe info", "16.08.2019", "125");
        dbHelper.insertPart(7, "Część", "Dodatkowe info", "16.08.2019", "125");
        dbHelper.insertPart(8, "Część", "Dodatkowe info", "16.08.2019", "125");
    }
}
