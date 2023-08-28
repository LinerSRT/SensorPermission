package ru.liner.sensorpermission.permission;

import androidx.annotation.NonNull;

import ru.liner.sensorpermission.sensor.Sensor;
import ru.liner.sensorpermission.sensor.SensorStatus;

public class PermissionInfo {
    @Sensor
    public int sensor;
    @SensorStatus
    public int status;
    public long lastUpdateTime;
    public PermissionInfo(int sensor, int status, long lastUpdateTime) {
        this.sensor = sensor;
        this.status = status;
        this.lastUpdateTime = lastUpdateTime;
    }

    @Override
    @NonNull
    public String toString() {
        return "PermissionInfo{" +
                "sensor=" + sensor +
                ", status=" + status +
                ", lastUpdateTime=" + lastUpdateTime +
                '}';
    }
}
