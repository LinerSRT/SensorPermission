package ru.liner.sensorpermission;

import android.annotation.SuppressLint;

import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import ru.liner.sensorpermission.xposed.XposedModule;
import ru.liner.sensorpermission.xposed.modules.CoreModule;
import ru.liner.sensorpermission.xposed.modules.SensorModule;
import ru.liner.sensorpermission.xposed.modules.SystemUIModule;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 24.08.2023, четверг
 */
public class XposedCore implements IXposedHookLoadPackage {
    private final HashMap<String, XposedModule> moduleList = new HashMap<>();

    private <M extends XposedModule> void addModule(String packageName, M module) {
        if (!moduleList.containsKey(packageName))
            moduleList.put(packageName, module);
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
                addModule(packageParam.packageName, CoreModule.of(packageParam));
                break;
            default:
                addModule(packageParam.packageName, SensorModule.of(packageParam));
                break;
        }
        for(Map.Entry<String, XposedModule> moduleEntry : moduleList.entrySet())
            if(moduleEntry.getValue().shouldProcess(packageParam))
                moduleEntry.getValue().processModule();
    }
}
