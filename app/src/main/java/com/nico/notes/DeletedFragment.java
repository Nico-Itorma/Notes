package com.nico.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;

public class DeletedFragment extends Fragment {

    RecyclerView recyclerView;
    DeletedAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    DelDBHelper delDBHelper;
    DatabaseHelper dbHelper;
    List<NotesCreated> notesCreateds;
    Context main_activity;

    public DeletedFragment(MainActivity mainActivity) {
        main_activity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_deleted, container, false);

        delDBHelper = new DelDBHelper(getContext());
        dbHelper = new DatabaseHelper(getContext());
        notesCreateds = delDBHelper.getEveryNote();

        recyclerView = view.findViewById(R.id.deleted_note);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        mAdapter = new DeletedAdapter(notesCreateds);
        recyclerView.setLayoutManager(mLayoutManager);

        mAdapter.setOnClick(new DeletedAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                final NotesCreated note = notesCreateds.get(position);

                androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(main_activity);
                alert.setIcon(R.drawable.ic_restore);
                alert.setMessage("Restore Deleted Note?").
                        setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbHelper.addOne(note);
                                delDBHelper.deleteOne(note);
                                MainActivity.getInstance().delRecyclerView(delDBHelper);
                                Toast.makeText(main_activity, "Note Restored", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Disagree", null);

                AlertDialog builder = alert.create();
                builder.show();
            }
        });

        mAdapter.setOnLongClick(new DeletedAdapter.OnLongClickListener() {
            @Override
            public void onLongClick(int position) {
                final NotesCreated note = notesCreateds.get(position);

                androidx.appcompat.app.AlertDialog.Builder alert = new androidx.appcompat.app.AlertDialog.Builder(main_activity);
                alert.setIcon(R.drawable.ic_delete);
                alert.setTitle("Permanently Delete Note?").
                        setPositiveButton("Agree", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delDBHelper.deleteOne(note);
                                MainActivity.getInstance().delRecyclerView(delDBHelper);
                                Toast.makeText(getContext(), "Note Permanently Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Disagree", null);

                AlertDialog builder = alert.create();
                builder.show();
            }
        });

        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menus, menu);
        menu.findItem(R.id.tb_add).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.delete:
                Toast.makeText(getContext(), "Delete Note", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.restore:
                Toast.makeText(getContext(), "Restore Notes", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}


