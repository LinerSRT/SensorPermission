package ru.liner.sensorpermission.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import ru.liner.sensorpermission.Core;
import ru.liner.sensorpermission.R;
import ru.liner.sensorpermission.sensor.SensorPermission;
import ru.liner.sensorpermission.utils.RemotePM;

public class PermissionRequestActivity extends FullscreenActivity {
    private ImageView permissionIcon;
    private TextView permissionMessage;
    private Button permissionAllow;
    private Button permissionDeny;
    private SensorPermission sensorPermission;
    private int sensorType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission_request);
        permissionIcon = findViewById(R.id.permissionIcon);
        permissionMessage = findViewById(R.id.permissionMessage);
        permissionAllow = findViewById(R.id.permissionAllow);
        permissionDeny = findViewById(R.id.permissionDeny);



        String callingPackage = getIntent().getStringExtra("calling_package");
        sensorPermission = TextUtils.isEmpty(callingPackage) ? new SensorPermission(callingPackage) : RemotePM.getObject(callingPackage, SensorPermission.class);
        sensorType = getIntent().getIntExtra("sensor_type", -999);
        if (sensorType == -999 || sensorPermission == null) {
            finishRequest();
        } else {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
            spannableStringBuilder.append("Allow ")
                    .append(createMarkedText(Core.getApplicationName(this, sensorPermission.getPackageName())))
                    .append(" access to ")
                    .append(createMarkedText(SensorPermission.getSensorName(this, sensorType)))
                    .append("?");
            permissionMessage.setText(spannableStringBuilder);
            permissionIcon.setImageDrawable(SensorPermission.getSensorIcon(this, sensorType));
            permissionAllow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sensorPermission.grantPermission(sensorType);
                    finishRequest();
                }
            });
            permissionDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sensorPermission.revokePermission(sensorType);
                    finishRequest();
                }
            });
        }
    }


    private void finishRequest() {
        RemotePM.putObject(sensorPermission.getPackageName(), sensorPermission);
        RemotePM.put("process_requested_permission", false);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishRequest();
    }

    private SpannableString createMarkedText(String text) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, spannableString.length(), 0);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, spannableString.length(), 0);
        return spannableString;
    }
}