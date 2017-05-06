package jianqiang.com.plugin1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class MyPlugin1_MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.e("baobao", "stub21");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.e("baobao", "stub22");
    }
}
