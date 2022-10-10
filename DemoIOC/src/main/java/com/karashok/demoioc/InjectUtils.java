package com.karashok.demoioc;

import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-23-2022
 */
public class InjectUtils {

    public static void inject(Object context) {
        injectLayout(context);
        injectView(context);
        injectClick(context);
    }

    private static void injectLayout(Object context) {
        Class<?> aClass = context.getClass();
        ContentView contentView = aClass.getAnnotation(ContentView.class);
        if (contentView != null) {
            int layoutId = contentView.value();
            try{
                Method method = aClass.getMethod("setContentView", int.class);
                method.invoke(context,layoutId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static void injectView(Object context) {
        Class<?> aClass = context.getClass();
        Field[] fields = aClass.getDeclaredFields();
        for (Field field : fields) {
            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                int viewId = viewInject.value();
                try{
                    Method method = aClass.getMethod("findViewById", int.class);
                    View view = (View) method.invoke(context,viewId);
                    field.setAccessible(true);
                    field.set(context,view);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void injectClick(Object context) {
        Class<?> aClass = context.getClass();
        Method[] methods = aClass.getDeclaredMethods();
        for (Method method : methods) {
            Annotation[] annotations = method.getAnnotations();
            if (annotations == null || annotations.length == 0) {
                continue;
            }
            for (Annotation annotation : annotations) {
                Class<? extends Annotation> annotationClass = annotation.annotationType();
                EventBase aAnnotation = annotationClass.getAnnotation(EventBase.class);
                if (aAnnotation == null) {
                    continue;
                }
                String listenerSetter = aAnnotation.listenerSetter();
                Class<?> listenerType = aAnnotation.listenerType();
                String callbackMethod = aAnnotation.callbackMethod();
                try{
                    Method valueMethod = annotationClass.getDeclaredMethod("value");
                    int[] viewIds = (int[])valueMethod.invoke(annotation);
                    Method findViewById = aClass.getMethod("findViewById",int.class);
                    EventInvocationHandler handler = new EventInvocationHandler(context,method);
                    Object proxy = Proxy.newProxyInstance(aClass.getClassLoader(),
                            new Class[]{listenerType}, handler);
                    for (int id : viewIds) {
                        View view = (View)findViewById.invoke(context,id);
                        if (view == null) {
                            continue;
                        }
                        Method setListener = view.getClass().getMethod(listenerSetter, listenerType);
                        setListener.invoke(view,proxy);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
