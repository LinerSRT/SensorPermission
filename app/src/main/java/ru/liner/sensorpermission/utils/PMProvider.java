package ru.liner.sensorpermission.utils;

import com.crossbowffs.remotepreferences.RemotePreferenceProvider;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 24.08.2023, четверг
 **/
public class PMProvider extends RemotePreferenceProvider {
    public PMProvider() {
        super("ru.liner.sensorpermission", new String[]{"sensorpermission"});
    }
}
