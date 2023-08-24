package ru.liner.sensorpermission;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import ru.liner.sensorpermission.utils.XLogger;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 24.08.2023, четверг
 **/
public class XposedCore implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam packageParam) {
        XLogger.log("Handle: "+packageParam.packageName);
    }
}
