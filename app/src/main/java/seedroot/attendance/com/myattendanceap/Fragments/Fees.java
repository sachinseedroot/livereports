package seedroot.attendance.com.myattendanceap.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import seedroot.attendance.com.myattendanceap.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sachin on 21/1/16.
 */
public class Fees extends Fragment {
    View rootview;
    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    Context mcontext;
    ListView listView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mcontext = activity;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.fees, container, false);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        TextView tv= (TextView) view.findViewById(R.id.headertextatT);
//        tv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeueLTStd-LtCn.otf"));
//        spinner1 = (Spinner) view.findViewById(R.id.ddpat1);
//        spinner2 = (Spinner) view.findViewById(R.id.ddpat12);
//       // spinner3 = (Spinner) view.findViewById(R.id.ddpat13);
//
//        List<String> categories = new ArrayList<String>();
//        categories.add("Select Class");
//        categories.add("Business Services");
//        List<String> categories2 = new ArrayList<String>();
//        categories2.add("Select Division");
//        categories2.add("Business Services");
//        List<String> categories3 = new ArrayList<String>();
//        categories3.add("Select Day");
//        categories3.add("Business Services");
//
//        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_item, categories);
//        ArrayAdapter<String> dataAdapter2 = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_item, categories2);
//        ArrayAdapter<String> dataAdapter3 = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_item, categories3);
//
//        // Drop down layout style - list view with radio button
//        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        dataAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        // attaching data adapter to spinner
//        spinner1.setAdapter(dataAdapter);
//        spinner2.setAdapter(dataAdapter2);
//        spinner3.setAdapter(dataAdapter3);
    }
}
