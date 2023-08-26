package ru.liner.sensorpermission.view;

import android.content.Context;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import ru.liner.sensorpermission.R;
import ru.liner.sensorpermission.sensor.Sensor;
import ru.liner.sensorpermission.sensor.SensorTool;
import ru.liner.sensorpermission.utils.ApplicationManager;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 26.08.2023, 14:06
 */
public class PermissionRequestView extends UIElement {
    private ImageView permissionIcon;
    private TextView permissionMessage;
    private Button permissionAllow;
    private Button permissionDeny;
    @Nullable
    private Callback callback;

    public PermissionRequestView(@NonNull Context context) {
        super(context);
    }

    @Override
    public void declareViews() {
        permissionIcon = find(R.id.permissionIcon);
        permissionMessage = find(R.id.permissionMessage);
        permissionAllow = find(R.id.permissionAllow);
        permissionDeny = find(R.id.permissionDeny);
    }

    @Override
    public void declareFunctionality() {
        permissionAllow.setOnClickListener(view -> {
            if(callback != null)
                callback.onAllowPressed(PermissionRequestView.this);
        });
        permissionDeny.setOnClickListener(view -> {
            if(callback != null)
                callback.onDenyPressed(PermissionRequestView.this);
        });
    }

    public void setData(@NonNull String packageName, @Sensor int sensor){
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        spannableStringBuilder.append("Allow ")
                .append(createMarkedText(ApplicationManager.getApplicationName(getContext(), packageName)))
                .append(" access to ")
                .append(createMarkedText(SensorTool.getSensorName(getContext(), sensor)))
                .append("?");
        permissionMessage.setText(spannableStringBuilder);
        permissionIcon.setImageDrawable(SensorTool.getSensorIcon(getContext(), sensor));
    }


    @Override
    public int getLayoutRes() {
        return R.layout.activity_permission_request;
    }

    private SpannableString createMarkedText(String text) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.textHighlightColor)), 0, spannableString.length(), 0);
        spannableString.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, spannableString.length(), 0);
        return spannableString;
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if ((event.getKeyCode() == KeyEvent.KEYCODE_BACK || event.getKeyCode() == KeyEvent.KEYCODE_HOME|| event.getKeyCode() == KeyEvent.KEYCODE_MOVE_HOME) && callback != null) {
            callback.onBackPressed(this);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }


    public void setCallback(@Nullable Callback callback) {
        this.callback = callback;
    }

    public interface Callback{
        void onBackPressed(PermissionRequestView view);
        void onAllowPressed(PermissionRequestView view);
        void onDenyPressed(PermissionRequestView view);
    }
}
