package ru.liner.sensorpermission.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import ru.liner.sensorpermission.utils.Consumer;
import ru.liner.sensorpermission.utils.RemotePM;
import ru.liner.sensorpermission.view.PermissionRequestView;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 26.08.2023, 13:57
 */
public class PermissionOverlayService extends Service {
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private PermissionRequestView permissionView;

    @Override
    public void onCreate() {
        super.onCreate();
        RemotePM.init(this);
        windowManager = ((WindowManager) getSystemService(WINDOW_SERVICE));
        layoutParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                        | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                        | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD,
                PixelFormat.TRANSLUCENT);
        layoutParams.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String packageName = intent.getStringExtra("package_name");
        int sensor = intent.getIntExtra("sensor", Integer.MIN_VALUE);
        if (packageName == null || packageName.length() == 0) {
            stopSelf();
        } else {
            if (permissionView == null)
                permissionView = new PermissionRequestView(this);
            permissionView.setData(packageName, sensor);
            permissionView.setCallback(new PermissionRequestView.Callback() {
                @Override
                public void onBackPressed(PermissionRequestView view) {
                    stopSelf();
                }

                @Override
                public void onAllowPressed(PermissionRequestView view) {

                }

                @Override
                public void onDenyPressed(PermissionRequestView view) {

                }
            });
            try {
                windowManager.removeViewImmediate(permissionView);
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            }
            windowManager.addView(permissionView, layoutParams);
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Consumer.of(windowManager)
                .ifPresent(input -> Consumer.of(permissionView).ifPresent(view -> {
                    try {
                        input.removeViewImmediate(view);
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}