package inc.maro.makeagift2.Helpers;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.util.TypedValue;

/**
 * Created by hIT on 24/8/2017.
 */

public class ScreenSizeHelper {
    private static int actionBarHeight;
    private static boolean isSetValuesHeight = false;
    private static boolean isSetValuesScreen = false;
    private static int screenWidth = 0;
    private static int screenHeight = 0;
    private static TypedValue tv = new TypedValue();

    private static ScreenSizeHelper instance = new ScreenSizeHelper();
    private static Activity activity = null;

    public static ScreenSizeHelper getInstance()
    {
        return instance;
    }

    private ScreenSizeHelper()
    {
        if (activity != null){
            setValues();
        }
    }

    public int getActionBarHeight()
    {
        if (isSetValuesHeight) {
            return actionBarHeight;
        }
        return -1;
    }

    public int getScreenWidth()
    {
        if (isSetValuesScreen)
            return screenWidth;
        return -1;
    }

    public int getScreenHeight()
    {
        if (isSetValuesScreen)
            return screenHeight;
        return -1;
    }

    public static void setActivity(Activity act)
    {
        activity =  act;
        setValues();
    }

    private static void setValues()
    {
        actionBarHeight = 0;
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        screenHeight = displayMetrics.heightPixels;
        screenWidth = displayMetrics.widthPixels;
        isSetValuesScreen = true;
        if (actionBarHeight == 0 && activity.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv,true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, activity.getResources().getDisplayMetrics());
            isSetValuesHeight = true;
        }
    }




}
