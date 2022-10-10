package com.karashok.demookhttp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public class RequestBody {

    private final static String CONTENT_TYPE = "application/x-www-form-urlencoded";

    private final static String CHARSET = "utf-8";

    HashMap<String, String> encodeedBodys = new HashMap<>();

    public String contentType() {
        return CONTENT_TYPE;
    }

    public long contentLength() {
        return body().getBytes().length;
    }

    public String body() {
        StringBuffer sb = new StringBuffer();
        Set<Map.Entry<String, String>> entries = encodeedBodys.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            sb.append(entry.getKey())
                    .append("=")
                    .append(entry.getValue())
                    .append("&");
        }
        if (sb.length() != 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public RequestBody add(String name, String value) {
        try{
            encodeedBodys.put(URLEncoder.encode(name,CHARSET),URLEncoder.encode(value,CHARSET));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this;
    }

}
