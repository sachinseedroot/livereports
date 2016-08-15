package seedroot.attendance.com.myattendanceap.Activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.crashlytics.android.Crashlytics;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.fabric.sdk.android.Fabric;
import seedroot.attendance.com.myattendanceap.Fragments.HomeFragment;
import seedroot.attendance.com.myattendanceap.R;
import seedroot.attendance.com.myattendanceap.Utils.AppSettings;


/**
 * Created by sachin on 21/1/16.
 */
public class SplashScreenActivity extends Activity {

    private static int SPLASH_TIME_OUT = 3000;

    Context mContext;
    SQLiteDatabase mydatabase;
    private ProgressDialog progDailog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.splash);
        mContext = this;

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.parentsplash);


        overrideFonts(this, relativeLayout);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity

                loaddata();
                Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                // close this activity
                finish();
            }
        }, SPLASH_TIME_OUT);


    }

    public static void overrideFonts(final Context context, final View v) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/HelveticaNeueLTStd-LtCn.otf");

        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView) {

                ((TextView) v).setTypeface(typeface);

            }
        } catch (Exception e) {
        }
    }

    public void loaddata() {


        File folder = new File("/data/data/seedroot.attendance.com.myattendanceap/databases/extras.db");

        //File dbfile = new File(folder.toString() + "/extras.db");

        if (folder.exists()) {
            String urldb = folder.toString() + "/extras.db";
            AppSettings.setdburl(SplashScreenActivity.this, urldb);
            mydatabase = openOrCreateDatabase(urldb, MODE_PRIVATE, null);

            Log.v("Epol-onCreate", "Database exists!");
            if (isNetworkConnected() == true) {
                // uploadremainingdata();
                Backgroundcheck backgroundcheck =new Backgroundcheck();
                backgroundcheck.execute();

                BackgroundPushPending backgroundcheckpending =new BackgroundPushPending();
                backgroundcheckpending.execute();
            }
//            Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//            finish();
        } else {
            folder.mkdirs();
            String urldb = folder.toString() + "/extras.db";
            AppSettings.setdburl(SplashScreenActivity.this, urldb);
            mydatabase = openOrCreateDatabase(urldb, MODE_PRIVATE, null);
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS logins(Username VARCHAR,Password VARCHAR,Role VARCHAR,Id VARCHAR,RollNo VARCHAR,Subjects VARCHAR,Fullname VARCHAR,Class VARCHAR);");
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS classdata(Subjects VARCHAR,Name VARCHAR,Division VARCHAR,Id VARCHAR);");
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS logs(TeacherId VARCHAR,Class VARCHAR,ClassId VARCHAR,Subject VARCHAR,SubjectId VARCHAR,Std_status VARCHAR,Date VARCHAR,Time VARCHAR,Uploaded VARCHAR,UploadedId VARCHAR);");
            mydatabase.execSQL("CREATE TABLE IF NOT EXISTS teacherlogins(Id VARCHAR,UserId VARCHAR,Password VARCHAR,Class VARCHAR,Subject VARCHAR,DateTime VARCHAR,UpdatedAt VARCHAR);");
        }


    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void uploadremainingdata() {
        String pattern = "dd-MM-yyyy";
        final String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        String qu = "select * from logs where Uploaded='false' and Date='" + date + "'";
        System.out.println("------sendingpendingdata---" + qu);
        Cursor cursor = mydatabase.rawQuery(qu, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    int pullpost = 0;
                    String url = null;
                    String uploaded = cursor.getString(cursor.getColumnIndex("Uploaded"));

                    pullpost = Request.Method.POST;
                    url = "http://63.142.250.106/attendance/save";


                    // String dates = cursor.getString(cursor.getColumnIndex("time"));
                    JSONArray toServerarrJ = new JSONArray();
                    String sub = cursor.getString(cursor.getColumnIndex("SubjectId"));
                    String clas = cursor.getString(cursor.getColumnIndex("ClassId"));
                    String Std_status = cursor.getString(cursor.getColumnIndex("Std_status"));
                    try {
                        JSONArray fromdbJ = new JSONArray(Std_status);
                        for (int i = 0; i < fromdbJ.length(); i++) {
                            JSONObject jsonObject = fromdbJ.optJSONObject(i);
                            JSONObject newJ = new JSONObject();
                            if (jsonObject.optString("present").equalsIgnoreCase("Absent")) {
                                newJ.put("present", false);
                            } else {
                                newJ.put("present", true);
                            }
                            newJ.put("name", jsonObject.optString("name"));
                            newJ.put("user_id", jsonObject.optString("user_id"));
                            toServerarrJ.put(newJ);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    try {
                        JSONObject jsonobject_one = new JSONObject();

                        jsonobject_one.put("subject", sub);
                        jsonobject_one.put("class", clas);
                        jsonobject_one.put("users", toServerarrJ);
                        JSONObject userjson = new JSONObject();
                        userjson.put("user_id", AppSettings.getuserId(mContext));
                        jsonobject_one.put("created_by", userjson);
                        // System.out.println("----giving---" + jsonArray.toString());
                        RequestQueue queue = Volley.newRequestQueue(mContext);
                        JsonObjectRequest req22 = new JsonObjectRequest(pullpost, url, jsonobject_one,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {

                                        System.out.println("----------responsePendingDataSuccess------" + response.toString());
                                        JSONObject jsonObject = response;
                                        String dateInString = new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(new Date());
                                        String id = jsonObject.optString("id");
                                        String subjectid = jsonObject.optString("subject");
                                        String classid = jsonObject.optString("class");
                                        String query = "update logs set UploadedId='" + id + "',Uploaded='True',Time='" + dateInString + "' where SubjectId='" + subjectid + "' and ClassId='" + classid + "'";
                                        System.out.println("-----------responsequert---" + query);
                                        mydatabase.execSQL(query);
                                        //       stoploadprogress();
                                        Toast.makeText(mContext, "Successfully Uploaded pending data.", Toast.LENGTH_SHORT).show();
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                System.out.println("----------errorPendingData------" + error.getMessage());
                                //  stoploadprogress();
                            }
                        });
                        req22.setRetryPolicy(new DefaultRetryPolicy(
                                45000,
                                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        queue.add(req22);

                        // js.put("data", jsonobject.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
//            Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//            finish();

        } else {
//            Intent i = new Intent(SplashScreenActivity.this, MainActivity.class);
//            startActivity(i);
//            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
//            finish();

        }

    }

    public void checkdata() {
        String qu = "select * from logs";
        System.out.println("------qu---" + qu);
        Cursor cursor = mydatabase.rawQuery(qu, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String subjectid = cursor.getString(cursor.getColumnIndex("SubjectId"));
                    String classid = cursor.getString(cursor.getColumnIndex("ClassId"));
                    String date = cursor.getString(cursor.getColumnIndex("Date"));
                    String stdstatus = cursor.getString(cursor.getColumnIndex("Std_status"));

                    try {
                        JSONObject jsonobject_one = new JSONObject();
                        jsonobject_one.put("subject", subjectid);
                        jsonobject_one.put("class", classid);
                        jsonobject_one.put("user_id", AppSettings.getuserId(mContext));
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                        Date ordate = simpleDateFormat.parse(date);
                        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat("MM-dd-yyyy");
                        String newdate = simpleDateFormat2.format(ordate);

                        jsonobject_one.put("date", newdate);
                        System.out.println("-----------objectpassing---"+jsonobject_one.toString());
                        hitCheck(jsonobject_one, stdstatus, stdstatus);
                    } catch (Exception e) {

                    }

                } while (cursor.moveToNext());
            }
            cursor.close();
        }


    }

    public void hitCheck(final JSONObject jsonobject_one, String s, final String stdstatus) {
        String url = "http://63.142.250.106/attendance/fix";
        loadprogressbar();
        final RequestQueue queue = Volley.newRequestQueue(mContext);
        final JsonArrayRequest req22 = new JsonArrayRequest(Request.Method.POST, url, jsonobject_one,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        stoploadprogress();
                        System.out.println("----------response------" + response.toString());
                        if (response != null && response.length() > 0) {
                            System.out.println("----------responseFix----successfull-----" + response.toString());
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject jsonObjectresponse = response.optJSONObject(i);
                                if (jsonObjectresponse != null && jsonObjectresponse.length() > 0) {
                                    sendpendingdata(jsonObjectresponse, stdstatus);
                                }
                            }
                        } else {
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("----------errorFix------" + error.getMessage());
                  stoploadprogress();
            }
        });
        req22.setRetryPolicy(new DefaultRetryPolicy(
                45000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(req22);
    }

    public void sendpendingdata(JSONObject jsonobject_one, String stdstatus) {
        String id = jsonobject_one.optString("id");
        String classid = jsonobject_one.optString("class");
        String subjecyid = jsonobject_one.optString("subject");
//            String query = "select * from logs where TeacherId='" + AppSettings.getuserId(mContext) + "' and Date='" + date + "' and SubjectId='"+subjecyid+"' and ClassId='"+classid+"'";
//
//
//        Cursor cursor = mydatabase.rawQuery(query, null);
//
//        if (cursor.getCount() > 0) {
//            if (cursor.moveToFirst()) {
//                do {
//                        String sub =
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//        }
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jsonArray = new JSONArray(stdstatus);

            jsonObject.put("users", jsonArray);


            if (!TextUtils.isEmpty(stdstatus) && jsonObject != null && jsonObject.length() > 0) {
                String url = "http://63.142.250.106/attendance/" + id;
                loadprogressbar();
                RequestQueue queue = Volley.newRequestQueue(mContext);
                JsonArrayRequest req22 = new JsonArrayRequest(Request.Method.PUT, url, jsonObject,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                System.out.println("-------updateId----Successfull"+response.toString());
                                stoploadprogress();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println("----------errorID------" + error.getMessage());
                          stoploadprogress();
                    }
                });
                req22.setRetryPolicy(new DefaultRetryPolicy(
                        45000,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                queue.add(req22);

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void loadprogressbar() {
//        if (progDailog == null) {
//            progDailog = new ProgressDialog(mContext);
//        }
//        if (progDailog.isShowing() == false) {
//            progDailog.setMessage("Saving, Please wait...");
//            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progDailog.setCancelable(true);
//            progDailog.show();
//        }
    }

    public void stoploadprogress() {

//        if (progDailog.isShowing() == true) {
//            progDailog.dismiss();
//        }

    }

    public class Backgroundcheck extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            checkdata();
            return null;
        }
    }
    public class BackgroundPushPending extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... params) {
            uploadremainingdata();
            return null;
        }
    }

}
