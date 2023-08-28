package ru.liner.sensorpermission.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ru.liner.sensorpermission.BuildConfig;

/** @noinspection unused*/
public class ApplicationManager {

    /**
     * Find associated with package name application and loads their name
     *
     * @param context     application context
     * @param packageName searched package
     * @return name of application if found, package name if not
     */
    public static String getApplicationName(@Nullable Context context, @NonNull String packageName) {
        return Consumer.of(context)
                .next(input -> {
                    PackageManager packageManager = input.getPackageManager();
                    Optional<ResolveInfo> packageInfo = packageManager.queryIntentActivities(
                                    new Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER),
                                    0
                            )
                            .stream()
                            .filter(resolveInfo -> resolveInfo.activityInfo.packageName.equals(packageName))
                            .findFirst();
                    return packageInfo.map(resolveInfo -> resolveInfo.loadLabel(packageManager).toString()).orElse(packageName);
                }).get();
    }

    public static List<ResolveInfo> getAllApplications(@Nullable Context context) {
        return Consumer.of(context)
                .next((Consumer.FunctionB<Context, List<ResolveInfo>>) input -> input.getPackageManager().queryIntentActivities(new Intent(Intent.ACTION_MAIN, null).addCategory(Intent.CATEGORY_LAUNCHER), 0))
                .next(input -> input.stream().filter(resolveInfo -> !resolveInfo.activityInfo.packageName.equals(BuildConfig.APPLICATION_ID)).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }

    private static boolean isSystemPackage(@NonNull ResolveInfo resolveInfo) {
        return (resolveInfo.activityInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0;
    }
}
