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

public class PinnedAdapter extends RecyclerView.Adapter<PinnedAdapter.PinnedViewHolder> {

    static List<DataModels> mNotesPinned;
    @SuppressLint("StaticFieldLeak")
    static Context main_activity;
    private static int position;

    public PinnedAdapter(List<DataModels> notesCreated) {
        mNotesPinned = notesCreated;
    }

    public PinnedAdapter(MainActivity mainActivity) {
        main_activity = mainActivity;
    }

    @NonNull
    @Override
    public PinnedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pinned_notes_template, parent, false);
        return new PinnedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final PinnedViewHolder holder, int position) {
        DataModels currentNote = mNotesPinned.get(position);
        holder.pinned_title.setText(currentNote.getTitle());
        holder.pinned_note.setText(currentNote.getNote());
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
        return mNotesPinned.size();
    }

    @Override
    public void onViewRecycled(@NonNull PinnedViewHolder holder) {
        holder.itemView.setOnLongClickListener(null);
        super.onViewRecycled(holder);
    }

    public static class PinnedViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView pinned_title;
        TextView pinned_note;

        public PinnedViewHolder(@NonNull View itemView) {
            super(itemView);
            pinned_title = itemView.findViewById(R.id.pinned_title_tv);
            pinned_note = itemView.findViewById(R.id.pinned_notes_tv);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(Menu.NONE, R.id.menu_unpin, Menu.NONE, R.string.unpin_note);
        }
    }
    public static int getPosition() {
        return position;
    }

    public static void setPosition(int pos) {
        position = pos;
    }
}
