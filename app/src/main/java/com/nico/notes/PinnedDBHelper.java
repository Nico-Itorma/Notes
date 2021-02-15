package com.nico.notes;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PinnedDBHelper extends SQLiteOpenHelper {

    public static final String NOTES_TABLE = "PINNED_NOTES_TABLE";
    public static final String COLUMN_NOTES_TITLE = "PINNED_TITLE";
    public static final String COLUMN_NOTES_BODY = "PINNED_BODY";
    public static final String COLUMN_ID = "PINNED_ID";

    public PinnedDBHelper(@Nullable Context context) {
        super(context, "PinnedNotes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + NOTES_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOTES_TITLE + " TEXT, " + COLUMN_NOTES_BODY + " TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void addOne(DataModels notesCreated)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOTES_TITLE, notesCreated.getTitle());
        cv.put(COLUMN_NOTES_BODY, notesCreated.getNote());

        db.insert(NOTES_TABLE, null, cv);

        db.close();
    }

    public List<DataModels> getEveryNote()
    {
        List<DataModels> PinnedList = new ArrayList<>();

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

                DataModels newData = new DataModels(id, title, note);
                PinnedList.add(newData);

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return PinnedList;
    }

    public void deleteOne(DataModels notesCreated)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        String query = "DELETE FROM " + NOTES_TABLE + " WHERE " + COLUMN_ID + " = " + notesCreated.getId();

        @SuppressLint("Recycle") Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
    }
}
