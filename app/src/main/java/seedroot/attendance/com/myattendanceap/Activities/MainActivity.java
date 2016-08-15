package seedroot.attendance.com.myattendanceap.Activities;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import seedroot.attendance.com.myattendanceap.Fragments.AskLoginFragment;
import seedroot.attendance.com.myattendanceap.Fragments.HomeFragment;
import seedroot.attendance.com.myattendanceap.Fragments.loginFragment;
import seedroot.attendance.com.myattendanceap.R;
import seedroot.attendance.com.myattendanceap.Utils.AppSettings;

public class MainActivity extends Activity {

    private static final int YOUR_PI_REQ_CODE = 111;
    Context mcontext;
    DrawerLayout drawer;
    Fragment[] objFragment = {null};
    TextView fees, accounttv;
    private ProgressDialog progDailog;
    boolean doubleBackToExitPressedOnce = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mcontext = this;

        boolean islogin = AppSettings.getislogin(mcontext);
        if (islogin == false) {

            FragmentManager fm = getFragmentManager();
            objFragment[0] = new AskLoginFragment();
            fm.beginTransaction().replace(R.id.container, objFragment[0]).commit();


        } else {
            if (isNetworkConnected() == true) {
                deltacall();
            } else {
                FragmentManager fm = getFragmentManager();
                objFragment[0] = new HomeFragment();
                fm.beginTransaction().replace(R.id.container, objFragment[0]).commit();
            }
        }

    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public static void overrideFonts(final Context context, final View v) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueLTStd-LtCn.otf");
        Typeface type = Typeface.createFromAsset(context.getAssets(), "fonts/FontAwesome.otf");
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView) {
                if (((TextView) v).getResources().getResourceEntryName(v.getId()).startsWith("i")) {
                    ((TextView) v).setTypeface(type);
                } else {
                    ((TextView) v).setTypeface(typeface);
                }
            }
        } catch (Exception e) {
        }
    }


    public void addfragment(Fragment fragment) {
        if (AppSettings.getislogin(mcontext) == false) {
            fragment = new loginFragment();
        }
        FragmentManager fm = getFragmentManager();
        objFragment[0] = fragment;
        fm.beginTransaction().replace(R.id.container, objFragment[0]).commit();
        //     drawer.closeDrawers();
    }


    public void sendNotification(String notificationString) {
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.bluer)
                        .setContentTitle("Notification arrived")
                        .setContentText(notificationString);
        Intent resultIntent = new Intent(this, MainActivity.class);

        PendingIntent resultPendingIntent =
                PendingIntent.getActivity(
                        this,
                        0,
                        resultIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );


        mBuilder.setContentIntent(resultPendingIntent);
        int mNotificationId = 001;
// Gets an instance of the NotificationManager service
        NotificationManager mNotifyMgr =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
