package seedroot.attendance.com.myattendanceap.Models;

import org.json.JSONObject;

/**
 * Created by Neha on 7/10/2016.
 */
public class StudentListModel {
    public String present;
    public String roll_number;
    public String name;
    public String user_id;



    public StudentListModel(JSONObject jsonObjectAttendance){

        present = jsonObjectAttendance.optString("present");
        roll_number = jsonObjectAttendance.optString("roll_number");
        user_id = jsonObjectAttendance.optString("user_id");
        name = jsonObjectAttendance.optString("name");
    }


}
