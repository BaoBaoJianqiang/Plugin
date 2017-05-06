package jianqiang.com.hostapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ResourceActivity extends BaseActivity {

    /**
     * 需要替换主题的控件
     * 这里就列举三个：TextView,ImageView,LinearLayout
     */
    private TextView textV;
    private ImageView imgV;
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resource);

        textV = (TextView) findViewById(R.id.text);
        imgV = (ImageView) findViewById(R.id.imageview);
        layout = (LinearLayout) findViewById(R.id.layout);

        Drawable drawable = getDrawable(R.drawable.ic_launcher);
        imgV.setBackground(drawable);

        findViewById(R.id.btn1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadResources();

                doSomething();

                //printResourceId();
                //setContent1();
                //printRField();
            }
        });

        findViewById(R.id.btn2).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                loadResources();

                getResFromR();
            }
        });
    }

    private void doSomething() {
        try {
            Class clazz = classLoader.loadClass("jianqiang.com.plugin1.UIUtil");
            Method method = clazz.getMethod("getTextString", Context.class);
            String str = (String) method.invoke(null, this);
            textV.setText(str);

            method = clazz.getMethod("getImageDrawable", Context.class);
            Drawable drawable = (Drawable) method.invoke(null, this);
            imgV.setBackground(drawable);

            method = clazz.getMethod("getLayout", Context.class);
            View view = (View) method.invoke(null, this);
            layout.addView(view);

        } catch (Exception e) {
            Log.e("DEMO", "msg:" + e.getMessage());
        }
    }

    /**
     * 另外的一种方式获取
     */
    private void getResFromR() {
        setTextStringId();
        setImgDrawableId();
        setLayoutId();
    }

    @SuppressLint("NewApi")
    private void setTextStringId() {
        try {
            Class clazz = classLoader.loadClass("jianqiang.com.plugin1.R$string");
            Field field = clazz.getField("myplugin1_app_name");
            int resId = (int) field.get(null);

            textV.setText(getResources().getString(resId));

        } catch (Exception e) {
            Log.i("Loader", "error:" + Log.getStackTraceString(e));
        }
    }

    @SuppressLint("NewApi")
    private void setImgDrawableId() {
        try {
            Class clazz = classLoader.loadClass("jianqiang.com.plugin1.R$drawable");
            Field field = clazz.getField("myplugin1_ic_launcher");
            int resId = (int) field.get(null);

            imgV.setBackground(getResources().getDrawable(resId));
        } catch (Exception e) {
            Log.i("Loader", "error:" + Log.getStackTraceString(e));
        }
    }

    @SuppressLint("NewApi")
    private void setLayoutId() {
        try {
            Class clazz = classLoader.loadClass("jianqiang.com.plugin1.R$layout");
            Field field = clazz.getField("myplugin1_main_activity");
            int resId = (int) field.get(null);

            View view = (View)LayoutInflater.from(this).inflate(resId, null);
            layout.addView(view);
        } catch (Exception e) {
            Log.i("Loader", "error:" + Log.getStackTraceString(e));
        }
    }
}
