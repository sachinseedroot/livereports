package seedroot.attendance.com.myattendanceap.Activities;

import android.app.Activity;
import android.os.Bundle;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import seedroot.attendance.com.myattendanceap.R;

/**
 * Created by sachin on 14/4/16.
 */
public class Test extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test);
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        try {
            String pattern = "dd-M-yyyy hh:mm:ss";
            String dateInString = new SimpleDateFormat(pattern).format(new Date());
            Date date = format.parse(dateInString);
            Date date1 = format.parse("31-5-2016 11:07:30");
            System.out.println("-----date--prev---" + date.getTime() + "---" + date1.getTime());
            if (date.getTime() - date1.getTime() >= 20 * 60 * 1000) {
                System.out.println("----------------true");
            } else {
                System.out.println("----------------false");

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }


}
