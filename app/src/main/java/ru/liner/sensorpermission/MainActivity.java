package ru.liner.sensorpermission;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;

import ru.liner.sensorpermission.utils.RemotePM;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RemotePM.put("HUII", 123123);

        Intent intent = new Intent("de.robv.android.xposed.installer.OPEN_SECTION");
        intent.putExtra("section", "modules");
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {

        }
    }
}