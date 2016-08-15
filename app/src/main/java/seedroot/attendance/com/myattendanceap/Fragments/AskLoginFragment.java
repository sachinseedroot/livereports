package seedroot.attendance.com.myattendanceap.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import seedroot.attendance.com.myattendanceap.R;
import seedroot.attendance.com.myattendanceap.Utils.AppSettings;


/**
 * Created by sachin on 21/1/16.
 */
public class
        AskLoginFragment extends Fragment {
    View rootview;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.asklogin, container, false);
        return rootview;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RelativeLayout stdlogin = (RelativeLayout) rootview.findViewById(R.id.std_login_btn);
        RelativeLayout techlogin = (RelativeLayout) rootview.findViewById(R.id.teacher_login_btn);

        stdlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isNetworkConnected()==true) {
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new loginFragment(), "NewFragmentTag");
                    ft.commit();
                    AppSettings.setusertype(getActivity().getApplicationContext(), "student");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(),"Please connect to internet",Toast.LENGTH_SHORT).show();
                }
            }
        });

        techlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // if(isNetworkConnected()==true) {
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, new loginFragment(), "NewFragmentTag");
                ft.commit();
                AppSettings.setusertype(getActivity().getApplicationContext(), "teacher");
//                }else{
//                    Toast.makeText(getActivity().getApplicationContext(),"Please connect to internet",Toast.LENGTH_SHORT).show();
                }
           // }
        });

    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getActivity().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
