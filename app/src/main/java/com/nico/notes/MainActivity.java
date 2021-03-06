package com.nico.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    public static MainActivity instance;

    String title;
    String notes;

    private DrawerLayout drawer;

    private EditText et_title, et_notes;
    NavigationView navigationView;

    AlertDialog dialog;
    DatabaseHelper dbHelper;
    ImageView tv_jingle;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String UPDATED = "time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_jingle = findViewById(R.id.tv_jingle);

        final DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.this);

        et_title = findViewById(R.id.et_title);
        et_notes = findViewById(R.id.et_note);

        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_notes:
                        toolbar.setTitle("Notes");
                        updateNoteFragment(dbHelper);
                        break;
                    case R.id.nav_pin:
                        tv_jingle.setVisibility(View.GONE);
                        toolbar.setTitle("Pinned Notes");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new PinnedFragment(MainActivity.this)).commit();
                        navigationView.setCheckedItem(R.id.nav_pin);
                        break;
                    case R.id.nav_settings:
                        tv_jingle.setVisibility(View.GONE);
                        toolbar.setTitle("Settings");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new SettingsFragment()).commit();
                        navigationView.setCheckedItem(R.id.nav_settings);
                        break;
                    case R.id.nav_deleted:
                        tv_jingle.setVisibility(View.GONE);
                        toolbar.setTitle("Deleted Notes");
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                                new DeletedFragment(MainActivity.this)).commit();
                        navigationView.setCheckedItem(R.id.nav_deleted);
                        break;
                    case R.id.nav_logout:
                        AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                        alert.setMessage("Close app?").
                                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        drawer.closeDrawer(GravityCompat.START);
                                        Toast.makeText(MainActivity.this, "Closing app...", Toast.LENGTH_SHORT).show();
                                        finish();
                                        System.exit(0);
                                    }
                                }).setNegativeButton("Cancel", null);
                        AlertDialog builder = alert.create();
                        builder.show();
                        break;
                }

                drawer.closeDrawer(GravityCompat.START);

                return true;
            }
        });

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null)
        {
            navigationView.setCheckedItem(R.id.nav_notes);
        }
        instance = this;

        updateNoteFragment(dbHelper);
    }

    public void updateNoteFragment(final DatabaseHelper dbHelper)
    {
        List<DataModels> notes = dbHelper.getEveryNote();
        if (notes.size() == 0)
        {
            tv_jingle.setVisibility(View.VISIBLE);
        }
        else {
            tv_jingle.setVisibility(View.GONE);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotesFragment(this)).commit();
    }

    @Override
    public void onBackPressed()
    {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else
        {
            AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
            alert.setMessage("Close app?").
                    setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "Closing app...", Toast.LENGTH_SHORT).show();
                            System.exit(0);
                        }
                    }).setNegativeButton("Cancel", null);
            AlertDialog builder = alert.create();
            builder.show();
        }
    }

    //Add button on toolbar
    public void btn_add(MenuItem item) {
        AlertDialog.Builder btn_add = new AlertDialog.Builder(MainActivity.this, R.style.Add);

        final View new_note = getLayoutInflater().inflate(R.layout.new_note, null);
        btn_add.setView(new_note);

        et_title = new_note.findViewById(R.id.et_title);
        et_notes = new_note.findViewById(R.id.et_note);

        dialog = btn_add.create();
        dialog.show();
    }

    //Save Button on new note
    public void save(View v)
    {
        title = et_title.getText().toString().trim();
        notes = et_notes.getText().toString().trim();
        DataModels notesCreated = new DataModels(-1, "Empty error", "Empty Error");
        try {
            if ((title.length() == 0) && (notes.length() == 0))
            {
                Toast.makeText(this, "Empty notes cannot be saved", Toast.LENGTH_SHORT).show();
            }
            else{
                notesCreated = new DataModels(1, title, notes);
            }

        }
        catch (Exception e)
        {
            Toast.makeText(this, "Error creating New Note: " + e, Toast.LENGTH_SHORT).show();
            notesCreated = new DataModels(0, "Error", "Something went wrong");
        }

        dbHelper = new DatabaseHelper(MainActivity.this);
        if ((title.length() == 0) && (notes.length() == 0))
        {
            Toast.makeText(this, "Empty notes cannot be saved", Toast.LENGTH_SHORT).show();
        }
        else
        {
            dbHelper.addOne(notesCreated);
            Date currentTime = Calendar.getInstance().getTime();
            String time = currentTime.toString();
            preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            editor = preferences.edit();
            editor.putString(UPDATED, time);
            editor.apply();
        }

        tv_jingle.setVisibility(View.INVISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                updateNoteFragment(dbHelper);
            }
        }, 600);

        dialog.dismiss();
    }
}



