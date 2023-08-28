package ru.liner.colorfy.utils;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * @author : "Line'R"
 * @mailto : serinity320@mail.com
 * @created : 05.11.2022, суббота
 **/
public class Reflect {
    public static Map<Class<?>, Class<?>> PRIMITIVE_TYPE_MAP;
    static {
        PRIMITIVE_TYPE_MAP = new HashMap<>();
        PRIMITIVE_TYPE_MAP.put(Float.class, float.class);
        PRIMITIVE_TYPE_MAP.put(Double.class, double.class);
        PRIMITIVE_TYPE_MAP.put(Integer.class, int.class);
        PRIMITIVE_TYPE_MAP.put(Long.class, long.class);
    }


    @Nullable
    public static Object getFieldSafety(@Nullable Object o, String filedName) {
        if(o == null)
            return null;
        Field field = findField(o.getClass(), filedName);
        if (field != null) {
            try {
                return field.get(o);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static boolean setFieldSafety(@NonNull Object o, String filedName, @Nullable Object value) {
        Field field = findField(o.getClass(), filedName);
        if (field != null) {
            try {
                field.set(o, value);
                return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return false;
    }




    @Nullable
    public static Field findField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            for(Field f : clazz.getDeclaredFields()){
                Log.d("TAGTAG", String.format("Declared field %s: %s %s", clazz.getSimpleName(), Modifier.toString(f.getModifiers()), f.getName()));
            }
            field.setAccessible(true);
            return field;
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null)
                return findField(superClass, fieldName);
            return null;
        }
    }

    @Nullable
    public static Object invokeSafety(@Nullable Object o, String methodName) {
        return invokeSafety(o, methodName, new Class[]{});
    }

    @Nullable
    public static Object invokeSafety(@Nullable Object o, String methodName, Class<?>[] params, Object... objects) {
        if(o == null)
            return null;
        Method method = findMethod(o.getClass(), methodName, params);
        if(method != null){
            try {
                return method.invoke(o, objects);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Nullable
    public static Method findMethod(Class<?> clazz, String methodName, Class<?>... params) {
        try {
            Method method = clazz.getDeclaredMethod(methodName, params);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null)
                return findMethod(superClass, methodName, params);
            return null;
        }
    }
}
