package ru.liner.sensorpermission.sensor;


import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({
        SensorStatus.UNKNOWN,
        SensorStatus.GRANTED,
        SensorStatus.DENIED,
        SensorStatus.GRANTED_ONCE,
})
@Retention(RetentionPolicy.SOURCE)
public @interface SensorStatus {
    int UNKNOWN = -1;
    int GRANTED = 0;
    int DENIED = 1;
    int GRANTED_ONCE = 2;
}
