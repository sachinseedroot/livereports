package seedroot.attendance.com.myattendanceap.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import seedroot.attendance.com.myattendanceap.R;
import seedroot.attendance.com.myattendanceap.Utils.AppSettings;

/**
 * Created by Z0NEN on 10/22/2014.
 */
public class HomeFragment extends Fragment {
    View rootview;
    Context mcont;
    TextView pendingnoti;
    LinearLayout closeAppBtn;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.content_main, container, false);
        return rootview;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mcont = getActivity().getApplicationContext();
        AppSettings.setisLogin(mcont,true);
        pendingnoti = (TextView)rootview.findViewById(R.id.pendingnotiTV);
        closeAppBtn = (LinearLayout)rootview.findViewById(R.id.closeAppBtn);
        RelativeLayout pendintrel = (RelativeLayout)rootview.findViewById(R.id.rel_pending);
        LinearLayout linearLayout =(LinearLayout) rootview.findViewById(R.id.parentcontetnmain);
        TextView wmsg = (TextView) rootview.findViewById(R.id.welcomemsg);
        TextView signout = (TextView) rootview.findViewById(R.id.logoutv);
        signout.setText("logout "+mcont.getResources().getString(R.string.logout));


        TextView att = (TextView) rootview.findViewById(R.id.attendtv);
        TextView news = (TextView) rootview.findViewById(R.id.newstv);
        news.setVisibility(View.GONE);
      //  TextView iatt = (TextView) rootview.findViewById(R.id.iatt);
        ImageView inews = (ImageView) rootview.findViewById(R.id.inews);

        RelativeLayout btnat = (RelativeLayout) rootview.findViewById(R.id.btnatt);
        RelativeLayout btnne = (RelativeLayout) rootview.findViewById(R.id.btnnews);

        if(AppSettings.getusertype(mcont).equalsIgnoreCase("student")){
            pendingnoti.setVisibility(View.GONE);
            pendintrel.setVisibility(View.GONE);
            btnne.setVisibility(View.VISIBLE);
            att.setText("Attendance");
            news.setText("News/Notices");
      //      iatt.setText(mcont.getResources().getString(R.string.calendar));
      //      inews.setText(mcont.getResources().getString(R.string.fa_news));
            String classdiv="";
            try {
                JSONArray jsonArray = new JSONArray(AppSettings.getuserclass(mcont));
                if(jsonArray.length()>0){
                    for(int i=0;i<jsonArray.length();i++){
                        JSONObject jsonObject =jsonArray.optJSONObject(0);
                        classdiv = jsonObject.optString("name")+" "+ jsonObject.optString("division");
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            wmsg.setText("Welcome,\n" + AppSettings.getusername(mcont)+"\n"+classdiv);

        }else{
            pendingnoti.setVisibility(View.VISIBLE);
            pendintrel.setVisibility(View.VISIBLE);
            wmsg.setText("Welcome,\n" + AppSettings.getusername(mcont));
            att.setText("Attendance");
          //  news.setText("Send Notification");
         //   iatt.setText(mcont.getResources().getString(R.string.calendar));
          //  inews.setText(mcont.getResources().getString(R.string.sendp));
            btnne.setVisibility(View.VISIBLE);
            inews.setImageDrawable(mcont.getResources().getDrawable(R.drawable.pending));
            btnne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new pendinguploadlistFragment(), "NewFragmentTag");
                    ft.commit();
                }
            });
        }


        btnat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (AppSettings.getusertype(mcont).equalsIgnoreCase("student")) {
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new AttendanceFragment(), "NewFragmentTag");
                    ft.commit();
                }else{
                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.container, new AttendenceForTeacher(), "NewFragmentTag");
                    ft.commit();
                }

        }
    });

//        btnne.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (AppSettings.getusertype(mcont).equalsIgnoreCase("student")) {
//                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
//                    ft.add(R.id.container, new NewsFragment(), "NewFragmentTag");
//                    ft.commit();
//                }else{
////                    final FragmentTransaction ft = getFragmentManager().beginTransaction();
////                    ft.replace(R.id.container, new Send(), "NewFragmentTag");
////                    ft.commit();
//                }
//            }
//        });

        signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppSettings.setisLogin(mcont, false);
                AppSettings.setusername(mcont, "");
                AppSettings.setusertype(mcont, "");
                AppSettings.setuserId(mcont,"");
                final FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.container, new AskLoginFragment(), "NewFragmentTag");
                ft.commit();
            }
        });


        closeAppBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               getActivity().finish();
            }
        });
        loadpendingdata();
        overrideFonts(mcont, linearLayout);


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

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    public void loadpendingdata() {

        String query="select * from logs where TeacherId='" + AppSettings.getuserId(mcont) +"' and Uploaded='false'";
        String path = AppSettings.getdburl(mcont);
        SQLiteDatabase mydatabase = mcont.openOrCreateDatabase(path, Context.MODE_PRIVATE, null);
        Cursor cursor = mydatabase.rawQuery(query, null);

        if (cursor.getCount() > 0) {
            pendingnoti.setText("Pending\nAttendance\n"+Integer.toString(cursor.getCount()));
//            pendingnoti.s

        }else{
            pendingnoti.setText("View\nUploaded\nAttendance");

        }
    }
}
