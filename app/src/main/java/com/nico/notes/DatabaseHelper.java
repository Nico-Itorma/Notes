package com.nico.notes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String NOTES_TABLE = "NOTES_TABLE";
    public static final String COLUMN_NOTES_TITLE = "NOTES_TITLE";
    public static final String COLUMN_NOTES_BODY = "NOTES_BODY";
    public static final String COLUMN_ID = "ID";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "Notes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + NOTES_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOTES_TITLE + " TEXT, " + COLUMN_NOTES_BODY + " TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {

    }

    public void addOne(NotesCreated notesCreated)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOTES_TITLE, notesCreated.getTitle());
        cv.put(COLUMN_NOTES_BODY, notesCreated.getNote());

        db.insert(NOTES_TABLE, null, cv);
    }

    public List<NotesCreated> getEveryNote()
    {
        List<NotesCreated> returnList = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + NOTES_TABLE;

        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst())
        {
            do
            {
                int id = cursor.getInt(0);
                String title = cursor.getString(1);
                String note = cursor.getString(2);

                NotesCreated newData = new NotesCreated(id, title, note);
                returnList.add(newData);

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return returnList;
    }

    public void deleteOne(NotesCreated notesCreated)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + NOTES_TABLE + " WHERE " + COLUMN_ID + " = " + notesCreated.getId();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
    }

    public NotesCreated getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        @SuppressLint("Recycle") Cursor cursor = db.query(NOTES_TABLE, new String[]{COLUMN_ID, COLUMN_NOTES_TITLE, COLUMN_NOTES_BODY,COLUMN_ID}, COLUMN_ID + "=?", new String[] {String.valueOf(id)}, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        assert cursor != null;
        return new NotesCreated(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
    }

    public List<NotesCreated> getNotes() {
        SQLiteDatabase db = this.getReadableDatabase();
        List<NotesCreated> notes = new ArrayList<>();

        String query = "SELECT * FROM " + NOTES_TABLE;

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);

        if (cursor != null) {

            do {
                NotesCreated newNote = new NotesCreated();
                newNote.setId(cursor.getInt(0));
                newNote.setTitle(cursor.getString(1));
                newNote.setNote(cursor.getString(2));

                notes.add(newNote);

            }while (cursor.moveToNext());

        }

        return notes;
    }

    public void editNote(NotesCreated notesCreated) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();

        Log.d("Edited", notesCreated.getTitle() + "\n ID: " + notesCreated.getId());

        cv.put(COLUMN_NOTES_TITLE, notesCreated.getTitle());
        cv.put(COLUMN_NOTES_BODY, notesCreated.getNote());

        db.update(NOTES_TABLE, cv, COLUMN_ID + "=?", new String[]{String.valueOf(notesCreated.getId())});
    }
}
