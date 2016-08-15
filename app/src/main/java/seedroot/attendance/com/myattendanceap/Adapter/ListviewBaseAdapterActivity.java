package seedroot.attendance.com.myattendanceap.Adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import seedroot.attendance.com.myattendanceap.Models.CalendarModel;
import seedroot.attendance.com.myattendanceap.R;

/**
 * Created by sachin on 28/7/15.
 */
public class ListviewBaseAdapterActivity extends BaseAdapter {

    Context con;
    LayoutInflater lytinf;
    int daysInMonth,dayofweek,j=0; //k=number of months
    String month;
    ArrayList<String> sundays = new ArrayList<String>();


    public ListviewBaseAdapterActivity(Context maincontext, String ki){

        this.con=maincontext;
        this.month =ki;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    public class Holder
    {
     //   TextView tv;// month header text
        LinearLayout l1,l2,l3,l4,l5,l6;//decalring all linear inflator layout
        RecyclerView mRecyclerView;
        RecyclerView.Adapter mAdapter;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {


    Holder holder = new Holder();
    View rowView;
    lytinf = (LayoutInflater) con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    rowView = lytinf.inflate(R.layout.list_item, parent, false);


            holder.l1 = (LinearLayout) rowView.findViewById(R.id.inflater1);
            holder.l2 = (LinearLayout) rowView.findViewById(R.id.inflater2);
            holder.l3 = (LinearLayout) rowView.findViewById(R.id.inflater3);
            holder.l4 = (LinearLayout) rowView.findViewById(R.id.inflater4);
            holder.l5 = (LinearLayout) rowView.findViewById(R.id.inflater5);
            holder.l6 = (LinearLayout) rowView.findViewById(R.id.inflater6);
           // holder.tv = (TextView) rowView.findViewById(R.id.monthName);

        //    holder.tv.setText(month);
           // else if(m==1){holder.tv.setText("August");}
     //   January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
            int monthcal=0;
            if(month.contains("January")){ monthcal = Calendar.JANUARY; }
            if(month.contains("February")){ monthcal = Calendar.FEBRUARY; }
            if(month.contains("March")){ monthcal = Calendar.MARCH; }
            if(month.contains("April")){ monthcal = Calendar.APRIL; }
            if(month.contains("May")){ monthcal = Calendar.MAY; }
            if(month.contains("June")){ monthcal = Calendar.JUNE; }
            if(month.contains("July")){ monthcal = Calendar.JULY; }
            if(month.contains("August")){ monthcal = Calendar.AUGUST; }
            if(month.contains("September")){ monthcal = Calendar.SEPTEMBER; }
            if(month.contains("October")){ monthcal = Calendar.OCTOBER; }
            if(month.contains("November")){ monthcal = Calendar.NOVEMBER; }
            if(month.contains("December")){ monthcal = Calendar.DECEMBER; }



            Calendar c2 = new GregorianCalendar(2016, monthcal, 1);// this should be dynamic
            daysInMonth= c2.getActualMaximum(Calendar.DAY_OF_MONTH);
            dayofweek = c2.get(Calendar.DAY_OF_WEEK);

            for(int i=0;i<daysInMonth;i++){
            Calendar c3 = new GregorianCalendar(2016, monthcal, (i+1));
            int dayofweek2 = c3.get(Calendar.DAY_OF_WEEK);
            if(Calendar.SUNDAY==dayofweek2){
                sundays.add(Integer.toString((i+1)));
              //  System.out.println("-----------" + (i+1));
                }
            }




            for (int i = 0; i < 6; i++) {// here

                if (i == 0) {
                    holder.mRecyclerView = (RecyclerView) rowView.findViewById(R.id.recyclerview_week1);
                }

                if (i == 1) {
                    holder.mRecyclerView = (RecyclerView) rowView.findViewById(R.id.recyclerview_week2);
                }

                if (i == 2) {
                    holder.mRecyclerView = (RecyclerView) rowView.findViewById(R.id.recyclerview_week3);
                }

                if (i == 3) {
                    holder.mRecyclerView = (RecyclerView) rowView.findViewById(R.id.recyclerview_week4);
                }

                if (i == 4) {
                    holder.mRecyclerView = (RecyclerView) rowView.findViewById(R.id.recyclerview_week5);
                }
                if (i == 5) {
                    holder.mRecyclerView = (RecyclerView) rowView.findViewById(R.id.recyclerview_week6);
                }
                if(sundays!=null && sundays.size()!=0) {
                    holder.mAdapter = new CalendarRecyclerviewAdapter(getDataSet(i), con, holder.l1, holder.l2, holder.l3, holder.l4, holder.l5, holder.l6, lytinf, sundays,monthcal);
                    holder.mRecyclerView.setHasFixedSize(true);
                    GridLayoutManager gvd = new GridLayoutManager(rowView.getContext(), 1);
                    gvd.setOrientation(GridLayoutManager.HORIZONTAL);
                    holder.mRecyclerView.setLayoutManager(gvd);
                    holder.mRecyclerView.setAdapter(holder.mAdapter);
                }

            }




    return rowView;

}

    private ArrayList<CalendarModel> getDataSet(int i) {

        ArrayList results = new ArrayList<CalendarModel>();
        CalendarModel obj;

        if(i==0) {

            for (int index = 0; index < 7; index++) {
                if (dayofweek <= index + 1) {
                    j = j + 1;
                    String indobj = String.valueOf(j);
                    obj = new CalendarModel(indobj);
                } else {
                    obj = new CalendarModel("");
                }
                results.add(index, obj);
            }


        }
        else  if(i==1){
            for (int index = 0; index < 7; index++) {
                if(j<=daysInMonth) {
                    j = j + 1;
                    String indobj = String.valueOf(j);

                    obj = new CalendarModel(indobj);
                    results.add(index, obj);
                }
            }
        }

        else if(i==2){

            for (int index = 0; index < 7; index++) {
                j= j+1;
                String indobj = String.valueOf(j);

                obj = new CalendarModel(indobj);
                results.add(index, obj);
            }
        }
        else if(i==3){
            for (int index = 0; index < 7; index++) {
                j= j+1;
                String indobj = String.valueOf(j);

                obj = new CalendarModel(indobj);
                results.add(index, obj);
            }
        }
        else if(i==4){

            for (int index = 0; index < 7; index++) {

                j= j+1;
                if(j<=daysInMonth) {
                    String indobj = String.valueOf(j);

                    obj = new CalendarModel(indobj);
                    results.add(index, obj);
                }
            }
        }
        else if(i==5){

            for (int index = 0; index < 7; index++) {

                j= j+1;
                if(j<=daysInMonth) {
                    String indobj = String.valueOf(j);

                    obj = new CalendarModel(indobj);
                    results.add(index, obj);
                }
            }
        }


        return results;
    }

    private Date getFirstDateOfCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_MONTH, Calendar.getInstance().getActualMinimum(Calendar.DAY_OF_MONTH));
        return cal.getTime();
    }

}
