package ru.liner.sensorpermission;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.util.List;

import ru.liner.sensorpermission.utils.RemotePM;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 24.08.2023, четверг
 **/
public class Core extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        RemotePM.init(this);
    }

    public static String getApplicationName(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);
        for(ResolveInfo resolveInfo : resolveInfoList)
            if(resolveInfo.activityInfo.packageName.equals(packageName))
                return resolveInfo.loadLabel(packageManager).toString();
        return packageName;
    }
}
