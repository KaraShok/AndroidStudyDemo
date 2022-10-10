package com.karashok.demoroutercore;

import com.karashok.demorouterannotation.DRouterMeta;

import java.util.Map;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-28-2022
 */
public interface IRouteGroup {

    void loadInto(Map<String, DRouterMeta> atlas);
}
