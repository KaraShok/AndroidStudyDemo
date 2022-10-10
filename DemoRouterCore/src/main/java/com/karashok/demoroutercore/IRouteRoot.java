package com.karashok.demoroutercore;

import java.util.Map;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-28-2022
 */
public interface IRouteRoot {

    /**
     * @param routes input
     */
    void loadInto(Map<String, Class<? extends IRouteGroup>> routes);
}
