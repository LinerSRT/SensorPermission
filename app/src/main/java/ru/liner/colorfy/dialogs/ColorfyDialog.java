package ru.liner.colorfy.dialogs;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.CallSuper;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.annotation.StyleRes;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ru.liner.colorfy.Config;
import ru.liner.colorfy.core.Colorfy;
import ru.liner.colorfy.core.RecyclerViewListener;
import ru.liner.colorfy.core.WallpaperData;
import ru.liner.colorfy.listener.IWallpaperDataListener;
import ru.liner.colorfy.listener.IWallpaperListener;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 24.05.2023, среда
 **/
public abstract class ColorfyDialog implements DialogInterface.OnShowListener, DialogInterface.OnDismissListener, IWallpaperDataListener, IWallpaperListener {
    @Nullable
    protected Colorfy colorfy;
    private List<RecyclerViewListener> recyclerViewListenerPool;
    protected Context context;
    protected View contentView;
    protected Dialog dialog;
    protected Window window;

    public ColorfyDialog(Context context) {
        this.context = context;
        this.colorfy = Colorfy.getInstance(context);
        this.colorfy.addWallpaperDataListener(getClass().getSimpleName(), this);
        this.colorfy.addWallpaperListener(getClass().getSimpleName(), this);
        this.recyclerViewListenerPool = new ArrayList<>();
        this.contentView = LayoutInflater.from(context).inflate(getContentViewLayout(), null, false);
        this.dialog = new Dialog(context, getDialogTheme());
        this.dialog.setOnShowListener(this);
        this.dialog.setOnDismissListener(this);
        this.window = dialog.getWindow();
        this.window.setBackgroundDrawableResource(android.R.color.transparent);
        this.window.requestFeature(Window.FEATURE_NO_TITLE);
        this.dialog.setContentView(contentView);
    }

    /**
     * Provide layout resource id, for creating dialog window
     *
     * @return none
     */
    @LayoutRes
    public abstract int getContentViewLayout();

    /**
     * Provide theme resource id, for styling dialog window. Maybe 0
     *
     * @return none
     */
    @StyleRes
    public abstract int getDialogTheme();


    /**
     * Show dialog
     */
    public void show() {
        if (colorfy != null && ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            colorfy.requestColors();
        dialog.show();
    }

    /**
     * Hide dialog
     */
    public void hide() {
        dialog.hide();
    }

    /**
     * Dismiss dialog
     */
    public void dismiss() {
        if (colorfy != null && Config.automaticListenersLifecycle) {
            colorfy.removeWallpaperDataListener(getClass().getSimpleName(), this);
            colorfy.removeWallpaperListener(getClass().getSimpleName(), this);
        }
        dialog.dismiss();
    }

    @Override
    public void onDismiss(DialogInterface dialogInterface) {

    }

    @Override
    @CallSuper
    public void onShow(DialogInterface dialogInterface) {
        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        window.getDecorView().setSystemUiVisibility(uiOptions);
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE);
        if (colorfy == null || ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            return;
        applyWallpaperData((ViewGroup) ((ViewGroup)dialog.findViewById(android.R.id.content)).getChildAt(0), colorfy.getLastWallpaperData());
    }

    @Override
    public void onChanged(@NonNull WallpaperData wallpaperData) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            return;
        applyWallpaperData((ViewGroup) ((ViewGroup)dialog.findViewById(android.R.id.content)).getChildAt(0), wallpaperData);
    }


    @RequiresPermission(anyOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    private void processRecyclerView(RecyclerView recyclerView) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || colorfy == null)
            return;
        WallpaperData wallpaperData = colorfy.getCurrentWallpaperData(); if (wallpaperData == null)
            return;
        applyWallpaperData(recyclerView, wallpaperData);
        String hashCode = recyclerView.getClass().getSimpleName() + recyclerView.getId();
        for (RecyclerViewListener item : recyclerViewListenerPool)
            if (item.getHashCode().equals(hashCode))
                return;
        RecyclerViewListener listener = new RecyclerViewListener(hashCode) {
            @Override
            public void requestColors(RecyclerView recyclerView) {
                applyWallpaperData(recyclerView, wallpaperData);
            }
        };
        recyclerViewListenerPool.add(listener);
        recyclerView.addOnScrollListener(listener);
    }

    private void applyWallpaperData(@NonNull RecyclerView recyclerView, @NonNull WallpaperData wallpaperData) {
        int childCount = recyclerView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            RecyclerView.ViewHolder holder = recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            if (holder instanceof IWallpaperDataListener)
                ((IWallpaperDataListener) holder).onChanged(wallpaperData);
        }
    }

    @RequiresPermission(anyOf = {Manifest.permission.READ_EXTERNAL_STORAGE})
    private void applyWallpaperData(@NonNull ViewGroup viewGroup, @Nullable WallpaperData wallpaperData) {
        if (wallpaperData == null)
            return;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof IWallpaperDataListener) {
                ((IWallpaperDataListener) child).onChanged(wallpaperData);
                if (child instanceof ViewGroup)
                    applyWallpaperData((ViewGroup) child, wallpaperData);
            } else if (child instanceof RecyclerView) {
                processRecyclerView((RecyclerView) child);
            } else if (child instanceof ViewGroup) {
                applyWallpaperData((ViewGroup) child, wallpaperData);
            }
        }
    }

    @Override
    public void onWallpaperChanged(@NonNull Bitmap bitmap, boolean isLiveWallpaper) {

    }
}
