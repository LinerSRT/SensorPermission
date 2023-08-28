package ru.liner.colorfy.views;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.appbar.MaterialToolbar;

import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 27.08.2023, 11:43
 */
public class ColorfyMaterialToolbar extends MaterialToolbar implements IWallpaperDataListener {
    public ColorfyMaterialToolbar(@NonNull Context context) {
        super(context);
    }

    public ColorfyMaterialToolbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ColorfyMaterialToolbar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        setBackgroundColor(wallpaperData.primaryColor);
        setTitle("ASDASDASD");
    }
}
