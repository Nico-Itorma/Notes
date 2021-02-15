package com.nico.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
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
    List<DataModels> notesCreateds;
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
        registerForContextMenu(recyclerView);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = DeletedAdapter.getPosition();
        final DataModels note = notesCreateds.get(position);
        androidx.appcompat.app.AlertDialog.Builder alert;
        AlertDialog builder;
        switch (item.getItemId()) {
            case R.id.menu_restore:
                alert = new androidx.appcompat.app.AlertDialog.Builder(main_activity);
                alert.setMessage("Restore Deleted Note?").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dbHelper.addOne(note);
                                delDBHelper.deleteOne(note);
                                MainActivity.instance.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DeletedFragment(MainActivity.instance)).commit();
                                Toast.makeText(main_activity, "Note Restored", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", null);

                builder = alert.create();
                builder.show();
                return true;
            case R.id.menu_delete:
                alert = new androidx.appcompat.app.AlertDialog.Builder(main_activity);
                alert.setMessage("Permanently Delete Note?").
                        setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                delDBHelper.deleteOne(note);
                                MainActivity.instance.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DeletedFragment(MainActivity.instance)).commit();
                                Toast.makeText(getContext(), "Note Permanently Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("No", null);

                builder = alert.create();
                builder.show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}


