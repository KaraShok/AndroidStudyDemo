package com.karashok.demohermes;

import android.app.Activity;
import android.app.Application;
import android.app.IntentService;
import android.app.Service;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-15-2022
 */
public class TypeUtils {

    private static final HashSet<Class<?>> CONTEXT_CLASSES = new HashSet<Class<?>>() {
        {
            add(Context.class);
            add(Activity.class);
            add(AppCompatActivity.class);
            add(Application.class);
            add(FragmentActivity.class);
            add(IntentService.class);
            add(Service.class);
        }
    };

    public static String getClassId(Class<?> cls) {
        ClassId clsAnnotation = cls.getAnnotation(ClassId.class);
        if (clsAnnotation != null) {
            return clsAnnotation.value();
        } else {
            return cls.getName();
        }
    }

    public static String getMethodId(Method method) {
        StringBuilder strB = new StringBuilder(method.getName());
        strB.append("(");
        strB.append(getMethodParameters(method.getParameterTypes()));
        strB.append(")");
        return strB.toString();
    }

    public static String getMethodParameters(Class<?>[] clses) {
        StringBuilder strB = new StringBuilder();
        if (clses.length > 0) {
            strB.append(clses[0].getName());
            for (int i = 1, length = clses.length; i < length; i++) {
                strB.append(",")
                        .append(clses[i].getName());
            }
        }
        return strB.toString();
    }

    /**
     * 参数是否是原始类型 boolean, byte, char, short, int, long, float, and double void
     * @param cls1
     * @param cls2
     * @return
     */
    private static boolean primitiveMatch(Class<?> cls1, Class<?> cls2) {
        if (!cls1.isPrimitive() && !cls2.isPrimitive()) {
            return false;
        } else if (cls1 == cls2) {
            return true;
        } else if (cls1.isPrimitive()) {
            return primitiveMatch(cls2,cls1);
        } else if (cls1 == Boolean.class && cls2 == boolean.class) {
            return true;
        } else if (cls1 == Byte.class && cls2 == byte.class) {
            return true;
        } else if (cls1 == Character.class && cls2 == char.class) {
            return true;
        } else if (cls1 == Short.class && cls2 == short.class) {
            return true;
        } else if (cls1 == Integer.class && cls2 == int.class) {
            return true;
        } else if (cls1 == Long.class && cls2 == long.class) {
            return true;
        } else if (cls1 == Float.class && cls2 == float.class) {
            return true;
        } else if (cls1 == Double.class && cls2 == double.class) {
            return true;
        } else if (cls1 == Void.class && cls2 == void.class) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断 clses1 里的参数是否是 clses2 的类型或父类
     * @param clses1
     * @param clses2
     * @return
     */
    public static boolean classAssignable(Class<?>[] clses1, Class<?>[] clses2) {
        if (clses1.length != clses2.length) {
            return false;
        }
        for (int i = 0, length = clses2.length; i < length; i++) {
            if (clses2[i] == null) {
                continue;
            }
            if (primitiveMatch(clses1[1],clses2[i])) {
                continue;
            }
            if (!clses1[i].isAssignableFrom(clses2[i])) {
                return false;
            }
        }
        return true;
    }

    public static Method getMethod(Class<?> cls, String methodName, Class<?>[] parameterTypes) {
        try{
            Method result = cls.getMethod(methodName,parameterTypes);
            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public static Method getMethodForGettingInstance(Class<?> cls, String methodName, Class<?>[] parameterTypes) {
        Method[] methods = cls.getMethods();
        Method result = null;
        if (parameterTypes == null) {
            parameterTypes = new Class[0];
        }
        for (Method method : methods) {
            String name = method.getName();
            if ("getInstance".equals(name) && classAssignable(method.getParameterTypes(),parameterTypes)) {
                result = method;
                break;
            }
        }
        return result;
    }

    public static Constructor<?> getConstructor(Class<?> cls, Class<?>[] parameterTypes) {
        Constructor<?> result = null;
        Constructor<?>[] constructors = cls.getConstructors();
        for (Constructor<?> constructor : constructors) {
            if (classAssignable(constructor.getParameterTypes(),parameterTypes)) {
                if (result == null) {
                    result = constructor;
                }
            }
        }
        return result;
    }

    public static void validateClass(Class<?> cls) throws IllegalArgumentException {
        if (cls == null) {
            throw new IllegalArgumentException("Cls is null");
        }
        if (cls.isAnonymousClass()) {
            throw new IllegalArgumentException("Error occurs when registering class " + cls.getName() +
                    ". Anonymous class cannot be accessed from outside the process.");
        }
        if (cls.isLocalClass()) {
            throw new IllegalArgumentException(
                    "Error occurs when registering class " + cls.getName()
                            + ". Local class cannot be accessed from outside the process.");
        }
        if (Modifier.isAbstract(cls.getModifiers())) {
            throw new IllegalArgumentException(
                    "Error occurs when registering class " + cls.getName()
                            + ". Abstract class cannot be accessed from outside the process.");
        }
        if (cls.isPrimitive() || cls.isInterface() || Context.class.isAssignableFrom(cls)) {
            return;
        }
    }

    public static void validateServiceInterface(Class<?> cls) {
        if (cls == null) {
            throw new IllegalArgumentException("Class object is null.");
        }
        if (!cls.isInterface()) {
            throw new IllegalArgumentException("Only interfaces can be passed as the parameters.");
        }
    }

    public static boolean arrayContainsAnnotation(Annotation[] annotations, Class<? extends Annotation> annotationClass) {
        if (annotations == null || annotationClass == null) {
            return false;
        }
        for (Annotation annotation : annotations) {
            if (annotationClass.isInstance(annotation)) {
                return true;
            }
        }
        return false;
    }

    public static Class<?> getContextClass(Class<?> cls) {
        for (Class<?> tmp = cls; tmp != Object.class; tmp = tmp.getSuperclass()) {
            if (CONTEXT_CLASSES.contains(tmp)) {
                return tmp;
            }
        }
        return null;
    }
}
