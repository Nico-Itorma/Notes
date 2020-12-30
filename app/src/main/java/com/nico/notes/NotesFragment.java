package com.nico.notes;

import android.content.Context;
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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.util.List;


public class NotesFragment extends Fragment {

    RecyclerView mRecyclerView;
    NotesAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    DatabaseHelper dbHelper;
    List<NotesCreated> notesCreateds;
    Context main_activity;

    public NotesFragment(MainActivity mainActivity) {
        main_activity = mainActivity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notes, container, false);

        dbHelper = new DatabaseHelper(getContext());
        notesCreateds = dbHelper.getEveryNote();

        mRecyclerView = view.findViewById(R.id.list);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new NotesAdapter(notesCreateds);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                NotesCreated noteclicked = notesCreateds.get(position);
                int id = noteclicked.getId();
                Intent i = new Intent(getContext(), EditNote.class);
                i.putExtra("ID", id);
                startActivity(i);
            }
        });

        mRecyclerView.setAdapter(mAdapter);
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
        menu.findItem(R.id.delete).setVisible(false);
        menu.findItem(R.id.restore).setVisible(false);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.tb_add) {
            Toast.makeText(getContext(), "Note should be deleted", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
