package ru.liner.sensorpermission.xposed;

import android.content.Context;

import androidx.annotation.NonNull;

import de.robv.android.xposed.XC_MethodHook;

public interface IModule {
    void process(@NonNull Context packageContext, @NonNull String packageName, @NonNull ClassLoader classLoader, @NonNull XC_MethodHook.MethodHookParam methodParameters);
    boolean shouldProcess(@NonNull String packageName);
}
