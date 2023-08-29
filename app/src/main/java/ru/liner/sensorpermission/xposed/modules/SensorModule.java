package ru.liner.sensorpermission.xposed.modules;

import android.content.Context;
import android.hardware.SensorManager;

import androidx.annotation.NonNull;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import ru.liner.preference.IPreference;
import ru.liner.preference.PreferenceWrapper;
import ru.liner.sensorpermission.BuildConfig;
import ru.liner.sensorpermission.permission.PermissionInfo;
import ru.liner.sensorpermission.permission.PermissionPackage;
import ru.liner.sensorpermission.sensor.SensorStatus;
import ru.liner.sensorpermission.utils.ApplicationManager;
import ru.liner.sensorpermission.utils.Consumer;
import ru.liner.sensorpermission.xposed.MethodHooker;
import ru.liner.sensorpermission.xposed.XposedModule;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 27.08.2023, 22:18
 */
public class SensorModule extends XposedModule {
    private IPreference preference;
    public SensorModule(@NonNull String packageName, @NonNull Context context, @NonNull ClassLoader classLoader) {
        super(packageName, context, classLoader, SensorModule.class.getSimpleName());
        preference = PreferenceWrapper.get(context);
    }

    @Override
    public void processModule() {
        log("Starting initialization for %s", packageName);
        MethodHooker.of(classLoader)
                .className(SensorManager.class)
                .methodName("getDefaultSensor")
                .beforeFunction(hookedMethod -> {
                    PermissionPackage permissionPackage = Consumer.of(preference.get(packageName, PermissionPackage.class)).orElse(new PermissionPackage(packageName));
                    PermissionInfo permissionInfo = permissionPackage.getPermission(hookedMethod.argument(0));
                    String applicationName = ApplicationManager.getApplicationName(context, packageName);
                    preference.put(packageName, permissionPackage);
                    switch (permissionInfo.status) {
                        case SensorStatus.DENIED:
                            hookedMethod.result(null);
                            log("Denied permission {%s} for %s", "sensorName", applicationName);
                            break;
                        case SensorStatus.GRANTED:
                            log("Granted permission {%s} for %s", "sensorName", applicationName);
                            break;
                        case SensorStatus.GRANTED_ONCE:
                            log("Granted once permission {%s} for %s", "sensorName", applicationName);
                            break;
                        case SensorStatus.UNKNOWN:
                            log("Unknown permission status {%s} for %s", "sensorName", applicationName);
                            hookedMethod.result(null);
                            break;
                    }
                })
                .hook();
        log("Initialization finished successfully!");
    }

    @Override
    public boolean shouldProcess(@NonNull XC_LoadPackage.LoadPackageParam loadedPackage) {
        return !loadedPackage.packageName.equals(BuildConfig.APPLICATION_ID) &&
                !loadedPackage.packageName.equals(SystemUIModule.PACKAGE) &&
                !loadedPackage.packageName.equals(XposedModule.ANDROID) &&
                !loadedPackage.packageName.startsWith(XposedModule.ANDROID_PACKAGE) &&
                !loadedPackage.packageName.startsWith(XposedModule.GOOGLE_PACKAGE);
    }

    public static SensorModule of(@NonNull XC_LoadPackage.LoadPackageParam packageParam) {
        return new SensorModule(packageParam.packageName, contextOf(packageParam), packageParam.classLoader);
    }
}
