package patryk.bezpieczneauto.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import java.util.ArrayList;

import patryk.bezpieczneauto.R;
import patryk.bezpieczneauto.model.Car;
import patryk.bezpieczneauto.model.CarPart;
import patryk.bezpieczneauto.model.Document;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "CarsDB.db";
    private static final int DATABASE_VERSION = 1;
    private static final String CARS_TABLE_NAME = "cars";
    private static final String CARS_COLUMN_ID = "id";
    private static final String CARS_COLUMN_MARKA = "marka";
    private static final String CARS_COLUMN_MODEL = "model";
    private static final String CARS_COLUMN_ROK_PRODUKCJI = "rok";
    private static final String CARS_COLUMN_POJEMNOSC = "pojemnosc";
    private static final String CARS_COLUMN_MOC_SILNIKA = "moc";
    private static final String CARS_COLUMN_IKONA = "ikona";
    private static final String CARS_COLUMN_IS_MAIN_CAR = "glowne";

    private static final String PARTS_TABLE_NAME = "parts";
    private static final String PARTS_COLUMN_ID = "part_id";
    private static final String PARTS_COLUMN_NEW_PART_NAME = "new_part_name";
    private static final String PARTS_COLUMN_ADDITIONAL_INFO = "additional_info";
    private static final String PARTS_COLUMN_REPLACEMENT_DATE = "replacement_date";
    private static final String PARTS_COLUMN_PRICE = "price";

    private static final String CAR_PHOTO_TABLE = "mainCarPhoto";
    private static final String CAR_PHOTO_ID = "photo_id";
    private static final String CAR_PHOTO = "photo";

    private static final String INSURANCE_TABLE = "insurance";
    private static final String INSURANCE_ID = "insurance_id";
    private static final String INSURANCE_CAR_ID = "car_id";
    private static final String INSURANCE_CAR_NAME = "car";
    private static final String INSURANCE_POLICY_NR = "policy";
    private static final String INSURANCE_ADDITIONAL_INFO = "additional_info";
    private static final String INSURANCE_DATE_FROM = "date_from";
    private static final String INSURANCE_DATE_TO = "date_to";

    private static final String CAR_SERVICE_TABLE = "service";
    private static final String CAR_SERVICE_ID = "service_id";
    private static final String CAR_SERVICE_CAR_ID = "car_id";
    private static final String CAR_SERVICE_CAR_NAME = "car";
    private static final String CAR_SERVICE_REG_NR = "reg_nr";
    private static final String CAR_SERVICE_MILEAGE = "mileage";
    private static final String CAR_SERVICE_DATE_FROM = "date_from";
    private static final String CAR_SERVICE_DATE_TO = "date_to";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + CARS_TABLE_NAME + " (" +
                CARS_COLUMN_ID + " INTEGER primary key autoincrement, " +
                CARS_COLUMN_MARKA + " TEXT NOT NULL, " +
                CARS_COLUMN_MODEL + " TEXT NOT NULL, " +
                CARS_COLUMN_ROK_PRODUKCJI + " TEXT NOT NULL, " +
                CARS_COLUMN_POJEMNOSC + " TEXT NOT NULL, " +
                CARS_COLUMN_MOC_SILNIKA + " TEXT NOT NULL, " +
                CARS_COLUMN_IKONA + " INTEGER NOT NULL, " +
                CARS_COLUMN_IS_MAIN_CAR + " INTEGER DEFAULT 0);"
        );

        db.execSQL("CREATE TABLE " + PARTS_TABLE_NAME + " (" +
                PARTS_COLUMN_ID + " integer, " +
                PARTS_COLUMN_NEW_PART_NAME + " TEXT NOT NULL, " +
                PARTS_COLUMN_ADDITIONAL_INFO + " TEXT, " +
                PARTS_COLUMN_REPLACEMENT_DATE + " TEXT NOT NULL, " +
                PARTS_COLUMN_PRICE + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + PARTS_COLUMN_ID + ") REFERENCES " + CARS_TABLE_NAME + "(" + CARS_COLUMN_ID + "));"
        );

        db.execSQL("CREATE TABLE " + CAR_PHOTO_TABLE + " (" +
                CAR_PHOTO_ID + " integer primary key autoincrement, " +
                CAR_PHOTO + " TEXT);"
        );

        db.execSQL("CREATE TABLE " + INSURANCE_TABLE + " (" +
                INSURANCE_ID + " integer primary key autoincrement, " +
                INSURANCE_CAR_ID + " integer, " +
                INSURANCE_CAR_NAME + " TEXT NOT NULL, " +
                INSURANCE_POLICY_NR + " TEXT NOT NULL, " +
                INSURANCE_ADDITIONAL_INFO + " TEXT NOT NULL, " +
                INSURANCE_DATE_FROM + " TEXT NOT NULL, " +
                INSURANCE_DATE_TO + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + INSURANCE_CAR_ID + ") REFERENCES " + CARS_TABLE_NAME + "(" + CARS_COLUMN_ID + "));"
        );

        db.execSQL("CREATE TABLE " + CAR_SERVICE_TABLE + " (" +
                CAR_SERVICE_ID + " integer primary key autoincrement, " +
                CAR_SERVICE_CAR_ID + " integer, " +
                CAR_SERVICE_CAR_NAME + " TEXT NOT NULL, " +
                CAR_SERVICE_REG_NR + " TEXT NOT NULL, " +
                CAR_SERVICE_MILEAGE + " TEXT NOT NULL, " +
                CAR_SERVICE_DATE_FROM + " TEXT NOT NULL, " +
                CAR_SERVICE_DATE_TO + " TEXT NOT NULL, " +
                "FOREIGN KEY (" + CAR_SERVICE_CAR_ID + ") REFERENCES " + CARS_TABLE_NAME + "(" + CARS_COLUMN_ID + "));"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + CARS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + PARTS_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CAR_PHOTO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + INSURANCE_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + CAR_SERVICE_TABLE);
        onCreate(db);
    }

    public long insertCar(String marka, String model, String rok, String pojemnosc, String moc, int icon_res_id, int czy_glowne) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("marka", marka);
        contentValues.put("model", model);
        contentValues.put("rok", rok);
        contentValues.put("pojemnosc", pojemnosc);
        contentValues.put("moc", moc);
        contentValues.put("ikona", icon_res_id);
        contentValues.put("glowne", czy_glowne);
        return db.insert(CARS_TABLE_NAME, null, contentValues);
    }

    public void updateCar(long id, Car car) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("marka", car.getMarka());
        contentValues.put("model", car.getModel());
        contentValues.put("rok", car.getRok_produkcji());
        contentValues.put("pojemnosc", car.getPojemnosc());
        contentValues.put("moc", car.getMoc());
        contentValues.put("ikona", car.getImg_resource());
        contentValues.put("glowne", car.isMainCar());
        db.update(CARS_TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void setMainCar(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("glowne", 0);
        db.update(CARS_TABLE_NAME, contentValues, "id != ?", new String[]{String.valueOf(id)});
        contentValues.put("glowne", 1);
        db.update(CARS_TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public Car getMainCar() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM cars WHERE glowne=?", new String[]{String.valueOf(1)});

        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();

            Car mainCar = new Car(
                    cursor.getString(cursor.getColumnIndex("marka")),
                    cursor.getString(cursor.getColumnIndex("model")),
                    cursor.getString(cursor.getColumnIndex("rok")),
                    cursor.getString(cursor.getColumnIndex("pojemnosc")),
                    cursor.getString(cursor.getColumnIndex("moc")),
                    cursor.getInt(cursor.getColumnIndex("ikona")),
                    cursor.getInt(cursor.getColumnIndex("glowne"))
            );

            cursor.close();

            return mainCar;
        }
        db.close();

        return null;
    }

    public boolean isMainCar(long id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT glowne FROM cars WHERE id=?", new String[]{String.valueOf(id + 1)});
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            int isMain = cursor.getInt(cursor.getColumnIndex("glowne"));
            cursor.close();

            return isMain == 1;
        }

        return false;
    }

    public void insertCarPhoto(Uri imgUri) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("photo", imgUri.toString());
        Cursor cursor = db.rawQuery("SELECT * FROM mainCarPhoto", null);
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            db.update(CAR_PHOTO_TABLE, contentValues, "photo_id=?", new String[]{String.valueOf(cursor.getString(0))});
            cursor.close();
        } else {
            db.insert(CAR_PHOTO_TABLE, null, contentValues);
        }
        cursor.close();
        db.close();
    }

    public String getCarPhoto() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM mainCarPhoto", null);

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();
            return cursor.getString(cursor.getColumnIndex("photo"));
        } else
            return null;
    }

    public void insertPart(int part_id, String part_name, String additional_info, String replacement_date, String price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("part_id", part_id);
        contentValues.put("new_part_name", part_name);
        contentValues.put("additional_info", additional_info);
        contentValues.put("replacement_date", replacement_date);
        contentValues.put("price", price);
        db.insert(PARTS_TABLE_NAME, null, contentValues);
        db.close();
    }

    public ArrayList<CarPart> getSpecficCarParts(long id) {
        ArrayList<CarPart> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from parts INNER join cars on cars.id=parts.part_id AND cars.id=?", new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            do {
                array_list.add(new CarPart(
                        cursor.getString(cursor.getColumnIndex("new_part_name")),
                        cursor.getString(cursor.getColumnIndex("additional_info")),
                        cursor.getString(cursor.getColumnIndex("replacement_date")),
                        cursor.getString(cursor.getColumnIndex("price")),
                        R.drawable.ic_menu_manage
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return array_list;
    }

    public Car getCar(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from cars where id=?", new String[]{String.valueOf(id)});

        if (cursor != null) {

            cursor.moveToFirst();
            return new Car(
                    cursor.getString(cursor.getColumnIndex("marka")),
                    cursor.getString(cursor.getColumnIndex("model")),
                    cursor.getString(cursor.getColumnIndex("rok")),
                    cursor.getString(cursor.getColumnIndex("pojemnosc")),
                    cursor.getString(cursor.getColumnIndex("moc")),
                    cursor.getInt(cursor.getColumnIndex("ikona")),
                    cursor.getInt(cursor.getColumnIndex("glowne"))
            );
        }

        db.close();
        return null;
    }

    public CarPart getPart(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from parts where part_id=" + id + "", null);

        if (cursor != null) {

            cursor.moveToFirst();
            return new CarPart(
                    cursor.getString(cursor.getColumnIndex("new_part_name")),
                    cursor.getString(cursor.getColumnIndex("additional_info")),
                    cursor.getString(cursor.getColumnIndex("replacement_date")),
                    cursor.getString(cursor.getColumnIndex("price")),
                    R.drawable.ic_menu_manage
            );
        }

        db.close();
        return null;
    }

    public ArrayList<Car> getAllCars() {
        ArrayList<Car> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from cars", null);

        if (cursor.moveToFirst()) {
            do {
                array_list.add(new Car(
                        cursor.getString(cursor.getColumnIndex("marka")),
                        cursor.getString(cursor.getColumnIndex("model")),
                        cursor.getString(cursor.getColumnIndex("rok")),
                        cursor.getString(cursor.getColumnIndex("pojemnosc")),
                        cursor.getString(cursor.getColumnIndex("moc")),
                        cursor.getInt(cursor.getColumnIndex("ikona")),
                        cursor.getInt(cursor.getColumnIndex("glowne"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return array_list;
    }

    public ArrayList<CarPart> getAllParts() {
        ArrayList<CarPart> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from parts", null);

        if (res.moveToFirst()) {
            do {
                array_list.add(new CarPart(
                        res.getString(res.getColumnIndex("new_part_name")),
                        res.getString(res.getColumnIndex("additional_info")),
                        res.getString(res.getColumnIndex("replacement_date")),
                        res.getString(res.getColumnIndex("price")),
                        R.drawable.ic_menu_manage
                ));
            } while (res.moveToNext());
        }
        res.close();
        db.close();
        return array_list;
    }

    public ArrayList<String> getCarsNames() {
        ArrayList<String> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select marka, model from cars", null);

        if (res.moveToFirst()) {
            do {
                array_list.add(
                        res.getString(res.getColumnIndex("marka")).toUpperCase()
                                + " "
                                + res.getString(res.getColumnIndex("model")).toUpperCase()
                );
            } while (res.moveToNext());
        }
        res.close();
        db.close();
        return array_list;
    }

    public void insertInsurance(int car_id, String car_name, String policy, String additional_info, String dateFrom, String dateTo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("car_id", car_id);
        contentValues.put("car", car_name);
        contentValues.put("policy", policy);
        contentValues.put("additional_info", additional_info);
        contentValues.put("date_from", dateFrom);
        contentValues.put("date_to", dateTo);
        db.insert(INSURANCE_TABLE, null, contentValues);
        db.close();
    }

    public void insertInsurance(int car_id, Document insurance) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("car_id", car_id);
        contentValues.put("car", insurance.getAuto());
        contentValues.put("policy", insurance.getInfo());
        contentValues.put("additional_info", insurance.getAdditionalInfo());
        contentValues.put("date_from", insurance.getDate());
        contentValues.put("date_to", insurance.getExpiryDate());
        db.insert(INSURANCE_TABLE, null, contentValues);
        db.close();
    }

    public Document getInsurance(int id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from insurance where insurance.insurance_id=?", new String[]{String.valueOf(id)});

        if (cursor != null) {

            cursor.moveToFirst();
            return new Document(
                    cursor.getString(cursor.getColumnIndex("car")),
                    cursor.getString(cursor.getColumnIndex("policy")),
                    cursor.getString(cursor.getColumnIndex("additional_info")),
                    cursor.getString(cursor.getColumnIndex("date_from")),
                    cursor.getString(cursor.getColumnIndex("date_to"))
            );
        }

        db.close();
        return null;
    }

    public ArrayList<Document> getInsurances() {
        ArrayList<Document> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from insurance", null);

        if (cursor.moveToFirst()) {
            do {
                array_list.add(new Document(
                        cursor.getString(cursor.getColumnIndex("car")),
                        cursor.getString(cursor.getColumnIndex("policy")),
                        cursor.getString(cursor.getColumnIndex("additional_info")),
                        cursor.getString(cursor.getColumnIndex("date_from")),
                        cursor.getString(cursor.getColumnIndex("date_to"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return array_list;
    }

    public void updateInsurance(int id, Document document) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("policy", document.getInfo());
        contentValues.put("additional_info", document.getAdditionalInfo());
        contentValues.put("date_from", document.getDate());
        contentValues.put("date_to", document.getExpiryDate());
        db.update(INSURANCE_TABLE, contentValues, "insurance_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public void insertCarService(int car_id, String car_name, String reg_nr, String mileage, String dateFrom, String dateTo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("car_id", car_id);
        contentValues.put("car", car_name);
        contentValues.put("reg_nr", reg_nr);
        contentValues.put("mileage", mileage);
        contentValues.put("date_from", dateFrom);
        contentValues.put("date_to", dateTo);
        db.insert(CAR_SERVICE_TABLE, null, contentValues);
        db.close();
    }

    public void insertCarService(int car_id, Document service) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("car_id", car_id);
        contentValues.put("car", service.getAuto());
        contentValues.put("reg_nr", service.getInfo());
        contentValues.put("mileage", service.getAdditionalInfo());
        contentValues.put("date_from", service.getDate());
        contentValues.put("date_to", service.getExpiryDate());
        db.insert(CAR_SERVICE_TABLE, null, contentValues);
        db.close();
    }

    public Document getCarService(int car_id) {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from service where service.service_id=?", new String[]{String.valueOf(car_id)});

        if (cursor != null) {

            cursor.moveToFirst();
            return new Document(
                    cursor.getString(cursor.getColumnIndex("car")),
                    cursor.getString(cursor.getColumnIndex("reg_nr")),
                    cursor.getString(cursor.getColumnIndex("mileage")),
                    cursor.getString(cursor.getColumnIndex("date_from")),
                    cursor.getString(cursor.getColumnIndex("date_to"))

            );
        }
        db.close();
        return null;
    }

    public ArrayList<Document> getCarServices() {
        ArrayList<Document> array_list = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from service", null);

        if (cursor.moveToFirst()) {
            do {
                array_list.add(new Document(
                        cursor.getString(cursor.getColumnIndex("car")),
                        cursor.getString(cursor.getColumnIndex("reg_nr")),
                        cursor.getString(cursor.getColumnIndex("mileage")),
                        cursor.getString(cursor.getColumnIndex("date_from")),
                        cursor.getString(cursor.getColumnIndex("date_to"))
                ));
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return array_list;
    }

    public void updateCarService(int id, Document document) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("reg_nr", document.getInfo());
        contentValues.put("mileage", document.getAdditionalInfo());
        contentValues.put("date_from", document.getDate());
        contentValues.put("date_to", document.getExpiryDate());
        db.update(CAR_SERVICE_TABLE, contentValues, "service_id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // Delete functions TO-DO

    // Usuwa auto o podanym id
    public void deleteCar(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(CARS_TABLE_NAME, "id=?", new String[]{Integer.toString(id)});
        db.close();
    }

    // Usuwa wszystkie części dla auta o podanym id
    public void deletePart(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(PARTS_TABLE_NAME, "part_id=?", new String[]{Integer.toString(id)});
        db.close();
    }

    public void deleteInsurance(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(INSURANCE_TABLE, "insurance_id=?", new String[]{Integer.toString(id)});
        db.close();
    }

    public void deleteCarService(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(CAR_SERVICE_TABLE, "service_id=?", new String[]{Integer.toString(id)});
        db.close();
    }
}
