package de.robv.android.xposed;

import de.robv.android.xposed.callbacks.XC_MethodHook;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class XposedHelpers {
    public static Class<?> findClass(String className, ClassLoader classLoader) {
        try {
            return Class.forName(className, false, classLoader);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static XC_MethodHook.Unhook findAndHookMethod(String className, ClassLoader classLoader, 
            String methodName, Object... parameterTypesAndCallback) {
        return new XC_MethodHook.Unhook();
    }
    
    public static XC_MethodHook.Unhook findAndHookMethod(Class<?> clazz, String methodName, 
            Object... parameterTypesAndCallback) {
        return new XC_MethodHook.Unhook();
    }
    
    public static Object callMethod(Object obj, String methodName, Object... args) {
        return null;
    }
    
    public static Object getObjectField(Object obj, String fieldName) {
        return null;
    }
    
    public static void setObjectField(Object obj, String fieldName, Object value) {}
}
