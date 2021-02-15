package com.nico.notes;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
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
    AlertDialog dialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {

        settingsList = new ArrayList<>();
        settingsList.add(new SettingsItem("Change login password"));

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        mRecyclerView = view.findViewById(R.id.recyclerview_settings);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new SettingsAdapter(settingsList);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter.setOnClick(new SettingsAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                View v = inflater.inflate(R.layout.activity_create_pass, container, false);
                builder.setView(v);
                final EditText change_pin1 = v.findViewById(R.id.create_pin1);
                final EditText change_pin2 = v.findViewById(R.id.create_pin2);
                Button confirm = v.findViewById(R.id.confirm);

                confirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String pin1 = change_pin1.getText().toString().trim();
                        String pin2 = change_pin2.getText().toString().trim();

                        if (pin1.equals(""))  {
                            change_pin1.setError("No PIN entered");
                        }
                        else if (pin2.equals(""))
                        {
                            change_pin2.setError("No PIN entered");
                        }
                        else if (pin1.length() == 4 && pin2.length() == 4) {
                            if (pin1.equals(pin2)) {
                                //save pin to password
                                SharedPreferences sp = getContext().getSharedPreferences("PIN", 0);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("password", pin2);
                                editor.apply();

                                Toast.makeText(getContext(), "Login pin changed", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                            else {
                                Toast.makeText(getContext(), "PIN do not match", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else if (pin1.length() > 4 || pin2.length() > 4)
                        {
                            Toast.makeText(getContext(), "Maximum of 4 digits only", Toast.LENGTH_SHORT).show();
                        }
                        else if (pin1.length() < 4 && pin2.length() < 4) {
                            change_pin1.setError("PIN must be at least 4 characters");
                        }
                    }
                });

                dialog = builder.create();
                dialog.show();

            }
        });


        mRecyclerView.setAdapter(mAdapter);
        return view;
    }
}


