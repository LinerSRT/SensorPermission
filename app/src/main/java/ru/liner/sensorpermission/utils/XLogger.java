package ru.liner.sensorpermission.utils;

import android.text.TextUtils;
import android.util.Log;

import de.robv.android.xposed.XposedBridge;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 24.08.2023, четверг
 **/
public class XLogger {
    private static final String DEFAULT_TAG = "SensorPermission";

    public static void log(String message, Object... objects) {
        XposedBridge.log(String.format("%s:%s", DEFAULT_TAG, String.format(message, objects)));
    }
}
