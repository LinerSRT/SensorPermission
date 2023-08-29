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
 * XPM is file-type custom preferences.
 * @noinspection unused
 */
public class XPM {

    public static final XPM INSTANCE = new XPM();
    private static final File preferenceFile = new File(Environment.getDataDirectory(), String.format("data/%s/preferences.dat", BuildConfig.APPLICATION_ID));
    private final Lock preferenceLock = new ReentrantLock();
    private final Gson gson;
    private List<Entry<?>> entryList;
    private final Map<String, KeyChangeListener<?>> listenerMap;


    /**
     * Default constructor
     */
    public XPM() {
        Debug.log("Preference file exists: %s", preferenceFile.exists());
        if (!preferenceFile.exists())
            try {
                if (!preferenceFile.createNewFile())
                    throw new RuntimeException("Cannot create preference file!");
            } catch (IOException e) {
                throw new RuntimeException("Cannot create preference file!");
            }
        gson = new Gson();
        listenerMap = new ConcurrentHashMap<>();
        loadPreferences();
    }


    /**
     * This method helps load saved value from preferences
     * @param key saved key
     * @param defaultValue default value
     * @return saved value if exists otherwise default value
     * @param <Value> any type
     * @noinspection unchecked
     */
    public <Value> Value get(@NonNull String key, @NonNull Value defaultValue) {
        Debug.log("Obtain %s with default value %s", key, defaultValue);
        loadPreferences();
        if (containKey(key))
            return (Value) entryList.get(indexOf(key)).value;
        return defaultValue;
    }

    /**
     * This method helps save any value at specific key.
     * If value exits and not changed, do not modifying preferences and notifying listeners
     * @param key for saving
     * @param value for saving
     * @param <Value> any type
     */
    public <Value> void set(@NonNull String key, @NonNull Value value) {
        Debug.log("Setting %s with value %s", key, value);
        loadPreferences();
        boolean shouldNotify = false;
        boolean shouldWrite = false;
        if (containKey(key)) {
            int index = indexOf(key);
            Entry<?> currentValue = entryList.get(index);
            Entry<?> newValue = new Entry<>(key, value);
            if (!currentValue.equals(newValue)) {
                entryList.set(index, newValue);
                shouldNotify = true;
                shouldWrite = true;
            } else {
                Debug.log("Saved value is same as %s, do not notify and write");
            }
        } else {
            entryList.add(new Entry<>(key, value));
            shouldNotify = true;
            shouldWrite = true;
        }
        if (shouldWrite)
            savePreferences();
        if (shouldNotify)
            notifyListeners(key, value);
    }

    /**
     * Remove specific value by key
     * @param key for preferences
     */
    public void remove(@NonNull String key) {
        loadPreferences();
        if (containKey(key)) {
            Debug.log("Removing %s from preferences", key);
            entryList.remove(indexOf(key));
            savePreferences();
            notifyListeners(key);
        }
    }

    /**
     * Remove all saved preferences
     */
    public void removeAll() {
        Debug.log("Removing all data from preferences");
        loadPreferences();
        for(Entry<?> entry : entryList)
            notifyListeners(entry.key);
        entryList.clear();
        savePreferences();
    }

    /**
     * Check if specific key is exists in preferences
     * @param key to check
     * @return true if exists
     */
    public boolean containKey(@NonNull String key) {
        for (Entry<?> entry : entryList)
            if (entry.key.equals(key))
                return true;
        return false;
    }

    /**
     * Find index for key, use with {@link XPM#containKey(String)}
     * @param key for search
     * @return index if exists, -1 if key not found
     */
    private int indexOf(@NonNull String key) {
        for (int i = 0; i < entryList.size(); i++)
            if (entryList.get(i).key.equals(key))
                return i;
        return -1;
    }

    /**
     * Notify all registered listeners about value changes
     * @param key preference key
     * @param value changed value
     * @param <Value> any type
     * @noinspection unchecked
     */
    private <Value> void notifyListeners(@NonNull String key, @NonNull Value value) {
        KeyChangeListener<Value> listener = (KeyChangeListener<Value>) listenerMap.get(key);
        if (listener != null)
            listener.onChanged(key, value);
    }

    /**
     * Notify all registered listeners about value removed
     * @param key preference key
     * @param <Value> any type
     * @noinspection unchecked
     */
    private <Value> void notifyListeners(@NonNull String key) {
        KeyChangeListener<Value> listener = (KeyChangeListener<Value>) listenerMap.get(key);
        if (listener != null)
            listener.onRemoved();
    }

    /**
     * Register new key change listener
     * @param key for listening
     * @param listener listener
     * @param <Value> any type
     */
    public <Value> void addKeyChangeListener(@NonNull String key, @NonNull KeyChangeListener<Value> listener) {
        listenerMap.put(key, listener);
    }

    /**
     * Unregister key change listener
     * @param key for listening
     */
    public void removeKeyChangeListener(@NonNull String key) {
        listenerMap.remove(key);
    }

    /**
     * Load preferences from file.
     * If file not readable or its first loading, save empty preference list for correct work
     */
    private void loadPreferences() {
        String preferencesContent = readPreferences();
        entryList = Consumer.of(readPreferences())
                .next((Consumer.FunctionB<String, List<Entry<?>>>) input -> gson.fromJson(input, new ListType<>(Entry.class)))
                .orElse(new ArrayList<>());
        if (entryList.isEmpty())
            savePreferences();
    }

    /**
     * Save preferences to file
     */
    private void savePreferences() {
        writePreferences(gson.toJson(entryList));
    }

    /**
     * Write preferences file with desired content
     * To avoid concurrent write using lock. Write allowed only if file not locked
     * @param preferencesContent file content
     */
    private void writePreferences(String preferencesContent) {
        try {
            preferenceLock.lock();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(preferenceFile));
            bufferedWriter.write(preferencesContent);
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            preferenceLock.unlock();
        }
    }

    /**
     * Read preference file content
     * Maybe remove redundant file lock?
     * @return string of preference file
     */
    private String readPreferences() {
        String preferencesContent = null;
        try {
            preferenceLock.lock();
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(new FileReader(preferenceFile));
            String line;
            while ((line = bufferedReader.readLine()) != null)
                stringBuilder.append(line).append("\n");
            bufferedReader.close();
            preferencesContent = stringBuilder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            preferenceLock.unlock();
        }
        return preferencesContent;
    }


    /**
     * Holder for preferences
     * @param <Value> any type
     */
    private static class Entry<Value> {
        @NonNull
        public String key;
        @NonNull
        public Value value;

        public Entry(@NonNull String key, @NonNull Value value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Entry<?> entry = (Entry<?>) o;

            if (!key.equals(entry.key)) return false;
            return value.equals(entry.value);
        }

        @Override
        public int hashCode() {
            int result = key.hashCode();
            result = 31 * result + value.hashCode();
            return result;
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
