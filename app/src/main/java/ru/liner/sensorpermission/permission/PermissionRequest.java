package ru.liner.sensorpermission.permission;

import androidx.annotation.NonNull;

import ru.liner.sensorpermission.sensor.Sensor;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 27.08.2023, 13:17
 */
public class PermissionRequest {
    @NonNull
    public String packageName;
    @Sensor
    public int sensor;

    public PermissionRequest(@NonNull String packageName, int sensor) {
        this.packageName = packageName;
        this.sensor = sensor;
    }

    @Override
    @NonNull
    public String toString() {
        return "PermissionRequest{" +
                "packageName='" + packageName + '\'' +
                ", sensor=" + sensor +
                '}';
    }
}
