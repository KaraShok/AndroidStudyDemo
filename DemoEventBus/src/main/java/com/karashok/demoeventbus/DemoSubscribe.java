package com.karashok.demoeventbus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author KaraShokZ.
 * @des 订阅事件的注解
 * @since 05-08-2022
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DemoSubscribe {

    DemoThreadMode threadMode() default DemoThreadMode.POSTING;
}
