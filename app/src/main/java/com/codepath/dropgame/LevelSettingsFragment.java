package com.codepath.dropgame;

import android.os.Bundle;
import android.preference.PreferenceFragment;

public class LevelSettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
    }
}
