package com.ppngp.ppn;

import android.os.Bundle;
import androidx.preference.PreferenceFragmentCompat;
import com.ppngp.ppn.R;

public class SettingsFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);
    }
}
