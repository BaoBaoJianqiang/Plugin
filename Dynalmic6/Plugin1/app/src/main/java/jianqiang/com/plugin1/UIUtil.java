package jianqiang.com.plugin1;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

public class UIUtil {
    public static String getTextString(Context ctx){
        Log.i("Loader", ctx.toString());

        return ctx.getResources().getString(R.string.myplugin1_app_name);
    }

    public static Drawable getImageDrawable(Context ctx){
        return ctx.getResources().getDrawable(R.drawable.myplugin1_ic_launcher);
    }

    public static View getLayout(Context ctx){
        return LayoutInflater.from(ctx).inflate(R.layout.myplugin1_main_activity, null);
    }

    public static int getTextStringId(){
        return R.string.myplugin1_app_name;
    }

    public static int getImageDrawableId(){
        return R.drawable.myplugin1_ic_launcher;
    }

    public static int getLayoutId(){
        return R.layout.myplugin1_main_activity;
    }
}
