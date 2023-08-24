package ru.liner.sensorpermission.sensor;

import android.util.SparseIntArray;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import ru.liner.sensorpermission.utils.Pair;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 24.08.2023, четверг
 **/
public class SensorPermission {
    private final String packageName;
    private final List<Pair<Integer, Integer>> sensorList;

    public SensorPermission(String packageName) {
        this.packageName = packageName;
        this.sensorList = new ArrayList<>();
    }

    public String getPackageName() {
        return packageName;
    }

    public boolean hasPermission(@Sensor int sensor) {
        for(Pair<Integer, Integer> sensorPair : sensorList)
            if(sensorPair.getKey() == sensor)
                return true;
        return false;
    }

    @State
    public int permissionStatus(@Sensor int sensor) {
        Pair<Integer, Integer> sensorPair = Pair.find(sensorList, sensor);
        return sensorPair == null ? State.UNKNOWN : sensorPair.getValue() == State.GRANTED ? State.GRANTED : State.DENIED;
    }

    public void grantPermission(@Sensor int sensor) {
        setSensorState(sensor, State.GRANTED);
    }

    public void revokePermission(@Sensor int sensor) {
        setSensorState(sensor, State.DENIED);
    }

    public void forgetPermission(@Sensor int sensor) {
        setSensorState(sensor, State.UNKNOWN);
    }

    private void setSensorState(@Sensor int sensor, @State int state){
        int sensorIndex = -1;
        for (int i = 0; i < sensorList.size(); i++) {
            Pair<Integer, Integer> pair = sensorList.get(i);
            if(pair.getKey() == sensor){
                sensorIndex = i;
                break;
            }
        }
        Pair<Integer, Integer> grantedPair = new Pair<>(sensor, state);
        if(sensorIndex == -1){
            sensorList.add(grantedPair);
        } else {
            sensorList.set(sensorIndex, grantedPair);
        }
    }

    @IntDef({State.GRANTED, State.DENIED, State.UNKNOWN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface State {
        int GRANTED = 0;
        int DENIED = 1;
        int UNKNOWN = -1;
    }

    @IntDef({
            Sensor.ACCELEROMETER,
            Sensor.MAGNETIC_FIELD,
            Sensor.ORIENTATION,
            Sensor.GYROSCOPE,
            Sensor.LIGHT,
            Sensor.PRESSURE,
            Sensor.TEMPERATURE,
            Sensor.PROXIMITY,
            Sensor.GRAVITY,
            Sensor.LINEAR_ACCELERATION,
            Sensor.ROTATION_VECTOR,
            Sensor.RELATIVE_HUMIDITY,
            Sensor.AMBIENT_TEMPERATURE,
            Sensor.MAGNETIC_FIELD_UNCALIBRATED,
            Sensor.GAME_ROTATION_VECTOR,
            Sensor.GYROSCOPE_UNCALIBRATED,
            Sensor.SIGNIFICANT_MOTION,
            Sensor.STEP_DETECTOR,
            Sensor.STEP_COUNTER,
            Sensor.GEOMAGNETIC_ROTATION_VECTOR,
            Sensor.HEART_RATE,
            Sensor.TILT_DETECTOR,
            Sensor.WAKE_GESTURE,
            Sensor.GLANCE_GESTURE,
            Sensor.PICK_UP_GESTURE,
            Sensor.WRIST_TILT_GESTURE,
            Sensor.DEVICE_ORIENTATION,
            Sensor.MOTION_DETECT,
            Sensor.HEART_BEAT,
            Sensor.ACCELEROMETER_UNCALIBRATED,
            Sensor.ALL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface Sensor {
        int ACCELEROMETER = 1;
        int MAGNETIC_FIELD = 2;
        int ORIENTATION = 3;
        int GYROSCOPE = 4;
        int LIGHT = 5;
        int PRESSURE = 6;
        int TEMPERATURE = 7;
        int PROXIMITY = 8;
        int GRAVITY = 9;
        int LINEAR_ACCELERATION = 10;
        int ROTATION_VECTOR = 11;
        int RELATIVE_HUMIDITY = 12;
        int AMBIENT_TEMPERATURE = 13;
        int MAGNETIC_FIELD_UNCALIBRATED = 14;
        int GAME_ROTATION_VECTOR = 15;
        int GYROSCOPE_UNCALIBRATED = 16;
        int SIGNIFICANT_MOTION = 17;
        int STEP_DETECTOR = 18;
        int STEP_COUNTER = 19;
        int GEOMAGNETIC_ROTATION_VECTOR = 20;
        int HEART_RATE = 21;
        int TILT_DETECTOR = 22;
        int WAKE_GESTURE = 23;
        int GLANCE_GESTURE = 24;
        int PICK_UP_GESTURE = 25;
        int WRIST_TILT_GESTURE = 26;
        int DEVICE_ORIENTATION = 27;
        int MOTION_DETECT = 30;
        int HEART_BEAT = 31;
        int ACCELEROMETER_UNCALIBRATED = 35;
        int ALL = -1;
    }
}
