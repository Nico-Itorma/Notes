
package com.nico.notes;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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


public class NotesFragment extends Fragment {

    RecyclerView mRecyclerView;
    NotesAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    DatabaseHelper dbHelper;
    PinnedDBHelper pinnedDBHelper;
    DelDBHelper delDBHelper;
    List<DataModels> notesCreateds;
    Context main_activity;

    //context
    public NotesFragment(MainActivity mainActivity) {
        main_activity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        dbHelper = new DatabaseHelper(getContext());
        pinnedDBHelper = new PinnedDBHelper(getContext());
        delDBHelper = new DelDBHelper(getContext());
        notesCreateds = dbHelper.getEveryNote();

        mRecyclerView = view.findViewById(R.id.list);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new NotesAdapter(notesCreateds);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                DataModels noteclicked = notesCreateds.get(position);
                int id = noteclicked.getId();
                Intent i = new Intent(getContext(), EditNote.class);
                i.putExtra("ID", id);
                startActivity(i);
            }
        });
        registerForContextMenu(mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
        return view;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = NotesAdapter.getPosition();
        final DataModels notes = notesCreateds.get(position);
        switch (item.getItemId()) {
            case R.id.menu_pin:
                Toast.makeText(main_activity, "Added to Pinned Notes \n" + "Note can be accessed on Pinned Notes", Toast.LENGTH_SHORT).show();
                pinnedDBHelper.addOne(notes);
                dbHelper.deleteOne(notes);
                MainActivity.instance.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotesFragment(MainActivity.instance)).commit();
                return true;
            case R.id.menu_delete:
                AlertDialog.Builder alert = new AlertDialog.Builder(main_activity);
                alert.setMessage("Are you sure to delete note?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        delDBHelper.addOne(notes);
                        dbHelper.deleteOne(notes);
                        MainActivity.instance.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotesFragment(MainActivity.instance)).commit();
                        Toast.makeText(main_activity, "Note Deleted", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("No", null);

                AlertDialog builder = alert.create();
                builder.show();
                return true;

            default:
                return super.onContextItemSelected(item);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.toolbar_menus, menu);
        menu.findItem(R.id.tb_add).setVisible(true);
        super.onCreateOptionsMenu(menu, inflater);
    }
}
