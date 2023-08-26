package ru.liner.sensorpermission.utils;

import java.io.IOException;

public class Consumer<T> {

    private final T value;

    public Consumer( T value) {
        this.value = value;
    }

    public Consumer() {
        this.value = null;
    }

    public static <T> Consumer<T> of(T value) {
        return new Consumer<>(value);
    }

    public static <T> Consumer<T> empty() {
        return new Consumer<>();
    }

    public <S> Consumer<S> next(FunctionB<? super T, ? extends S> function) {
        try {
            return new Consumer<>(value == null ? null : function.apply(value));
        } catch (IOException e) {
            e.printStackTrace();
            return Consumer.empty();
        }
    }

    public void ifPresent(Function<? super T> consumer){
        if(value != null) {
            try {
                consumer.apply(value);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public T get() {
        return value;
    }

    public boolean asBoolean() {
        if (value == null)
            return false;
        if (value instanceof Boolean)
            return (Boolean) value;
        return false;
    }

    public int asInt() {
        if (value == null)
            return 0;
        if (value instanceof Integer)
            return (Integer) value;
        return 0;
    }

    public long asLong() {
        if (value == null)
            return 0;
        if (value instanceof Long)
            return (Long) value;
        return 0;
    }

    public double asDouble() {
        if (value == null)
            return 0;
        if (value instanceof Double)
            return (Double) value;
        return 0;
    }

    public float asFloat() {
        if (value == null)
            return 0;
        if (value instanceof Float)
            return (Float) value;
        return 0;
    }

    public T orElse(T other){
        return value == null ? other : value;
    }

    public boolean isPresent(){
        return value != null;
    }

    public interface Function<T>{
        void apply(T input) throws IOException;
    }

    public interface FunctionB<T, R>{
        R apply(T input) throws IOException;
    }
}