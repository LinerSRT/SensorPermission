package ru.liner.sensorpermission.xposed;

import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import de.robv.android.xposed.XC_MethodHook;
import ru.liner.sensorpermission.BuildConfig;
import ru.liner.sensorpermission.permission.PermissionInfo;
import ru.liner.sensorpermission.permission.PermissionPackage;
import ru.liner.sensorpermission.sensor.SensorStatus;
import ru.liner.sensorpermission.sensor.SensorTool;
import ru.liner.sensorpermission.service.PermissionOverlayService;
import ru.liner.sensorpermission.utils.ApplicationManager;
import ru.liner.sensorpermission.utils.Consumer;
import ru.liner.sensorpermission.utils.RemotePM;
import ru.liner.sensorpermission.utils.XLogger;

/**
 * @noinspection unchecked
 */
public class PermissionModule implements IModule {
    @Override
    public void process(@NonNull Context packageContext, @NonNull String packageName, @NonNull ClassLoader classLoader, @NonNull XC_MethodHook.MethodHookParam methodParameters) {
        RemotePM.init(packageContext);
        String applicationName = ApplicationManager.getApplicationName(packageContext, packageName);
        XLogger.log("Loading data for [%s]", applicationName);
        List<Sensor> sensorList = Consumer
                .of(methodParameters.getResult())
                .next((Consumer.FunctionB<Object, List<Sensor>>) input -> new ArrayList<>((Collection<? extends Sensor>) input))
                .orElse(new ArrayList<>());
        if (sensorList.isEmpty()) {
            XLogger.log("Failed to load device sensor list, aborting module for %s", packageName);
        } else {
            PermissionPackage permissionPackage = Consumer.of(RemotePM.getObject(packageName, PermissionPackage.class)).orElse(new PermissionPackage(packageName));
            sensorList = sensorList.stream().filter(sensor -> {
                PermissionInfo permissionInfo = permissionPackage.getPermission(sensor.getType());
                XLogger.log("Checking %s for access to %s", applicationName, SensorTool.getSensorName(packageContext, sensor.getType()));
                switch (permissionInfo.status) {
                    case SensorStatus.UNKNOWN:
                        //TODO Ask permission here
                        //TODO Start new activity or overlay service
                        //TODO Block current thread before user choose their permission
                        //TODO Resume thread after user choose
                        packageContext.startService(new Intent(packageContext, PermissionOverlayService.class));
                        XLogger.log("Requesting permission");
                        break;
                    case SensorStatus.GRANTED:
                        XLogger.log("Permission granted");
                        return true;
                    case SensorStatus.GRANTED_ONCE:
                        permissionInfo.status = SensorStatus.UNKNOWN;
                        permissionPackage.setPermission(permissionInfo);
                        XLogger.log("Permission granted once, switching to unknown");
                        return true;
                    case SensorStatus.DENIED:
                    default:
                        XLogger.log("Permission denied");
                        return false;
                }
                return true;
            }).collect(Collectors.toList());
            RemotePM.putObject(packageName, permissionPackage);
            methodParameters.setResult(sensorList);
        }
    }

    @Override
    public boolean shouldProcess(@NonNull String packageName) {
        return !packageName.equals(BuildConfig.APPLICATION_ID) &&
                !packageName.equals("android") &&
                !packageName.startsWith("com.android");
    }
}
