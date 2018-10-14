package com.example.anandinarvekar.pillaiattendanceandnoticeapplication;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.ArrayList;

public class StudentAttendanceActivity extends AppCompatActivity {

    private ArrayList<AttendanceRecycledItems> itemsArrayList;
    private AttendanceItemsAdapter myAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    public String RollNo;
    TextView tvname, tvstream;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_attendance);

        Bundle bundle = getIntent().getExtras();
        String name = bundle.getString("StudentName");
        String Stream = bundle.getString("StudentStream");
        String Admission = bundle.getString("AdmissionNo");
        RollNo = bundle.getString("Roll");

        tvname = (TextView)findViewById(R.id.tvname);
        tvstream = (TextView)findViewById(R.id.tvstream);

        tvname.setText(name);
        tvstream.setText(Stream);

        recyclerView = (RecyclerView) findViewById(R.id.rv_stu_attendance);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        itemsArrayList = new ArrayList<AttendanceRecycledItems>();

        myAdapter = new AttendanceItemsAdapter(itemsArrayList, getApplicationContext());
        recyclerView.setAdapter(myAdapter);

        SyncUserAttendance attendance = new SyncUserAttendance();
        attendance.execute("");


    }

    public class SyncUserAttendance extends AsyncTask<String, String, String> {
        ProgressDialog progressDialog;
        private String TAG;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(StudentAttendanceActivity.this,
                    "Syncronising Data", "Fetching Attendance from database", true);

        }

        @Override
        protected String doInBackground(String... strings) {
            String msg = "";
            try {
                ConnectionHelper conStr = new ConnectionHelper();
                connect = conStr.connection();        // Connect to database
                if (connect == null) {
                    ConnectionResult = "Check Your Internet Access!";
                } else {
                    String query = "{call usp_GetStudentAttendanceMonthWise(?)}";
                    CallableStatement stmt = connect.prepareCall(query);
                    stmt.setString(1, RollNo);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        String month = rs.getString("MonthWise");
                        String attendedlecs = rs.getString("LectureStudentsAttended");
                        String totalLecs = rs.getString("TotalLecturesMonth");
                        String attendancepercent = rs.getString("AttendancePercent");

                        itemsArrayList.add(new AttendanceRecycledItems(month, attendancepercent));
                    }
                    ConnectionResult = " successful";
                    isSuccess = true;
                    connect.close();
                }
            } catch (Exception ex) {
                isSuccess = false;
                ConnectionResult = ex.getMessage();
            }

            return msg;

        }

        @Override
        protected void onPostExecute(String s) {
            progressDialog.dismiss();
            if (!isSuccess) {

            } else {
                try {
                    myAdapter = new AttendanceItemsAdapter(itemsArrayList, getApplicationContext());
                    recyclerView.setAdapter(myAdapter);
                    // myAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Log.d(TAG, "onPostExecute: here");
                }
            }
        }
    }
}
