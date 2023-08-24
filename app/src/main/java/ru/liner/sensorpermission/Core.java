package ru.liner.sensorpermission;

import android.app.Application;

import ru.liner.sensorpermission.utils.PM;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 24.08.2023, четверг
 **/
public class Core extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PM.init(this);
    }
}
