package com.mindpin.android.kcroundprogressbar.common;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import com.actionbarsherlock.app.ActionBar;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by dd on 14-7-16.
 */
public class DisplayModule {
    private static final int FROYO_TITLEBAR_HEIGHT = 36;
    public static int get_statusbar_height(Context context) {
        Class c;
        try {
            c =
                    Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
            int y = context.getResources().getDimensionPixelSize(x);
            return y;
        } catch (Exception e) {
        }
        return 0;
    }

    public static int get_actionbar_height(Context context) {
        TypedValue tv = new TypedValue();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true))
                return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
        } else {
            if (context.getTheme().resolveAttribute(com.actionbarsherlock.R.attr.actionBarSize, tv, true)) {
                return TypedValue.complexToDimensionPixelSize(tv.data, context.getResources().getDisplayMetrics());
            }
            else{
                return FROYO_TITLEBAR_HEIGHT;
            }
        }
        return 0;
    }

    public static int get_display_height(Context context) {
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    public static int dp_to_px(Context context, int dp) {
        return (int) (context.getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    public static int px_to_dp(Context context, int px) {
        return (int) (px / context.getResources().getDisplayMetrics().density + 0.5f);
    }


    public static void display_default_titlebar(Activity activity, boolean isShow) {
        if (isShow) {
            if (activity.findViewById(android.R.id.title) != null)
                ((View) activity.findViewById(android.R.id.title).getParent()).setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                android.app.ActionBar actionbar = activity.getActionBar();
                actionbar.show();
            }
        } else {
            if (activity.findViewById(android.R.id.title) != null)
                ((View) activity.findViewById(android.R.id.title).getParent()).setVisibility(View.GONE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                android.app.ActionBar actionbar = activity.getActionBar();
                actionbar.hide();
            }
        }
    }

    public static void display_sherlock(Activity activity, boolean isShow) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        Class clazz = activity.getClass();
        Method method = clazz.getMethod("getSupportActionBar");
        ActionBar actionBar = (ActionBar) method.invoke(activity);
        if (isShow)
            actionBar.show();
        else
            actionBar.hide();
    }
}
