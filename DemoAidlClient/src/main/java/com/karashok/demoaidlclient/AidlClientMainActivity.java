package com.karashok.demoaidlclient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.karashok.demoaidlserver.IDemoAidlInterface;
import com.karashok.demoaidlserver.Person;

import java.util.List;
import java.util.function.Consumer;

public class AidlClientMainActivity extends AppCompatActivity {

    private IDemoAidlInterface demoAidlInterface;
    private int personNum = 1;
    private int personAge = 20;
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aidl_client_main);
        bindService();
        findViewById(R.id.add_person)
                .setOnClickListener(clickListener);
        findViewById(R.id.get_person)
                .setOnClickListener(clickListener);
        tv = findViewById(R.id.data_tv);
    }

    private void bindService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName("com.karashok.demoaidlserver","com.karashok.demoaidlserver.DemoServerService"));
        bindService(intent,conn, Context.BIND_AUTO_CREATE);
    }

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d("AidlClientMain", "Connect Service");
            demoAidlInterface = IDemoAidlInterface.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

  private View.OnClickListener clickListener =
      new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int vId = v.getId();
          if (vId == R.id.add_person) {
            try {
              demoAidlInterface.addPerson(new Person("Demo-" + personNum, personNum + personAge));
              personNum++;
            } catch (Exception e) {
                Log.d("AidlClientMainAddPerson", e.toString());
            }
          } else if (vId == R.id.get_person) {
            try {
              List<Person> personList = demoAidlInterface.getPersonList();
              StringBuilder sb = new StringBuilder();
              personList.forEach(
                  new Consumer<Person>() {
                    @Override
                    public void accept(Person person) {
                      sb.append(person.toString());
                      sb.append("\n");
                    }
                  });
              tv.setText(sb);
            } catch (Exception e) {
              Log.d("AidlClientMainGetPerson", e.toString());
            }
          }
        }
      };
}