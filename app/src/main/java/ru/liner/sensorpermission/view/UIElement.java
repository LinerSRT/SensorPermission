package ru.liner.sensorpermission.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.lang.reflect.Field;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 26.08.2023, 14:01
 */
public abstract class UIElement extends ConstraintLayout {
    public static final int NO_LAYOUT = 0;
    @NonNull
    protected View parent;
    @NonNull
    protected Context context;
    protected int width;
    protected int height;

    public UIElement(@NonNull Context context) {
        this(context, null);
    }

    public UIElement(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UIElement(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.parent = this;
        this.context = context;
        if (getLayoutRes() != NO_LAYOUT)
            LayoutInflater.from(context).inflate(getLayoutRes(), this, true);
        declareViews();
        for(Field field : getClass().getDeclaredFields())
            Log.d("TAGTAG", "UIElement: "+field.getName());


        if (attrs != null)
            obtainStyleAttributes(attrs, defStyleAttr);
        declareFunctionality();
    }

    public abstract void declareViews();

    public abstract void declareFunctionality();

    public void obtainStyleAttributes(@NonNull AttributeSet attributeSet, int defStyleAttr) {
    }

    @LayoutRes
    public int getLayoutRes() {
        return NO_LAYOUT;
    }

    @CallSuper
    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        width = right - left;
        height = bottom - top;
    }


    public <T extends View> T find(@IdRes int id) {
        return findViewById(id);
    }
}
