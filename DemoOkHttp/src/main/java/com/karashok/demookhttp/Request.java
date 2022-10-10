package com.karashok.demookhttp;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public class Request {

    private HashMap<String, String> headers;

    private String method;

    private RequestBody requestBody;

    private HttpUrl httpUrl;

    public Request(Builder builder) {
        this.httpUrl = builder.httpUrl;
        this.method = builder.method;
        this.headers = builder.headers;
        this.requestBody = builder.requestBody;
    }

    public String method() {
        return method;
    }

    public HttpUrl httpUrl() {
        return httpUrl;
    }

    public RequestBody requestBody() {
        return requestBody;
    }

    public Map<String, String> headers() {
        return headers;
    }

    public static final class Builder {

        HttpUrl httpUrl;

        HashMap<String, String> headers = new HashMap<>();

        String method = "GET";

        RequestBody requestBody;

        public Builder url(String url) {
            try{
                this.httpUrl = new HttpUrl(url);
                return this;
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Failed Http Url",e);
            }
        }

        public Builder addHeader(String name, String value) {
            headers.put(name, value);
            return this;
        }

        public Builder removeHeader(String name) {
            headers.remove(name);
            return this;
        }

        public Builder get() {
            method = "GET";
            return this;
        }

        public Builder post(RequestBody requestBody) {
            this.requestBody = requestBody;
            method = "POST";
            return this;
        }

        public Request build() {
            if (httpUrl == null) {
                throw new IllegalStateException("httpUrl = null");
            }
            return new Request(this);
        }
    }
}
