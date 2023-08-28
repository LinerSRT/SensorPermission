package ru.liner.sensorpermission;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import ru.liner.sensorpermission.xposed.XposedModule;
import ru.liner.sensorpermission.xposed.modules.SensorModule;
import ru.liner.sensorpermission.xposed.modules.SensorPermissionModule;
import ru.liner.sensorpermission.xposed.modules.SystemUIModule;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 24.08.2023, четверг
 */
public class XposedCore implements IXposedHookLoadPackage {
    private final List<XposedModule> moduleList = new ArrayList<>();

    private <M extends XposedModule> void addModule(String packageName, M module) {
        if (moduleList.stream().noneMatch(xposedModule -> xposedModule.getPackageName().equals(packageName)))
            moduleList.add(module);
    }


    @SuppressLint("PrivateApi")
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam packageParam) {
        switch (packageParam.packageName) {
            case XposedModule.ANDROID:
                //Do nothing
                break;
            case SystemUIModule.PACKAGE:
                addModule(packageParam.packageName, SystemUIModule.of(packageParam));
                break;
            case BuildConfig.APPLICATION_ID:
                addModule(packageParam.packageName, SensorPermissionModule.of(packageParam));
                break;
            default:
                addModule(packageParam.packageName, SensorModule.of(packageParam));
                break;
        }

        for (XposedModule module : moduleList)
            if (module.shouldProcess(packageParam))
                module.processModule();

    }
}
