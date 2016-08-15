package seedroot.attendance.com.myattendanceap.Adapter;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v4.content.ContextCompat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.android.volley.toolbox.Volley;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import seedroot.attendance.com.myattendanceap.Models.CalendarModel;
import seedroot.attendance.com.myattendanceap.R;
import seedroot.attendance.com.myattendanceap.Utils.AppSettings;

public class CalendarRecyclerviewAdapter extends RecyclerView
        .Adapter<CalendarRecyclerviewAdapter
        .DataObjectHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private ArrayList<CalendarModel> mDataset;
    private static MyClickListener myClickListener;
    Context con;
    ProgressDialog progDailog;
    LinearLayout l1, l2, l3, l4, l5, l6;
    View addview;
    LayoutInflater lytinf;
    int recylNo, prevValue, currValue;
    String value = "";
    ArrayList<String> sundays;
    JSONArray jsonArrasubjects = null;
    int monthco=0;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {
        TextView label;
        RelativeLayout parentcalendar;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public DataObjectHolder(View itemView) {
            super(itemView);
            label = (TextView) itemView.findViewById(R.id.txt_no);
            parentcalendar = (RelativeLayout) itemView.findViewById(R.id.parentcalendar);
            LinearLayout layout = (LinearLayout) itemView.findViewById(R.id.relatt);
            layout.setBackground(con.getResources().getDrawable(R.drawable.tab));

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (isNetworkConnected() == true) {

                try {
                    jsonArrasubjects = new JSONArray(AppSettings.getusersubject(con));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                TextView txt = (TextView) v.findViewById(R.id.txt_no);
                String click = txt.getText().toString();
                if (!click.isEmpty()) {
                    String name = v.getParent().toString();
                    String[] parts = name.split("/");
                    String p1 = parts[1];
                    String id = p1.replace("}", "");//fetching recyclerview's id in a string
                    addview = lytinf.inflate(R.layout.infalte_view, null);

                    if (id.equals("recyclerview_week1")) {
                        if (l1.getChildCount() > 0) {
                            l1.removeAllViews();


                        } else {
                            //  l1.setLayoutTransition(transition);
                            l1.addView(addview, 0);
                            makevolleycall(click);
                        }


                    }
                    if (id.equals("recyclerview_week2")) {

                        if (l2.getChildCount() > 0) {
                            l2.removeAllViews();
                        } else {
                            //      l2.setLayoutTransition(transition);
                            l2.addView(addview, 0);
                            makevolleycall(click);
                        }
                    }
                    if (id.equals("recyclerview_week3")) {

                        if (l3.getChildCount() > 0) {
                            l3.removeAllViews();
                        } else {
                            //        l3.setLayoutTransition(transition);
                            l3.addView(addview, 0);
                            makevolleycall(click);
                        }
                    }
                    if (id.equals("recyclerview_week4")) {

                        if (l4.getChildCount() > 0) {
                            l4.removeAllViews();
                        } else {
                            //        l4.setLayoutTransition(transition);
                            l4.addView(addview, 0);
                            makevolleycall(click);
                        }
                    }
                    if (id.equals("recyclerview_week5")) {


                        if (l5.getChildCount() > 0) {
                            l5.removeAllViews();
                        } else {
                            //          l5.setLayoutTransition(transition);
                            l5.addView(addview, 0);
                            makevolleycall(click);
                        }
                    }
                    if (id.equals("recyclerview_week6")) {

                        if (l6.getChildCount() > 0) {
                            l6.removeAllViews();
                        } else {
                            //          l6.setLayoutTransition(transition);
                            l6.addView(addview, 0);
                            makevolleycall(click);
//                        getdate(clickedate,"sub1",ontv,"Subject One");
//                        getdate(clickedate,"sub2",twtv,"Subject two");
//                        getdate(clickedate, "sub3", thtv, "Subject three");
                        }
                    }

                }
            } else {
                Toast.makeText(con, "Check Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public CalendarRecyclerviewAdapter(ArrayList<CalendarModel> myDataset, Context cnt, LinearLayout ly1, LinearLayout ly2, LinearLayout ly3, LinearLayout ly4, LinearLayout ly5, LinearLayout ly6, LayoutInflater lyf, ArrayList<String> dates, int monthcal) {

        mDataset = myDataset;
        monthco = monthcal+1;
        con = cnt;
        l1 = ly1;
        l2 = ly2;
        l3 = ly3;
        l4 = ly4;
        l5 = ly5;
        l6 = ly6;
        lytinf = lyf;
        sundays = dates;
        if (AppSettings.getusername(con).contains("Mahesh Kale")) {
            value = "s1";
        }
        if (AppSettings.getusername(con).contains("Rahul Sharma")) {
            value = "s4";
        }
        if (AppSettings.getusername(con).contains("Vinod Mehra")) {
            value = "s2";
        }
        if (AppSettings.getusername(con).contains("Akash Singh")) {
            value = "s3";
        }

    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.caldendaritem, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {


        if (sundays.contains(mDataset.get(position).mText1)) {
            holder.parentcalendar.setBackgroundColor(con.getResources().getColor(R.color.divider));
            holder.parentcalendar.setBackground(con.getResources().getDrawable(R.drawable.tabsun));
        } else {
//            if(mDataset.get(position).mText1.equals("4") || mDataset.get(position).mText1.equals("14") || mDataset.get(position).mText1.equals("18") || mDataset.get(position).mText1.equals("28"))
//            {
//                holder.parentcalendar.setBackgroundColor(ContextCompat.getColor(con, R.color.red));
//                holder.parentcalendar.setBackground(ContextCompat.getDrawable(con, R.drawable.tabab));

//            }else{
            holder.parentcalendar.setBackgroundColor(con.getResources().getColor(R.color.green));
            holder.parentcalendar.setBackground(con.getResources().getDrawable(R.drawable.tab));
//            }

        }


        holder.label.setText(mDataset.get(position).mText1);


    }


    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public interface MyClickListener {
        public void onItemClick(int position, View v);
    }


    public void loadprogressbar() {
        if (progDailog == null) {
            progDailog = new ProgressDialog(con);
        }
        if (progDailog.isShowing() == false) {
            progDailog.setMessage("Loading, Please wait...");
            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progDailog.setCancelable(true);
            progDailog.show();
        }
    }

    public void stoploadprogress() {

        if (progDailog.isShowing() == true) {
            progDailog.dismiss();
        }

    }


    public void makevolleycall(String click) {
        final LinearLayout linearLayoutstatus = (LinearLayout) addview.findViewById(R.id.subjectsid);
        for(int k=0;k<jsonArrasubjects.length();k++){
            JSONObject jsonObjects = jsonArrasubjects.optJSONObject(k);
            String name = jsonObjects.optString("name");
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.addRule(RelativeLayout.CENTER_VERTICAL);
            TextView textView = new TextView(con);
            textView.setTextColor(con.getResources().getColor(android.R.color.darker_gray));
            textView.setText(name);
            System.out.println("-------------name-------"+name);
            textView.setLayoutParams(layoutParams);
            linearLayoutstatus.addView(textView);
        }
        final ProgressBar progressBar = (ProgressBar) addview.findViewById(R.id.loadingpsinnerattendance);
        progressBar.setVisibility(View.VISIBLE);
        int clickedDate = Integer.parseInt(click);

        currValue = clickedDate;
        String clickedate = Integer.toString(currValue) + "/"+Integer.toString(monthco)+"/2016";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        try {
            Date d1 = simpleDateFormat.parse(clickedate);
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            String date = simpleDateFormat1.format(d1);
            //Toast.makeText(con, clickedate + " -|- " + d1, Toast.LENGTH_SHORT).show();

            JSONObject dateJo = new JSONObject();
            try {
                dateJo.put("date", date);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            // loadprogressbar();
            String url = "http://63.142.250.106/attendance/view/" + AppSettings.getuserId(con) + "/date";
            System.out.println("------url----" + url);
            System.out.println("------json----" + dateJo.toString());
            final RequestQueue queue = Volley.newRequestQueue(con);
            final JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url, dateJo, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    progressBar.setVisibility(View.GONE);
                    System.out.println("---------------jsonarray-----" + response);
                    JSONArray result = response;
                    String subjectstatus = "";
                    if (result.length() > 0) {
                        LayoutTransition transition = new LayoutTransition();


                        for (int i = 0; i < result.length(); i++) {
                            if (jsonArrasubjects != null) {


                                JSONObject jsonObject = response.optJSONObject(i);
                                JSONObject subject = jsonObject.optJSONObject("subject");
                                String subjectname = subject.optString("name");
                                JSONArray jsonArray = jsonObject.optJSONArray("users");

                                if (jsonArray.length() > 0) {
                                    for (int j = 0; j < jsonArray.length(); j++) {
                                        JSONObject std_details = jsonArray.optJSONObject(j);
                                        if (std_details.optString("user_id").equals(AppSettings.getuserId(con))) {



                                            subjectstatus = subjectname + ": " + std_details.optBoolean("present");
                                            System.out.println("--------------status--------" + subjectstatus);
                                            int count = linearLayoutstatus.getChildCount();
                                            for(int m=0;m<count;m++){
                                                TextView textView=(TextView)linearLayoutstatus.getChildAt(m);
                                                if(textView.getText().toString().equals(subjectname)){
                                                    if (std_details.optBoolean("present") == true) {
                                                        textView.setText(subjectname+": Present");
                                                        textView.setTextColor(con.getResources().getColor(R.color.black));
                                                    } else {
                                                        textView.setText(subjectname+": Absent");
                                                        textView.setTextColor(con.getResources().getColor(R.color.red));
                                                    }
                                                }else{

                                                }
                                            }

                                        }

                                    }
                                }
                            }
                        }


                    } else {
                        Toast.makeText(con, "No record Found", Toast.LENGTH_SHORT).show();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println("---------------error-----" + error.getMessage());
                    Toast.makeText(con, "Failed to get record.", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            });
            jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(
                    45000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            queue.add(jsonArrayRequest);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//
    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
