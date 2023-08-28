package ru.liner.sensorpermission.xposed;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import ru.liner.sensorpermission.utils.Consumer;

/**
 * Author: Line'R
 * E-mail: serinity320@mail.com
 * Github: https://github.com/LinerSRT
 * Date: 28.08.2023, 0:06
 *
 * @noinspection unused, UnusedReturnValue
 */
public class MethodHooker {
    @NonNull
    private final ClassLoader classLoader;
    @Nullable
    private String className;

    @Nullable
    private Class<?> aClass;
    @Nullable
    private String methodName;
    @Nullable
    private Object[] methodArguments;
    @Nullable
    private Function beforeFunction;
    @Nullable
    private Function afterFunction;

    private MethodHooker(@NonNull ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public MethodHooker className(@NonNull String className) {
        this.className = className;
        return this;
    }

    public MethodHooker className(@NonNull Class<?> aClass) {
        this.className = aClass.getSimpleName();
        this.aClass = aClass;
        return this;
    }

    public MethodHooker methodName(@NonNull String methodName) {
        this.methodName = methodName;
        return this;
    }

    public MethodHooker methodArguments(@NonNull Object... methodArguments) {
        this.methodArguments = methodArguments;
        return this;
    }

    public MethodHooker method(@NonNull String methodName, @NonNull Object... methodArguments) {
        methodName(methodName);
        methodArguments(methodArguments);
        return this;
    }

    public MethodHooker beforeFunction(@Nullable Function beforeFunction) {
        this.beforeFunction = beforeFunction;
        return this;
    }

    public MethodHooker afterFunction(@Nullable Function afterFunction) {
        this.afterFunction = afterFunction;
        return this;
    }


    public static void log(String message, Object... objects) {
        XposedBridge.log(String.format("{%s} --> %s", MethodHooker.class.getSimpleName(), String.format(message, objects)));
    }

    private Method findMethod() {
        log("Searching %s in %s", methodName, className);
        Class<?> clazz = XposedHelpers.findClassIfExists(className, classLoader);
        if (clazz == null && aClass != null)
            clazz = aClass;
        if (clazz != null) {
            for (Method method : clazz.getDeclaredMethods())
                if (method.getName().equals(methodName)) {
                    method.setAccessible(true);
                    log("Method %s found, parameters: %s", methodName, Arrays.toString(method.getParameterTypes()));
                    return method;
                }
            log("Failed to find %s in %s", methodName, className);
        } else {
            log("Failed to find class: %s", className);
        }
        return null;
    }

    public void hook() {
        if (className == null || methodName == null)
            return;
        Method method = Consumer.of(XposedHelpers.findMethodExactIfExists(className, classLoader, methodName, methodArguments == null ? new Object[]{Object.class} : methodArguments)).orElse(findMethod());
        if (method == null)
            log("Failed to find method [%s] not found in %s", methodName, className);
        Consumer.of(method)
                .ifPresent(input -> XposedBridge.hookMethod(input, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) {
                        if (beforeFunction != null)
                            beforeFunction.apply(new HookedMethod(param));
                    }

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) {
                        if (afterFunction != null)
                            afterFunction.apply(new HookedMethod(param));
                    }
                }));
    }

    public interface Function {
        void apply(HookedMethod hookedMethod);
    }

    /**
     * @noinspection unused
     */
    public static class HookedMethod {
        @NonNull
        private final Member method;
        @NonNull
        private final Object object;
        private final Object[] arguments;
        private final XC_MethodHook.MethodHookParam param;

        public HookedMethod(XC_MethodHook.MethodHookParam param) {
            this.param = param;
            this.method = param.method;
            this.object = param.thisObject;
            this.arguments = param.args;
        }

        @NonNull
        public Object object() {
            return object;
        }

        @NonNull
        public Member method() {
            return method;
        }


        public <V> V fieldValue(@NonNull String fieldName) {
            return fieldValue(object(), fieldName);
        }

        /**
         * @noinspection unchecked
         */
        @Nullable
        public <V> V fieldValue(@NonNull Object object, @NonNull String fieldName) {
            Optional<Field> searchedField = Arrays.stream(object.getClass().getDeclaredFields())
                    .filter(field -> field.getName().equals(fieldName))
                    .findAny();
            if (searchedField.isPresent()) {
                try {
                    Field field = searchedField.get();
                    field.setAccessible(true);
                    return (V) field.get(object);
                } catch (IllegalAccessException e) {
                    log("An error occurred while retrieving value of %s", fieldName);
                    return null;
                }
            } else {
                log("Can't find field %s in %s", fieldName, object.getClass().getName());
                return null;
            }
        }

        public void fieldValue(@NonNull String fieldName, @Nullable Object object) {
            Optional<Field> searchedField = Arrays.stream(object().getClass().getDeclaredFields())
                    .filter(field -> field.getName().equals(fieldName))
                    .findAny();
            if (searchedField.isPresent()) {
                try {
                    Field field = searchedField.get();
                    field.setAccessible(true);
                    field.set(object(), object);
                    log("Successfully set %s to %s", object, fieldName);
                } catch (IllegalAccessException e) {
                    log("An error occurred while retrieving value of %s", fieldName);
                }
            } else {
                log("Can't find field %s in %s", fieldName, object().getClass().getName());
            }
        }

        /**
         * @noinspection unchecked
         */
        public <V> V call(@NonNull Object object, @NonNull String methodName) {
            return (V) XposedHelpers.callMethod(object, methodName);
        }

        /**
         * @noinspection unchecked
         */
        public <V> V call(@NonNull String methodName) {
            return (V) XposedHelpers.callMethod(object(), methodName);
        }

        /**
         * @noinspection unchecked
         */
        public <ArgumentType> ArgumentType argument(int index) {
            if (index >= 0 && index < arguments.length)
                return (ArgumentType) arguments[index];
            return (ArgumentType) this;
        }

        public void argument(int index, Object argumentValue) {
            if (index >= 0 && index < arguments.length)
                param.args[index] = argumentValue;
        }

        public int lastArgumentIndex() {
            return arguments.length - 1;
        }

        public <MethodResult> void result(MethodResult methodResult) {
            param.setResult(methodResult);
        }

        public void log(String message, Object... objects) {
            XposedBridge.log(String.format("{Hooker} --> {%s} --> %s", method.getName(), String.format(message, objects)));
        }
    }

    public static MethodHooker of(@NonNull ClassLoader classLoader) {
        return new MethodHooker(classLoader);
    }

    public static Class<?> classFrom(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return Object.class;
        }
    }
}
