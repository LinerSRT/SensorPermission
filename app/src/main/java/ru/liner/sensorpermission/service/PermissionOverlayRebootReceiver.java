package ru.liner.sensorpermission.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ru.liner.sensorpermission.utils.Consumer;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 27.08.2023, 14:11
 */
public class PermissionOverlayRebootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Consumer.of(intent.getAction())
                .ifPresent(input -> {
                    if (input.equals(Intent.ACTION_BOOT_COMPLETED)) {
                        Intent service = new Intent(context, PermissionOverlayService.class);
                        context.startForegroundService(service);
                    }
                });
    }
}
