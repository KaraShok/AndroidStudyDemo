package com.karashok.demoioc;

import android.view.View;

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
@Target(ElementType.METHOD)
@EventBase(listenerSetter = "setOnClickListener",
            listenerType = View.OnClickListener.class,
            callbackMethod = "onClick"
)
public @interface OnClick {
    int[] value() default -1;
}
