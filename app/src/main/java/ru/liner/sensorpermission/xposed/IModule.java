package ru.liner.sensorpermission.xposed;

import android.content.Context;

import androidx.annotation.NonNull;

import de.robv.android.xposed.XC_MethodHook;

/** @noinspection unused*/
public interface IModule {
    String TAG = IModule.class.getSimpleName();
    void process(@NonNull Context packageContext, @NonNull String packageName, @NonNull ClassLoader classLoader, XC_MethodHook.MethodHookParam methodParameters);
    boolean shouldProcess(@NonNull String packageName);
}
