package ru.liner.sensorpermission;

import android.os.Bundle;
import android.view.View;

import java.util.Random;

import ru.liner.colorfy.core.ColorfyActivity;
import ru.liner.sensorpermission.permission.PermissionRequest;
import ru.liner.sensorpermission.utils.RemotePM;

public class MainActivity extends ColorfyActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.makeReqeust).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemotePM.put("pending_permission_request", new PermissionRequest(BuildConfig.APPLICATION_ID, new Random().nextInt(24)+1));
            }
        });
        findViewById(R.id.clearRequest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RemotePM.clear("pending_permission_request");
            }
        });
    }
}