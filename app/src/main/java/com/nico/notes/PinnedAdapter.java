package com.nico.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
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

public class PinnedAdapter extends RecyclerView.Adapter<PinnedAdapter.PinnedViewHolder> {

    static List<NotesCreated> mNotesPinned;
    @SuppressLint("StaticFieldLeak")
    static Context main_activity;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(PinnedAdapter.OnItemClickListener listener) {
        mListener = listener;
    }

    public PinnedAdapter(List<NotesCreated> notesCreated) {
        mNotesPinned = notesCreated;
    }

    public PinnedAdapter(MainActivity mainActivity) {
        main_activity = mainActivity;
    }

    public static class PinnedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, PopupMenu.OnMenuItemClickListener {

        TextView pinned_title;
        TextView pinned_note;
        ImageButton pinned_image_btn;
        int id;

        PinnedDBHelper pinnedDBHelper = new PinnedDBHelper(main_activity);
        DatabaseHelper dbHelper = new DatabaseHelper(main_activity);
        DelDBHelper delDBHelper = new DelDBHelper(main_activity);

        public PinnedViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            pinned_title = itemView.findViewById(R.id.pinned_title_tv);
            pinned_note = itemView.findViewById(R.id.pinned_notes_tv);
            pinned_image_btn = itemView.findViewById(R.id.pinned_image_btn);
            pinned_image_btn.setOnClickListener(this);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View view) {
            id = getAdapterPosition();
            showPopUp(view);
        }

        public void showPopUp(View view) {
            PopupMenu popupMenu = new PopupMenu(view.getContext(), view);
            popupMenu.inflate(R.menu.pinned_menu);
            popupMenu.setOnMenuItemClickListener(this);
            popupMenu.show();
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            if (item.getItemId() == R.id.unpin) {
                final NotesCreated noteclicked = mNotesPinned.get(id);
                dbHelper.addOne(noteclicked);
                pinnedDBHelper.deleteOne(noteclicked);
                MainActivity.getInstance().buildRecyclerView(dbHelper);
                MainActivity.getInstance().pinnedRecyclerView(pinnedDBHelper);
                Toast.makeText(main_activity, "Note's Unpinned", Toast.LENGTH_SHORT).show();
                return true;
            }
            return false;
        }
    }

    @NonNull
    @Override
    public PinnedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pinned_notes_template, parent, false);
        return new PinnedViewHolder(v, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull PinnedViewHolder holder, int position) {
        NotesCreated currentNote = mNotesPinned.get(position);
        holder.pinned_title.setText(currentNote.getTitle());
        holder.pinned_note.setText(currentNote.getNote());
    }

    @Override
    public int getItemCount() {
        return mNotesPinned.size();
    }
}
