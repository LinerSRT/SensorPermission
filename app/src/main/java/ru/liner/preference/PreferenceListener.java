package ru.liner.preference;

import android.content.Context;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 29.08.2023, 14:47
 */
public abstract class PreferenceListener<V> implements Runnable {
    private final IPreference preference;
    private boolean isRunning;
    private boolean removeNotified;
    private Thread thread;

    private V oldValue;

    public PreferenceListener(Context context) {
        this.preference = PreferenceWrapper.get(context);
    }

    public void start() {
        if ((thread != null && thread.isAlive()) || isRunning)
            return;
        thread = new Thread(this);
        thread.start();
        isRunning = true;
    }

    public void stop() {
        if (!isRunning)
            return;
        isRunning = false;
        if (thread != null)
            thread.interrupt();
        try {
            if (thread != null)
                thread.join();
        } catch (InterruptedException ignored) {
        }
        thread = null;
    }


    public boolean isRunning() {
        return isRunning;
    }

    @Override
    public void run() {
        while (isRunning) {
            if (preference.has(key())) {
                removeNotified = false;
                if(defaultValue() == null && defaultValueClass() != null){
                    V newValue = preference.get(key(), defaultValueClass());
                    if(oldValue == null || !oldValue.equals(newValue)){
                        oldValue = newValue;
                        changed(newValue);
                    }
                } else if(defaultValue() != null){
                    changed(preference.get(key(), defaultValue()));
                }
            } else {
                if (!removeNotified) {
                    removeNotified = true;
                    oldValue = null;
                    removed();
                }
            }
            try {
                Thread.sleep(48);
            } catch (InterruptedException ignored) {

            }
        }
    }

    public abstract String key();

    public V defaultValue(){
        return null;
    }
    public Class<V> defaultValueClass(){
        return null;
    }

    public abstract void changed(V newValue);

    public abstract void removed();
}