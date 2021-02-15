package com.nico.notes;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DeletedAdapter extends RecyclerView.Adapter<DeletedAdapter.DeletedViewHolder> {

    List<DataModels> mNotesDeleted;
    private static int position;

    public DeletedAdapter(List<DataModels> notesCreateds) {
        mNotesDeleted = notesCreateds;
    }

    @NonNull
    @Override
    public DeletedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.deleted_notes_template, parent, false);
        return new DeletedViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final DeletedViewHolder holder, int position) {
        DataModels currentNote = mNotesDeleted.get(position);
        holder.mTitle.setText(currentNote.getTitle());
        holder.mNote.setText(currentNote.getNote());
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
        return mNotesDeleted.size();
    }

    @Override
    public void onViewRecycled(@NonNull DeletedViewHolder holder) {
        holder.itemView.setOnCreateContextMenuListener(null);
        super.onViewRecycled(holder);
    }

    public static class DeletedViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        public TextView mTitle;
        public TextView mNote;

        public DeletedViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.deleted_title_tv);
            mNote = itemView.findViewById(R.id.deleted_notes_tv);
            itemView.setOnCreateContextMenuListener(this);

        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.add(Menu.NONE, R.id.menu_restore, Menu.NONE, R.string.restore_note);
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
