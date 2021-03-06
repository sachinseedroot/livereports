package seedroot.attendance.com.myattendanceap.Adapter;

import android.animation.LayoutTransition;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import seedroot.attendance.com.myattendanceap.Fragments.AttendenceForTeacher;
import seedroot.attendance.com.myattendanceap.Fragments.HomeFragment;
import seedroot.attendance.com.myattendanceap.Fragments.PendingstudentlistFragment;
import seedroot.attendance.com.myattendanceap.Models.CalendarModel;
import seedroot.attendance.com.myattendanceap.Models.PendingListModel;
import seedroot.attendance.com.myattendanceap.R;
import seedroot.attendance.com.myattendanceap.Utils.AppSettings;

public class PendingListAdapter extends RecyclerView
        .Adapter<PendingListAdapter
        .DataObjectHolder> {

    private Typeface typefaceFontAws;
    private ArrayList<PendingListModel> mDataset;
    private static MyClickListener myClickListener;
    Context con;
    private ProgressDialog progDailog;
    Activity activity2;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        TextView classname;
        TextView subjectname;
        Button btnsubmit;
        TextView time;
        TextView rec_viewattendance;
        TextView rec_date_pending;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public DataObjectHolder(View itemView) {
            super(itemView);

            classname = (TextView) itemView.findViewById(R.id.rec_classpending);
            subjectname = (TextView) itemView.findViewById(R.id.rec_subject);
            btnsubmit = (Button) itemView.findViewById(R.id.rec_btn_submit_pending);
            time = (TextView) itemView.findViewById(R.id.rec_time);
            rec_viewattendance = (TextView) itemView.findViewById(R.id.rec_viewattendance);
            rec_date_pending = (TextView) itemView.findViewById(R.id.rec_date_pending);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {
            if (isNetworkConnected() == true) {
                new AlertDialog.Builder(activity2)
                        .setTitle("Attention !")
                        .setMessage("Do you wish to upload ?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {


                                String classid = mDataset.get(getAdapterPosition()).classid;
                                String subjectid = mDataset.get(getAdapterPosition()).subjecyid;
                                String stdstatus = mDataset.get(getAdapterPosition()).std_status;
                                String date = mDataset.get(getAdapterPosition()).date;
                                System.out.println("--------" + classid);
                                System.out.println("--------" + subjectid);
                                System.out.println("--------" + stdstatus);
                                System.out.println("--------" + date);

                                loaddatatoserver(stdstatus,classid,subjectid,getAdapterPosition());
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();


            } else {
                Toast.makeText(con, "Check Internet Connection.", Toast.LENGTH_SHORT).show();
            }
        }


    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public PendingListAdapter(ArrayList<PendingListModel> myDataset, Context cnt, Typeface typeface2, Activity activity) {
        con = cnt;
        mDataset = myDataset;
        typefaceFontAws = typeface2;
        activity2 = activity;

    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.pendinglistitem, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, final int position) {

        holder.classname.setText(mDataset.get(position).classname);
        holder.subjectname.setText(mDataset.get(position).subject);
        holder.time.setText(mDataset.get(position).time);
        holder.rec_date_pending.setText(mDataset.get(position).date);
        boolean isAttUploaded = mDataset.get(position).status;
        if (isAttUploaded == true) {
            holder.btnsubmit.setBackgroundColor(con.getResources().getColor(R.color.green));
            holder.btnsubmit.setText(con.getResources().getString(R.string.rightcheck) + " Submitted");
            holder.btnsubmit.setTypeface(typefaceFontAws);
        } else {
            holder.btnsubmit.setBackgroundColor(con.getResources().getColor(R.color.red));
            holder.btnsubmit.setText("Pending");
            holder.btnsubmit.setTypeface(typefaceFontAws);
            holder.btnsubmit.setClickable(false);
        }
        holder.rec_viewattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("name",AppSettings.getusername(con));
                bundle.putString("date",mDataset.get(position).date);
                bundle.putString("classtec",mDataset.get(position).classname);
                bundle.putString("subject",mDataset.get(position).subject);
                bundle.putString("jsonarray",mDataset.get(position).std_status.toString());
                PendingstudentlistFragment pendingstudentlistFragment = new PendingstudentlistFragment();

                pendingstudentlistFragment.setArguments(bundle);
                final FragmentTransaction ft = activity2.getFragmentManager().beginTransaction();
                ft.add(R.id.container, pendingstudentlistFragment, "NewFragmentTag");
                ft.commit();
            }
        });

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
            progDailog = new ProgressDialog(activity2);
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


    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) con.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    public void loaddatatoserver(String stdstatuss, String classids, String subjectids, final int position) {
        JSONArray toServerarrJ = new JSONArray();
        try {
            JSONArray fromdbJ = new JSONArray(stdstatuss);
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
                newJ.put("roll_number", jsonObject.optString("roll_number"));
                toServerarrJ.put(newJ);
            }

                if (toServerarrJ != null && toServerarrJ.length() > 0) {

                    final JSONObject jsonobject_one = new JSONObject();
                    jsonobject_one.put("subject", subjectids);
                    jsonobject_one.put("class", classids);
                    jsonobject_one.put("users", toServerarrJ);
                    JSONObject userjson = new JSONObject();
                    userjson.put("user_id",AppSettings.getuserId(con));
                    jsonobject_one.put("created_by",userjson);
                    System.out.println("------------json-------"+jsonobject_one.toString());
                    loadprogressbar();
                    RequestQueue queue = Volley.newRequestQueue(activity2);
                    JsonObjectRequest req22 = new JsonObjectRequest(Request.Method.POST, "http://63.142.250.106/attendance/save", jsonobject_one,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {

                                    System.out.println("----------response------" + response.toString());
                                    String dateInString = new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(new Date());
                                    JSONObject jsonObject = response;
                                    String id = jsonObject.optString("id");
                                    String subjectid = jsonObject.optString("subject");
                                    String classid = jsonObject.optString("class");
                                    String query = "update logs set UploadedId='" + id + "',Uploaded='True', Time='"+dateInString+"' where SubjectId='" + subjectid + "' and ClassId='" + classid + "'";
                                    System.out.println("-----------responsequert---" + query);
                                    SQLiteDatabase mydatabase =  activity2.openOrCreateDatabase(AppSettings.getdburl(activity2), Context.MODE_PRIVATE, null);
                                    mydatabase.execSQL(query);
                                    stoploadprogress();
                                    Toast.makeText(activity2, "Successfully Uploaded.", Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                }
                            }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            System.out.println("----------error------" + error.getMessage());
                            stoploadprogress();


                        }
                    });
                    req22.setRetryPolicy(new DefaultRetryPolicy(
                            45000,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    queue.add(req22);
                }else {
                    Toast.makeText(activity2, "Failed to Upload.", Toast.LENGTH_SHORT).show();
                }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}