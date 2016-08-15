//package seedroot.attendance.com.myattendanceap.Fragments;
//
//import android.app.AlertDialog.Builder;
//import android.app.Fragment;
//import android.app.FragmentTransaction;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.graphics.Typeface;
//import android.net.ConnectivityManager;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.AdapterView.OnItemSelectedListener;
//import android.widget.ArrayAdapter;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.CompoundButton.OnCheckedChangeListener;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.RelativeLayout.LayoutParams;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//import com.android.volley.DefaultRetryPolicy;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response.ErrorListener;
//import com.android.volley.Response.Listener;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonArrayRequest;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.Volley;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//import seedroot.attendance.com.myattendanceap.BuildConfig;
//import seedroot.attendance.com.myattendanceap.Utils.AppSettings;
//import seedroot.attendance.com.myattendanceap.Utils.TypeFaceHelper;
//
//public class AttendenceForTeachers extends Fragment {
//    boolean changeindatetime;
//    CheckBox checkall;
//    int chr;
//    String[] classids;
//    int cmin;
//    int cnt;
//    String ctime;
//    boolean isrowpresent;
//    boolean isupdate;
//    JSONArray jsonArraySpinenr;
//    Context mContext;
//    SQLiteDatabase mydatabase;
//    String prevatt;
//    ProgressDialog progDailog;
//    RelativeLayout relbtn;
//    View rootview;
//    Spinner spinner1;
//    Spinner spinner2;
//    TextView spinner3;
//    RelativeLayout stdheader;
//    LinearLayout studentinfo_checkbox;
//    RelativeLayout svrel;
//    String uid;
//
//    /* renamed from: seedroot.attendance.com.myattendanceap.Fragments.AttendenceForTeachers.1 */
//    class C02761 implements OnItemSelectedListener {
//        C02761() {
//        }
//
//        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//            if (AttendenceForTeachers.this.spinner1.getSelectedItem().toString().equals("Select Class")) {
//                AttendenceForTeachers.this.spinner2.setVisibility(8);
//                AttendenceForTeachers.this.stdheader.setVisibility(8);
//                return;
//            }
//            JSONObject jsonObject;
//            AttendenceForTeachers.this.spinner2.setVisibility(0);
//            List<String> categories2 = new ArrayList();
//            String classes2 = AppSettings.getusersubject(AttendenceForTeachers.this.mContext);
//            String uid = BuildConfig.FLAVOR;
//            categories2.add("Select Subject");
//            String classname = AttendenceForTeachers.this.spinner1.getSelectedItem().toString();
//            String div = BuildConfig.FLAVOR;
//            if (classname.contains("-")) {
//                String[] splitstring = classname.split("-");
//                classname = splitstring[0];
//                div = splitstring[1];
//            }
//            if (AttendenceForTeachers.this.jsonArraySpinenr.length() > 0) {
//                for (int m = 0; m < AttendenceForTeachers.this.jsonArraySpinenr.length(); m++) {
//                    jsonObject = AttendenceForTeachers.this.jsonArraySpinenr.optJSONObject(m);
//                    if (classname.equals(jsonObject.optString("name")) && div.equals(jsonObject.optString("div"))) {
//                        uid = jsonObject.optString("id");
//                    }
//                }
//            }
//            try {
//                JSONArray jsonArray = new JSONArray(classes2);
//                if (jsonArray.length() > 0) {
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        jsonObject = jsonArray.getJSONObject(i);
//                        if (!TextUtils.isEmpty(uid) && uid.equals(jsonObject.optString("classID").toString())) {
//                            categories2.add(jsonObject.optString("name"));
//                            System.out.println("---classid" + jsonObject.optString("class_id").toString());
//                        }
//                    }
//                } else {
//                    Toast.makeText(AttendenceForTeachers.this.mContext, "Failed to load subject", 0).show();
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            AttendenceForTeachers.this.spinner2.setAdapter(new ArrayAdapter(AttendenceForTeachers.this.getActivity().getApplicationContext(), C0300R.layout.simpletextview, categories2));
//        }
//
//        public void onNothingSelected(AdapterView<?> adapterView) {
//        }
//    }
//
//    /* renamed from: seedroot.attendance.com.myattendanceap.Fragments.AttendenceForTeachers.2 */
//    class C02772 implements OnItemSelectedListener {
//        C02772() {
//        }
//
//        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//            if (AttendenceForTeachers.this.spinner2.getSelectedItem().toString().equals("Select Subject")) {
//                AttendenceForTeachers.this.svrel.setVisibility(8);
//                AttendenceForTeachers.this.stdheader.setVisibility(8);
//                return;
//            }
//            AttendenceForTeachers.this.svrel.setVisibility(0);
//            AttendenceForTeachers.this.stdheader.setVisibility(0);
//            AttendenceForTeachers.this.relbtn.setVisibility(0);
//            String classname = AttendenceForTeachers.this.spinner1.getSelectedItem().toString();
//            String subjectname = AttendenceForTeachers.this.spinner2.getSelectedItem().toString();
//            AttendenceForTeachers.this.studentinfo_checkbox.removeAllViews();
//            AttendenceForTeachers.this.createdb(classname, subjectname);
//        }
//
//        public void onNothingSelected(AdapterView<?> adapterView) {
//        }
//    }
//
//    /* renamed from: seedroot.attendance.com.myattendanceap.Fragments.AttendenceForTeachers.3 */
//    class C02783 implements OnClickListener {
//        C02783() {
//        }
//
//        public void onClick(View v) {
//            FragmentTransaction ft = AttendenceForTeachers.this.getFragmentManager().beginTransaction();
//            ft.replace(C0300R.id.container, new HomeFragment(), "NewFragmentTag");
//            ft.commit();
//        }
//    }
//
//    /* renamed from: seedroot.attendance.com.myattendanceap.Fragments.AttendenceForTeachers.4 */
//    class C02834 implements OnClickListener {
//
//        /* renamed from: seedroot.attendance.com.myattendanceap.Fragments.AttendenceForTeachers.4.1 */
//        class C02791 implements DialogInterface.OnClickListener {
//            C02791() {
//            }
//
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        }
//
//        /* renamed from: seedroot.attendance.com.myattendanceap.Fragments.AttendenceForTeachers.4.2 */
//        class C02802 implements DialogInterface.OnClickListener {
//            final /* synthetic */ String val$date;
//            final /* synthetic */ String val$dateInString;
//
//            C02802(String str, String str2) {
//                this.val$date = str;
//                this.val$dateInString = str2;
//            }
//
//            public void onClick(DialogInterface dialog, int which) {
//                AttendenceForTeachers.this.isrowpresent = false;
//                AttendenceForTeachers.this.builtupdate(this.val$date, this.val$dateInString);
//            }
//        }
//
//        /* renamed from: seedroot.attendance.com.myattendanceap.Fragments.AttendenceForTeachers.4.3 */
//        class C02813 implements DialogInterface.OnClickListener {
//            C02813() {
//            }
//
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        }
//
//        /* renamed from: seedroot.attendance.com.myattendanceap.Fragments.AttendenceForTeachers.4.4 */
//        class C02824 implements DialogInterface.OnClickListener {
//            final /* synthetic */ String val$date;
//            final /* synthetic */ String val$dateInString;
//
//            C02824(String str, String str2) {
//                this.val$date = str;
//                this.val$dateInString = str2;
//            }
//
//            public void onClick(DialogInterface dialog, int which) {
//                AttendenceForTeachers.this.builtupdate(this.val$date, this.val$dateInString);
//            }
//        }
//
//        C02834() {
//        }
//
//        public void onClick(View v) {
//            AttendenceForTeachers.this.cnt = 0;
//            String date = AttendenceForTeachers.this.spinner3.getText().toString().trim();
//            String classname = AttendenceForTeachers.this.spinner1.getSelectedItem().toString();
//            String subjectname = AttendenceForTeachers.this.spinner2.getSelectedItem().toString();
//            String dateInString = new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(new Date());
//            if (AttendenceForTeachers.this.getbufferstatus(classname, subjectname)) {
//                new Builder(AttendenceForTeachers.this.getActivity()).setTitle("Attention !").setMessage("Time Over. Is this a new class?").setPositiveButton(17039379, new C02802(date, dateInString)).setNegativeButton(17039369, new C02791()).setIcon(17301543).show();
//            } else {
//                new Builder(AttendenceForTeachers.this.getActivity()).setTitle("Save Attendance !").setMessage("Are you sure you want to save this entry?").setPositiveButton(17039379, new C02824(date, dateInString)).setNegativeButton(17039369, new C02813()).setIcon(17301543).show();
//            }
//        }
//    }
//
//    /* renamed from: seedroot.attendance.com.myattendanceap.Fragments.AttendenceForTeachers.5 */
//    class C02845 implements OnCheckedChangeListener {
//        C02845() {
//        }
//
//        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            if (AttendenceForTeachers.this.studentinfo_checkbox.getChildCount() > 0) {
//                for (int i = 0; i < AttendenceForTeachers.this.studentinfo_checkbox.getChildCount(); i++) {
//                    CheckBox checkBox = (CheckBox) ((RelativeLayout) AttendenceForTeachers.this.studentinfo_checkbox.getChildAt(i)).getChildAt(1);
//                    if (AttendenceForTeachers.this.checkall.isChecked()) {
//                        checkBox.setChecked(true);
//                    } else {
//                        checkBox.setChecked(false);
//                    }
//                }
//            }
//        }
//    }
//
//    /* renamed from: seedroot.attendance.com.myattendanceap.Fragments.AttendenceForTeachers.6 */
//    class C04126 implements Listener<JSONObject> {
//        final /* synthetic */ JSONArray val$jsonArray;
//
//        C04126(JSONArray jSONArray) {
//            this.val$jsonArray = jSONArray;
//        }
//
//        public void onResponse(JSONObject response) {
//            System.out.println("----------response------" + response.toString());
//            JSONObject jsonObject = response;
//            String id = jsonObject.optString("id");
//            String subjectid = jsonObject.optString("subject");
//            String query = "update logs set UploadedId='" + id + "',Uploaded='True',Std_status='" + this.val$jsonArray.toString() + "' where SubjectId='" + subjectid + "' and ClassId='" + jsonObject.optString("class") + "'";
//            System.out.println("-----------responsequert---" + query);
//            AttendenceForTeachers.this.mydatabase.execSQL(query);
//            AttendenceForTeachers.this.stoploadprogress();
//            Toast.makeText(AttendenceForTeachers.this.mContext, "Successfully Uploaded.", 0).show();
//            FragmentTransaction ft = AttendenceForTeachers.this.getFragmentManager().beginTransaction();
//            ft.replace(C0300R.id.container, new HomeFragment(), "NewFragmentTag");
//            ft.commit();
//        }
//    }
//
//    /* renamed from: seedroot.attendance.com.myattendanceap.Fragments.AttendenceForTeachers.7 */
//    class C04137 implements ErrorListener {
//        C04137() {
//        }
//
//        public void onErrorResponse(VolleyError error) {
//            System.out.println("----------error------" + error.getMessage());
//            AttendenceForTeachers.this.stoploadprogress();
//        }
//    }
//
//    /* renamed from: seedroot.attendance.com.myattendanceap.Fragments.AttendenceForTeachers.8 */
//    class C04148 implements Listener<JSONArray> {
//        final /* synthetic */ JSONArray val$jsonArray;
//
//        C04148(JSONArray jSONArray) {
//            this.val$jsonArray = jSONArray;
//        }
//
//        public void onResponse(JSONArray response) {
//            System.out.println("----------response------" + response.toString());
//            JSONArray jsonArray1 = response;
//            if (jsonArray1.length() > 0) {
//                JSONObject jsonObject = jsonArray1.optJSONObject(0);
//                String id = jsonObject.optString("id");
//                String subjectid = jsonObject.optString("subject");
//                String query = "update logs set UploadedId='" + id + "',Uploaded='True',Std_status='" + this.val$jsonArray.toString() + "' where SubjectId='" + subjectid + "' and ClassId='" + jsonObject.optString("class") + "'";
//                System.out.println("-----------responsequert---" + query);
//                AttendenceForTeachers.this.mydatabase.execSQL(query);
//                AttendenceForTeachers.this.stoploadprogress();
//                Toast.makeText(AttendenceForTeachers.this.mContext, "Successfully Uploaded.", 0).show();
//                FragmentTransaction ft = AttendenceForTeachers.this.getFragmentManager().beginTransaction();
//                ft.replace(C0300R.id.container, new HomeFragment(), "NewFragmentTag");
//                ft.commit();
//            }
//        }
//    }
//
//    /* renamed from: seedroot.attendance.com.myattendanceap.Fragments.AttendenceForTeachers.9 */
//    class C04159 implements ErrorListener {
//        C04159() {
//        }
//
//        public void onErrorResponse(VolleyError error) {
//            System.out.println("----------error------" + error.getMessage());
//            AttendenceForTeachers.this.stoploadprogress();
//        }
//    }
//
//    public AttendenceForTeachers() {
//        this.isupdate = false;
//        this.cnt = 0;
//        this.changeindatetime = false;
//        this.isrowpresent = false;
//        this.prevatt = BuildConfig.FLAVOR;
//    }
//
//    @Nullable
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        this.rootview = inflater.inflate(C0300R.layout.attenforteacher, container, false);
//        return this.rootview;
//    }
//
//    public void onViewCreated(View view, Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//        this.mContext = getActivity().getApplicationContext();
//        this.mydatabase = this.mContext.openOrCreateDatabase(AppSettings.getdburl(this.mContext), 0, null);
//        this.studentinfo_checkbox = (LinearLayout) view.findViewById(C0300R.id.studentinfo_checkbox);
//        this.checkall = (CheckBox) view.findViewById(C0300R.id.checkall);
//        this.spinner1 = (Spinner) view.findViewById(C0300R.id.ddpat1);
//        this.spinner2 = (Spinner) view.findViewById(C0300R.id.ddpat12);
//        this.spinner3 = (TextView) view.findViewById(C0300R.id.ddpat13);
//        this.svrel = (RelativeLayout) view.findViewById(C0300R.id.scrollviewrel);
//        this.relbtn = (RelativeLayout) view.findViewById(C0300R.id.btnsub);
//        this.stdheader = (RelativeLayout) view.findViewById(C0300R.id.stdheader);
//        this.jsonArraySpinenr = new JSONArray();
//        List<String> categories = new ArrayList();
//        String classes = AppSettings.getuserclass(this.mContext);
//        categories.add("Select Class");
//        try {
//            JSONArray jsonArray = new JSONArray(classes);
//            if (jsonArray.length() > 0) {
//                this.classids = new String[jsonArray.length()];
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = jsonArray.getJSONObject(i);
//                    JSONObject newjsonob = new JSONObject();
//                    categories.add(jsonObject.optString("name") + "-" + jsonObject.optString("division"));
//                    newjsonob.put("id", jsonObject.optString("id"));
//                    newjsonob.put("name", jsonObject.optString("name"));
//                    newjsonob.put("div", jsonObject.optString("division"));
//                    this.jsonArraySpinenr.put(newjsonob);
//                    this.classids[i] = jsonObject.optString("id");
//                    System.out.println("----------classid---" + jsonObject.optString("id"));
//                }
//            } else {
//                Toast.makeText(this.mContext, "Failed to load class", 0).show();
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        this.spinner1.setAdapter(new ArrayAdapter(getActivity().getApplicationContext(), C0300R.layout.simpletextview, categories));
//        this.spinner1.setVisibility(0);
//        this.spinner2.setVisibility(8);
//        this.svrel.setVisibility(8);
//        this.relbtn.setVisibility(8);
//        this.stdheader.setVisibility(8);
//        this.spinner3.setText(new SimpleDateFormat("dd-MM-yyyy").format(new Date()));
//        Calendar c = Calendar.getInstance();
//        this.chr = c.get(10);
//        this.cmin = c.get(13);
//        this.ctime = Integer.toString(this.chr) + ":" + Integer.toString(this.cmin);
//        System.out.println("----------" + Integer.toString(this.chr) + ":" + Integer.toString(this.cmin));
//        this.spinner1.setOnItemSelectedListener(new C02761());
//        this.spinner2.setOnItemSelectedListener(new C02772());
//        ((ImageButton) this.rootview.findViewById(C0300R.id.backattea)).setOnClickListener(new C02783());
//        overrideFonts(getActivity().getApplicationContext(), (LinearLayout) this.rootview.findViewById(C0300R.id.parentattteach));
//        this.relbtn.setOnClickListener(new C02834());
//        this.checkall.setOnCheckedChangeListener(new C02845());
//    }
//
//    public static void overrideFonts(Context context, View v) {
//        Typeface typeface = Typeface.createFromAsset(context.getAssets(), TypeFaceHelper.FONT_AWESOME);
//        try {
//            if (v instanceof ViewGroup) {
//                ViewGroup vg = (ViewGroup) v;
//                for (int i = 0; i < vg.getChildCount(); i++) {
//                    overrideFonts(context, vg.getChildAt(i));
//                }
//            } else if (v instanceof TextView) {
//                ((TextView) v).setTypeface(typeface);
//            }
//        } catch (Exception e) {
//        }
//    }
//
//    public void builtupdate(String date, String time) {
//        int i;
//        JSONArray jSONArray;
//        JSONArray jsonArray = new JSONArray();
//        for (i = 0; i < this.studentinfo_checkbox.getChildCount(); i++) {
//            RelativeLayout relativeLayout = (RelativeLayout) this.studentinfo_checkbox.getChildAt(i);
//            TextView textViewRollno = (TextView) relativeLayout.getChildAt(0);
//            CheckBox checkBox = (CheckBox) relativeLayout.getChildAt(1);
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("name", checkBox.getText().toString().trim());
//                jsonObject.put("user_id", checkBox.getTag().toString());
//                jsonObject.put("roll_number", textViewRollno.getText().toString());
//                if (checkBox.isChecked()) {
//                    jsonObject.put("present", "Present");
//                } else {
//                    jsonObject.put("present", "Absent");
//                }
//                jsonArray.put(jsonObject);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        String subject_Id = null;
//        try {
//            jSONArray = new JSONArray(AppSettings.getusersubject(this.mContext));
//            for (i = 0; i < jSONArray.length(); i++) {
//                jsonObject = jSONArray.optJSONObject(i);
//                if (jsonObject.optString("name").equalsIgnoreCase(this.spinner2.getSelectedItem().toString())) {
//                    subject_Id = jsonObject.optString("id");
//                }
//            }
//        } catch (JSONException e2) {
//            e2.printStackTrace();
//        }
//        String class_id = null;
//        try {
//            jSONArray = new JSONArray(AppSettings.getuserclass(this.mContext));
//            for (i = 0; i < jSONArray.length(); i++) {
//                JSONObject jsonObject2 = jSONArray.optJSONObject(i);
//                String obj = this.spinner1.getSelectedItem().toString();
//                String[] spin = spi.split("-");
//                if (jsonObject2.optString("name").equalsIgnoreCase(spin[0])) {
//                    if (jsonObject2.optString("division").equalsIgnoreCase(spin[1])) {
//                        class_id = jsonObject2.optString("id");
//                    }
//                }
//            }
//        } catch (JSONException e22) {
//            e22.printStackTrace();
//        }
//        if (this.isrowpresent) {
//            System.out.println("---------------row present---------------");
//            String query = "update logs set Uploaded='false',Std_status='" + jsonArray.toString() + "' where Class ='" + this.spinner1.getSelectedItem().toString() + "' and Subject='" + this.spinner2.getSelectedItem().toString() + "' and Date='" + this.spinner3.getText().toString().trim() + "'";
//            System.out.println("-----------responsequert---" + query);
//            this.mydatabase.execSQL(query);
//        } else {
//            this.mydatabase.execSQL("insert into logs(TeacherId ,Class ,Subject ,Std_status ,Date ,Time ,Uploaded,ClassId, SubjectId ) values('" + AppSettings.getuserId(this.mContext) + "','" + this.spinner1.getSelectedItem().toString() + "','" + this.spinner2.getSelectedItem().toString() + "','" + jsonArray.toString() + "','" + date + "','" + time + "','false','" + class_id + "','" + subject_Id + "')");
//        }
//        if (isNetworkConnected()) {
//            loadprogressbar();
//            String qu = "select * from logs where Class ='" + this.spinner1.getSelectedItem().toString() + "' and Subject='" + this.spinner2.getSelectedItem().toString() + "' and Date='" + this.spinner3.getText().toString().trim() + "'";
//            System.out.println("------qu---" + qu);
//            Cursor cursor = this.mydatabase.rawQuery(qu, null);
//            if (cursor.getCount() > 0) {
//                if (cursor.moveToFirst()) {
//                    do {
//                        int pullpost;
//                        String url;
//                        String uploaded = cursor.getString(cursor.getColumnIndex("Uploaded"));
//                        if (!this.isrowpresent) {
//                            pullpost = 1;
//                            url = "http://63.142.250.106/attendance/save";
//                            System.out.println("---------------------------submiiting first");
//                        } else if (this.isrowpresent) {
//                            pullpost = 2;
//                            url = "http://63.142.250.106/attendance/" + cursor.getString(cursor.getColumnIndex("UploadedId"));
//                            System.out.println("---------------------------submiiting again--" + url);
//                        } else {
//                            pullpost = 1;
//                            url = "http://63.142.250.106/attendance/save";
//                        }
//                        JSONArray toServerarrJ = new JSONArray();
//                        String sub = cursor.getString(cursor.getColumnIndex("SubjectId"));
//                        String clas = cursor.getString(cursor.getColumnIndex("ClassId"));
//                        try {
//                            jSONArray = new JSONArray(cursor.getString(cursor.getColumnIndex("Std_status")));
//                            for (i = 0; i < jSONArray.length(); i++) {
//                                jsonObject = jSONArray.optJSONObject(i);
//                                JSONObject newJ = new JSONObject();
//                                if (jsonObject.optString("present").equalsIgnoreCase("Absent")) {
//                                    newJ.put("present", false);
//                                } else {
//                                    newJ.put("present", true);
//                                }
//                                String optString = jsonObject.optString("name");
//                                newJ.put("name", optString);
//                                optString = jsonObject.optString("user_id");
//                                newJ.put("user_id", optString);
//                                optString = jsonObject.optString("roll_number");
//                                newJ.put("roll_number", optString);
//                                toServerarrJ.put(newJ);
//                            }
//                        } catch (JSONException e222) {
//                            e222.printStackTrace();
//                        }
//                        if (toServerarrJ != null && toServerarrJ.length() > 0) {
//                            try {
//                                JSONObject jsonobject_one = new JSONObject();
//                                if (this.isrowpresent) {
//                                    jsonobject_one.put("id", cursor.getString(cursor.getColumnIndex("UploadedId")));
//                                }
//                                jsonobject_one.put("subject", sub);
//                                jsonobject_one.put("class", clas);
//                                jsonobject_one.put("users", toServerarrJ);
//                                JSONObject userjson = new JSONObject();
//                                userjson.put("user_id", AppSettings.getuserId(this.mContext));
//                                jsonobject_one.put("created_by", userjson);
//                                System.out.println("----giving---" + jsonobject_one.toString());
//                                if (this.isrowpresent) {
//                                    RequestQueue queueA = Volley.newRequestQueue(this.mContext);
//                                    JsonArrayRequest jsonArrayRequestA = new JsonArrayRequest(pullpost, url, jsonobject_one, new C04148(jsonArray), new C04159());
//                                    jsonArrayRequestA.setRetryPolicy(new DefaultRetryPolicy(45000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                                    queueA.add(jsonArrayRequestA);
//                                } else {
//                                    RequestQueue queue = Volley.newRequestQueue(this.mContext);
//                                    JsonObjectRequest req22 = new JsonObjectRequest(pullpost, url, jsonobject_one, new C04126(jsonArray), new C04137());
//                                    req22.setRetryPolicy(new DefaultRetryPolicy(45000, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
//                                    queue.add(req22);
//                                }
//                            } catch (JSONException e2222) {
//                                e2222.printStackTrace();
//                            }
//                        }
//                    } while (cursor.moveToNext());
//                }
//                cursor.close();
//                return;
//            }
//            Toast.makeText(this.mContext, "Failed to load Students", 0).show();
//            return;
//        }
//        Toast.makeText(this.mContext, "No internet, Data Saved locally. ", 1).show();
//    }
//
//    public void loadprogressbar() {
//        if (this.progDailog == null) {
//            this.progDailog = new ProgressDialog(getActivity());
//        }
//        if (!this.progDailog.isShowing()) {
//            this.progDailog.setMessage("Saving, Please wait...");
//            this.progDailog.setProgressStyle(0);
//            this.progDailog.setCancelable(true);
//            this.progDailog.show();
//        }
//    }
//
//    public void stoploadprogress() {
//        if (this.progDailog.isShowing()) {
//            this.progDailog.dismiss();
//        }
//    }
//
//    public void createdb(String classname, String subjectname) {
//        JSONArray jSONArray;
//        int i;
//        boolean isbuffertimeover = getbufferstatus(classname, subjectname);
//        this.relbtn.setVisibility(0);
//        ArrayList<String> studentname = new ArrayList();
//        ArrayList<String> studentrollno = new ArrayList();
//        ArrayList<String> studentnametag = new ArrayList();
//        this.studentinfo_checkbox.removeAllViews();
//        Cursor cursor = this.mydatabase.rawQuery("select * from logins where Role ='student' order by cast(RollNo as int)  asc", null);
//        if (cursor.getCount() > 0) {
//            if (cursor.moveToFirst()) {
//                do {
//                    String cl = cursor.getString(cursor.getColumnIndex("Class"));
//                    String rolno = cursor.getString(cursor.getColumnIndex("RollNo"));
//                    try {
//                        jSONArray = new JSONArray(cl);
//                        if (jSONArray.length() > 0) {
//                            for (i = 0; i < jSONArray.length(); i++) {
//                                JSONObject jsonObject = jSONArray.optJSONObject(i);
//                                String[] spin = this.spinner1.getSelectedItem().toString().split("-");
//                                String aa = jsonObject.optString("name");
//                                String dd = jsonObject.optString("division");
//                                if (aa.equalsIgnoreCase(spin[0])) {
//                                    if (dd.equalsIgnoreCase(spin[1])) {
//                                        try {
//                                            jSONArray = new JSONArray(cursor.getString(cursor.getColumnIndex("Subjects")));
//                                            if (jSONArray.length() > 0) {
//                                                for (int i2 = 0; i2 < jSONArray.length(); i2++) {
//                                                    JSONObject jsonObject2 = jSONArray.optJSONObject(i2);
//                                                    String spi2 = this.spinner2.getSelectedItem().toString();
//                                                    String aa2 = jsonObject2.optString("name");
//                                                    System.out.println("---spin---sub--" + spi2 + "--" + aa2);
//                                                    if (aa2.equalsIgnoreCase(spi2) && !TextUtils.isEmpty(rolno)) {
//                                                        if (!rolno.equals("null")) {
//                                                            studentname.add(cursor.getString(cursor.getColumnIndex("Fullname")));
//                                                            studentrollno.add(rolno);
//                                                            studentnametag.add(cursor.getString(cursor.getColumnIndex("Username")));
//                                                        }
//                                                    }
//                                                }
//                                            }
//                                        } catch (JSONException e) {
//                                            e.printStackTrace();
//                                        }
//                                    } else {
//                                        continue;
//                                    }
//                                }
//                            }
//                        }
//                    } catch (JSONException e2) {
//                        e2.printStackTrace();
//                    }
//                } while (cursor.moveToNext());
//            }
//            cursor.close();
//        } else {
//            Toast.makeText(this.mContext, "Failed to load Students", 0).show();
//            this.studentinfo_checkbox.removeAllViews();
//            this.relbtn.setVisibility(8);
//        }
//        if (studentname.size() > 0) {
//            System.out.println("----------------------student size-------" + studentname.size());
//            for (i = 0; i < studentname.size(); i++) {
//                View relativeLayout = new RelativeLayout(this.mContext);
//                relativeLayout.setLayoutParams(new LayoutParams(-1, -2));
//                relativeLayout.setPadding(15, 0, 15, 0);
//                ViewGroup.LayoutParams layoutParams = new LayoutParams(-2, -2);
//                layoutParams.addRule(11);
//                layoutParams = new LayoutParams(-2, -2);
//                layoutParams.addRule(9);
//                CheckBox checkBox = new CheckBox(this.mContext);
//                checkBox.setTag(studentnametag.get(i));
//                checkBox.setText((CharSequence) studentname.get(i));
//                checkBox.setTextColor(this.mContext.getResources().getColor(C0300R.color.black));
//                checkBox.setLayoutParams(layoutParams);
//                View textView = new TextView(this.mContext);
//                textView.setText((CharSequence) studentrollno.get(i));
//                textView.setTextSize(24.0f);
//                textView.setTextColor(this.mContext.getResources().getColor(C0300R.color.black));
//                textView.setLayoutParams(layoutParams);
//                if (!TextUtils.isEmpty(this.prevatt)) {
//                    try {
//                        jSONArray = new JSONArray(this.prevatt);
//                        if (jSONArray.length() > 0) {
//                            for (int k = 0; k < jSONArray.length(); k++) {
//                                jsonObject = jSONArray.optJSONObject(k);
//                                if (jsonObject.optString("name").equals(studentname.get(i))) {
//                                    if (jsonObject.optString("present").equals("Present")) {
//                                        checkBox.setChecked(true);
//                                    } else {
//                                        checkBox.setChecked(false);
//                                    }
//                                }
//                            }
//                        }
//                    } catch (JSONException e22) {
//                        e22.printStackTrace();
//                    }
//                }
//                relativeLayout.addView(textView);
//                relativeLayout.addView(checkBox);
//                this.studentinfo_checkbox.addView(relativeLayout);
//                System.out.println("----------------------student i-------" + i);
//            }
//            return;
//        }
//        Toast.makeText(this.mContext, "No Students Found", 0).show();
//        this.studentinfo_checkbox.removeAllViews();
//        this.relbtn.setVisibility(8);
//    }
//
//    private boolean getbufferstatus(String classname, String subjectname) {
//        this.prevatt = BuildConfig.FLAVOR;
//        Cursor cursor = this.mydatabase.rawQuery("select Time,Std_status from logs where Class ='" + classname + "' and Subject='" + subjectname + "' and Date='" + this.spinner3.getText().toString().trim() + "' and TeacherId='" + AppSettings.getuserId(this.mContext) + "'", null);
//        if (cursor.getCount() <= 0) {
//            this.isrowpresent = false;
//        } else if (cursor.moveToFirst()) {
//            do {
//                this.isrowpresent = true;
//                String datesaved = cursor.getString(cursor.getColumnIndex("Time"));
//                this.prevatt = cursor.getString(cursor.getColumnIndex("Std_status"));
//                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
//                try {
//                    Date date = format.parse(new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(new Date()));
//                    Date date1 = format.parse(datesaved);
//                    System.out.println("-----date--prev---" + date.getTime() + "---" + date1.getTime());
//                    if (date.getTime() - date1.getTime() >= 1800000) {
//                        System.out.println("----------------true");
//                        return true;
//                    }
//                    System.out.println("----------------false");
//                    return false;
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                    if (!cursor.moveToNext()) {
//                    }
//                }
//            } while (cursor.moveToNext());
//            cursor.close();
//        } else {
//            cursor.close();
//        }
//        return false;
//    }
//
//    private boolean isNetworkConnected() {
//        return ((ConnectivityManager) this.mContext.getSystemService("connectivity")).getActiveNetworkInfo() != null;
//    }
//}
