package seedroot.attendance.com.myattendanceap.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;

import seedroot.attendance.com.myattendanceap.Activities.MainActivity;
import seedroot.attendance.com.myattendanceap.R;
import seedroot.attendance.com.myattendanceap.Utils.AppSettings;

/**
 * Created by sachin on 21/1/16.
 */
public class loginFragment extends Fragment {
    View rootview;
    ProgressDialog progDailog;
    Context mContext;
    SQLiteDatabase mydatabase;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.loginpage, container, false);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getActivity().getApplicationContext();
        String path = AppSettings.getdburl(mContext);
        mydatabase = mContext.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
        TextView tv = (TextView) view.findViewById(R.id.logintext);
        RelativeLayout r1 = (RelativeLayout) view.findViewById(R.id.l1);
        RelativeLayout r2 = (RelativeLayout) view.findViewById(R.id.l2);
        RelativeLayout r3 = (RelativeLayout) view.findViewById(R.id.l3);
        tv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeueLTStd-LtCn.otf"));
        if (AppSettings.getislogin(getActivity()) == true) {
            tv.setText("Logout");
            tv.setTextColor(getActivity().getResources().getColor(R.color.red));
            r1.setVisibility(View.GONE);
            r2.setVisibility(View.GONE);
            r3.setVisibility(View.GONE);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    getActivity().startActivity(i);
                    AppSettings.setisLogin(getActivity().getApplicationContext(), false);
                }
            });

        } else {
            tv.setText("Sign in with your account");
            tv.setTextColor(getActivity().getResources().getColor(R.color.white));
            r1.setVisibility(View.VISIBLE);
            r2.setVisibility(View.VISIBLE);
            r3.setVisibility(View.VISIBLE);
        }
        final EditText e1 = (EditText) view.findViewById(R.id.e1);
        final EditText e2 = (EditText) view.findViewById(R.id.e2);
        if (AppSettings.getusertype(getActivity().getApplicationContext()).equalsIgnoreCase("student")) {
            e1.setHint("Enter Student Id");
            e2.setHint("Enter Password");
        } else {
            e1.setHint("Enter Teacher Id");
            e2.setHint("Enter Password");
        }
        r3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(e1.getText().toString().trim()) || !TextUtils.isEmpty(e2.getText().toString().trim())) {
                    View view = getActivity().getCurrentFocus();
                    if (view != null) {
                        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    }
                    if (AppSettings.getusertype(getActivity().getApplicationContext()).equalsIgnoreCase("student")) {
                        loginstudent(e1.getText().toString().trim(), e2.getText().toString().trim());
                    } else {
                        login(e1.getText().toString().trim(), e2.getText().toString().trim());
                    }

                } else {
                    Toast.makeText(getActivity(), "Username / Password cannot be empty", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    public void loadprogressbar() {
        progDailog = new ProgressDialog(getActivity());
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

    //UserId VARCHAR,Password VARCHAR,Class VARCHAR,Subject VARCHAR,DateTime VARCHAR)
    public void login(String uid, final String psd) {

        String url = "select * from teacherlogins where UserId ='" + uid + "' and Password='" + psd + "'";
        System.out.println("------------url---------" + url);
        Cursor cursor = mydatabase.rawQuery(url, null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String id = cursor.getString(cursor.getColumnIndex("UserId"));
                    String classs = cursor.getString(cursor.getColumnIndex("Class"));
                    String subjects = cursor.getString(cursor.getColumnIndex("Subject"));
                    AppSettings.setuserId(mContext, id);
                    AppSettings.setuserclass(mContext, classs);
                    AppSettings.setusersubject(mContext, subjects);
                    AppSettings.setisLogin(mContext,true);
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new HomeFragment(), "NewFragmentTag");
                    ft.commit();
                } while (cursor.moveToNext());
            }
            cursor.close();
        } else {
            loadprogressbar();
            RequestQueue queue = Volley.newRequestQueue(mContext);
            JSONObject jsonobject_one = new JSONObject();
            try {
                jsonobject_one.put("user_id", uid);
                jsonobject_one.put("password", psd);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JsonObjectRequest req22 = new JsonObjectRequest("http://63.142.250.106/user/login", jsonobject_one,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            System.out.println("----------response------" + response.toString());
                            if (response.optString("message").toString().equals("login success")) {
                                JSONObject jsonObject = response.optJSONObject("user");

                                String id = jsonObject.optString("user_id");
                                String fullname = jsonObject.optString("first_name") + " " + jsonObject.optString("last_name");
                                String usertype = jsonObject.optString("role");
                                String updatedAt = jsonObject.optString("updatedAt");
                                String idmain = jsonObject.optString("id");
                                AppSettings.setusername(mContext, fullname);
                                AppSettings.setuserId(mContext, id);
                                AppSettings.setusertype(mContext, usertype);

                                String jsonarrayc = null;
                                String jsonarrays = null;
                                JSONArray subject = jsonObject.optJSONArray("subjects");
                                if (subject != null && subject.length() > 0) {
                                    jsonarrays = subject.toString();
                                    AppSettings.setusersubject(mContext, jsonarrays);
                                }
                                JSONArray classes = jsonObject.optJSONArray("class_id");
                                if (classes != null && classes.length() > 0) {
                                    jsonarrayc = classes.toString();
                                    AppSettings.setuserclass(mContext, jsonarrayc);
                                }
                                Calendar c = Calendar.getInstance();
                                SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                                String formattedDate = df.format(c.getTime());

                                mydatabase.execSQL("insert into teacherlogins(UserId,Password,Class,Subject,DateTime,UpdatedAt,Id) values('" + id + "','" + psd + "','" + jsonarrayc + "','" + jsonarrays + "','" + formattedDate + "','" + updatedAt + "','"+idmain+"')");

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
//                                            if (cursor.moveToFirst()) {
//                                                do {
//
//                                                } while (cursor.moveToNext());
//                                                cursor.close();
//                                            }
                                        } else {
                                            mydatabase.execSQL("Insert into logins(Id,Username,Password,Role,Subjects,Fullname,Class,RollNo) values('" + id2 + "','" + Password2 + "','" + Password2 + "','" + role2 + "','" + jsonarrays2 + "','" + username2 + "','" + jsonarrayc2 + "','" + rollnum2 + "')");
                                            System.out.println("---inserted value---------------" + username2);
                                        }
                                        System.out.println("---" + i);
                                    }
                                    AppSettings.setisLogin(mContext, true);
                                    Toast.makeText(mContext, "Login Successful", Toast.LENGTH_SHORT).show();
                                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                                    ft.replace(R.id.container, new HomeFragment(), "NewFragmentTag");
                                    ft.commit();

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(mContext,
                                            "Error: " + e.getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Toast.makeText(mContext, "Failed to Login", Toast.LENGTH_SHORT).show();
                            }
                            stoploadprogress();

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("----------error------" + error.getMessage());
                    Toast.makeText(mContext, "Failed to Login", Toast.LENGTH_SHORT).show();
                    stoploadprogress();
                }
            });
            req22.setRetryPolicy(new DefaultRetryPolicy(
                    45000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(req22);

        }
    }

    public void loginstudent(String uid, final String psd) {
        loadprogressbar();
        RequestQueue queue = Volley.newRequestQueue(mContext);
        JSONObject jsonobject_one = new JSONObject();
        try {
            jsonobject_one.put("user_id", uid);
            jsonobject_one.put("password", psd);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest req22 = new JsonObjectRequest("http://63.142.250.106/user/login", jsonobject_one,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        System.out.println("----------response------" + response.toString());
                        if (response.optString("message").toString().equals("login success")) {
                            JSONObject jsonObject = response.optJSONObject("user");

                            String id = jsonObject.optString("user_id");
                            String fullname = jsonObject.optString("first_name") + " " + jsonObject.optString("last_name");
                            String usertype = jsonObject.optString("role");
                            AppSettings.setusername(mContext, fullname);
                            AppSettings.setuserId(mContext, id);
                            AppSettings.setusertype(mContext, usertype);
                            AppSettings.setisLogin(mContext, true);
                            String jsonarrayc = null;
                            String jsonarrays = null;
                            JSONArray subject = jsonObject.optJSONArray("subjects");
                            if (subject != null && subject.length() > 0) {
                                jsonarrays = subject.toString();
                                AppSettings.setusersubject(mContext, jsonarrays);
                            }
                            JSONArray classes = jsonObject.optJSONArray("class_id");
                            if (classes != null && classes.length() > 0) {
                                jsonarrayc = classes.toString();
                                AppSettings.setuserclass(mContext, jsonarrayc);
                            }
                            Calendar c = Calendar.getInstance();
                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            String formattedDate = df.format(c.getTime());
                            mydatabase.execSQL("insert into teacherlogins(UserId,Password,Class,Subject,DateTime) values('" + id + "','" + psd + "','" + jsonarrayc + "','" + jsonarrays + "','" + formattedDate + "')");
                            final FragmentTransaction ft = getFragmentManager().beginTransaction();
                            ft.add(R.id.container, new HomeFragment(), "NewFragmentTag");
                            ft.commit();
                        } else {
                            Toast.makeText(mContext, "Failed to Login", Toast.LENGTH_SHORT).show();
                        }
                        stoploadprogress();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("----------error------" + error.getMessage());
                Toast.makeText(mContext, "Failed to Login", Toast.LENGTH_SHORT).show();
                stoploadprogress();
            }
        });

        req22.setRetryPolicy(new DefaultRetryPolicy(
                45000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        queue.add(req22);


    }
}