package ru.liner.colorfy.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;
import ru.liner.colorfy.utils.Reflect;
import ru.liner.sensorpermission.R;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 05.11.2022, суббота
 **/
public class ColorfyNestedScrollView extends NestedScrollView implements IWallpaperDataListener {


    public ColorfyNestedScrollView(@NonNull Context context) {
        super(context);
    }

    public ColorfyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorfyNestedScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        colorizeEdgeEffect(this, wallpaperData);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.scrollbar);
        Objects.requireNonNull(drawable).setColorFilter(new PorterDuffColorFilter(wallpaperData.primaryColor, PorterDuff.Mode.SRC_IN));
        if (isVerticalScrollBarEnabled()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setVerticalScrollbarThumbDrawable(drawable);
            } else {
                setVerticalThumbDrawable(drawable);
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                setHorizontalScrollbarThumbDrawable(drawable);
            } else {
                setHorizontalThumbDrawable(drawable);
            }
        }
    }

    @SuppressWarnings("JavaReflectionMemberAccess")
    @SuppressLint("DiscouragedPrivateApi")
    public void setVerticalThumbDrawable(Drawable drawable) {
        try {
            Field scrollCacheField = View.class.getDeclaredField("mScrollCache");
            scrollCacheField.setAccessible(true);
            Object scrollCache = scrollCacheField.get(this);
            Field scrollBarField = Objects.requireNonNull(scrollCache).getClass().getDeclaredField("scrollBar");
            scrollBarField.setAccessible(true);
            Object scrollbar = scrollBarField.get(scrollCache);
            Method setVerticalThumbDrawable = Objects.requireNonNull(scrollbar).getClass().getDeclaredMethod("setVerticalThumbDrawable", Drawable.class);
            setVerticalThumbDrawable.invoke(scrollbar, drawable);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("JavaReflectionMemberAccess")
    @SuppressLint("DiscouragedPrivateApi")
    public void setHorizontalThumbDrawable(Drawable drawable) {
        try {
            Field scrollCacheField = View.class.getDeclaredField("mScrollCache");
            scrollCacheField.setAccessible(true);
            Object scrollCache = scrollCacheField.get(this);
            Field scrollBarField = Objects.requireNonNull(scrollCache).getClass().getDeclaredField("scrollBar");
            scrollBarField.setAccessible(true);
            Object scrollbar = scrollBarField.get(scrollCache);
            Method setVerticalThumbDrawable = Objects.requireNonNull(scrollbar).getClass().getDeclaredMethod("setHorizontalThumbDrawable", Drawable.class);
            setVerticalThumbDrawable.invoke(scrollbar, drawable);
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private static void colorizeEdgeEffect(View view, WallpaperData wallpaperData){
        try {
            Field edgeGlowTopField = Reflect.findField(view.getClass(), "mEdgeGlowTop");
            Field edgeGlowBottomField = Reflect.findField(view.getClass(), "mEdgeGlowBottom");
            if(edgeGlowTopField == null || edgeGlowBottomField == null)
                return;
            Object edgeGlowTop = edgeGlowTopField.get(view);
            Object edgeGlowBottom = edgeGlowBottomField.get(view);
            if(edgeGlowTop == null || edgeGlowBottom == null)
                return;
            Method setColorMethod = Reflect.findMethod(edgeGlowTop.getClass(), "setColor", int.class);
            if(setColorMethod != null){
                setColorMethod.invoke(edgeGlowTop, wallpaperData.primaryColor);
                setColorMethod.invoke(edgeGlowBottom, wallpaperData.primaryColor);
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
