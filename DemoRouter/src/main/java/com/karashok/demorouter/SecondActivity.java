package com.karashok.demorouter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.karashok.demorouterannotation.DAutoParams;
import com.karashok.demorouterannotation.Router;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Router(path = "/main/test")
public class SecondActivity extends AppCompatActivity {

    @DAutoParams
    String a;
    @DAutoParams
    int b;
    @DAutoParams
    short c;
    @DAutoParams
    long d;
    @DAutoParams
    float e;
    @DAutoParams
    double f;
    @DAutoParams
    byte g;
    @DAutoParams
    boolean h;
    @DAutoParams
    char i;


    @DAutoParams
    String[] aa;
    @DAutoParams
    int[] bb;
    @DAutoParams
    short[] cc;
    @DAutoParams
    long[] dd;
    @DAutoParams
    float[] ee;
    @DAutoParams
    double[] ff;
    @DAutoParams
    byte[] gg;
    @DAutoParams
    boolean[] hh;
    @DAutoParams
    char[] ii;

    @DAutoParams
    TestParcelable j;
    @DAutoParams
    TestParcelable[] jj;


    @DAutoParams
    List<TestParcelable> k1;
    @DAutoParams
    ArrayList<TestParcelable> k2;

    @DAutoParams
    List<String> k3;

    @DAutoParams
    List<Integer> k4;

    @DAutoParams(name = "hhhhhh")
    int test;

    @DAutoParams(name = "/main/service1")
    TestService testService1;
    @DAutoParams(name = "/main/service2")
    TestService testService2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        com.karashok.demoroutercore.DRouter.getInstance().inject(this);
        Log.e("DemoRouterSecond", toString());

        testService1.test();
        testService2.test();
    }

    @Override
    public void onBackPressed() {
        setResult(200);
        super.onBackPressed();
    }

    @Override
    public String toString() {
        return "DemoRouterSecond{" +
                "a='" + a + '\'' +
                ", b=" + b +
                ", c=" + c +
                ", d=" + d +
                ", e=" + e +
                ", f=" + f +
                ", g=" + g +
                ", h=" + h +
                ", i=" + i +
                ", aa=" + Arrays.toString(aa) +
                ", bb=" + Arrays.toString(bb) +
                ", cc=" + Arrays.toString(cc) +
                ", dd=" + Arrays.toString(dd) +
                ", ee=" + Arrays.toString(ee) +
                ", ff=" + Arrays.toString(ff) +
                ", gg=" + Arrays.toString(gg) +
                ", hh=" + Arrays.toString(hh) +
                ", ii=" + Arrays.toString(ii) +
                ", j=" + j +
                ", jj=" + Arrays.toString(jj) +
                ", k1=" + k1 +
                ", k2=" + k2 +
                ", k3=" + k3 +
                ", k4=" + k4 +
                '}';
    }
}