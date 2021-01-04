package com.nico.notes;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    List<NotesCreated> mNotesCreated;
    Context main_activity;

    public PinnedFragment(MainActivity mainActivity) {
        main_activity = mainActivity;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pinned_notes, container, false);

        pinnedDBHelper = new PinnedDBHelper(getContext());
        mNotesCreated = pinnedDBHelper.getEveryNote();

        recyclerView = view.findViewById(R.id.pinned_list);
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setHasFixedSize(true);
        mAdapter = new PinnedAdapter(mNotesCreated);
        recyclerView.setLayoutManager(mLayoutManager);

        recyclerView.setAdapter(mAdapter);
        return view;
    }
}


