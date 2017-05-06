package jianqiang.com.hostapp;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;
import java.lang.reflect.Method;

import dalvik.system.DexClassLoader;

public class BaseActivity extends Activity {

    private AssetManager mAssetManager;
    private Resources mResources;
    private Resources.Theme mTheme;
    private String dexpath = null;    //apk文件地址
    private File fileRelease = null;  //释放目录

    protected DexClassLoader classLoader = null;

    TextView tv;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(newBase);

        Utils.extractAssets(newBase, "plugin2.apk");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*初始化classloader
         * dexpath dex文件地址
         * fileRelease 文件释放地址
         *  父classLoader
         */
        File extractFile = this.getFileStreamPath("plugin2.apk");
        dexpath = extractFile.getPath();
        //dexpath = Environment.getExternalStorageDirectory() + File.separator + "plugin.apk";
        fileRelease = getDir("dex", 0);

        Log.d("DEMO", "dexpath:" + dexpath);
        Log.d("DEMO", "fileRelease.getAbsolutePath():" + fileRelease.getAbsolutePath());

        classLoader = new DexClassLoader(dexpath, fileRelease.getAbsolutePath(), null, getClassLoader());
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