package ru.liner.sensorpermission.service;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;

import ru.liner.colorfy.core.Colorfy;
import ru.liner.sensorpermission.R;
import ru.liner.sensorpermission.permission.PermissionRequest;
import ru.liner.sensorpermission.utils.RemotePM;
import ru.liner.sensorpermission.utils.RemotePMListener;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 27.08.2023, 14:04
 */
public class PermissionOverlayService extends ImmortalService {
    private static final String TAG = PermissionOverlayService.class.getSimpleName();
    private WindowManager windowManager;
    private Colorfy colorfy;
    private RemotePMListener<PermissionRequest> requestListener;

    @Override
    public void onCreate() {
        super.onCreate();
        RemotePM.init(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        colorfy = Colorfy.getInstance(this);
        requestListener = new RemotePMListener<PermissionRequest>(this) {
            @Override
            public String key() {
                return "pending_permission_request";
            }

            @Override
            public void changed(PermissionRequest newValue) {
                log("Received new request %s", newValue);
            }

            @Override
            public void removed() {
                log("Cleared request");
            }

            @Override
            public Class<PermissionRequest> defaultValueClass() {
                return PermissionRequest.class;
            }
        };
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        requestListener.start();
        Log.d(TAG, "Service started");
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        requestListener.stop();
        Log.d(TAG, "Service destroyed");
    }

    @NonNull
    @Override
    public String getChannelID() {
        return PermissionOverlayService.class.getSimpleName();
    }

    @NonNull
    @Override
    public String getChannelDescription() {
        return "Display permission overlay when app trying to access sensor";
    }

    @Override
    public int getNotificationIcon() {
        return R.drawable.baseline_privacy_tip_24;
    }

    @NonNull
    @Override
    public Class<? extends BroadcastReceiver> getRestartReceiverClass() {
        return PermissionOverlayRestartReceiver.class;
    }

    private void log(String message, Object... objects){
        Log.d(TAG, String.format(message, objects));
    }
}
