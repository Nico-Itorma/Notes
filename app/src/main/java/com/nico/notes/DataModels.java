package com.nico.notes;

import org.jetbrains.annotations.NotNull;

public class DataModels {
    int id;
    String title;
    String note;

    public DataModels(int id, String title, String note) {
        this.id = id;
        this.title = title;
        this.note = note;
    }

    public DataModels() {
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


}
