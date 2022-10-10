package com.karashok.demohermes;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

/**
 * @author KaraShokZ.
 * @des
 * @since 05-15-2022
 */
public class HermesService extends Service {

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

  private IHermesService.Stub mBinder =
      new IHermesService.Stub() {
        @Override
        public Response send(Request request) throws RemoteException {
            Log.d("DemoHermes:Service", "send: " + request.type);
            ResponceMake responceMake = null;
          switch (request.type) {
            case Request.TYPE_GET:
              responceMake = new InstanceResponceMake();
              break;
            case Request.TYPE_NEW:
              responceMake = new ObjectResponceMake();
              break;
          }
          return responceMake.makeResponce(request);
        }
      };
}
