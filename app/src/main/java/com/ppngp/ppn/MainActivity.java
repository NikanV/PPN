package com.ppngp.ppn;

import static com.dev7.lib.v2ray.utils.V2rayConstants.SERVICE_CONNECTION_STATE_BROADCAST_EXTRA;
import static com.dev7.lib.v2ray.utils.V2rayConstants.SERVICE_DOWNLOAD_SPEED_BROADCAST_EXTRA;
import static com.dev7.lib.v2ray.utils.V2rayConstants.SERVICE_DOWNLOAD_TRAFFIC_BROADCAST_EXTRA;
import static com.dev7.lib.v2ray.utils.V2rayConstants.SERVICE_DURATION_BROADCAST_EXTRA;
import static com.dev7.lib.v2ray.utils.V2rayConstants.SERVICE_UPLOAD_SPEED_BROADCAST_EXTRA;
import static com.dev7.lib.v2ray.utils.V2rayConstants.SERVICE_UPLOAD_TRAFFIC_BROADCAST_EXTRA;
import static com.dev7.lib.v2ray.utils.V2rayConstants.V2RAY_SERVICE_STATICS_BROADCAST_INTENT;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import com.dev7.lib.v2ray.V2rayController;
import com.dev7.lib.v2ray.utils.V2rayConfigs;
import com.dev7.lib.v2ray.utils.V2rayConstants;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private Button connection;
    private TextView connection_speed, connection_traffic, connection_time, server_delay;
    private Spinner server_spinner;
    private BroadcastReceiver v2rayBroadCastReceiver;
    private Map<String, String> serverConfigMap;

    @SuppressLint({"SetTextI18n", "UnspecifiedRegisterReceiverFlag"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            V2rayController.init(this, R.mipmap.ic_launcher, "V2ray Android");
            connection = findViewById(R.id.btn_connection);
            connection_speed = findViewById(R.id.connection_speed);
            connection_time = findViewById(R.id.connection_duration);
            connection_traffic = findViewById(R.id.connection_traffic);
            server_delay = findViewById(R.id.server_delay);
            server_spinner = findViewById(R.id.server_spinner);

            // Initialize the server configuration map
            serverConfigMap = new HashMap<>();
            serverConfigMap.put("Tehran", "tehran_config_string");
            serverConfigMap.put("Mashhad", "mashhad_config_string");
            serverConfigMap.put("Shiraz", "shiraz_config_string");
            serverConfigMap.put("Isfahan", "isfahan_config_string");
            serverConfigMap.put("Karaj", "karaj_config_string");
            serverConfigMap.put("Arak", "arak_config_string");
        }

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.servers_array, R.layout.spinner_item);
        adapter.setDropDownViewResource(R.layout.spinner_item);
        server_spinner.setAdapter(adapter);

        connection.setOnClickListener(view -> {
            String selectedServer = server_spinner.getSelectedItem().toString();
            String configString = serverConfigMap.get(selectedServer);
            if (V2rayController.getConnectionState() == V2rayConstants.CONNECTION_STATES.DISCONNECTED) {
                V2rayController.startV2ray(this, selectedServer, configString, null);
            } else {
                V2rayController.stopV2ray(this);
            }
        });

        server_delay.setOnClickListener(view -> {
            server_delay.setText("Ping: measuring...");
            new Handler().postDelayed(() -> server_delay.setText("Ping: " + V2rayController.getV2rayServerDelay(getDefaultConfig()) + "ms"), 200);
        });

        // Check connection state when activity launch
        switch (V2rayController.getConnectionState()) {
            case CONNECTED:
                connection.setText("Disconnect");
                // check connection latency
                server_delay.callOnClick();
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
                    connection_time.setText("Connection time: " + Objects.requireNonNull(intent.getExtras()).getString(SERVICE_DURATION_BROADCAST_EXTRA));
                    connection_speed.setText("Connection speed: " + intent.getExtras().getString(SERVICE_UPLOAD_SPEED_BROADCAST_EXTRA) + " | " + intent.getExtras().getString(SERVICE_DOWNLOAD_SPEED_BROADCAST_EXTRA));
                    connection_traffic.setText("Spent traffic: " + intent.getExtras().getString(SERVICE_UPLOAD_TRAFFIC_BROADCAST_EXTRA) + " | " + intent.getExtras().getString(SERVICE_DOWNLOAD_TRAFFIC_BROADCAST_EXTRA));
                    switch ((V2rayConstants.CONNECTION_STATES) Objects.requireNonNull(intent.getExtras().getSerializable(SERVICE_CONNECTION_STATE_BROADCAST_EXTRA))) {
                        case CONNECTED:
                            connection.setText("Disconnect");
                            break;
                        case DISCONNECTED:
                            connection.setText("Connect");
                            server_delay.setText("Ping: Tap to test");
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

        // Setup Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setLabelVisibilityMode(NavigationBarView.LABEL_VISIBILITY_LABELED);

        Map<Integer, Runnable> navigationActions = new HashMap<>();
        navigationActions.put(R.id.navigation_home, () -> {
            // Handle Home action
        });
        navigationActions.put(R.id.navigation_settings, () -> {
            // Handle Settings action
        });
        navigationActions.put(R.id.navigation_import, () -> {
            // Handle Import Config action
        });

        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            Runnable action = navigationActions.get(item.getItemId());
            if (action != null) {
                action.run();
                return true;
            }
            return false;
        });
    }

    public static String getDefaultConfig() {
        return ""; // Return the default V2ray configuration here
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (v2rayBroadCastReceiver != null) {
            unregisterReceiver(v2rayBroadCastReceiver);
        }
    }
}
