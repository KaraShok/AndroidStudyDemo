package com.karashok.demoioc;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-23-2022
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.ANNOTATION_TYPE)
public @interface EventBase {

    /**
     * set 事件订阅
     * @return
     */
    String listenerSetter();

    /**
     * 事件监听的类型
     * @return
     */
    Class<?> listenerType();

    /**
     * 事件处理
     * @return
     */
    String callbackMethod();
}
