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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PermissionRequest that = (PermissionRequest) o;

        if (sensor != that.sensor) return false;
        return packageName.equals(that.packageName);
    }

    @Override
    public int hashCode() {
        int result = packageName.hashCode();
        result = 31 * result + sensor;
        return result;
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
