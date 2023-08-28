package ru.liner.sensorpermission.permission;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ru.liner.sensorpermission.sensor.Sensor;
import ru.liner.sensorpermission.sensor.SensorStatus;

public class PermissionPackage {
    private final String packageName;
    private List<PermissionInfo> permissionList;

    public PermissionPackage(String packageName) {
        this.packageName = packageName;
        this.permissionList = new ArrayList<>();
    }

    public PermissionInfo getPermission(@Sensor int sensor) {
        return permissionList
                .stream()
                .filter(permissionInfo -> permissionInfo.sensor == sensor)
                .findFirst()
                .orElse(new PermissionInfo(sensor, SensorStatus.UNKNOWN, System.currentTimeMillis()));
    }

    public boolean hasPermission(@Sensor int sensor) {
        return permissionList
                .stream()
                .anyMatch(permissionInfo -> permissionInfo.sensor == sensor);
    }

    public void setPermission(@NonNull PermissionInfo newPermissionInfo) {
        if (hasPermission(newPermissionInfo.sensor)) {
            permissionList = permissionList
                    .stream()
                    .map(permissionInfo -> permissionInfo.sensor == newPermissionInfo.sensor ? newPermissionInfo : permissionInfo)
                    .collect(Collectors.toList());
        } else {
            permissionList.add(newPermissionInfo);
        }
    }

    public String getPackageName() {
        return packageName;
    }

    @Override
    @NonNull
    public String toString() {
        return "PermissionPackage{" +
                "packageName='" + packageName + '\'' +
                ", permissionList=" + Arrays.toString(permissionList.toArray()) +
                '}';
    }
}
