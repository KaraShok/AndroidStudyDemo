package com.karashok.demoroutercore;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-28-2022
 */
public class NoRouteFoundException extends RuntimeException {

    public NoRouteFoundException(String detailMessage) {
        super(detailMessage);
    }
}
