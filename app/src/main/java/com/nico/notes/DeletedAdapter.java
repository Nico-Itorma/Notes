package com.nico.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class DeletedAdapter extends RecyclerView.Adapter<DeletedAdapter.DeletedViewHolder> {

    List<NotesCreated> mNotesDeleted;
    private OnLongClickListener mLongclick;
    private OnClickListener mClickListener;

    public DeletedAdapter(List<NotesCreated> notesCreateds) {
        mNotesDeleted = notesCreateds;
    }

    public interface OnLongClickListener {
        void onLongClick(int position);
    }

    public interface OnClickListener{
        void onClick(int position);
    }

    public void setOnClick(OnClickListener OnClickListener) {
        mClickListener = OnClickListener;
    }

    public void setOnLongClick(OnLongClickListener mLongClick)
    {
        mLongclick = mLongClick;
    }

    public static class DeletedViewHolder extends RecyclerView.ViewHolder {
        public TextView mTitle;
        public TextView mNote;

        public DeletedViewHolder(@NonNull View itemView, final OnClickListener click, final OnLongClickListener mListener) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.deleted_title_tv);
            mNote = itemView.findViewById(R.id.deleted_notes_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (click != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            click.onClick(position);
                        }
                    }
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    if (mListener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            mListener.onLongClick(position);
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    @NonNull
    @Override
    public DeletedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.deleted_notes_template, parent, false);
        return new DeletedViewHolder(v, mClickListener, mLongclick);
    }

    @Override
    public void onBindViewHolder(@NonNull DeletedViewHolder holder, int position) {
        NotesCreated currentNote = mNotesDeleted.get(position);
        holder.mTitle.setText(currentNote.getTitle());
        holder.mNote.setText(currentNote.getNote());
    }

    @Override
    public int getItemCount() {
        return mNotesDeleted.size();
    }

}
