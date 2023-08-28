package ru.liner.sensorpermission.xposed;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ru.liner.sensorpermission.BuildConfig;
import ru.liner.sensorpermission.utils.Shell;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 27.08.2023, 21:23
 */
public class LspdScope {
    private static final String TAG = LspdScope.class.getSimpleName();
    private static final String BINARY = "/data/adb/lspd/bin/cli";

    public static boolean add(@NonNull String packageName, @IntRange(from = 0, to = Integer.MAX_VALUE) int uid) {
        if(contains(packageName)){
            XLogger.log(TAG, "Skipping %s, already in scope list", packageName);
            return false;
        }
        Shell.Result addSystemUIScope = Shell.exec(String.format(".%s scope set %s -a %s/%s", BINARY, BuildConfig.APPLICATION_ID, packageName, uid), true);
        if (addSystemUIScope.isSuccess()) {
            XLogger.log(TAG, "Added %s to %s scope list", packageName, BuildConfig.APPLICATION_ID);
            return true;
        } else {
            XLogger.log(TAG, "Failed to add %s to %s scope list", packageName, BuildConfig.APPLICATION_ID);
            return false;
        }
    }

    public static boolean addSilent(@NonNull String packageName, @IntRange(from = 0, to = Integer.MAX_VALUE) int uid){
        return Shell.exec(String.format(".%s scope set %s -a %s/%s", BINARY, BuildConfig.APPLICATION_ID, packageName, uid), true).isSuccess();
    }

    public static List<String> list() {
        Shell.Result result = Shell.exec(String.format(".%s scope ls %s", BINARY, BuildConfig.APPLICATION_ID), true);
        return result.isSuccess() ? Arrays.stream(result.getResponse().split("/0")).collect(Collectors.toList()) : new ArrayList<>();
    }

    public static boolean contains(@NonNull String checkPackageName) {
        for (String packageName : list())
            return packageName.equals(checkPackageName);
        return false;
    }
}
