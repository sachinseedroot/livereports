package seedroot.attendance.com.myattendanceap.Adapter;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import seedroot.attendance.com.myattendanceap.Models.PendingListModel;
import seedroot.attendance.com.myattendanceap.Models.StudentListModel;
import seedroot.attendance.com.myattendanceap.R;
import seedroot.attendance.com.myattendanceap.Utils.AppSettings;

public class StudentListAdapter extends RecyclerView
        .Adapter<StudentListAdapter
        .DataObjectHolder> {

    private Typeface typefaceFontAws;
    private ArrayList<StudentListModel> mDataset;
    private static MyClickListener myClickListener;
    Context con;
    private ProgressDialog progDailog;
    Activity activity2;

    public class DataObjectHolder extends RecyclerView.ViewHolder
            implements View
            .OnClickListener {

        TextView roll;
        TextView name;
        TextView status;
        TextView uid;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public DataObjectHolder(View itemView) {
            super(itemView);

            roll = (TextView) itemView.findViewById(R.id.std_rec_roll);
            name = (TextView) itemView.findViewById(R.id.std_name_roll);
            status = (TextView) itemView.findViewById(R.id.std_status);
            uid = (TextView) itemView.findViewById(R.id.std_id);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(final View v) {


        }


    }

    public void setOnItemClickListener(MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public StudentListAdapter(ArrayList<StudentListModel> myDataset, Context cnt, Typeface typeface2, Activity activity) {
        con = cnt;
        mDataset = myDataset;
        typefaceFontAws = typeface2;
        activity2 = activity;

    }

    @Override
    public DataObjectHolder onCreateViewHolder(ViewGroup parent,
                                               int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student_list, parent, false);

        DataObjectHolder dataObjectHolder = new DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(DataObjectHolder holder, int position) {

        holder.roll.setText("Roll No. "+mDataset.get(position).roll_number);
        holder.name.setText(mDataset.get(position).name);

        holder.uid.setText("User Id: "+mDataset.get(position).user_id);
        if (mDataset.get(position).present.contains("Present")) {
            holder.status.setTextColor(con.getResources().getColor(R.color.green));

        } else {
            holder.status.setTextColor(con.getResources().getColor(R.color.red));
        }
        holder.status.setText(mDataset.get(position).present);

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

    public void loaddatatoserver(String stdstatuss, String classids, String subjectids, final int position, final Button btnsubmit2) {
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
                                    btnsubmit2.setBackgroundColor(activity2.getResources().getColor(R.color.green));
                                    btnsubmit2.setText(activity2.getResources().getString(R.string.rightcheck) + " Submitted");
                                    btnsubmit2.setTypeface(typefaceFontAws);

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