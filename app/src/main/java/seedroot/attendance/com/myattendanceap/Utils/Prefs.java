package seedroot.attendance.com.myattendanceap.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nitesh on 8/7/15.
 */
public final class Prefs {

    public static SharedPreferences get(Context context){
        return context.getSharedPreferences(AppConstant.PREFS_KEY , Context.MODE_PRIVATE);
    }
}
