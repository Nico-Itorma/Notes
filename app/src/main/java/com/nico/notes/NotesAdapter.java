package com.nico.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    static List<NotesCreated> mNotesCreated;
    private OnItemClickListener mListener;
    @SuppressLint("StaticFieldLeak")
    static Context main_activity;

    public NotesAdapter(MainActivity mainActivity) {
        main_activity = mainActivity;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        public TextView title;
        public TextView note;
        ImageButton image_btn;
        int position;
        int id;

        PinnedDBHelper pinnedDBHelper = new PinnedDBHelper(main_activity);
        DatabaseHelper dbHelper = new DatabaseHelper(main_activity);
        DelDBHelper delDBHelper = new DelDBHelper(main_activity);

        public NotesViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.title_tv);
            note = itemView.findViewById(R.id.notes_tv);
            image_btn = itemView.findViewById(R.id.image_btn);
            image_btn.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }

        @Override
        public void onClick(View view) {
            //Log.d("NotesViewHolder", "Clicked: " + getAdapterPosition());
            id = getAdapterPosition();
            showPopUp(view);
        }

        public void showPopUp(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.notes_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            final NotesCreated noteclicked = mNotesCreated.get(id);
            switch (item.getItemId()) {
                case R.id.pin:
                    Toast.makeText(main_activity, "Added to Pinned Notes \n" + "Note can be accessed on Pinned Notes", Toast.LENGTH_SHORT).show();
                    dbHelper.deleteOne(noteclicked);
                    pinnedDBHelper.addOne(noteclicked);
                    MainActivity.getInstance().buildRecyclerView(dbHelper);
                    return true;
                case R.id.delete:
                    androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(main_activity);
                    alert.setTitle("DELETE NOTE?");
                    alert.setMessage("Are you sure to delete note?").
                            setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    delDBHelper.addOne(noteclicked);
                                    dbHelper.deleteOne(noteclicked);
                                    MainActivity.getInstance().buildRecyclerView(dbHelper);
                                    Toast.makeText(main_activity, "Note Deleted", Toast.LENGTH_SHORT).show();
                                }
                            }).setNegativeButton("Disagree", null);

                    AlertDialog builder = alert.create();
                    builder.show();

                    return true;
                default:
                    return false;
            }
        }
    }

    public NotesAdapter(List<NotesCreated> notesCreated) {
        mNotesCreated = notesCreated;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_template, parent, false);
        return new NotesViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        NotesCreated currentNote = mNotesCreated.get(position);
        holder.title.setText(currentNote.getTitle());
        holder.note.setText(currentNote.getNote());
    }

    @Override
    public int getItemCount() {
        return mNotesCreated.size();
    }

}
