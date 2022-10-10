package com.karashok.demoglide.glide;

import android.content.Context;

import com.karashok.demoglide.glide.load.Engine;
import com.karashok.demoglide.glide.request.RequestOptions;

/**
 * @author KaraShokZ.
 * @des
 * @since 07-19-2022
 */
public class GlideContext {

    Context context;
    RequestOptions defaultRequestOptions;
    Engine engine;
    Registry registry;

    public GlideContext(Context context, RequestOptions defaultRequestOptions, Engine engine,
                        Registry registry) {
        this.context = context;
        this.defaultRequestOptions = defaultRequestOptions;
        this.engine = engine;
        this.registry = registry;
    }

    public Context getApplicationContext() {
        return context;
    }

    public RequestOptions getDefaultRequestOptions() {
        return defaultRequestOptions;
    }

    public Engine getEngine() {
        return engine;
    }

    public Registry getRegistry() {
        return registry;
    }
}
