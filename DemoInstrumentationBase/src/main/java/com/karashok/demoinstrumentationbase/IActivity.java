package com.karashok.demoinstrumentationbase;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-03-2022
 */
public interface IActivity {

    public void attach(Activity proxyActivity);

    public void onCreate(Bundle saveInstanceState);

    public void onStart();

    public void onResume();

    public void onPause();

    public void onStop();

    public void onDestroy();

    public void onSaveInstanceState(Bundle outState);

    public boolean onTouchEvent(MotionEvent event);

    public void onBackPressed();
}
