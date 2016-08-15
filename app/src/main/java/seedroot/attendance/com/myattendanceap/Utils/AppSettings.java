package seedroot.attendance.com.myattendanceap.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by nitesh on 8/7/15.
 */
public class AppSettings {


    public static void setisLogin(Context context, boolean geofenceAdded) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean("isLogin", geofenceAdded);
        editor.commit();
    }

    public static boolean getislogin(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getBoolean("isLogin", false);
    }


    public static void setusername(Context context, String home_ta) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", home_ta);
        editor.commit();
    }

    public static String getusername(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getString("username", null);
    }

    public static void setusertype(Context context, String home_ta) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("usertype", home_ta);
        editor.commit();
    }

    public static String getusertype(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getString("usertype", null);
    }

    public static void setuserdetailse(Context context, String home_ta) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userdetailse", home_ta);
        editor.commit();
    }

    public static String getuserdetailse(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getString("userdetailse", null);
    }

    public static void setdate(Context context, String home_ta) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("dat", home_ta);
        editor.commit();
    }

    public static String getdate(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getString("dat", null);
    }

    public static void setdburl(Context context, String home_ta) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("dburl", home_ta);
        editor.commit();
    }

    public static String getdburl(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getString("dburl", null);
    }

    public static void setuserId(Context context, String home_ta) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userId", home_ta);
        editor.commit();
    }

    public static String getuserId(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getString("userId", null);
    }

    public static void setuserclass(Context context, String home_ta) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("userclass", home_ta);
        editor.commit();
    }

    public static String getuserclass(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getString("userclass", null);
    }

    public static void setusersubject(Context context, String home_ta) {
        SharedPreferences prefs = Prefs.get(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("usersubject", home_ta);
        editor.commit();
    }

    public static String getusersubject(Context context) {
        SharedPreferences prefs = Prefs.get(context);
        return prefs.getString("usersubject", null);
    }

}
