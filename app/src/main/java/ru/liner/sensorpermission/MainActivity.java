package ru.liner.sensorpermission;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Random;
import java.util.UUID;

import ru.liner.colorfy.core.ColorfyActivity;
import ru.liner.preference.IPreference;
import ru.liner.preference.PreferenceListener;
import ru.liner.preference.PreferenceWrapper;
import ru.liner.sensorpermission.permission.PermissionRequest;

public class MainActivity extends ColorfyActivity {
    private TextView xpmValue;
    private Button xpmAdd;
    private Button xpmRemove;
    private IPreference preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        preference = PreferenceWrapper.get(this);
        findViewById(R.id.makeReqeust).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preference.put("pending_permission_request", new PermissionRequest(BuildConfig.APPLICATION_ID, new Random().nextInt(24) + 1));
            }
        });
        findViewById(R.id.clearRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preference.clear("pending_permission_request");
            }
        });
        xpmValue = findViewById(R.id.xpmValue);
        xpmAdd = findViewById(R.id.xpmAdd);
        xpmRemove = findViewById(R.id.xpmRemove);
        PreferenceListener<String> preferenceListener = new PreferenceListener<String>(this) {
            @Override
            public String key() {
                return "test";
            }

            @Override
            public void changed(String newValue) {
                xpmValue.setText(String.format("XPM value: %s", newValue));
            }

            @Override
            public String defaultValue() {
                return "null";
            }

            @Override
            public void removed() {
                xpmValue.setText("XPM value: null");
            }
        };
        preferenceListener.start();
        xpmAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preference.put("test", UUID.randomUUID().toString());
            }
        });
        xpmRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preference.clear("test");
            }
        });
        xpmValue.setText(String.format("XPM value: %s", preference.get("test", "null")));
    }
}