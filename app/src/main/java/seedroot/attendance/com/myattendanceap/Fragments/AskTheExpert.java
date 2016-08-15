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
public class AskTheExpert extends Fragment {
    View rootview;
    Spinner spinner;
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
        rootview = inflater.inflate(R.layout.asktheexpert, container, false);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tv= (TextView) view.findViewById(R.id.headertextask);
        spinner = (Spinner) view.findViewById(R.id.ddpask);
        listView = (ListView) view.findViewById(R.id.listviewask);
        tv.setTypeface(Typeface.createFromAsset(getActivity().getAssets(), "fonts/HelveticaNeueLTStd-LtCn.otf"));
        List<String> categories = new ArrayList<String>();
        categories.add("Select a topic");
        categories.add("Business Services");
        categories.add("Computers");
        categories.add("Education");
        categories.add("Personal");
        categories.add("Travel");
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mcontext, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        String s1 = "Query #19"+"\n"+"Q: How to order books?"+"\n"+"A: goto this menu and select xyz";
        String s2 = "Query #20"+"\n"+"Q: How to order books?"+"\n"+"A: goto this menu and select xyz";
        String[] values = new String[] { s1,s2  };
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mcontext,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
    }
}
