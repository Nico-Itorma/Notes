package com.nico.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import static android.widget.Toast.LENGTH_SHORT;


public class EditNote extends AppCompatActivity {

    EditText et_title, et_note;
    TextView time_update;
    DatabaseHelper dbHelper;
    DataModels note;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String UPDATED = "time";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        et_title = findViewById(R.id.edit_title);
        et_note = findViewById(R.id.edit_note);
        time_update = findViewById(R.id.notes_update_tv);

        Intent i = getIntent();
        int id = i.getIntExtra("ID", 0);

        dbHelper = new DatabaseHelper(this);
        note = dbHelper.getNote(id);
        //Toast.makeText(this, "ID: " + id, Toast.LENGTH_SHORT).show();

        et_title.setText(note.getTitle());
        et_note.setText(note.getNote());
        SharedPreferences preferences = getSharedPreferences("sharedPrefs", MODE_PRIVATE);
        String text = "Edited: " + preferences.getString("time", "");
        time_update.setText(text);
    }

    public void save(View view) {
        saveChanges();
        finish();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alert = new AlertDialog.Builder(EditNote.this);
        alert.setMessage("Save edited notes?").
                setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        saveChanges();
                        finish();
                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        AlertDialog builder = alert.create();
        builder.show();
    }

    private void saveChanges() {
        if ((et_title.getText().length() != 0) || (et_note.getText().length()) != 0) {
            note.setTitle(et_title.getText().toString().trim());
            note.setNote(et_note.getText().toString().trim());

            Date currentTime = Calendar.getInstance().getTime();
            String time = currentTime.toString();
            preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            editor = preferences.edit();
            editor.putString(UPDATED, time);
            editor.apply();

            dbHelper.editNote(note);

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("ID", note.getId());
            startActivity(i);

        } else {
            Toast.makeText(this, "Empty notes cannot be saved", Toast.LENGTH_SHORT).show();
        }

    }
}