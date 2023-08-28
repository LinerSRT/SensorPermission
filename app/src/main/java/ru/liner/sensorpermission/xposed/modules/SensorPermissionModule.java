package ru.liner.sensorpermission.xposed.modules;

import android.Manifest;
import android.app.AppOpsManager;
import android.content.Context;

import androidx.annotation.NonNull;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import ru.liner.sensorpermission.BuildConfig;
import ru.liner.sensorpermission.utils.Shell;
import ru.liner.sensorpermission.xposed.LspdScope;
import ru.liner.sensorpermission.xposed.XposedModule;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 27.08.2023, 22:18
 */
public class SensorPermissionModule extends XposedModule {
    public SensorPermissionModule(@NonNull Context context, @NonNull ClassLoader classLoader) {
        super(BuildConfig.APPLICATION_ID, context, classLoader, SensorPermissionModule.class.getSimpleName());
    }

    @Override
    public void processModule() {
        log("Starting initialization...");
        if(grantPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
            log("PackageManager --> Granted [READ_EXTERNAL_STORAGE]");
        if(grantPermission(Manifest.permission.QUERY_ALL_PACKAGES))
            log("PackageManager --> Granted [QUERY_ALL_PACKAGES]");
        if(grantPermission(Manifest.permission.SYSTEM_ALERT_WINDOW))
            log("PackageManager --> Granted [SYSTEM_ALERT_WINDOW]");
        if(grantAppOps(AppOpsManager.OPSTR_SYSTEM_ALERT_WINDOW))
            log("AppOpsManager --> Granted [OPSTR_SYSTEM_ALERT_WINDOW]");
        addToLspdScope("android");
        addToLspdScope("com.android.systemui");
        //ApplicationManager.getAllApplications(context).forEach(userApplication -> addToLspdScope(userApplication.activityInfo.packageName));
        log("Initialization finished successfully!");
    }

    private void addToLspdScope(@NonNull String packageName){
        if(LspdScope.contains(packageName)){
            log("LSPosed Scope --> Skipping %s, already in scope", packageName);
        } else {
            if(LspdScope.addSilent(packageName, 0)){
                log("LSPosed Scope --> Successfully added %s into scope", packageName);
            } else {
                log("LSPosed Scope --> Can't add %s into scope, aborting", packageName);
            }
        }
    }


    private boolean grantPermission(String permission){
        return Shell.exec(String.format("pm grant %s %s", BuildConfig.APPLICATION_ID, permission), true).isSuccess();
    }

    /** @noinspection SameParameterValue*/
    private boolean grantAppOps(String permission){
        return Shell.exec(String.format("appops set %s %s allow", BuildConfig.APPLICATION_ID, permission.split(":")[1].toUpperCase()), true).isSuccess();
    }

    public static SensorPermissionModule of(@NonNull XC_LoadPackage.LoadPackageParam packageParam){
        return new SensorPermissionModule(getApplicationContext(packageParam), packageParam.classLoader);
    }
}
