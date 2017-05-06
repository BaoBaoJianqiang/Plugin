package jianqiang.com.plugin1;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MyPlugin1_MainActivity extends Activity {

    private static View parentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.e("baobao", "stub1");
        Log.e("baobao", R.layout.myplugin1_main_activity + "");

        super.onCreate(savedInstanceState);
        if(parentView == null){
            setContentView(R.layout.myplugin1_main_activity);
        }else{
            setContentView(parentView);
        }

        findViewById(R.id.myplugin1_btn).setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                Toast.makeText(getApplicationContext(), "I came from 插件~~", Toast.LENGTH_LONG).show();
            }});

    }

    public static void setLayoutView(View view){
        parentView = view;
    }
}
