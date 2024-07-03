package com.ppngp.ppn;

import static com.dev7.lib.v2ray.utils.V2rayConstants.SERVICE_CONNECTION_STATE_BROADCAST_EXTRA;
import static com.dev7.lib.v2ray.utils.V2rayConstants.SERVICE_DOWNLOAD_TRAFFIC_BROADCAST_EXTRA;
import static com.dev7.lib.v2ray.utils.V2rayConstants.SERVICE_DURATION_BROADCAST_EXTRA;
import static com.dev7.lib.v2ray.utils.V2rayConstants.SERVICE_UPLOAD_TRAFFIC_BROADCAST_EXTRA;
import static com.dev7.lib.v2ray.utils.V2rayConstants.V2RAY_SERVICE_STATICS_BROADCAST_INTENT;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.dev7.lib.v2ray.V2rayController;
import com.dev7.lib.v2ray.utils.V2rayConstants;
import com.google.android.material.navigation.NavigationBarView;

public class HomePage extends AppCompatActivity {

    private Button connection;
    private TextView serverName, connectionTraffic, connectionTime, serverDelay;
    private BroadcastReceiver v2rayBroadCastReceiver;
    private HashMap<String, String> serverConfigMap;
    private ArrayList<String> serverNames;

    @SuppressLint({"SetTextI18n", "UnspecifiedRegisterReceiverFlag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        if (savedInstanceState == null) {
            V2rayController.init(this, R.mipmap.ppn_icon, "PPN");
        }

        serverConfigMap = Utils.readJsonFromAssets(this, "v2ray_configs.json");
        serverNames = new ArrayList<>(serverConfigMap.keySet());

        setElements();
        setServerName();
        setListeners();
        setNavBar();

        switch (V2rayController.getConnectionState()) {
            case CONNECTED:
                connection.setText("Disconnect");
                serverDelay.callOnClick();
                break;
            case DISCONNECTED:
                connection.setText("Connect");
                break;
            case CONNECTING:
                connection.setText("Connecting...");
                break;
            default:
                break;
        }

        v2rayBroadCastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                runOnUiThread(() -> {
                    connectionTime.setText(Objects.requireNonNull(intent.getExtras()).getString(SERVICE_DURATION_BROADCAST_EXTRA));
                    connectionTraffic.setText(intent.getExtras().getString(SERVICE_DOWNLOAD_TRAFFIC_BROADCAST_EXTRA) + " | " + intent.getExtras().getString(SERVICE_UPLOAD_TRAFFIC_BROADCAST_EXTRA));
                    switch ((V2rayConstants.CONNECTION_STATES) Objects.requireNonNull(intent.getExtras().getSerializable(SERVICE_CONNECTION_STATE_BROADCAST_EXTRA))) {
                        case CONNECTED:
                            connection.setText("Disconnect");
                            break;
                        case DISCONNECTED:
                            connection.setText("Connect");
                            serverDelay.setText("Tap to test");
                            break;
                        case CONNECTING:
                            connection.setText("Connecting...");
                            break;
                        default:
                            break;
                    }
                });
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(v2rayBroadCastReceiver, new IntentFilter(V2RAY_SERVICE_STATICS_BROADCAST_INTENT), RECEIVER_EXPORTED);
        } else {
            registerReceiver(v2rayBroadCastReceiver, new IntentFilter(V2RAY_SERVICE_STATICS_BROADCAST_INTENT));
        }
    }

    private void setElements() {
        serverName = findViewById(R.id.server_name);
        connection = findViewById(R.id.btn_connection);
        connectionTime = findViewById(R.id.connection_duration);
        connectionTraffic = findViewById(R.id.connection_traffic);
        serverDelay = findViewById(R.id.server_delay);
    }

    @SuppressLint("SetTextI18n")
    private void setListeners() {
        connection.setOnClickListener(view -> {
            ArrayList<String> serverAndRemark = getSelectedServerAndRemark();
            if (V2rayController.getConnectionState() == V2rayConstants.CONNECTION_STATES.DISCONNECTED) {
                V2rayController.startV2ray(this, serverAndRemark.get(0), serverAndRemark.get(1), null);
            } else {
                V2rayController.stopV2ray(this);
            }
        });

        serverDelay.setOnClickListener(view -> {
            serverDelay.setText("measuring...");
            String selectedServer = serverName.getText().toString().replace("[", "").replace("]", "");
            new Handler().postDelayed(() -> serverDelay.setText(V2rayController.getV2rayServerDelay(serverConfigMap.get(selectedServer)) + "ms"), 200);
        });
    }

    private ArrayList<String> getSelectedServerAndRemark() {
        ArrayList<String> result = new ArrayList<>();
        String selectedServer = getIntent().getStringExtra("ServerName");
        if (selectedServer == null) {
            selectedServer = getDefaultServer();
        }
        result.add(selectedServer);
        result.add(serverConfigMap.get(selectedServer));
        return result;
    }

    private void setNavBar() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        Map<Integer, Runnable> navigationActions = new HashMap<>();
        navigationActions.put(R.id.navigation_home, () -> {
            // Already in MainActivity, do nothing
        });
        navigationActions.put(R.id.navigation_settings, () -> {
            tr(SettingsActivity.class);
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

        bottomNavigationView.setSelectedItemId(R.id.navigation_home);
    }

    @SuppressLint("SetTextI18n")
    private void setServerName() {
        serverName.setText("[" + getSelectedServerAndRemark().get(0) + "]");
    }

    private String getDefaultServer() {
        return serverNames.get(0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (v2rayBroadCastReceiver != null) {
            unregisterReceiver(v2rayBroadCastReceiver);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        serverConfigMap = Utils.readJsonFromAssets(this, "v2ray_configs.json");
        serverNames = new ArrayList<>(serverConfigMap.keySet());
        setServerName();
    }

    private void tr(Class c) {
        Intent intent = new Intent(HomePage.this, c);
        startActivity(intent);
    }
}
