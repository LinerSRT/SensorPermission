package ru.liner.preference;

import ru.liner.sensorpermission.BuildConfig;
import ru.liner.sensorpermission.Constant;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 29.08.2023, 13:45
 */
public class RemotePreferenceProvider extends com.crossbowffs.remotepreferences.RemotePreferenceProvider {

    public RemotePreferenceProvider() {
        super(BuildConfig.APPLICATION_ID, new String[]{Constant.preferences});
    }
}
