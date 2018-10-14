package com.example.anandinarvekar.pillaiattendanceandnoticeapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class Attendance_Update_Activity extends AppCompatActivity {

    private ArrayList<RecycledItems> itemsArrayList;
    private RecycledItemsAdapter myAdapter;
    RecycledItemsAdapter recycledItemsAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter re_adapter;

    String StreamID, LectureNo, LastLec, Date2;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    String RollNo="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance__update_);

        recyclerView = (RecyclerView)findViewById(R.id.stu_recycle_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);

        itemsArrayList = new ArrayList<RecycledItems>();

        Bundle bundle = getIntent().getExtras();
        CheckBox pre = (CheckBox)recyclerView.findViewById(R.id.present_chk);
//        final boolean isChecked = pre.isChecked();
        StreamID = bundle.getString("StreamID");
        LectureNo = bundle.getString("LectureNo");
        LastLec = bundle.getString("LastLec");
        Date2 = bundle.getString("Date");

       myAdapter = new RecycledItemsAdapter(itemsArrayList,getApplicationContext(),LectureNo,Date2);
        recyclerView.setAdapter(myAdapter);

        SyncAttendanceData data = new SyncAttendanceData();
        data.execute("");

    }



    public class SyncAttendanceData extends AsyncTask<String, String, String> {

        ProgressDialog progressDialog;
        private String TAG;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(Attendance_Update_Activity.this,
                    "Syncronising Data", "Fetching Students List from database", true);

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
                    String query = "{call usp_GetStudentDetailsByStream(?)}";
                    CallableStatement stmt = connect.prepareCall(query);
                     stmt.setString(1,StreamID);
                    ResultSet rs = stmt.executeQuery();
                    while (rs.next()) {
                        RollNo = rs.getString("ROLL_NO");
                        String Name = rs.getString("STUDENT_NAME");

                        itemsArrayList.add(new RecycledItems(Name,RollNo));
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
            if(!isSuccess)
            {

            }
            else {
                try
                {
                  myAdapter = new RecycledItemsAdapter(itemsArrayList, getApplicationContext(),LectureNo,Date2);
                    recyclerView.setAdapter(myAdapter);
                  // myAdapter.notifyDataSetChanged();
                }
                catch (Exception e)
                {
                    Log.d(TAG,"onPostExecute: here");
                }
            }
                    

        }

    }

}