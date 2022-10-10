package com.karashok.demoinstrumentationbase;

import android.content.Context;
import android.content.Intent;

/**
 * @author KaraShokZ.
 * @des
 * @since 06-03-2022
 */
public interface IBroadcast {

    public void attach(Context context);

    public void onReceive(Context context, Intent intent);
}
