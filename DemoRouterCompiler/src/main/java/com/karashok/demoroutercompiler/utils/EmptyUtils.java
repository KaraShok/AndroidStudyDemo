package com.karashok.demoroutercompiler.utils;

import java.util.Collection;
import java.util.Map;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-27-2022
 */
public class EmptyUtils {

    public static boolean isEmpty(CharSequence chars) {
        return chars == null || chars.length() == 0;
    }

    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    public static boolean isEmpty(Map<?,?> map) {
        return map == null || map.isEmpty();
    }
}
