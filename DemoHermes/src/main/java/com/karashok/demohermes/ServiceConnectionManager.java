package com.karashok.demohermes;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Log;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-15-2022
 */
public class ServiceConnectionManager {

    private static final ServiceConnectionManager instance = new ServiceConnectionManager();

    private final ConcurrentHashMap<Class<? extends HermesService>,IHermesService> mHermesServiceMap = new ConcurrentHashMap<>();

    private final ConcurrentHashMap<Class<? extends HermesService>,HermesServiceConnection> mHermesConnectionMap = new ConcurrentHashMap<>();

    public static ServiceConnectionManager getInstance() {
        return instance;
    }

    public void bind(Context context, String packageName, Class<HermesService> hermesServiceClass) {
        HermesServiceConnection connection = new HermesServiceConnection(hermesServiceClass);
        mHermesConnectionMap.put(hermesServiceClass,connection);
        Intent intent;
        if (TextUtils.isEmpty(packageName)) {
            intent = new Intent(context,hermesServiceClass);
        } else {
            intent = new Intent();
            intent.setClassName(packageName,hermesServiceClass.getName());
        }
        context.bindService(intent,connection,Context.BIND_AUTO_CREATE);
    }

    public Response request(Class<HermesService> hermesServiceClass, Request request) {
        IHermesService service = mHermesServiceMap.get(hermesServiceClass);
        if (service != null) {
            try{
                return service.send(request);
            } catch (Exception e) {

            }
        }
        return null;
    }

    private class HermesServiceConnection implements ServiceConnection {

        private Class<? extends HermesService> mClass;

        public HermesServiceConnection(Class<? extends HermesService> mClass) {
            this.mClass = mClass;
        }

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d("DemoHermes", "HermesServiceConnection -> onServiceConnected: " + mClass.getName());
            IHermesService iHermesService = IHermesService.Stub.asInterface(iBinder);
            mHermesServiceMap.put(mClass,iHermesService);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mHermesServiceMap.remove(mClass);
        }
    }
}
