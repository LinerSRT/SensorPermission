package ru.liner.sensorpermission.utils;

import android.os.Environment;

import androidx.annotation.NonNull;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import ru.liner.sensorpermission.BuildConfig;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 29.08.2023, 0:25
 *
 * @noinspection unused
 */
public class XPM {
    private static final File preferenceFile = new File(Environment.getDataDirectory(), String.format("data/%s/preferences.dat", BuildConfig.APPLICATION_ID));
    private final Lock lock = new ReentrantLock();
    private final Gson gson;
    private List<Entry<Object>> entryList;
    private final Map<String, KeyChangeListener<?>> listenerMap;


    public XPM() {
        if (!preferenceFile.exists())
            try {
                if (!preferenceFile.createNewFile())
                    throw new RuntimeException("Cannot create preference file!");
            } catch (IOException e) {
                e.printStackTrace();
            }
        gson = new Gson();
        listenerMap = new ConcurrentHashMap<>();
        loadPreferences();
    }

    /** @noinspection unchecked*/
    public <Value> Value get(String key, Value defaultValue) {
        loadPreferences();
        if(containKey(key))
            return (Value) entryList.get(indexOf(key)).value;
        return defaultValue;
    }

    public <Value> void set(String key, Value value) {
        //TODO Check if value are same, do not any changes!
        loadPreferences();
        if(containKey(key)){
            entryList.set(indexOf(key), new Entry<>(key, value));
        } else {
            entryList.add(new Entry<>(key, value));
        }
        savePreferences();
        notifyListeners(key, value);
    }

    public void remove(String key){
        loadPreferences();
        if(containKey(key)){
            entryList.remove(indexOf(key));
            savePreferences();
            notifyListeners(key);
        }
    }

    public boolean containKey(String key) {
        for (Entry<?> entry : entryList)
            if (entry.key.equals(key))
                return true;
        return false;
    }

    private int indexOf(String key){
        for (int i = 0; i < entryList.size(); i++)
            if(entryList.get(i).key.equals(key))
                return i;
        return -1;
    }

    /** @noinspection unchecked*/
    private <Value> void notifyListeners(String key, Value value) {
        KeyChangeListener<Value> listener = (KeyChangeListener<Value>) listenerMap.get(key);
        if (listener != null)
            listener.onChanged(key, value);
    }
    /** @noinspection unchecked*/
    private <Value> void notifyListeners(String key) {
        KeyChangeListener<Value> listener = (KeyChangeListener<Value>) listenerMap.get(key);
        if (listener != null)
            listener.onRemoved();
    }

    public <Value> void addKeyChangeListener(String key, KeyChangeListener<Value> listener) {
        listenerMap.put(key, listener);
    }

    public void removeKeyChangeListener(String key) {
        listenerMap.remove(key);
    }

    private void loadPreferences() {
        String preferencesContent = readPreferences();
        if (preferencesContent != null) {
            entryList = gson.fromJson(preferencesContent, new ListType<>(Entry.class));
            if(entryList == null) {
                entryList = new ArrayList<>();
                savePreferences();
            }
        } else {
            entryList = new ArrayList<>();
        }
    }

    private void savePreferences(){
        writePreferences(gson.toJson(entryList));
    }

    private void writePreferences(String preferencesContent) {
        try {
            lock.lock();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(preferenceFile));
            bufferedWriter.write(preferencesContent);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    private String readPreferences() {
        String preferencesContent = null;
        try {
            lock.lock();
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(preferenceFile));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line).append("\n");
            }
            bufferedReader.close();
            preferencesContent = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
        return preferencesContent;
    }



    private static class Entry<Value> {
        @NonNull
        public String key;
        @NonNull
        public Value value;

        public Entry(@NonNull String key, @NonNull Value value) {
            this.key = key;
            this.value = value;
        }
    }

    public interface KeyChangeListener<Value> {
        void onChanged(@NonNull String key, @NonNull Value newValue);
        void onRemoved();
    }

    public static class ListType<Token> implements ParameterizedType {
        private final Class<?> clazz;

        public ListType(Class<Token> wrapper) {
            this.clazz = wrapper;
        }

        @NonNull
        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @NonNull
        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }
}
