package ru.liner.preference;

import android.content.Context;

import androidx.annotation.NonNull;

import ru.liner.sensorpermission.BuildConfig;
import ru.liner.sensorpermission.Constant;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 29.08.2023, 14:35
 */
public class PreferenceWrapper {
    public static IPreference get(@NonNull Context context, @NonNull String preferenceName){
        String packageName = android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q ? context.getOpPackageName() : context.getPackageName();
        return packageName.equals(BuildConfig.APPLICATION_ID) ? new InternalPreferences(context, preferenceName) : new RemotePreferences(context, preferenceName);
    }
    public static IPreference get(@NonNull Context context){
        return get(context, Constant.preferences);
    }
}
