package com.karashok.demookhttp;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-21-2022
 */
public class Call {

    private Request request;

    private HttpClient httpClient;

    /**
     * 是否执行过
     */
    private boolean executed;

    private boolean canceled;

    public Call(Request request, HttpClient httpClient) {
        this.request = request;
        this.httpClient = httpClient;
    }

    public Call enqueue(Callback callback) {
        synchronized (this) {
            if (executed) {
                throw new IllegalStateException("Already Execute");
            }
            executed = true;
            httpClient.dispatcher().enqueue(new AsyncCall(callback));
            return this;
        }
    }

    public Request request() {
        return request;
    }

    public void cancel() {
        canceled = true;
    }

    public boolean isCanceled() {
        return canceled;
    }

    public HttpClient httpClient() {
        return httpClient;
    }

    private Response getResponse() throws IOException {
        ArrayList<Interceptor> interceptors = new ArrayList<>();
        interceptors.addAll(httpClient.interceptors());
        interceptors.add(new RetryInterceptor());
        interceptors.add(new HeadersInterceptor());
        interceptors.add(new ConnectionInterceptor());
        interceptors.add(new CallServiceInterceptor());

        InterceptorChain chain = new InterceptorChain(interceptors,0,this, null);
        return chain.proceed();
    }

    final class AsyncCall implements Runnable {

        private final Callback callback;

        public AsyncCall(Callback callback) {
            this.callback = callback;
        }

        @Override
        public void run() {
            boolean signalledCallback = false;
            try{
                Response response = getResponse();
                signalledCallback = true;
                if (canceled) {
                    callback.onFailure(Call.this,new IOException("Canceled"));
                } else {
                    callback.onResponse(Call.this,response);
                }
            } catch (IOException e) {
                if (!signalledCallback) {
                    callback.onFailure(Call.this,e);
                }
            } finally{
                httpClient.dispatcher().finished(this);
            }
        }

        public String host() {
            return request.httpUrl().getHost();
        }

    }
}
