package ru.liner.sensorpermission.service;

import android.app.AlarmManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import ru.liner.colorfy.core.Colorfy;
import ru.liner.sensorpermission.BuildConfig;
import ru.liner.sensorpermission.R;
import ru.liner.sensorpermission.permission.PermissionRequest;
import ru.liner.sensorpermission.utils.Consumer;
import ru.liner.sensorpermission.utils.RemotePM;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 27.08.2023, 14:04
 */
public class PermissionOverlayService extends ImmortalService implements RemotePM.ChangeListener<PermissionRequest> {
    private static final String TAG = PermissionOverlayService.class.getSimpleName();
    private WindowManager windowManager;
    private Colorfy colorfy;

    @Override
    public void onCreate() {
        super.onCreate();
        RemotePM.init(this);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        colorfy = Colorfy.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started");

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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
    public String getNotificationTitle() {
        return "Permission overlay service";
    }

    @NonNull
    @Override
    protected String getNotificationText() {
        return "Display permission overlay when app trying to access sensor";
    }

    @NonNull
    @Override
    public Class<? extends BroadcastReceiver> getRestartReceiverClass() {
        return PermissionOverlayRestartReceiver.class;
    }

    @Override
    public void onChanged(@Nullable PermissionRequest newValue) {
        Consumer.of(newValue).ifPresent(input -> Log.d(TAG, "Received pending request: " + newValue.toString()));
    }

    @Override
    public void onRemoved() {
        Log.d(TAG, "Pending request was removed");
    }

    @NonNull
    @Override
    public String key() {
        return "pending_permission_request";
    }

    @NonNull
    @Override
    public PermissionRequest defaultValue() {
        return new PermissionRequest(BuildConfig.APPLICATION_ID, 0);
    }
}
