package ru.liner.sensorpermission.xposed.modules;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.Set;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import ru.liner.sensorpermission.BuildConfig;
import ru.liner.sensorpermission.utils.Consumer;
import ru.liner.sensorpermission.xposed.MethodHooker;
import ru.liner.sensorpermission.xposed.XposedModule;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 27.08.2023, 22:18
 *
 * @noinspection unchecked
 */
public class SystemUIModule extends XposedModule {
    public static final String PACKAGE = "com.android.systemui";
    private static final String NOTIFICATION_INFO_CLASS = String.format("%s.statusbar.NotificationInfo", PACKAGE);

    public SystemUIModule(@NonNull Context context, @NonNull ClassLoader classLoader) {
        super(PACKAGE, context, classLoader, SystemUIModule.class.getSimpleName());
    }

    @Override
    public void processModule() {
        log("Starting initialization...");
        MethodHooker.of(classLoader)
                .className(NOTIFICATION_INFO_CLASS)
                .methodName("bindNotification")
                .beforeFunction(hookedMethod ->
                        Consumer.of(hookedMethod.argument(hookedMethod.lastArgumentIndex()))
                                .next(input -> (Set<String>) input)
                                .ifPresent(input -> {
                                    input.add(BuildConfig.APPLICATION_ID);
                                    hookedMethod.argument(hookedMethod.lastArgumentIndex(), input);
                                }))
                .hook();
        log("Initialization finished successfully!");
    }


    public static SystemUIModule of(@NonNull XC_LoadPackage.LoadPackageParam packageParam) {
        return new SystemUIModule(contextOf(packageParam), packageParam.classLoader);
    }
}
