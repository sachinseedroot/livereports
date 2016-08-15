package seedroot.attendance.com.myattendanceap.Models;

import org.json.JSONObject;

/**
 * Created by Neha on 7/10/2016.
 */
public class PendingListModel {
    public String date;
    public String subject;
    public String classname;
    public String time;
    public boolean status;
    public String uploadedid;
    public String classid;
    public String subjecyid;
    public String std_status;


    public PendingListModel(JSONObject jsonObjectAttendance){

        date = jsonObjectAttendance.optString("date");
        subject = jsonObjectAttendance.optString("subjectname");
        classname = jsonObjectAttendance.optString("classname");
        status = jsonObjectAttendance.optBoolean("uploaded");
        time = jsonObjectAttendance.optString("time");

        uploadedid = jsonObjectAttendance.optString("uploadedid");
        classid = jsonObjectAttendance.optString("classid");
        subjecyid = jsonObjectAttendance.optString("subjectid");
        std_status = jsonObjectAttendance.optString("stdstatus");

    }


}
