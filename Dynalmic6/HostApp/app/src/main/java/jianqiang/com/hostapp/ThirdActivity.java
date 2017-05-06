package jianqiang.com.hostapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;


public class ThirdActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);

        Log.i("demo", "resIds:" + R.layout.activity_third);

        Log.i("demo", "context1:" + this.getApplicationContext());
        Log.i("demo", "context1:" + this.getPackageResourcePath());

        Button button = (Button) findViewById(R.id.btn);
        button.setText("test inject");
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doSomething();
            }
        });
    }

    @SuppressLint("NewApi")
    private void doSomething() {
        loadResources();

        Class<?> clazz = null;
        try {
            clazz = classLoader.loadClass("jianqiang.com.plugin1.MyPlugin1_MainActivity");

            Class rClazz = classLoader.loadClass("jianqiang.com.plugin1.R$layout");
            Field field = rClazz.getField("myplugin1_main_activity");
            Integer ojb = (Integer) field.get(null);

            View view = LayoutInflater.from(this).inflate(ojb, null);

            Method method = clazz.getMethod("setLayoutView", View.class);
            method.invoke(null, view);

            Log.i("demo", "field:" + ojb);
        } catch (Throwable e) {
            Log.i("inject", "error:" + Log.getStackTraceString(e));
            e.printStackTrace();
        }

        loadApkClassLoader(classLoader);

        Intent intent = new Intent(ThirdActivity.this, clazz);
        startActivity(intent);
    }

    @SuppressLint("NewApi")
    private void loadApkClassLoader(DexClassLoader newLoader) {
        try {
            // 配置动态加载环境
            Object currentActivityThread = RefInvoke.invokeStaticMethod(
                    "android.app.ActivityThread", "currentActivityThread",
                    new Class[]{}, new Object[]{});         //获取主线程对象
            String packageName = this.getPackageName();     //当前apk的包名
            ArrayMap mPackages = (ArrayMap) RefInvoke.getFieldOjbect(
                    "android.app.ActivityThread", currentActivityThread,
                    "mPackages");
            WeakReference wr = (WeakReference) mPackages.get(packageName);
            RefInvoke.setFieldOjbect("android.app.LoadedApk", "mClassLoader",
                    wr.get(), newLoader);

            Log.i("demo", "classloader:" + newLoader);
        } catch (Exception e) {
            Log.i("demo", "load apk classloader error:" + Log.getStackTraceString(e));
        }
    }
}