package com.karashok.demoioc;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


/**
 * @author KaraShokZ.
 * @des
 * @since 05-23-2022
 */
@ContentView(R.layout.dialog_news)
public class NewsDialog extends IOCBaseDialog {

    @ViewInject(R.id.dialogBtn)
    Button btn;

    public NewsDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showToast("dialogBtn "+ btn);
    }


    @OnClick(R.id.dialogBtn)
    public void click(View view) {
        showToast("dialog点击啦");
    }

    @Override
    public void show() {
        super.show();
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.gravity = Gravity.BOTTOM;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().getDecorView().setPadding(0, 0, 0, 0);
        getWindow().setAttributes(layoutParams);
    }

    private void showToast(String content) {
        Toast.makeText(getContext(), content, Toast.LENGTH_SHORT).show();
    }
}
