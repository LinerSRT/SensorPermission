package ru.liner.sensorpermission.sensor;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.SensorManager;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import ru.liner.sensorpermission.BuildConfig;

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
        try {
            Resources resources = context.getPackageManager().getResourcesForApplication(BuildConfig.APPLICATION_ID);
            String resource;
            switch (sensor) {
                case Sensor.ACCELEROMETER:
                case Sensor.ACCELEROMETER_UNCALIBRATED:
                    resource = "sensor_accelerometer";
                    break;
                case Sensor.MAGNETIC_FIELD:
                case Sensor.MAGNETIC_FIELD_UNCALIBRATED:
                    resource = "sensor_magnetic_field";
                    break;
                case Sensor.ORIENTATION:
                    resource = "sensor_orientation";
                    break;
                case Sensor.GYROSCOPE:
                case Sensor.GYROSCOPE_UNCALIBRATED:
                    resource = "sensor_gyroscope";
                    break;
                case Sensor.LIGHT:
                    resource = "sensor_light";
                    break;
                case Sensor.PRESSURE:
                    resource = "sensor_pressure";
                    break;
                case Sensor.TEMPERATURE:
                case Sensor.AMBIENT_TEMPERATURE:
                    resource = "sensor_temperature";
                    break;
                case Sensor.PROXIMITY:
                    resource = "sensor_proximity";
                    break;
                case Sensor.GRAVITY:
                    resource = "sensor_gravity";
                    break;
                case Sensor.LINEAR_ACCELERATION:
                    resource = "sensor_linear_acceleration";
                    break;
                case Sensor.ROTATION_VECTOR:
                    resource = "sensor_rotation_vector";
                    break;
                case Sensor.RELATIVE_HUMIDITY:
                    resource = "sensor_relative_humidity";
                    break;
                case Sensor.GAME_ROTATION_VECTOR:
                    resource = "sensor_game_rotation_vector";
                    break;
                case Sensor.SIGNIFICANT_MOTION:
                    resource = "sensor_significant_motion";
                    break;
                case Sensor.STEP_DETECTOR:
                    resource = "sensor_step_detector";
                    break;
                case Sensor.STEP_COUNTER:
                    resource = "sensor_step_counter";
                    break;
                case Sensor.GEOMAGNETIC_ROTATION_VECTOR:
                    resource = "sensor_geomagnetic_rotation_vector";
                    break;
                case Sensor.HEART_RATE:
                    resource = "sensor_heart_rate";
                    break;
                case Sensor.TILT_DETECTOR:
                    resource = "sensor_tilt_detector";
                    break;
                default:
                    resource = null;
            }
            return TextUtils.isEmpty(resource) ? sensorManager.getDefaultSensor(sensor).getStringType() : resources.getString(resources.getIdentifier(resource, "string", BuildConfig.APPLICATION_ID));
        } catch (PackageManager.NameNotFoundException e) {
            return sensorManager.getDefaultSensor(sensor).getStringType();
        }
    }
}
