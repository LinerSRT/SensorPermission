package ru.liner.sensorpermission;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import ru.liner.sensorpermission.service.PermissionOverlayService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService(new Intent(this, PermissionOverlayService.class).putExtra("sensor", 2).putExtra("package_name", BuildConfig.APPLICATION_ID));
    }
}