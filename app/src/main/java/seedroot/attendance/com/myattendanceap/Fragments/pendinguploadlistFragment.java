package seedroot.attendance.com.myattendanceap.Fragments;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import seedroot.attendance.com.myattendanceap.Activities.MainActivity;
import seedroot.attendance.com.myattendanceap.Adapter.CalendarRecyclerviewAdapter;
import seedroot.attendance.com.myattendanceap.Adapter.ListviewBaseAdapterActivity;
import seedroot.attendance.com.myattendanceap.Adapter.PendingListAdapter;
import seedroot.attendance.com.myattendanceap.Models.PendingListModel;
import seedroot.attendance.com.myattendanceap.R;
import seedroot.attendance.com.myattendanceap.Utils.AppSettings;

/**
 * Created by sachin on 21/1/16.
 */
public class pendinguploadlistFragment extends Fragment {
    View rootview;
    Context mcontext;
    ProgressDialog progDailog;
    String urlUser = "http://63.142.250.106/attendance/save";
    RecyclerView recyclerViewpendintlist;
    TextView datepending;
    TextView pendingcount;
    TextView pendingname;
    ArrayList<PendingListModel> pendingListModels;
    Typeface typefaceAW;
    ImageButton backpressed;
    TextView pendingtxt,datewisetxt;
    private int year, month, day;
    RelativeLayout txt_date_rel,txt_pending_rel;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.pendinglist, container, false);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mcontext == null) {
            mcontext = getActivity().getApplicationContext();
        }
        recyclerViewpendintlist = (RecyclerView) rootview.findViewById(R.id.recyclerlist_pendinglist);
        pendingcount = (TextView) rootview.findViewById(R.id.pendingcount);
        datepending = (TextView) rootview.findViewById(R.id.pendingdate);
        pendingname = (TextView) rootview.findViewById(R.id.pendingname);
        backpressed = (ImageButton) rootview.findViewById(R.id.backpress);
        pendingtxt = (TextView) rootview.findViewById(R.id.txt_pending);
        datewisetxt = (TextView) rootview.findViewById(R.id.txt_date);
        txt_pending_rel = (RelativeLayout)rootview.findViewById(R.id.txt_pending_rel);
        txt_date_rel= (RelativeLayout)rootview.findViewById(R.id.txt_date_rel);
        init(2);
        backpressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, new HomeFragment(), "NewFragmentTag");
                ft.commit();
            }
        });

        pendingtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingtxt.setTextColor(mcontext.getResources().getColor(R.color.white));
                txt_pending_rel.setBackground(mcontext.getResources().getDrawable(R.drawable.roundercornerbuttonhollowsolid));

                datewisetxt.setTextColor(mcontext.getResources().getColor(R.color.colorPrimaryDark));
                txt_date_rel.setBackground(mcontext.getResources().getDrawable(R.drawable.roundercornerbuttonhollow));
                init(1);
            }
        });

        datewisetxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pendingtxt.setTextColor(mcontext.getResources().getColor(R.color.colorPrimaryDark));
                txt_pending_rel.setBackground(mcontext.getResources().getDrawable(R.drawable.roundercornerbuttonhollow));

                datewisetxt.setTextColor(mcontext.getResources().getColor(R.color.white));
                txt_date_rel.setBackground(mcontext.getResources().getDrawable(R.drawable.roundercornerbuttonhollowsolid));
                init(2);
            }
        });
        pendingtxt.setTextColor(mcontext.getResources().getColor(R.color.colorPrimaryDark));
        txt_pending_rel.setBackground(mcontext.getResources().getDrawable(R.drawable.roundercornerbuttonhollow));

        datewisetxt.setTextColor(mcontext.getResources().getColor(R.color.white));
        txt_date_rel.setBackground(mcontext.getResources().getDrawable(R.drawable.roundercornerbuttonhollowsolid));
    }


    public void loadprogressbar() {
        if (progDailog == null) {
            progDailog = new ProgressDialog(getActivity());
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

    public void getdata(String qry) {
        int remainingcount = 0;
        pendingListModels = new ArrayList<>();
        String path = AppSettings.getdburl(mcontext);
        SQLiteDatabase mydatabase = mcontext.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
        Cursor cursor = mydatabase.rawQuery(qry, null);

        if (cursor.getCount() > 0) {
            recyclerViewpendintlist.setVisibility(View.VISIBLE);
            if (cursor.moveToFirst()) {
                do {
                    String uploaded = cursor.getString(cursor.getColumnIndex("Uploaded"));
                    String classs = cursor.getString(cursor.getColumnIndex("Class"));
                    String subjects = cursor.getString(cursor.getColumnIndex("Subject"));
                    String date = cursor.getString(cursor.getColumnIndex("Date"));
                    String time = cursor.getString(cursor.getColumnIndex("Time"));

                    String classsid = cursor.getString(cursor.getColumnIndex("ClassId"));
                    String subjectsid = cursor.getString(cursor.getColumnIndex("SubjectId"));
                    String uploadedid = cursor.getString(cursor.getColumnIndex("UploadedId"));
                    String stdstatus = cursor.getString(cursor.getColumnIndex("Std_status"));
                    boolean isuploaded = false;
                    if (uploaded.equalsIgnoreCase("true")) {
                        isuploaded = true;
                    } else {
                        isuploaded = false;
                        remainingcount = remainingcount + 1;
                    }
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("uploaded", isuploaded);
                        jsonObject.put("classname", classs);
                        jsonObject.put("subjectname", subjects);
                        jsonObject.put("date", date);
                        jsonObject.put("time", time);

                        jsonObject.put("classid", classsid);
                        jsonObject.put("subjectid", subjectsid);
                        jsonObject.put("uploadedid", uploadedid);
                        jsonObject.put("stdstatus", stdstatus);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    PendingListModel pendingListModel = new PendingListModel(jsonObject);
                    pendingListModels.add(pendingListModel);

                } while (cursor.moveToNext());
            }
            cursor.close();

            pendingcount.setText("Pending: " + remainingcount + " / " + pendingListModels.size());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
            recyclerViewpendintlist.setLayoutManager(linearLayoutManager);
            recyclerViewpendintlist.setHasFixedSize(true);
            PendingListAdapter mAdapter = new PendingListAdapter(pendingListModels, mcontext, typefaceAW,getActivity());
            recyclerViewpendintlist.setAdapter(mAdapter);

        } else {
            recyclerViewpendintlist.setVisibility(View.INVISIBLE);
            pendingcount.setText("");
            Toast.makeText(mcontext, "No record found", Toast.LENGTH_SHORT).show();
        }
    }

    public void init(int type) {
        if(type==1) {
            pendingname.setText(AppSettings.getusername(mcontext));
            final String tuid = AppSettings.getuserId(mcontext);
            datepending.setVisibility(View.GONE);
            typefaceAW = Typeface.createFromAsset(mcontext.getAssets(), "fonts/FontAwesome.otf");
            String query = "select * from logs where TeacherId='" + tuid + "' and Uploaded='false'";
            getdata(query);
        }else{
            pendingname.setText(AppSettings.getusername(mcontext));
            final String tuid = AppSettings.getuserId(mcontext);
            final String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
            datepending.setVisibility(View.VISIBLE);
            datepending.setText("Date: " + date);
            typefaceAW = Typeface.createFromAsset(mcontext.getAssets(), "fonts/FontAwesome.otf");
            datepending.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DialogFragment newFragment = new SelectDateFragment();
                    newFragment.show(getFragmentManager(), "DatePicker");

                }
            });
            String query = "select * from logs where TeacherId='" + tuid + "' and Date='" + date + "'";
            getdata(query);
        }
    }


    public class SelectDateFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar calendar = Calendar.getInstance();
            int yy = calendar.get(Calendar.YEAR);
            int mm = calendar.get(Calendar.MONTH);
            int dd = calendar.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, yy, mm, dd);
        }

        public void onDateSet(DatePicker view, int yy, int mm, int dd) {
            populateSetDate(yy, mm + 1, dd);
        }

        public void populateSetDate(int year, int month, int day) {
            String monthString="";
            String dayString="";
            if (month < 10) {
                monthString = "0" + Integer.toString(month);
            }else{
                monthString = Integer.toString(month);
            }

            if (day < 10) {
                dayString = "0" + Integer.toString(day);
            }else{
                dayString = Integer.toString(day);
            }

            String dates = dayString + "-" + monthString + "-" + year;
            String query = "select * from logs where TeacherId='" + AppSettings.getuserId(mcontext) + "' and Date='" + dates + "'";
            getdata(query);
            datepending.setText("Date: " + dayString + "-" + monthString + "-" + year);
        }

    }


}
