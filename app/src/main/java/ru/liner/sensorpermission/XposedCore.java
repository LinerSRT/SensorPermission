package ru.liner.sensorpermission;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import android.app.AndroidAppHelper;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import ru.liner.sensorpermission.utils.XLogger;
import ru.liner.sensorpermission.xposed.PermissionModule;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 24.08.2023, четверг
 **/
public class XposedCore implements IXposedHookLoadPackage {
    private static final String SYSTEM_SENSOR_MANAGER_CLASS = "android.hardware.SystemSensorManager";
    private static final PermissionModule permissionModule = new PermissionModule();

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam packageParam) {
        findAndHookMethod(SYSTEM_SENSOR_MANAGER_CLASS, packageParam.classLoader, "getFullSensorList", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam methodHookParam) {
                if (permissionModule.shouldProcess(packageParam.packageName)) {
                    permissionModule.process(AndroidAppHelper.currentApplication(), packageParam.packageName, packageParam.classLoader, methodHookParam);
                } else {
                    XLogger.log("Skipping %s, module does not allow this package to modify", packageParam.packageName);
                }
            }
        });
    }
}
