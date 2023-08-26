package ru.liner.sensorpermission.sensor;

public enum SensorGroup {
    MOTION(
            Sensor.ACCELEROMETER,
            Sensor.ACCELEROMETER_UNCALIBRATED,
            Sensor.GRAVITY,
            Sensor.GYROSCOPE,
            Sensor.GYROSCOPE_UNCALIBRATED,
            Sensor.LINEAR_ACCELERATION,
            Sensor.ROTATION_VECTOR,
            Sensor.SIGNIFICANT_MOTION,
            Sensor.STEP_DETECTOR,
            Sensor.STEP_COUNTER
    ),
    POSITION(
            Sensor.GAME_ROTATION_VECTOR,
            Sensor.GEOMAGNETIC_ROTATION_VECTOR,
            Sensor.MAGNETIC_FIELD,
            Sensor.MAGNETIC_FIELD_UNCALIBRATED,
            Sensor.ORIENTATION,
            Sensor.DEVICE_ORIENTATION,
            Sensor.PROXIMITY
    ),
    ENVIRONMENT(
            Sensor.AMBIENT_TEMPERATURE,
            Sensor.LIGHT,
            Sensor.PRESSURE,
            Sensor.RELATIVE_HUMIDITY,
            Sensor.TEMPERATURE
    ),
    HEALTH(
            Sensor.HEART_BEAT,
            Sensor.HEART_RATE
    ),
    UNRECOGNISED;

    @Sensor
    private final int[] sensors;

    SensorGroup(@Sensor int... sensors) {
        this.sensors = sensors;
    }


    public static SensorGroup get(@Sensor int sensor){
        if(MOTION.isInGroup(sensor))
            return MOTION;
        if(POSITION.isInGroup(sensor))
            return POSITION;
        if(ENVIRONMENT.isInGroup(sensor))
            return ENVIRONMENT;
        if(HEALTH.isInGroup(sensor))
            return HEALTH;
        return UNRECOGNISED;
    }

    public boolean isInGroup(@Sensor int sensor) {
        for (@Sensor int s : sensors)
            if (s == sensor)
                return true;
        return false;
    }

}
