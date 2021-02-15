package com.nico.notes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
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

public class PinnedFragment extends Fragment {

    RecyclerView recyclerView;
    PinnedAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;

    PinnedDBHelper pinnedDBHelper;
    List<DataModels> mNotesCreated;
    DatabaseHelper dbHelper;
    Context main_activity;

    public PinnedFragment(MainActivity mainActivity) {
        main_activity = mainActivity;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pinned_notes, container, false);

        pinnedDBHelper = new PinnedDBHelper(getContext());
        dbHelper = new DatabaseHelper(getContext());
        mNotesCreated = pinnedDBHelper.getEveryNote();

        recyclerView = view.findViewById(R.id.pinned_list);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        mAdapter = new PinnedAdapter(mNotesCreated);
        recyclerView.setLayoutManager(mLayoutManager);
        registerForContextMenu(recyclerView);
        recyclerView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        int position = PinnedAdapter.getPosition();
        final DataModels pinned_note = mNotesCreated.get(position);

        if (item.getItemId() == R.id.menu_unpin) {
            dbHelper.addOne(pinned_note);
            pinnedDBHelper.deleteOne(pinned_note);
            MainActivity.instance.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new NotesFragment(MainActivity.instance)).commit();
            MainActivity.instance.getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PinnedFragment(MainActivity.instance)).commit();
            Toast.makeText(getContext(), "Note's Unpinned", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onContextItemSelected(item);
    }
}


