package ru.liner.sensorpermission.utils;

import androidx.annotation.Nullable;

import java.util.List;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 24.08.2023, четверг
 **/
public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() {
        return key;
    }

    public void setKey(K key) {
        this.key = key;
    }

    public V getValue() {
        return value;
    }

    public void setValue(V value) {
        this.value = value;
    }

    @Nullable
    public static <K, V> Pair<K, V> find(List<Pair<K, V>> pairList, K key) {
        for (Pair<K, V> pair : pairList)
            if (pair.getKey().equals(key))
                return pair;
        return null;
    }


    @Override
    public String toString() {
        return "Pair{" +
                "key=" + key.toString() +
                ", value=" + value.toString() +
                '}';
    }
}
