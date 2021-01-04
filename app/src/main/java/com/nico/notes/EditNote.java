package com.nico.notes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class EditNote extends AppCompatActivity {

    EditText et_title, et_note;
    DatabaseHelper dbHelper;
    NotesCreated note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        et_title = findViewById(R.id.edit_title);
        et_note = findViewById(R.id.edit_note);

        Intent i = getIntent();
        int id = i.getIntExtra("ID", 0);

        dbHelper = new DatabaseHelper(this);
        note = dbHelper.getNote(id);
        //Toast.makeText(this, "ID: " + id, Toast.LENGTH_SHORT).show();

        et_title.setText(note.getTitle());
        et_note.setText(note.getNote());


    }

    public void save(View view) {

        if ((et_title.getText().length() != 0) && (et_note.getText().length()) != 0) {
            note.setTitle(et_title.getText().toString().trim());
            note.setNote(et_note.getText().toString().trim());
            dbHelper.editNote(note);

            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            i.putExtra("ID", note.getId());
            startActivity(i);

        } else {
            Toast.makeText(this, "Empty notes cannot be saved", Toast.LENGTH_SHORT).show();
        }

        finish();
    }
}