package ru.liner.sensorpermission.xposed;

import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

import android.content.Context;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;

import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 27.08.2023, 22:15
 */
public class XposedModule {
    public static final String ANDROID = "android";
    @NonNull
    protected String packageName;
    @NonNull
    protected Context context;
    @NonNull
    protected ClassLoader classLoader;
    @NonNull
    protected String TAG;

    public XposedModule(@NonNull String packageName, @NonNull Context context, @NonNull ClassLoader classLoader, @NonNull String TAG) {
        this.packageName = packageName;
        this.context = context;
        this.classLoader = classLoader;
        this.TAG = TAG;
    }

    public boolean shouldProcess(@NonNull XC_LoadPackage.LoadPackageParam loadedPackage) {
        return loadedPackage.packageName.equals(packageName);
    }

    public void processModule() {
    }

    public void log(String message, Object... objects) {
        XposedBridge.log(String.format("{%s} --> %s", TAG, String.format(message, objects)));
    }

    public static Context getSystemContext() {
        return (Context) callMethod(callStaticMethod(findClass("android.app.ActivityThread", null), "currentActivityThread"), "getSystemContext");
    }

    @NonNull
    public String getPackageName() {
        return packageName;
    }

    public static Context getApplicationContext(XC_LoadPackage.LoadPackageParam packageParam) {
        try {
            return getSystemContext().createPackageContext(packageParam.packageName, Context.CONTEXT_IGNORE_SECURITY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return getSystemContext();
    }
}
