package com.nico.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    static List<DataModels> mNotesCreated;
    private OnItemClickListener mListener;
    @SuppressLint("StaticFieldLeak")
    static Context main_activity;
    private static int position;

    //context
    public NotesAdapter(MainActivity mainActivity) {
        main_activity = mainActivity;
    }

    public NotesAdapter(List<DataModels> notesCreated) {
        mNotesCreated = notesCreated;
    }

    //click listener
    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notes_template, parent, false);
        return new NotesViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final NotesViewHolder holder, int position) {
        DataModels currentNote = mNotesCreated.get(position);
        holder.title.setText(currentNote.getTitle());
        holder.note.setText(currentNote.getNote());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return mNotesCreated.size();
    }

    @Override
    public void onViewRecycled(@NonNull NotesViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView title;
        public TextView note;
        int position;

        public NotesViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            title = itemView.findViewById(R.id.title_tv);
            note = itemView.findViewById(R.id.notes_tv);
            itemView.setOnCreateContextMenuListener(this);

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
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(Menu.NONE, R.id.menu_pin, Menu.NONE, R.string.pin_note);
            contextMenu.add(Menu.NONE, R.id.menu_delete, Menu.NONE, R.string.delete_note);
        }
    }
    public static int getPosition() {
        return position;
    }

    public static void setPosition(int pos) {
        position = pos;
    }

}
