package com.karashok.demookhttp;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public final class HttpClient {

    private Dispatcher dispatcher;

    private ConnectionPool connectionPool;

    private int retrys;

    private List<Interceptor> interceptors;

    public HttpClient() {
        this(new Builder());
    }

    public HttpClient(Builder builder) {
        this.dispatcher = builder.dispatcher;
        this.connectionPool = builder.connectionPool;
        this.retrys = builder.retrys;
        this.interceptors = builder.interceptors;
    }

    public Call newCall(Request request) {
        return new Call(request,this);
    }

    public int retrys() {
        return retrys;
    }

    public Dispatcher dispatcher() {
        return dispatcher;
    }

    public ConnectionPool connectionPool() {
        return connectionPool;
    }

    public List<Interceptor> interceptors() {
        return interceptors;
    }

    public static final class Builder {

        Dispatcher dispatcher = new Dispatcher();

        ConnectionPool connectionPool = new ConnectionPool();

        int retrys = 3;

        List<Interceptor> interceptors = new ArrayList<>();

        public Builder retrys(int retrys) {
            this.retrys = retrys;
            return this;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            interceptors.add(interceptor);
            return this;
        }
    }
}
