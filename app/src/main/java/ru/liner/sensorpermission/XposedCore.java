package ru.liner.sensorpermission;

import android.app.AndroidAppHelper;
import android.content.Context;
import android.hardware.Sensor;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
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
        XposedHelpers.findAndHookMethod("android.hardware.SystemSensorManager", packageParam.classLoader,
                "getFullSensorList", new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        if (!packageParam.packageName.equals(BuildConfig.APPLICATION_ID)) {
                            List<Sensor> sensorList = new ArrayList<>((Collection<? extends Sensor>) param.getResult());
                            Iterator<Sensor> sensorIterator = sensorList.iterator();
                            RemotePM.init();
                            String callingPackageName = AndroidAppHelper.currentPackageName();
                            Context context = AndroidAppHelper.currentApplication();
                            while (sensorIterator.hasNext()) {
                                Sensor sensor = sensorIterator.next();
                                SensorPermission sensorPermission = RemotePM.hasKey(callingPackageName) ? RemotePM.getObject(callingPackageName, SensorPermission.class) : new SensorPermission(callingPackageName);
                                if (!sensorPermission.hasPermission(sensor.getType()))
                                    sensorPermission.forgetPermission(sensor.getType());
                                switch (sensorPermission.permissionStatus(sensor.getType())) {
                                    case SensorPermission.State.DENIED:
                                        XLogger.log("Denied permission %s for %s, hide sensor...", sensor.getStringType(), callingPackageName);
                                        sensorIterator.remove();
                                        break;
                                    case SensorPermission.State.GRANTED:
                                        XLogger.log("Granted permission %s for %s, continue...", sensor.getStringType(), callingPackageName);
                                        break;
                                    default:
                                    case SensorPermission.State.UNKNOWN:
                                        //TODO Create activity for ask permission
                                        XLogger.log("Missing permission definition for %s", callingPackageName);
                                        break;
                                }
                                RemotePM.putObject(callingPackageName, sensorPermission);
                            }
                            param.setResult(sensorList);
                        }
                    }
                });
    }
}
