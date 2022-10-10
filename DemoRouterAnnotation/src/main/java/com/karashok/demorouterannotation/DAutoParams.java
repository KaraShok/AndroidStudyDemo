package com.karashok.demorouterannotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-27-2022
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.FIELD)
public @interface DAutoParams {

    String name() default "";
}
