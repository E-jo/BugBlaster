package com.codepath.dropgame;

import android.app.Activity;
import android.os.Bundle;

public class LevelSettingsActivity extends Activity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new LevelSettingsFragment())
                .commit();

    }
}
