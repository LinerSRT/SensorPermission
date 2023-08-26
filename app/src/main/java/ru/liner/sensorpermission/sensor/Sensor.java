package ru.liner.sensorpermission.sensor;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 25.08.2023, пятница
 **/
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
