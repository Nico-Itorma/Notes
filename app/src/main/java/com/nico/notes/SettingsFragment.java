package com.nico.notes;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SettingsFragment extends Fragment {

    RecyclerView mRecyclerView;
    SettingsAdapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    ArrayList<SettingsItem> settingsList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        settingsList = new ArrayList<>();
        settingsList.add(new SettingsItem("Change Login Password"));

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerview_settings);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new SettingsAdapter(settingsList);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter.setOnClick(new SettingsAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {
                //TODO: how how how
            }
        });


        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}


