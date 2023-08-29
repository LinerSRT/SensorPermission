package ru.liner.sensorpermission.sensor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;

public class SensorTool {
    /**
     * Adapted for using from XPosed (Context may be not our app, so we need load resources not using R*)
     *
     * @param context application context
     * @param sensor  sensor type
     * @return name of associated sensor
     */
    @SuppressLint("DiscouragedApi")
    public static String getSensorName(@NonNull Context context, @Sensor int sensor) {
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        return sensorManager.getDefaultSensor(sensor).getStringType();
    }
}
