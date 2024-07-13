package com.ppngp.ppn;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
import java.util.Map;

public class ImportConfigActivity extends AppCompatActivity {

    LinearLayout serverLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_config);

        serverLayout = findViewById(R.id.button_layout);

        addServerListeners();
        setNavBar();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void addServerListeners() {
        for (int i = 0; i < serverLayout.getChildCount(); i++) {
            MaterialButton mb = (MaterialButton) serverLayout.getChildAt(i);
            mb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tr(HomePage.class, mb.getText().toString());
                }
            });
        }
    }

    private void setNavBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        Map<Integer, Runnable> navigationActions = new HashMap<>();
        navigationActions.put(R.id.navigation_home, () -> {
            tr(HomePage.class, null);
        });
        navigationActions.put(R.id.navigation_import, () -> {
            // Already in Import Config Activity, do nothing
        });
        navigationActions.put(R.id.navigation_settings, () -> {
            tr(SettingsActivity.class, null);
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Runnable action = navigationActions.get(item.getItemId());
            if (action != null) {
                action.run();
                return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_import);
    }

    private void tr(Class c, String str) {
        Intent intent = new Intent(ImportConfigActivity.this, c);
        intent.putExtra("ServerName", str);
        startActivity(intent);
        this.finish();
    }

}
