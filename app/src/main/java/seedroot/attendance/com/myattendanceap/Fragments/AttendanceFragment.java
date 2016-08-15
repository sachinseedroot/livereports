package seedroot.attendance.com.myattendanceap.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.List;

import seedroot.attendance.com.myattendanceap.Adapter.ListviewBaseAdapterActivity;
import seedroot.attendance.com.myattendanceap.R;
import seedroot.attendance.com.myattendanceap.Utils.AppSettings;

/**
 * Created by sachin on 21/1/16.
 */
public class AttendanceFragment extends Fragment {
    View rootview;
    Spinner spinner1;
    Context mcontext;
    ListView listv;
    ProgressDialog progDailog;
    String urlUser = "http://63.142.250.106/attendance/view/";
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
     //   mcontext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.attendance, container, false);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        spinner1 = (Spinner) view.findViewById(R.id.ddpmonth);
        mcontext =getActivity().getApplicationContext();
        urlUser = urlUser+AppSettings.getuserId(mcontext);
        listv = (ListView) view.findViewById(R.id.listView);
        List<String> categories = new ArrayList<String>();
        categories.add("Select Month");

        for(int i=0;i<12;i++){
            String months = getMonthForInt(i) +" 2016";
            categories.add(months);
        }


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), R.layout.spinneritem, categories);
       // dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner1.setAdapter(dataAdapter);
        int spinnerPosition = dataAdapter.getPosition("June 2016");
        spinner1.setSelection(spinnerPosition);
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner1.getSelectedItem().toString().equals("Select Month")) {
                    listv.setVisibility(View.GONE);
                } else {

                    listv.setVisibility(View.VISIBLE);
                    listv.setAdapter(new ListviewBaseAdapterActivity(getActivity().getApplicationContext(), spinner1.getSelectedItem().toString()));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        TextView back = (TextView) rootview.findViewById(R.id.backatt);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.container, new HomeFragment(), "NewFragmentTag");
                ft.commit();
            }
        });


        LinearLayout linearLayout = (LinearLayout) rootview.findViewById(R.id.parentattendance);
        overrideFonts(getActivity().getApplicationContext(), linearLayout);

//        final TextView refresh = (TextView) rootview.findViewById(R.id.refreshbtn);
//        refresh.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//            //   refresh();
//            }
//        });


    }

    public String getMonthForInt(int num) {
        String month = "wrong";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    public static void overrideFonts(final Context context, final View v) {
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/FontAwesome.otf");

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

    public void refresh(){
        loadprogressbar();

        RequestQueue queue = Volley.newRequestQueue(mcontext);
        JsonArrayRequest req = new JsonArrayRequest(urlUser,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {


                        stoploadprogress();
                        if(response!=null && response.length()>0){
                            AppSettings.setuserdetailse(getActivity().getApplicationContext(), response.toString());
                        }else{
                            Toast.makeText(mcontext,
                                   "Failed to load data.", Toast.LENGTH_SHORT).show();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                stoploadprogress();
                Toast.makeText(mcontext,
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });





    }
    public void loadprogressbar() {
        if(progDailog==null) {
            progDailog = new ProgressDialog(getActivity());
        }
        if(progDailog.isShowing()==false){
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


}