// Builds the notification and issues it.
        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    public void deltacall() {
        loadprogressbar();
        String userid = "";
        String updatedat = "";
        final String url = "select * from teacherlogins where UserId ='" + AppSettings.getuserId(mcontext) + "'";
        String path = AppSettings.getdburl(mcontext);
        final SQLiteDatabase mydatabase = mcontext.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
        System.out.println("------------url---------" + url);
        Cursor cursor = mydatabase.rawQuery(url, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    userid = cursor.getString(cursor.getColumnIndex("Id"));
                    updatedat = cursor.getString(cursor.getColumnIndex("UpdatedAt"));



                } while (cursor.moveToNext());
            }
            cursor.close();
            if (!TextUtils.isEmpty(updatedat) && !TextUtils.isEmpty(userid)) {
                final RequestQueue queue = Volley.newRequestQueue(mcontext);
                JSONObject jsonobject_one = new JSONObject();
                try {
                    jsonobject_one.put("id", userid);
                    jsonobject_one.put("date", updatedat);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                final String finalUserid = userid;
                System.out.println("------------------------json---"+jsonobject_one.toString());
                final JsonObjectRequest req22 = new JsonObjectRequest("http://63.142.250.106/user/delta", jsonobject_one,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {

                                if (!response.optString("message").toString().equals("no updates")) {
                                    JSONObject jsonObject = response.optJSONObject("user");
                                    JSONArray subject = jsonObject.optJSONArray("subjects");
                                    String jsonarrayc = null;
                                    String jsonarrays = null;
                                    String id = jsonObject.optString("user_id");
                                    String fullname = jsonObject.optString("first_name") + " " + jsonObject.optString("last_name");
                                    String usertype = jsonObject.optString("role");
                                    String updatedAt = jsonObject.optString("updatedAt");
                                    AppSettings.setusername(mcontext, fullname);
                                    AppSettings.setuserId(mcontext, id);
                                    AppSettings.setusertype(mcontext, usertype);

                                    if (subject != null && subject.length() > 0) {
                                        jsonarrays = subject.toString();
                                        AppSettings.setusersubject(mcontext, jsonarrays);
                                    }
                                    JSONArray classes = jsonObject.optJSONArray("class_id");
                                    if (classes != null && classes.length() > 0) {
                                        jsonarrayc = classes.toString();
                                        AppSettings.setuserclass(mcontext, jsonarrayc);
                                    }

                                    String updateTeacher = "update teacherlogins set Class='" + jsonarrayc + "',Subject='" + jsonarrays + "',UpdatedAt='" + updatedAt + "' where UserId='" + finalUserid + "'";
                                    mydatabase.execSQL(updateTeacher);


                                    try {
                                        JSONArray jsonArray = jsonObject.optJSONArray("users");
                                        for (int i = 0; i < jsonArray.length(); i++) {

                                            JSONObject object = (JSONObject) jsonArray
                                                    .get(i);
                                            String id2 = object.optString("id");
                                            String username2 = object.optString("first_name") + " " + object.optString("last_name");
                                            String Password2 = object.optString("user_id");
                                            String role2 = object.optString("role");
                                            String rollnum2 = object.optString("roll_number");
                                            String jsonarrayc2 = null;
                                            String jsonarrays2 = null;
                                            JSONArray subject2 = object.optJSONArray("subjects");
                                            if (subject2 != null && subject2.length() > 0) {
                                                jsonarrays2 = subject2.toString();

                                            }
                                            JSONArray classes2 = object.optJSONArray("class_id");
                                            if (classes2 != null && classes2.length() > 0) {
                                                jsonarrayc2 = classes2.toString();

                                            }


                                            String qry = "select Id,Fullname from logins where Id='" + id2 + "'";
                                            Cursor cursor = mydatabase.rawQuery(qry, null);
                                            if (cursor.getCount() > 0) {
//
                                                mydatabase.execSQL("Update logins set Subjects='" + jsonarrays2 + "',Class='" + jsonarrayc2 + "',Username='" + Password2 + "',Password='" + Password2 + "',Fullname='" + username2 + "',RollNo='" + rollnum2 + "' where Id ='" + id2 + "'");
                                                System.out.println("-------------updated---"+Password2);
                                            } else {
                                                mydatabase.execSQL("Insert into logins(Id,Username,Password,Role,Subjects,Fullname,Class,RollNo) values('" + id2 + "','" + Password2 + "','" + Password2 + "','" + role2 + "','" + jsonarrays2 + "','" + username2 + "','" + jsonarrayc2 + "','" + rollnum2 + "')");
                                                System.out.println("-------------Inserted---"+Password2);
                                            }
                                            System.out.println("---" + i);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                        Toast.makeText(mcontext,
                                                "Error: " + e.getMessage(),
                                                Toast.LENGTH_LONG).show();
                                    }


                                } else {
                                    System.out.println("=======no updates==========");
                                }
                                stoploadprogress();
                                FragmentManager fm = getFragmentManager();
                                objFragment[0] = new HomeFragment();
                                fm.beginTransaction().replace(R.id.container, objFragment[0]).commit();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("----------error------" + error.getMessage());
                        Toast.makeText(mcontext, "Failed to update", Toast.LENGTH_SHORT).show();
                        stoploadprogress();
                        FragmentManager fm = getFragmentManager();
                        objFragment[0] = new HomeFragment();
                        fm.beginTransaction().replace(R.id.container, objFragment[0]).commit();

                    }
                });
                req22.setRetryPolicy(new DefaultRetryPolicy(
                        45000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(req22);
            }else{
                stoploadprogress();
                FragmentManager fm = getFragmentManager();
                objFragment[0] = new HomeFragment();
                fm.beginTransaction().replace(R.id.container, objFragment[0]).commit();
            }
        }
    }

    public void loadprogressbar() {
        progDailog = new ProgressDialog(mcontext);
        progDailog.setMessage("Loading, Please wait...");
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(false);
        progDailog.show();
    }

    public void stoploadprogress() {

        if (progDailog.isShowing() == true) {
            progDailog.dismiss();
        }

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mcontext.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

    }
}