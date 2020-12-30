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
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

import java.util.List;

import static android.widget.Toast.LENGTH_SHORT;


public class MainActivity extends AppCompatActivity {

    @SuppressLint("StaticFieldLeak")
    private static MainActivity instance;

    String title;
    String notes;

    private DrawerLayout drawer;

    private EditText et_title, et_notes;
    NavigationView navigationView;

    AlertDialog dialog;
    DatabaseHelper dbHelper;
    ImageView tv_jingle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NotesFragment notesFragment = new NotesFragment(this);
        DeletedFragment deletedFragment = new DeletedFragment(this);
        NotesAdapter notesAdapter = new NotesAdapter(this);
        PinnedAdapter pinnedAdapter = new PinnedAdapter(this);

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
                        buildRecyclerView(dbHelper);
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
                        alert.setMessage("Close the Notes?").
                                setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(MainActivity.this, "Logging Out...", LENGTH_SHORT).show();
                                        Handler handler = new Handler();
                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                                System.exit(0);
                                            }
                                        }, 1000);
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
        buildRecyclerView(dbHelper);
    }

    public static MainActivity getInstance()
    {
        return instance;
    }

    public void buildRecyclerView(final DatabaseHelper dbHelper) {

        List<NotesCreated> notes = dbHelper.getEveryNote();
        if (notes.size() == 0)
        {
            tv_jingle.setVisibility(View.VISIBLE);
        }
        else
        {
            tv_jingle.setVisibility(View.GONE);
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotesFragment(this)).commit();
    }

    //RecyclerView update on DeletedNotes Fragment
    public void delRecyclerView(DelDBHelper delDBHelper) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DeletedFragment(this)).commit();
    }

    public void pinnedRecyclerView(PinnedDBHelper pinnedDBHelper){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PinnedFragment(this)).commit();
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
            alert.setMessage("Log Out?").
                    setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(MainActivity.this, "Logging Out...", LENGTH_SHORT).show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    finish();
                                    System.exit(0);
                                }
                            }, 1000);
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
        NotesCreated notesCreated = new NotesCreated(-1, "Empty error", "Empty Error");
        try {
            if ((title.length() == 0) && (notes.length() == 0))
            {
                Toast.makeText(this, "Empty notes cannot be saved", Toast.LENGTH_SHORT).show();
            }
            else{
                notesCreated = new NotesCreated(1, title, notes);
//                Toast.makeText(this, notesCreated.toString(), LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {
            Toast.makeText(this, "Error creating New Note: " + e, Toast.LENGTH_SHORT).show();
            notesCreated = new NotesCreated(0, "Error", "Something went wrong");
        }

        dbHelper = new DatabaseHelper(MainActivity.this);
        if ((title.length() == 0) && (notes.length() == 0))
        {
            Toast.makeText(this, "Empty notes cannot be saved", Toast.LENGTH_SHORT).show();
        }
        else
        {
            dbHelper.addOne(notesCreated);
        }

        tv_jingle.setVisibility(View.INVISIBLE);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                buildRecyclerView(dbHelper);
            }
        }, 600);

        dialog.dismiss();
    }
}



