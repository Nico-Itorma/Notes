package com.nico.notes;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsAdapter.SettingsViewHolder> {

    private final ArrayList<SettingsItem> mSettingsList;
    OnClickListener settings;

    //interface for onclick listener
    public interface OnClickListener{
        void onClick(int position);
    }

    public void setOnClick(SettingsAdapter.OnClickListener OnClickListener) {
        settings = OnClickListener;
    }

    public static class SettingsViewHolder extends RecyclerView.ViewHolder{
        public TextView mTextView;

        public SettingsViewHolder(@NonNull View itemView, final OnClickListener settingsClick) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.settings_tv);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (settingsClick != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            settingsClick.onClick(position);
                        }
                    }
                }
            });
        }
    }

    public SettingsAdapter(ArrayList<SettingsItem> settingsList)
    {
        this.mSettingsList = settingsList;
    }

    @NonNull
    @Override
    public SettingsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_layout, parent, false);
        return new SettingsViewHolder(view, settings);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingsViewHolder holder, int position) {
        SettingsItem currentItem = mSettingsList.get(position);
        holder.mTextView.setText(currentItem.getmSettings_name());
    }

    @Override
    public int getItemCount() {
        return mSettingsList.size();
    }
}
