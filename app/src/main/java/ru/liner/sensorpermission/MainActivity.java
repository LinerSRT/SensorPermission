package ru.liner.sensorpermission;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Random;
import java.util.UUID;

import ru.liner.colorfy.core.ColorfyActivity;
import ru.liner.sensorpermission.permission.PermissionRequest;
import ru.liner.sensorpermission.utils.RemotePM;
import ru.liner.sensorpermission.utils.XPM;

public class MainActivity extends ColorfyActivity {
    private TextView xpmValue;
    private Button xpmAdd;
    private Button xpmRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.makeReqeust).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemotePM.put("pending_permission_request", new PermissionRequest(BuildConfig.APPLICATION_ID, new Random().nextInt(24) + 1));
            }
        });
        findViewById(R.id.clearRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemotePM.clear("pending_permission_request");
            }
        });
        xpmValue = findViewById(R.id.xpmValue);
        xpmAdd = findViewById(R.id.xpmAdd);
        xpmRemove = findViewById(R.id.xpmRemove);
        XPM xpm = new XPM();
        xpm.addKeyChangeListener("test", new XPM.KeyChangeListener<String>() {
            @Override
            public void onChanged(@NonNull String key, @NonNull String newValue) {
                xpmValue.setText(String.format("XPM value: %s", newValue));
            }

            @Override
            public void onRemoved() {
                xpmValue.setText("XPM value: null");
            }
        });
        xpmAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xpm.set("test", UUID.randomUUID().toString());
            }
        });
        xpmRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                xpm.remove("test");
            }
        });
        xpmValue.setText(String.format("XPM value: %s", xpm.get("test", "null")));
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    xpm.set("test", UUID.randomUUID().toString());
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();
    }
}