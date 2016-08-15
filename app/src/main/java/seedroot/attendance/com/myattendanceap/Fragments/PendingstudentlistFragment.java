package seedroot.attendance.com.myattendanceap.Fragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import seedroot.attendance.com.myattendanceap.Activities.MainActivity;
import seedroot.attendance.com.myattendanceap.Adapter.StudentListAdapter;
import seedroot.attendance.com.myattendanceap.Models.StudentListModel;
import seedroot.attendance.com.myattendanceap.R;

/**
 * Created by sachin on 21/1/16.
 */
public class PendingstudentlistFragment extends Fragment {
    View rootview;
    Context mcontext;

    RecyclerView recyclerlist_pendinglist_std;
    TextView pendingdatestd;
    TextView tec_name;
    TextView tec_class_subject;
    ArrayList<StudentListModel> pendingListModels;
    Typeface typefaceAW;
    ImageButton backpressstduent;
    JSONArray jsonArrayStudent=null;

    String name;
    String date;
    String classtec;
    String subject;
    TextView tec_totalcountt;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mcontext = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.pendingstudentist, container, false);
        Bundle bundle = this.getArguments();
        name = bundle.getString("name");
        date = bundle.getString("date");
        classtec = bundle.getString("classtec");
        subject = bundle.getString("subject");
        String jsonarray = bundle.getString("jsonarray");
        try {
            jsonArrayStudent  = new JSONArray(jsonarray);
        } catch (JSONException e) {
            e.printStackTrace();
            jsonArrayStudent = null;
        }


        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mcontext == null) {
            mcontext = getActivity().getApplicationContext();
        }
        recyclerlist_pendinglist_std = (RecyclerView) rootview.findViewById(R.id.recyclerlist_pendinglist_std);
        pendingdatestd = (TextView) rootview.findViewById(R.id.pendingdatestd);
        tec_name = (TextView) rootview.findViewById(R.id.tec_name);
        tec_class_subject = (TextView) rootview.findViewById(R.id.tec_class_subject);
        backpressstduent = (ImageButton) rootview.findViewById(R.id.backpressstduent);
        tec_totalcountt = (TextView)rootview.findViewById(R.id.tec_totalcountt);
        tec_name.setText( name);
        pendingdatestd.setText("Date: "+date);
        tec_class_subject.setText(classtec+" ("+subject+")");
        typefaceAW = Typeface.createFromAsset(mcontext.getAssets(), "fonts/FontAwesome.otf");
        backpressstduent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, new pendinguploadlistFragment(), "NewFragmentTag");
                ft.commit();
            }
        });
        getdata();
    }



    public void getdata() {

        pendingListModels = new ArrayList<>();
       if(jsonArrayStudent!=null && jsonArrayStudent.length()>0){
           tec_totalcountt.setText("Total Students: "+jsonArrayStudent.length());
                    for(int i=0;i<jsonArrayStudent.length();i++){
                        JSONObject jsonObject = jsonArrayStudent.optJSONObject(i);
                    StudentListModel pendingListModel = new StudentListModel(jsonObject);
                    pendingListModels.add(pendingListModel);
                    }





            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mcontext);
            recyclerlist_pendinglist_std.setLayoutManager(linearLayoutManager);
            recyclerlist_pendinglist_std.setHasFixedSize(true);
            StudentListAdapter mAdapter = new StudentListAdapter(pendingListModels, mcontext, typefaceAW,getActivity());
            recyclerlist_pendinglist_std.setAdapter(mAdapter);

        } else {
            recyclerlist_pendinglist_std.setVisibility(View.INVISIBLE);
            Toast.makeText(mcontext, "No record found", Toast.LENGTH_SHORT).show();
        }
    }




}
