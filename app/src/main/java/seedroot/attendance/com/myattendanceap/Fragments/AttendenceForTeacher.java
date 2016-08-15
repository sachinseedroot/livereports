package seedroot.attendance.com.myattendanceap.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import seedroot.attendance.com.myattendanceap.BuildConfig;
import seedroot.attendance.com.myattendanceap.R;
import seedroot.attendance.com.myattendanceap.Utils.AppSettings;
import seedroot.attendance.com.myattendanceap.Utils.TypeFaceHelper;

/**
 * Created by Neha on 8/9/2016.
 */
public class AttendenceForTeacher extends Fragment {
    boolean changeindatetime;
    CheckBox checkall;
    int chr;
    JSONObject classDetails;
    int cmin;
    int cnt;
    String ctime;
    boolean isrowpresent;
    boolean isupdate;
    JSONArray jsonArraySpinenr;
    Context mContext;
    SQLiteDatabase mydatabase;
    String prevatt;
    ProgressDialog progDailog;
    RelativeLayout relbtn;
    View rootview;
    Spinner spinner1;
    Spinner spinner2;
    TextView spinner3;
    RelativeLayout stdheader;
    LinearLayout studentinfo_checkbox;
    RelativeLayout svrel;
    String uid;
    JSONObject classSubjectCodes;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.attenforteacher, container, false);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.mContext = getActivity().getApplicationContext();
        this.mydatabase = this.mContext.openOrCreateDatabase(AppSettings.getdburl(this.mContext), Context.MODE_PRIVATE, null);
        this.studentinfo_checkbox = (LinearLayout) view.findViewById(R.id.studentinfo_checkbox);
        this.checkall = (CheckBox) view.findViewById(R.id.checkall);
        this.spinner1 = (Spinner) view.findViewById(R.id.ddpat1);
        this.spinner2 = (Spinner) view.findViewById(R.id.ddpat12);
        this.spinner3 = (TextView) view.findViewById(R.id.ddpat13);
        this.svrel = (RelativeLayout) view.findViewById(R.id.scrollviewrel);
        this.relbtn = (RelativeLayout) view.findViewById(R.id.btnsub);
        this.stdheader = (RelativeLayout) view.findViewById(R.id.stdheader);
        this.jsonArraySpinenr = new JSONArray();
        List<String> categories = new ArrayList();
        final String classes = AppSettings.getuserclass(this.mContext);
        categories.add("Select Class");
        try {
            JSONArray jsonArray = new JSONArray(classes);
            if (jsonArray.length() > 0) {
                this.classDetails = new JSONObject();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    JSONObject newjsonob = new JSONObject();
                    categories.add(jsonObject.optString("name") + "-" + jsonObject.optString("division"));
                    newjsonob.put("id", jsonObject.optString("id"));
                    newjsonob.put("name", jsonObject.optString("name"));
                    newjsonob.put("div", jsonObject.optString("division"));
                    this.jsonArraySpinenr.put(newjsonob);
                    this.classDetails.put(jsonObject.optString("id"), jsonObject.optString("name") + "-" + jsonObject.optString("division"));
                    System.out.println("----------classid---" + jsonObject.optString("id"));
                }
            } else {
                Toast.makeText(this.mContext, "Failed to load class", Toast.LENGTH_SHORT).show();
                classDetails = null;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        this.spinner1.setAdapter(new ArrayAdapter(getActivity().getApplicationContext(), R.layout.simpletextview, categories));
        // this.spinner1.setVisibility(View.GONE);
        this.spinner2.setVisibility(View.GONE);
        this.svrel.setVisibility(View.GONE);
        this.relbtn.setVisibility(View.GONE);
        this.stdheader.setVisibility(View.GONE);
        this.spinner3.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
        Calendar c = Calendar.getInstance();
        this.chr = c.get(Calendar.HOUR);
        this.cmin = c.get(Calendar.MINUTE);
        this.ctime = Integer.toString(this.chr) + ":" + Integer.toString(this.cmin);
        System.out.println("----------" + Integer.toString(this.chr) + ":" + Integer.toString(this.cmin));
        this.spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                classSubjectCodes = new JSONObject();
                String classname = spinner1.getSelectedItem().toString();
                if (!classname.equalsIgnoreCase("Select Class")) {
                    spinner2.setVisibility(View.VISIBLE);
                    relbtn.setVisibility(View.VISIBLE);
                    try {
                        if (classDetails != null && classDetails.length() > 0) {
                            String classid = "";
                            for (int i = 0; i < classDetails.length(); i++) {
                                String classnameselected = classDetails.get(classDetails.names().getString(i)).toString();
                                if (classnameselected.equalsIgnoreCase(classname)) {
                                    classid = classDetails.names().getString(i);

                                }
                            }
                            List<String> subjectNames = new ArrayList<String>();
                            subjectNames.add("Select Subjects");
                            if (!TextUtils.isEmpty(classid)) {

                                String subjectarray = AppSettings.getusersubject(mContext);
                                JSONArray jsonArraysubjects = new JSONArray(subjectarray);
                                for (int k = 0; k < jsonArraysubjects.length(); k++) {
                                    JSONObject jsonObjectSubjects = jsonArraysubjects.optJSONObject(k);
                                    if (classid.equals(jsonObjectSubjects.optString("classID"))) {
                                        subjectNames.add(jsonObjectSubjects.optString("name"));
                                    }
                                }
                            } else {
                                Toast.makeText(mContext, "Failed to load subjects", Toast.LENGTH_SHORT).show();
                                subjectNames = null;
                            }

                            if (subjectNames != null && subjectNames.size() > 0 && !TextUtils.isEmpty(classid)) {
                                classSubjectCodes.put("classid", classid);
                                classSubjectCodes.put("classname", classname);
                                spinner2.setAdapter(new ArrayAdapter(getActivity().getApplicationContext(), R.layout.simpletextview, subjectNames));
                            }

                        }
                    } catch (Exception e) {
                        Toast.makeText(mContext, "Failed to load subjects", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    spinner2.setVisibility(View.GONE);
                    relbtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        this.spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String subjectname = spinner2.getSelectedItem().toString();

                if (!subjectname.equalsIgnoreCase("Select Subjects")) {
                    relbtn.setVisibility(View.VISIBLE);
                    String subjectJsonArray = AppSettings.getusersubject(mContext);
                    try {

                        JSONArray jsonArraySubjects = new JSONArray(subjectJsonArray);
                        if (jsonArraySubjects != null && jsonArraySubjects.length() > 0 && classSubjectCodes != null && classSubjectCodes.length() > 0) {
                            classSubjectCodes.put("subject_code", "");
                            String classid = classSubjectCodes.optString("classid");
//                            String classname = classSubjectCodes.optString("classname");
                            for (int i = 0; i < jsonArraySubjects.length(); i++) {
                                JSONObject jsonObjectSubject = jsonArraySubjects.optJSONObject(i);
                                String subject_code = jsonObjectSubject.optString("subject_code");
                                String clsid = jsonObjectSubject.optString("classID");
                                String subname = jsonObjectSubject.optString("name");
                                if (clsid.equals(classid) && subjectname.equals(subname)) {
                                    classSubjectCodes.put("subject_code", subject_code);
                                }
                            }
                            System.out.println("-------classsubjectcdes----" + classSubjectCodes.toString());
                            if (!TextUtils.isEmpty(classSubjectCodes.getString("subject_code"))) {
                                relbtn.setVisibility(View.VISIBLE);
                                createdb(classSubjectCodes.getString("classid"), classSubjectCodes.getString("subject_code"));
                            } else {
                                relbtn.setVisibility(View.GONE);
                            }
                        }


                    } catch (Exception e) {
                        Toast.makeText(mContext, "Failed to load Students", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    relbtn.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ((ImageButton) this.rootview.findViewById(R.id.backattea)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, new HomeFragment(), "NewFragmentTag");
                ft.commit();
            }
        });
        overrideFonts(mContext, (LinearLayout) this.rootview.findViewById(R.id.parentattteach));
        this.relbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        this.checkall.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    public static void overrideFonts(Context context, View v) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), TypeFaceHelper.FONT_AWESOME);
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    overrideFonts(context, vg.getChildAt(i));
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(typeface);
            }
        } catch (Exception e) {
        }
    }

    public void builtupdate(String date, String time) {
        int i;
        JSONArray jSONArray;
        JSONArray jsonArray = new JSONArray();
        for (i = 0; i < this.studentinfo_checkbox.getChildCount(); i++) {
            RelativeLayout relativeLayout = (RelativeLayout) this.studentinfo_checkbox.getChildAt(i);
            TextView textViewRollno = (TextView) relativeLayout.getChildAt(0);
            CheckBox checkBox = (CheckBox) relativeLayout.getChildAt(1);
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("name", checkBox.getText().toString().trim());
                jsonObject.put("user_id", checkBox.getTag().toString());
                jsonObject.put("roll_number", textViewRollno.getText().toString());
                if (checkBox.isChecked()) {
                    jsonObject.put("present", "Present");
                } else {
                    jsonObject.put("present", "Absent");
                }
                jsonArray.put(jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        String subject_Id = null;
        JSONObject jsonObject;
        try {
            jSONArray = new JSONArray(AppSettings.getusersubject(this.mContext));
            for (i = 0; i < jSONArray.length(); i++) {
                jsonObject = jSONArray.optJSONObject(i);
                if (jsonObject.optString("name").equalsIgnoreCase(this.spinner2.getSelectedItem().toString())) {
                    subject_Id = jsonObject.optString("id");
                }
            }
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        String class_id = null;
        try {
            jSONArray = new JSONArray(AppSettings.getuserclass(this.mContext));
            for (i = 0; i < jSONArray.length(); i++) {
                JSONObject jsonObject2 = jSONArray.optJSONObject(i);
                String obj = this.spinner1.getSelectedItem().toString();
                String[] spin = obj.split("-");
                if (jsonObject2.optString("name").equalsIgnoreCase(spin[0])) {
                    if (jsonObject2.optString("division").equalsIgnoreCase(spin[1])) {
                        class_id = jsonObject2.optString("id");
                    }
                }
            }
        } catch (JSONException e22) {
            e22.printStackTrace();
        }
        if (this.isrowpresent) {
            System.out.println("---------------row present---------------");
            String query = "update logs set Uploaded='false',Std_status='" + jsonArray.toString() + "' where Class ='" + this.spinner1.getSelectedItem().toString() + "' and Subject='" + this.spinner2.getSelectedItem().toString() + "' and Date='" + this.spinner3.getText().toString().trim() + "'";
            System.out.println("-----------responsequert---" + query);
            this.mydatabase.execSQL(query);
        } else {
            this.mydatabase.execSQL("insert into logs(TeacherId ,Class ,Subject ,Std_status ,Date ,Time ,Uploaded,ClassId, SubjectId ) values('" + AppSettings.getuserId(this.mContext) + "','" + this.spinner1.getSelectedItem().toString() + "','" + this.spinner2.getSelectedItem().toString() + "','" + jsonArray.toString() + "','" + date + "','" + time + "','false','" + class_id + "','" + subject_Id + "')");
        }
        if (isNetworkConnected()) {
            loadprogressbar();
            String qu = "select * from logs where Class ='" + this.spinner1.getSelectedItem().toString() + "' and Subject='" + this.spinner2.getSelectedItem().toString() + "' and Date='" + this.spinner3.getText().toString().trim() + "'";
            System.out.println("------qu---" + qu);
            Cursor cursor = this.mydatabase.rawQuery(qu, null);
            if (cursor.getCount() > 0) {
                if (cursor.moveToFirst()) {
                    do {
                        int pullpost;
                        String url;
                        String uploaded = cursor.getString(cursor.getColumnIndex("Uploaded"));
                        if (!this.isrowpresent) {
                            pullpost = 1;
                            url = "http://63.142.250.106/attendance/save";
                            System.out.println("---------------------------submiiting first");
                        } else if (this.isrowpresent) {
                            pullpost = 2;
                            url = "http://63.142.250.106/attendance/" + cursor.getString(cursor.getColumnIndex("UploadedId"));
                            System.out.println("---------------------------submiiting again--" + url);
                        } else {
                            pullpost = 1;
                            url = "http://63.142.250.106/attendance/save";
                        }
                        JSONArray toServerarrJ = new JSONArray();
                        String sub = cursor.getString(cursor.getColumnIndex("SubjectId"));
                        String clas = cursor.getString(cursor.getColumnIndex("ClassId"));
                        try {
                            jSONArray = new JSONArray(cursor.getString(cursor.getColumnIndex("Std_status")));
                            for (i = 0; i < jSONArray.length(); i++) {
                                jsonObject = jSONArray.optJSONObject(i);
                                JSONObject newJ = new JSONObject();
                                if (jsonObject.optString("present").equalsIgnoreCase("Absent")) {
                                    newJ.put("present", false);
                                } else {
                                    newJ.put("present", true);
                                }
                                String optString = jsonObject.optString("name");
                                newJ.put("name", optString);
                                optString = jsonObject.optString("user_id");
                                newJ.put("user_id", optString);
                                optString = jsonObject.optString("roll_number");
                                newJ.put("roll_number", optString);
                                toServerarrJ.put(newJ);
                            }
                        } catch (JSONException e222) {
                            e222.printStackTrace();
                        }
                        if (toServerarrJ != null && toServerarrJ.length() > 0) {
                            try {
                                JSONObject jsonobject_one = new JSONObject();
                                if (this.isrowpresent) {
                                    jsonobject_one.put("id", cursor.getString(cursor.getColumnIndex("UploadedId")));
                                }
                                jsonobject_one.put("subject", sub);
                                jsonobject_one.put("class", clas);
                                jsonobject_one.put("users", toServerarrJ);
                                JSONObject userjson = new JSONObject();
                                userjson.put("user_id", AppSettings.getuserId(this.mContext));
                                jsonobject_one.put("created_by", userjson);
                                System.out.println("----giving---" + jsonobject_one.toString());
                                if (this.isrowpresent) {
                                    RequestQueue queueA = Volley.newRequestQueue(this.mContext);
                                    JsonArrayRequest jsonArrayRequestA = new JsonArrayRequest(pullpost, url, jsonobject_one, new Response.Listener<JSONArray>() {
                                        @Override
                                        public void onResponse(JSONArray response) {

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });

                                    jsonArrayRequestA.setRetryPolicy(new DefaultRetryPolicy(
                                            45000,
                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    queueA.add(jsonArrayRequestA);
                                } else {
                                    RequestQueue queue = Volley.newRequestQueue(this.mContext);
                                    JsonObjectRequest req22 = new JsonObjectRequest(pullpost, url, jsonobject_one, new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {

                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });
                                    req22.setRetryPolicy(new DefaultRetryPolicy(
                                            45000,
                                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                                    queue.add(req22);
                                }
                            } catch (JSONException e2222) {
                                e2222.printStackTrace();
                            }
                        }
                    } while (cursor.moveToNext());
                }
                cursor.close();
                return;
            }
            Toast.makeText(this.mContext, "Failed to load Students", Toast.LENGTH_SHORT).show();
            return;
        }
        Toast.makeText(this.mContext, "No internet, Data Saved locally. ", Toast.LENGTH_SHORT).show();
    }

    public void loadprogressbar() {
        if (this.progDailog == null) {
            this.progDailog = new ProgressDialog(getActivity());
        }
        if (!this.progDailog.isShowing()) {
            this.progDailog.setMessage("Saving, Please wait...");
            this.progDailog.setProgressStyle(0);
            this.progDailog.setCancelable(true);
            this.progDailog.show();
        }
    }

    public void stoploadprogress() {
        if (this.progDailog.isShowing()) {
            this.progDailog.dismiss();
        }
    }

    public void createdb(String classidc, String subjectidc) {
        JSONArray jSONArray;
        int i;
        boolean isbuffertimeover = getbufferstatus(classidc, subjectidc);
        this.relbtn.setVisibility(View.GONE);
        ArrayList<String> studentname = new ArrayList();
        ArrayList<String> studentrollno = new ArrayList();
        ArrayList<String> studentnametag = new ArrayList();
        this.studentinfo_checkbox.removeAllViews();
        Cursor cursor = this.mydatabase.rawQuery("select * from logins where Role ='student' order by cast(RollNo as int)  asc", null);
        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    String cl = cursor.getString(cursor.getColumnIndex("Class"));
                    String rolno = cursor.getString(cursor.getColumnIndex("RollNo"));
                    try {
                        jSONArray = new JSONArray(cl);
                        if (jSONArray.length() > 0) {
                            for (i = 0; i < jSONArray.length(); i++) {
                                JSONObject jsonObject = jSONArray.optJSONObject(i);
                                //String[] spin = this.spinner1.getSelectedItem().toString().split("-");
                                String id = jsonObject.optString("id");
                                //   String dd = jsonObject.optString("division");
                                if (id.equalsIgnoreCase(classidc)) {

                                    try {
                                        jSONArray = new JSONArray(cursor.getString(cursor.getColumnIndex("Subjects")));
                                        if (jSONArray.length() > 0) {
                                            for (int i2 = 0; i2 < jSONArray.length(); i2++) {
                                                JSONObject jsonObject2 = jSONArray.optJSONObject(i2);
//                                                    String spi2 = this.spinner2.getSelectedItem().toString();
                                                String subjectcode = jsonObject2.optString("subject_code");
//                                                    System.out.println("---spin---sub--" + spi2 + "--" + aa2);
                                                if (subjectcode.equalsIgnoreCase(subjectidc) && !TextUtils.isEmpty(rolno)) {
                                                    if (!rolno.equals("null")) {
                                                        studentname.add(cursor.getString(cursor.getColumnIndex("Fullname")));
                                                        studentrollno.add(rolno);
                                                        studentnametag.add(cursor.getString(cursor.getColumnIndex("Username")));
                                                    }
                                                }
                                            }
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            }
                        }
                    } catch (JSONException e2) {
                        e2.printStackTrace();
                    }
                } while (cursor.moveToNext());
            }
            cursor.close();
        }
//        else {
//            Toast.makeText(this.mContext, "Failed to load Students", Toast.LENGTH_SHORT).show();
//            this.studentinfo_checkbox.removeAllViews();
//            this.relbtn.setVisibility(View.GONE);
//        }
        if (studentname.size() > 0) {
            this.stdheader.setVisibility(View.VISIBLE);
            this.relbtn.setVisibility(View.VISIBLE);
            System.out.println("----------------------student size-------" + studentname.size());
            RelativeLayout.LayoutParams layoutParamsRel = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 30);
            layoutParamsRel.setMargins(15, 0, 15, 0);

            RelativeLayout.LayoutParams layoutParamsTV = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsTV.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
            layoutParamsTV.addRule(RelativeLayout.CENTER_VERTICAL);
//            layoutParamsTV.setMargins(0, 0, 0, 0);

            RelativeLayout.LayoutParams layoutParamsArrowTV = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsArrowTV.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            layoutParamsArrowTV.addRule(RelativeLayout.CENTER_VERTICAL);
//            layoutParamsArrowTV.setMargins(0, 0, 25, 0);
            for (i = 0; i < studentname.size(); i++) {

                RelativeLayout listrel = new RelativeLayout(mContext);
                listrel.setLayoutParams(layoutParamsRel);

                CheckBox valueTV = new CheckBox(mContext);
                valueTV.setTextColor(mContext.getResources().getColor(R.color.black));
                valueTV.setText(studentname.get(i));
                valueTV.setTextSize(18f);

                TextView rightbackarow = new TextView(mContext);
                rightbackarow.setTextColor(mContext.getResources().getColor(R.color.black));
//                rightbackarow.setText(mContext.getResources().getString(R.string.fa_right_arrow));
//                rightbackarow.setTypeface(fontawesome);
                rightbackarow.setText(studentrollno.get(i));
                rightbackarow.setTextSize(18f);

                valueTV.setLayoutParams(layoutParamsTV);
                rightbackarow.setLayoutParams(layoutParamsArrowTV);


                if (!TextUtils.isEmpty(this.prevatt)) {
                    try {
                        jSONArray = new JSONArray(this.prevatt);
                        if (jSONArray.length() > 0) {
                            for (int k = 0; k < jSONArray.length(); k++) {
                                JSONObject jsonObject = jSONArray.optJSONObject(k);
                                if (jsonObject.optString("name").equals(studentname.get(i))) {
                                    if (jsonObject.optString("present").equals("Present")) {
                                        valueTV.setChecked(true);
                                    } else {
                                        valueTV.setChecked(false);
                                    }
                                }
                            }
                        }
                    } catch (JSONException e22) {
                        e22.printStackTrace();
                    }
                }
//                relativeLayout.addView(textView);
//                relativeLayout.addView(checkBox);
//                this.studentinfo_checkbox.addView(relativeLayout);

                listrel.addView(valueTV);
                listrel.addView(rightbackarow);
                studentinfo_checkbox.addView(listrel);
                // System.out.println("----------------------student i-------" + i);
            }
            System.out.println("----------view created=------------");
        } else {
            Toast.makeText(this.mContext, "No Students Found", Toast.LENGTH_SHORT).show();
            this.studentinfo_checkbox.removeAllViews();
            this.relbtn.setVisibility(View.GONE);
            this.stdheader.setVisibility(View.GONE);
        }
    }

    private boolean getbufferstatus(String classid, String subjectid) {
        this.prevatt = BuildConfig.FLAVOR;
        Cursor cursor = this.mydatabase.rawQuery("select Time,Std_status from logs where ClassId ='" + classid + "' and SubjectId='" + subjectid + "' and Date='" + this.spinner3.getText().toString().trim() + "' and TeacherId='" + AppSettings.getuserId(this.mContext) + "'", null);
        if (cursor.getCount() <= 0) {
            this.isrowpresent = false;
        } else if (cursor.moveToFirst()) {
            do {
                this.isrowpresent = true;
                String datesaved = cursor.getString(cursor.getColumnIndex("Time"));
                this.prevatt = cursor.getString(cursor.getColumnIndex("Std_status"));
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                try {
                    Date date = format.parse(new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(new Date()));
                    Date date1 = format.parse(datesaved);
                    System.out.println("-----date--prev---" + date.getTime() + "---" + date1.getTime());
                    if (date.getTime() - date1.getTime() >= 1800000) {
                        System.out.println("----------------true");
                        return true;
                    }
                    System.out.println("----------------false");
                    return false;
                } catch (ParseException e) {
                    e.printStackTrace();
                    if (!cursor.moveToNext()) {
                    }
                }
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            cursor.close();
        }
        return false;
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
