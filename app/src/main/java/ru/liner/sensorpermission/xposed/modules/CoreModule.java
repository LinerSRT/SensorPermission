package ru.liner.sensorpermission.xposed.modules;

import android.Manifest;
import android.content.Context;

import androidx.annotation.NonNull;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import ru.liner.sensorpermission.BuildConfig;
import ru.liner.sensorpermission.utils.Shell;
import ru.liner.sensorpermission.xposed.LspdScope;
import ru.liner.sensorpermission.xposed.XposedModule;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 27.08.2023, 22:18
 */
public class CoreModule extends XposedModule {
    public CoreModule(@NonNull Context context, @NonNull ClassLoader classLoader) {
        super(BuildConfig.APPLICATION_ID, context, classLoader, CoreModule.class.getSimpleName());
    }

    @Override
    public void processModule() {
        log("Starting initialization...");
        if(grantPermission(Manifest.permission.READ_EXTERNAL_STORAGE))
            log("PackageManager --> Granted [READ_EXTERNAL_STORAGE]");
        if(grantPermission(Manifest.permission.QUERY_ALL_PACKAGES))
            log("PackageManager --> Granted [QUERY_ALL_PACKAGES]");
        if(grantPermission(Manifest.permission.SYSTEM_ALERT_WINDOW))
            log("PackageManager --> Granted [SYSTEM_ALERT_WINDOW]");
        if(grantAppOps(AppOpsPermission.SYSTEM_ALERT_WINDOW))
            log("AppOpsManager --> Granted [SYSTEM_ALERT_WINDOW]");
        if(disableHeadsUpNotification(packageName))
            log("Notification --> Disabling headsup notification");
        addToLspdScope("android");
        addToLspdScope("com.android.systemui");
        //ApplicationManager.getAllApplications(context).forEach(userApplication -> addToLspdScope(userApplication.activityInfo.packageName));
//        context.startService(new Intent().setComponent(new ComponentName(packageName, String.format("%s.service.PermissionOverlayService", packageName))));
        log("Initialization finished successfully!");
    }

    private void addToLspdScope(@NonNull String packageName){
        if(LspdScope.contains(packageName)){
            log("LSPosed Scope --> Skipping %s, already in scope", packageName);
        } else {
            if(LspdScope.addSilent(packageName, 0)){
                log("LSPosed Scope --> Successfully added %s into scope", packageName);
            } else {
                log("LSPosed Scope --> Can't add %s into scope, aborting", packageName);
            }
        }
    }

    private boolean disableHeadsUpNotification(@NonNull String packageName){
        return Shell.exec(String.format("service call notification 9 s16 \"%s\" i32 uid i32 0", packageName), true).isSuccess();
    }

    private boolean grantPermission(String permission){
        return Shell.exec(String.format("pm grant %s %s", BuildConfig.APPLICATION_ID, permission), true).isSuccess();
    }

    /** @noinspection SameParameterValue*/
    private boolean grantAppOps(AppOpsPermission permission){
        return Shell.exec(String.format("appops set %s %s allow", BuildConfig.APPLICATION_ID, permission.name()), true).isSuccess();
    }

    /** @noinspection SameParameterValue*/
    private boolean denyAppOps(AppOpsPermission permission){
        return Shell.exec(String.format("appops set %s %s deny", BuildConfig.APPLICATION_ID, permission.name()), true).isSuccess();
    }

    /** @noinspection unused*/
    private enum AppOpsPermission {
            COARSE_LOCATION,
            FINE_LOCATION,
            GPS,
            VIBRATE,
            READ_CONTACTS,
            WRITE_CONTACTS,
            READ_CALL_LOG,
            WRITE_CALL_LOG,
            READ_CALENDAR,
            WRITE_CALENDAR,
            WIFI_SCAN,
            POST_NOTIFICATION,
            NEIGHBORING_CELLS,
            CALL_PHONE,
            READ_SMS,
            WRITE_SMS,
            RECEIVE_SMS,
            RECEIVE_EMERGECY_SMS,
            RECEIVE_MMS,
            RECEIVE_WAP_PUSH,
            SEND_SMS,
            READ_ICC_SMS,
            WRITE_ICC_SMS,
            WRITE_SETTINGS,
            SYSTEM_ALERT_WINDOW,
            ACCESS_NOTIFICATIONS,
            CAMERA,
            RECORD_AUDIO,
            PLAY_AUDIO,
            READ_CLIPBOARD,
            WRITE_CLIPBOARD,
            TAKE_MEDIA_BUTTONS,
            TAKE_AUDIO_FOCUS,
            AUDIO_MASTER_VOLUME,
            AUDIO_VOICE_VOLUME,
            AUDIO_RING_VOLUME,
            AUDIO_MEDIA_VOLUME,
            AUDIO_ALARM_VOLUME,
            AUDIO_NOTIFICATION_VOLUME,
            AUDIO_BLUETOOTH_VOLUME,
            WAKE_LOCK,
            MONITOR_LOCATION,
            MONITOR_HIGH_POWER_LOCATION,
            GET_USAGE_STATS,
            MUTE_MICROPHONE,
            TOAST_WINDOW,
            PROJECT_MEDIA,
            ACTIVATE_VPN,
            WRITE_WALLPAPER,
            ASSIST_STRUCTURE,
            ASSIST_SCREENSHOT,
            OP_READ_PHONE_STATE,
            ADD_VOICEMAIL,
            USE_SIP,
            PROCESS_OUTGOING_CALLS,
            USE_FINGERPRINT,
            BODY_SENSORS,
            READ_CELL_BROADCASTS,
            MOCK_LOCATION,
            READ_EXTERNAL_STORAGE,
            WRITE_EXTERNAL_STORAGE,
            TURN_ON_SCREEN,
            GET_ACCOUNTS,
            RUN_IN_BACKGROUND,
            AUDIO_ACCESSIBILITY_VOLUME,
            READ_PHONE_NUMBERS,
            REQUEST_INSTALL_PACKAGES,
            PICTURE_IN_PICTURE,
            INSTANT_APP_START_FOREGROUND,
            ANSWER_PHONE_CALLS;
        /** @noinspection unused*/
        /** @noinspection unused*/
    }


    public static CoreModule of(@NonNull XC_LoadPackage.LoadPackageParam packageParam){
        return new CoreModule(contextOf(packageParam), packageParam.classLoader);
    }
}
