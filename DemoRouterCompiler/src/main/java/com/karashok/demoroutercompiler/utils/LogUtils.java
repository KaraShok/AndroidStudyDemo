package com.karashok.demoroutercompiler.utils;

import javax.annotation.processing.Messager;
import javax.tools.Diagnostic;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-27-2022
 */
public class LogUtils {

    private static LogUtils logUtils;

    public static final LogUtils newLog(Messager messager) {
        logUtils = new LogUtils(messager);
       return logUtils;
    }

    public static final LogUtils getInstance() {
        return logUtils;
    }

    private Messager messager;

    private LogUtils(Messager messager) {
        this.messager = messager;
    }

    public void i(String msg) {
        messager.printMessage(Diagnostic.Kind.NOTE,msg);
    }
}
