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


    public static void log(String tag, String message, Object... objects) {
        if(TextUtils.isEmpty(tag)) {
            StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTraceElements) {
                if (!stackTraceElement.getClassName().equals(XLogger.class.getName()) && stackTraceElement.getClassName().indexOf("java.lang.Thread") != 0) {
                    tag = stackTraceElement.getClassName();
                    break;
                }
            }
        }
        if (objects.length == 0) {
            try {
                XposedBridge.log(String.format("%s : %s", TextUtils.isEmpty(tag) ? DEFAULT_TAG : tag, message));
            } catch (NoClassDefFoundError e) {
                Log.d(TextUtils.isEmpty(tag) ? DEFAULT_TAG : tag, String.format(message, objects));
            }
        } else {
            try {
                XposedBridge.log(String.format("%s : %s", TextUtils.isEmpty(tag) ? DEFAULT_TAG : tag, String.format(message, objects)));
            } catch (NoClassDefFoundError e) {
                Log.d(TextUtils.isEmpty(tag) ? DEFAULT_TAG : tag, String.format(message, objects));
            }
        }
    }
    public static void log(String message, Object... objects) {
        log(null, message, objects);
    }
}
