package com.karashok.demookhttp;

import java.util.HashMap;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public class Response {

    int code;

    int contentLength = -1;

    HashMap<String, String> headers = new HashMap<>();

    String body;

    boolean isKeepAlive;

    public Response(int code, int contentLength, HashMap<String, String> headers, String body,
                    boolean isKeepAlive) {
        this.code = code;
        this.contentLength = contentLength;
        this.headers = headers;
        this.body = body;
        this.isKeepAlive = isKeepAlive;
    }

    public int getCode() {
        return code;
    }

    public int getContentLength() {
        return contentLength;
    }

    public HashMap<String, String> getHeaders() {
        return headers;
    }

    public String getBody() {
        return body;
    }

    public boolean isKeepAlive() {
        return isKeepAlive;
    }
}
