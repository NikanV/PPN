package com.ppngp.ppn;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SettingsActivity extends AppCompatActivity {

    TextView deviceId;
    MaterialButton logoutBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        deviceId = findViewById(R.id.device_id);
        setUUID();

        logoutBtn = findViewById(R.id.logout_btn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserHandler.logoutUser();
                tr(LoginPage.class);
            }
        });


        setNavBar();
    }

    private void setNavBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        Map<Integer, Runnable> navigationActions = new HashMap<>();
        navigationActions.put(R.id.navigation_home, () -> {
            tr(HomePage.class);
        });
        navigationActions.put(R.id.navigation_settings, () -> {
            // Already in Settings Activity, do nothing
        });
        navigationActions.put(R.id.navigation_import, () -> {
            tr(ImportConfigActivity.class);
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Runnable action = navigationActions.get(item.getItemId());
            if (action != null) {
                action.run();
                return true;
            }
            return false;
        });

        bottomNavigationView.setSelectedItemId(R.id.navigation_settings);
    }

    @SuppressLint("HardwareIds")
    private void setUUID() {
        String androidId = Settings.Secure.getString(SettingsActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID);
        String uuid = UUID.nameUUIDFromBytes(androidId.getBytes()).toString();
        deviceId.setText(uuid);
    }

    private void tr(Class c) {
        Intent intent = new Intent(SettingsActivity.this, c);
        startActivity(intent);
        this.finish();
    }
}
