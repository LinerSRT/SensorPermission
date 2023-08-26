package ru.liner.sensorpermission.sensor;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.hardware.SensorManager;

import androidx.annotation.IntDef;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import ru.liner.sensorpermission.R;
import ru.liner.sensorpermission.utils.Pair;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 24.08.2023, четверг
 **/
public class SensorPermission implements Serializable {
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

    public boolean permissionAllowed(@Sensor int sensor){
        int sensorIndex = -1;
        for (int i = 0; i < sensorList.size(); i++) {
            Pair<Integer, Integer> pair = sensorList.get(i);
            if(pair.getKey() == sensor){
                sensorIndex = i;
                break;
            }
        }
        return sensorIndex != -1 && sensorList.get(sensorIndex).getValue() == State.GRANTED;
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

    public static String getSensorName(@NonNull Context context, @Sensor int sensor){
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        switch (sensor){
            case Sensor.ACCELEROMETER:
            case Sensor.ACCELEROMETER_UNCALIBRATED:
                return context.getString(R.string.sensor_accelerometer);
            case Sensor.MAGNETIC_FIELD:
            case Sensor.MAGNETIC_FIELD_UNCALIBRATED:
                return context.getString(R.string.sensor_magnetic_field);
            case Sensor.ORIENTATION:
                return context.getString(R.string.sensor_orientation);
            case Sensor.GYROSCOPE:
            case Sensor.GYROSCOPE_UNCALIBRATED:
                return context.getString(R.string.sensor_gyroscope);
            case Sensor.LIGHT:
                return context.getString(R.string.sensor_light);
            case Sensor.PRESSURE:
                return context.getString(R.string.sensor_pressure);
            case Sensor.TEMPERATURE:
            case Sensor.AMBIENT_TEMPERATURE:
                return context.getString(R.string.sensor_temperature);
            case Sensor.PROXIMITY:
                return context.getString(R.string.sensor_proximity);
            case Sensor.GRAVITY:
                return context.getString(R.string.sensor_gravity);
            case Sensor.LINEAR_ACCELERATION:
                return context.getString(R.string.sensor_linear_acceleration);
            case Sensor.ROTATION_VECTOR:
                return context.getString(R.string.sensor_rotation_vector);
            case Sensor.RELATIVE_HUMIDITY:
                return context.getString(R.string.sensor_relative_humidity);
            case Sensor.GAME_ROTATION_VECTOR:
                return context.getString(R.string.sensor_game_rotation_vector);
            case Sensor.SIGNIFICANT_MOTION:
                return context.getString(R.string.sensor_significant_motion);
            case Sensor.STEP_DETECTOR:
                return context.getString(R.string.sensor_step_detector);
            case Sensor.STEP_COUNTER:
                return context.getString(R.string.sensor_step_counter);
            case Sensor.GEOMAGNETIC_ROTATION_VECTOR:
                return context.getString(R.string.sensor_geomagnetic_rotation_vector);
            case Sensor.HEART_RATE:
                return context.getString(R.string.sensor_heart_rate);
            case Sensor.TILT_DETECTOR:
                return context.getString(R.string.sensor_tilt_detector);
            default:
                return sensorManager.getDefaultSensor(sensor).getStringType();
        }
    }


    public static Drawable getSensorIcon(@NonNull Context context, @Sensor int sensor){
        SensorManager sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        switch (sensor){
            case Sensor.ACCELEROMETER:
            case Sensor.ACCELEROMETER_UNCALIBRATED:
            case Sensor.ORIENTATION:
            case Sensor.GYROSCOPE:
            case Sensor.GYROSCOPE_UNCALIBRATED:
            case Sensor.TILT_DETECTOR:
                return ContextCompat.getDrawable(context, R.drawable.sensor_accelerometer);
            case Sensor.MAGNETIC_FIELD:
            case Sensor.MAGNETIC_FIELD_UNCALIBRATED:
                return ContextCompat.getDrawable(context, R.drawable.sensor_magnetic_field);
            case Sensor.LIGHT:
                return ContextCompat.getDrawable(context, R.drawable.sensor_light);
            case Sensor.PRESSURE:
                return ContextCompat.getDrawable(context, R.drawable.sensor_pressure);
            case Sensor.ROTATION_VECTOR:
            case Sensor.GAME_ROTATION_VECTOR:
            case Sensor.GEOMAGNETIC_ROTATION_VECTOR:
                return ContextCompat.getDrawable(context, R.drawable.sensor_rotation);
            case Sensor.HEART_RATE:
                return ContextCompat.getDrawable(context, R.drawable.sensor_accelerometer);
            default:
                return ContextCompat.getDrawable(context, R.drawable.sensor_unknown);
        }
    }


}
