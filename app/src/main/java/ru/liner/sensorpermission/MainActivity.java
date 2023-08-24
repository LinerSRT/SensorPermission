package ru.liner.sensorpermission;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ru.liner.sensorpermission.utils.RemotePM;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        RemotePM.put("HUII", 123123);
    }
}