package seedroot.attendance.com.myattendanceap.Fragments;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import seedroot.attendance.com.myattendanceap.R;

/**
 * Created by Z0NEN on 10/22/2014.
 */
public class NewsFragment extends Fragment {
    View rootview;
    Context mcont;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootview = inflater.inflate(R.layout.news, container, false);
        return rootview;
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView back = (TextView) rootview.findViewById(R.id.backattnews);
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
}
