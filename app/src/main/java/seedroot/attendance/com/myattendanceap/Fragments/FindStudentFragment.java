package seedroot.attendance.com.myattendanceap.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import seedroot.attendance.com.myattendanceap.R;

/**
 * Created by Z0NEN on 10/22/2014.
 */
public class FindStudentFragment extends Fragment {
    View rootview;

    EditText data;
    RelativeLayout send;
    @Nullable
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


                }else {
                    Toast.makeText(getActivity().getApplicationContext(),"Field cannot be empty",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
