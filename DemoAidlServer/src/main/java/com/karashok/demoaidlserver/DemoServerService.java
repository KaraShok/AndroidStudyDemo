package com.karashok.demoaidlserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author KaraShokZ
 * @since 05-03-2022
 */
public class DemoServerService extends Service {

    private static final List<Person> personList = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
    Log.d("DemoServerService", "onBind: ");
        return iBinder;
    }


    private IBinder iBinder = new IDemoAidlInterface.Stub() {
        @Override
        public void addPerson(Person person) throws RemoteException {
            personList.add(person);
        }

        @Override
        public List<Person> getPersonList() throws RemoteException {
            return personList;
        }
    };
}
