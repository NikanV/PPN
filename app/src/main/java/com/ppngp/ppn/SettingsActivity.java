package com.ppngp.ppn;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
import java.util.Map;

public class SettingsActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        setNavBar();
    }

    private void setNavBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        Map<Integer, Runnable> navigationActions = new HashMap<>();
        navigationActions.put(R.id.navigation_home, () -> {
            Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        navigationActions.put(R.id.navigation_settings, () -> {
            // Already in Settings Activity, do nothing
        });
        navigationActions.put(R.id.navigation_import, () -> {
            Intent intent = new Intent(SettingsActivity.this, ImportConfigActivity.class);
            startActivity(intent);
            finish();
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Runnable action = navigationActions.get(item.getItemId());
            if (action != null) {
                action.run();
                return true;
            }
            return false;
        });

        // Set the selected item to navigation_settings
        bottomNavigationView.setSelectedItemId(R.id.navigation_settings);
    }
}
