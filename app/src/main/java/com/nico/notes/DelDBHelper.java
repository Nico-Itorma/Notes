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

public class DelDBHelper extends SQLiteOpenHelper {

    public static final String NOTES_TABLE = "DELETED_NOTES_TABLE";
    public static final String COLUMN_NOTES_TITLE = "DELETED_TITLE";
    public static final String COLUMN_NOTES_BODY = "DELETED_BODY";
    public static final String COLUMN_ID = "DELETED_ID";

    public DelDBHelper(@Nullable Context context) {
        super(context, "DeletedNotes.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + NOTES_TABLE + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_NOTES_TITLE + " TEXT, " + COLUMN_NOTES_BODY + " TEXT)";

        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public boolean addOne(NotesCreated notesCreated)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_NOTES_TITLE, notesCreated.getTitle());
        cv.put(COLUMN_NOTES_BODY, notesCreated.getNote());

        long insert = db.insert(NOTES_TABLE, null, cv);
        return insert != -1;
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
}
