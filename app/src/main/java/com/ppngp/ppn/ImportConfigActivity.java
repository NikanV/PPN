package com.ppngp.ppn;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImportConfigActivity extends AppCompatActivity {

    private EditText editTextServerName, editTextServerUrl;
    private Button buttonAddServer, buttonRemoveServer;
    private ListView listViewServers;
    private ArrayAdapter<String> adapter;
    private List<String> serverList;
    private HashMap<String, String> serverConfigMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_config);

        editTextServerName = findViewById(R.id.editTextServerName);
        editTextServerUrl = findViewById(R.id.editTextServerUrl);
        buttonAddServer = findViewById(R.id.buttonAddServer);
        buttonRemoveServer = findViewById(R.id.buttonRemoveServer);
        listViewServers = findViewById(R.id.listViewServers);

        serverConfigMap = Utils.readJsonFromAssets(this, "v2ray_configs.json");
        serverList = new ArrayList<>(serverConfigMap.keySet());
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_multiple_choice, serverList);
        listViewServers.setAdapter(adapter);

        buttonAddServer.setOnClickListener(v -> addServer());
        buttonRemoveServer.setOnClickListener(v -> removeSelectedServers());

        setNavBar();
    }

    private void addServer() {
        String serverName = editTextServerName.getText().toString();
        String serverUrl = editTextServerUrl.getText().toString();
        if (!serverName.isEmpty() && !serverUrl.isEmpty()) {
            serverConfigMap.put(serverName, serverUrl);
            serverList.add(serverName);
            adapter.notifyDataSetChanged();
            Utils.writeJsonToAssets(this, "v2ray_configs.json", serverConfigMap);
        }
    }

    private void removeSelectedServers() {
        for (int i = listViewServers.getCount() - 1; i >= 0; i--) {
            if (listViewServers.isItemChecked(i)) {
                String serverName = serverList.get(i);
                serverConfigMap.remove(serverName);
                serverList.remove(i);
            }
        }
        listViewServers.clearChoices();
        adapter.notifyDataSetChanged();
        Utils.writeJsonToAssets(this, "v2ray_configs.json", serverConfigMap);
    }

    private void setNavBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        Map<Integer, Runnable> navigationActions = new HashMap<>();
        navigationActions.put(R.id.navigation_home, () -> {
            Intent intent = new Intent(ImportConfigActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        });
        navigationActions.put(R.id.navigation_settings, () -> {
            Intent intent = new Intent(ImportConfigActivity.this, SettingsActivity.class);
            startActivity(intent);
            finish();
        });
        navigationActions.put(R.id.navigation_import, () -> {
            // Already in Import Config Activity, do nothing
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Runnable action = navigationActions.get(item.getItemId());
            if (action != null) {
                action.run();
                return true;
            }
            return false;
        });

        // Set the selected item to navigation_import
        bottomNavigationView.setSelectedItemId(R.id.navigation_import);
    }

}
