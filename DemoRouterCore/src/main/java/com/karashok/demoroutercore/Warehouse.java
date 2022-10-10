package com.karashok.demoroutercore;

import com.karashok.demorouterannotation.DRouterMeta;

import java.util.HashMap;
import java.util.Map;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-28-2022
 */
public class Warehouse {

    // root 映射表 保存分组信息
    static Map<String, Class<? extends IRouteGroup>> groupsIndex = new HashMap<>();

    // group 映射表 保存组中的所有数据
    static Map<String, DRouterMeta> routes = new HashMap<>();

    // group 映射表 保存组中的所有数据
    static Map<Class, IService> services = new HashMap<>();
}
