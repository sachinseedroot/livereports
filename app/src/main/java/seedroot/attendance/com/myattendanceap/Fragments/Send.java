package seedroot.attendance.com.myattendanceap.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import seedroot.attendance.com.myattendanceap.R;


/**
 * Created by Z0NEN on 10/22/2014.
 */
public class Send extends Fragment {
    View rootview;

    EditText data;
    RelativeLayout send;
    ProgressDialog progDailog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.content_main2, container, false);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        data = (EditText) rootview.findViewById(R.id.sendpushTV);
        send = (RelativeLayout) rootview.findViewById(R.id.sendpushbtn);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!TextUtils.isEmpty(data.getText().toString())){
                    loadprogressbar();


                }else {
                    Toast.makeText(getActivity().getApplicationContext(),"Field cannot be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView back = (TextView) rootview.findViewById(R.id.backatteasend);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.container, new HomeFragment(), "NewFragmentTag");
                ft.commit();
            }
        });
        overrideFonts(getActivity().getApplicationContext(),back);
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

    public void loadprogressbar(){
        progDailog = new ProgressDialog(getActivity());
        progDailog.setMessage("Sending, Please wait...");
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
    }
    public void stoploadprogress(){

        if (progDailog.isShowing()==true)
        {
            progDailog.dismiss();
        }

    }
}
