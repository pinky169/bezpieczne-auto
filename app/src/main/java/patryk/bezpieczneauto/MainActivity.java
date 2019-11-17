package patryk.bezpieczneauto;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import patryk.bezpieczneauto.Database.DBHelper;
import patryk.bezpieczneauto.Fragments.CarDataFragment;
import patryk.bezpieczneauto.Fragments.DocumentsFragment;
import patryk.bezpieczneauto.Fragments.ReplacementsFragment;
import patryk.bezpieczneauto.Objects.Car;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new DBHelper(getBaseContext());

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        // Wystartuj aplikację na fragmencie z danymi pojazdu
        // If zapobiega ponownemu ładowaniu fragmentu przy zmianie orientacji ekranu
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CarDataFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_about) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new CarDataFragment()).commit();
        } else if (id == R.id.nav_replacements) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ReplacementsFragment()).commit();
        } else if (id == R.id.nav_documents) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DocumentsFragment()).commit();
        } else if (id == R.id.nav_localization) {
            return true;
            //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ShareLocalizationFragment()).commit();
        } else if (id == R.id.nav_share) {
            sendCarData();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void sendCarData() {
        //Pobierz dane głównego auta
        Car mainCar = dbHelper.getMainCar();

        if (mainCar != null) {

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String carInfo = getResources().getString(R.string.data_to_send_format,
                    mainCar.getMarka(),
                    mainCar.getModel(),
                    mainCar.getRok_produkcji(),
                    mainCar.getPojemnosc(),
                    mainCar.getMoc()
            );
            sendIntent.putExtra(Intent.EXTRA_TEXT, carInfo);
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
        } else {
            Toast.makeText(this, getResources().getString(R.string.set_as_default_car_hint), Toast.LENGTH_LONG).show();
        }
    }
}
