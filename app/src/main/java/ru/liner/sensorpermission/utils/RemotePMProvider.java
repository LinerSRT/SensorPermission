package ru.liner.sensorpermission.utils;

import com.crossbowffs.remotepreferences.RemotePreferenceProvider;

import ru.liner.sensorpermission.BuildConfig;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 24.08.2023, четверг
 **/
public class RemotePMProvider extends RemotePreferenceProvider {
    public RemotePMProvider() {
        super(BuildConfig.APPLICATION_ID, new String[]{"sensorpermission"});
    }
}
