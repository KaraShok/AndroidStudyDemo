package com.karashok.demohermes;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-16-2022
 */
public class ResponseBean {

    public Object data;//UserManager

    public ResponseBean(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponceBean{" +
                "data=" + data.toString() +
                '}';
    }
}
