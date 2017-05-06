package jianqiang.com.hostapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jianqiang.mypluginlibrary.IBean;
import com.example.jianqiang.mypluginlibrary.IDynamic;
import com.example.jianqiang.mypluginlibrary.YKCallBack;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {

    private AssetManager mAssetManager;
    private Resources mResources;
    private Resources.Theme mTheme;
    private String dexpath = null;    //apk文件地址
    private File fileRelease = null;  //释放目录
    private DexClassLoader classLoader = null;

    TextView tv;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);
        try {
            Utils.extractAssets(newBase, "plugin2.apk");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        File extractFile = this.getFileStreamPath("plugin2.apk");
        dexpath = extractFile.getPath();

        fileRelease = getDir("dex", 0); //0 表示Context.MODE_PRIVATE

        Log.d("DEMO", "dexpath:" + dexpath);
        Log.d("DEMO", "fileRelease.getAbsolutePath():" +
                fileRelease.getAbsolutePath());

        classLoader = new DexClassLoader(dexpath,
                fileRelease.getAbsolutePath(), null, getClassLoader());

        Button btn_1 = (Button) findViewById(R.id.btn_1);
        Button btn_2 = (Button) findViewById(R.id.btn_2);
        Button btn_3 = (Button) findViewById(R.id.btn_3);
        Button btn_4 = (Button) findViewById(R.id.btn_4);
        Button btn_5 = (Button) findViewById(R.id.btn_5);
        Button btn_6 = (Button) findViewById(R.id.btn_6);

        tv = (TextView)findViewById(R.id.tv);

        //普通调用，反射的方式
        btn_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Class mLoadClassBean;
                try {
                    mLoadClassBean = classLoader.loadClass("jianqiang.com.plugin1.Bean");
                    Object beanObject = mLoadClassBean.newInstance();

                    Log.d("DEMO", "ClassLoader:" + mLoadClassBean.getClassLoader());
                    Log.d("DEMO", "ClassLoader:" + mLoadClassBean.getClassLoader().getParent());

                    Method getNameMethod = mLoadClassBean.getMethod("getName");
                    getNameMethod.setAccessible(true);
                    String name = (String) getNameMethod.invoke(beanObject);

//                    tv.setText(name);
                    Toast.makeText(getApplicationContext(), name, Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    Log.e("DEMO", "msg:" + e.getMessage());
                }
            }
        });

        //带参数调用
        btn_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Class mLoadClassBean;
                try {
                    mLoadClassBean = classLoader.loadClass("jianqiang.com.plugin1.Bean");
                    Object beanObject = mLoadClassBean.newInstance();

                    Log.d("DEMO", beanObject.getClass().getClassLoader() + "");
                    Log.d("DEMO", IBean.class.getClassLoader() + "");
                    Log.d("DEMO", ClassLoader.getSystemClassLoader() + "");

                    IBean bean = (IBean) beanObject;
                    bean.setName("Hello");
                    tv.setText(bean.getName());
                } catch (Exception e) {
                    Log.e("DEMO", "msg:" + e.getMessage());
                }

            }
        });

        //带回调函数的调用
        btn_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Class mLoadClassDynamic;
                try {
                    mLoadClassDynamic = classLoader.loadClass("jianqiang.com.plugin1.Dynamic");
                    Object dynamicObject = mLoadClassDynamic.newInstance();

                    IDynamic dynamic = (IDynamic) dynamicObject;

                    YKCallBack callback = new YKCallBack() {
                        public void callback(IBean arg0) {
                            tv.setText(arg0.getName());
                        }

                        ;
                    };
                    dynamic.methodWithCallBack(callback);
                } catch (Exception e) {
                    Log.e("DEMO", "msg:" + e.getMessage());
                }

            }
        });

        //带资源文件的调用
        btn_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadResources();
                Class mLoadClassDynamic;
                try {
                    mLoadClassDynamic = classLoader.loadClass("jianqiang.com.plugin1.Dynamic");
                    Object dynamicObject = mLoadClassDynamic.newInstance();

                    IDynamic dynamic = (IDynamic) dynamicObject;

                    //dynamic.showPluginWindow(MainActivity.this);
                    dynamic.showPluginWindow(getApplicationContext());
                } catch (Exception e) {
                    Log.e("DEMO", "msg:" + e.getMessage());
                }
            }
        });

        //页面跳转
        btn_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadResources();
                Class mLoadClassDynamic;
                try {
                    mLoadClassDynamic = classLoader.loadClass("jianqiang.com.plugin1.Dynamic");
                    Object dynamicObject = mLoadClassDynamic.newInstance();

                    Class newActivity = classLoader.loadClass("jianqiang.com.plugin1.MyPlugin1_MainActivity");
                    IDynamic dynamic = (IDynamic) dynamicObject;
                    dynamic.startPluginActivity(MainActivity.this, newActivity);
                } catch (Exception e) {
                    Log.e("DEMO", "msg:" + e.getMessage());
                }
            }
        });

        //带资源文件的调用
        btn_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                loadResources();
                Class mLoadClassDynamic;
                try {
                    mLoadClassDynamic = classLoader.loadClass("jianqiang.com.plugin1.Dynamic");
                    Object dynamicObject = mLoadClassDynamic.newInstance();

                    IDynamic dynamic = (IDynamic) dynamicObject;
                    String content = dynamic.getStringForResId(MainActivity.this);
                    tv.setText(content);
                    Toast.makeText(getApplicationContext(), content + "", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    Log.e("DEMO", "msg:" + e.getMessage());
                }
            }
        });
    }

    protected void loadResources() {
        try {
            AssetManager assetManager = AssetManager.class.newInstance();
            Method addAssetPath = assetManager.getClass().getMethod("addAssetPath", String.class);
            addAssetPath.invoke(assetManager, dexpath);
            mAssetManager = assetManager;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Resources superRes = super.getResources();
        superRes.getDisplayMetrics();
        superRes.getConfiguration();
        mResources = new Resources(mAssetManager, superRes.getDisplayMetrics(), superRes.getConfiguration());
        mTheme = mResources.newTheme();
        mTheme.setTo(super.getTheme());
    }

    @Override
    public AssetManager getAssets() {
        return mAssetManager == null ? super.getAssets() : mAssetManager;
    }

    @Override
    public Resources getResources() {
        return mResources == null ? super.getResources() : mResources;
    }

    @Override
    public Resources.Theme getTheme() {
        return mTheme == null ? super.getTheme() : mTheme;
    }
}