package ru.liner.sensorpermission;

import android.annotation.SuppressLint;
import android.app.AndroidAppHelper;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import ru.liner.sensorpermission.sensor.SensorPermission;
import ru.liner.sensorpermission.utils.RemotePM;
import ru.liner.sensorpermission.utils.XLogger;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 24.08.2023, четверг
 **/
@SuppressWarnings("unchecked")
public class XposedCore implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam packageParam) {
        XposedBridge.hookAllMethods(SensorManager.class, "getDefaultSensor", new XC_MethodReplacement() {
            @Override
            protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                RemotePM.init();
                String callingPackageName = AndroidAppHelper.currentPackageName();
                int sensorType = (int) param.args[0];
                List<Sensor> sensorList = (List<Sensor>) XposedHelpers.callMethod(param.thisObject, "getSensorList", new Class[]{int.class}, sensorType);
                @SuppressLint("InlinedApi")
                boolean wakeUpSensor =
                        sensorType == Sensor.TYPE_PROXIMITY
                                || sensorType == Sensor.TYPE_SIGNIFICANT_MOTION
                                || sensorType == Sensor.TYPE_LOW_LATENCY_OFFBODY_DETECT
                                || sensorType == Sensor.TYPE_HINGE_ANGLE;

                if(!RemotePM.hasKey(callingPackageName))
                    RemotePM.putObject(callingPackageName, new SensorPermission(callingPackageName));
                SensorPermission sensorPermission = RemotePM.getObject(callingPackageName, SensorPermission.class);
                switch (sensorPermission.permissionStatus(sensorType)) {
                    case SensorPermission.State.DENIED:
                        XLogger.log("Denied permission %s for %s, hide sensor...", sensorType, callingPackageName);
                        return null;
                    case SensorPermission.State.GRANTED:
                        XLogger.log("Granted permission %s for %s, continue...", sensorType, callingPackageName);
                        for (Sensor sensor : sensorList) {
                            if (sensor.isWakeUpSensor() == wakeUpSensor) {
                                return sensor;
                            }
                        }
                        return null;
                    default:
                    case SensorPermission.State.UNKNOWN:
                        //TODO Create activity for ask permission
                        return null;
                }
            }
        });
    }
}
