package ru.liner.colorfy.views;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;
import ru.liner.colorfy.utils.ColorUtils;
import ru.liner.colorfy.utils.Utils;
import ru.liner.sensorpermission.R;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 04.11.2022, пятница
 **/
public class ColorfyTextView extends AppCompatTextView implements IWallpaperDataListener {
    private boolean useOnPrimaryTextColor;
    private boolean enableBackgroundColor;
    private boolean usePrimaryBackgroundColor;
    private boolean tintCompoundDrawables;

    public ColorfyTextView(@NonNull Context context) {
        this(context, null);
    }

    public ColorfyTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ColorfyTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ColorfyTextView, defStyleAttr, 0);
        useOnPrimaryTextColor = typedArray.getBoolean(R.styleable.ColorfyTextView_useOnPrimaryTextColor, false);
        enableBackgroundColor = typedArray.getBoolean(R.styleable.ColorfyTextView_enableBackgroundColor, false);
        usePrimaryBackgroundColor = typedArray.getBoolean(R.styleable.ColorfyTextView_usePrimaryBackgroundColor, false);
        tintCompoundDrawables = typedArray.getBoolean(R.styleable.ColorfyTextView_tintCompoundDrawables, false);
        typedArray.recycle();
    }

    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        if (isEnabled()) {
            setTextColor(useOnPrimaryTextColor ? wallpaperData.textOnPrimaryColor : wallpaperData.textColor);
            setLinkTextColor(useOnPrimaryTextColor ? wallpaperData.textOnPrimaryColor : wallpaperData.secondaryColor);
        } else {
            setTextColor(wallpaperData.disabledTextColor);
        }
        if (enableBackgroundColor) {
            Drawable drawable = getBackground();
            if (drawable instanceof ColorDrawable) {
                setBackground(new ColorDrawable(usePrimaryBackgroundColor ? wallpaperData.primaryColor : ColorUtils.lightenColor(wallpaperData.backgroundColor, 0.9f)));
            } else if (drawable != null) {
                setBackgroundTintList(ColorStateList.valueOf(usePrimaryBackgroundColor ? wallpaperData.primaryColor : ColorUtils.lightenColor(wallpaperData.backgroundColor, 0.9f)));
            }
        }
        if (tintCompoundDrawables) {
            Drawable[] drawables = getCompoundDrawables();
            for (int i = 0; i < drawables.length; i++)
                drawables[i] = Utils.tintDrawable(drawables[i], wallpaperData.primaryColor);
            setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
        }
    }
}
