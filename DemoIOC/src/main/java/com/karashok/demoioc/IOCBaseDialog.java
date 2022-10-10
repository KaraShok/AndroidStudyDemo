package com.karashok.demoioc;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;


/**
 * @author KaraShokZ.
 * @des
 * @since 05-23-2022
 */
public class IOCBaseDialog extends Dialog {

    public IOCBaseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectUtils.inject(this);
    }
}
