package inc.maro.makeagift2.Helpers;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.TypedValue;

import java.lang.ref.WeakReference;

/**
 * Created by hIT on 24/8/2017.
 */

public class ScreenSizeHelper {
    private static int actionBarHeight;
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    private static TypedValue tv = new TypedValue();
    private static ScreenSizeHelper instance = null;
    private static WeakReference<Activity> activity = null;

    private int widthView = -1;
    private int heightView = -1;

    public static void setActivity(Activity act){
        activity = new WeakReference<Activity>(act);
        instance = new ScreenSizeHelper();
    }

    public static ScreenSizeHelper getInstance(){
        return instance;
    }

    public int getScreenWidth(){
        return screenWidth - widthView;
    }

    public int getScreenHeight(){
        return screenHeight - getActionBarHeight() - heightView;
    }

    public float getYPercentage(float y) {
        return getScreenHeight() * y;
    }

    public float getXPercentage(float x) {
        return getScreenWidth() * x;
    }

    private ScreenSizeHelper(){
        setValues();
    }

    private int getActionBarHeight(){
        return actionBarHeight;
    }

    private static void setValues(){
        actionBarHeight = 0;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.get().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        if (actionBarHeight == 0 && activity.get().getTheme().resolveAttribute(android.R.attr.actionBarSize, tv,true)){
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, activity.get().getResources().getDisplayMetrics());
        }
    }

    public void setWidthView(int widthView) {
        this.widthView = widthView;
    }

    public void setHeightView(int heightView) {
        this.heightView = heightView;
    }
}