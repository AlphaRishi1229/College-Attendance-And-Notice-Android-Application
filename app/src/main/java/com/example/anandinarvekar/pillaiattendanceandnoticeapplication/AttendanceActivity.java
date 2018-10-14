package com.example.anandinarvekar.pillaiattendanceandnoticeapplication;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AttendanceActivity extends AppCompatActivity {
Button btnupdateattendance;
Button btneditattendance;
TextView TeacherName, tvdate;
Spinner spnstream, spnclass, spnlecno;
CheckBox lastlecchk;
String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
String LastLecOfDay;
DatePicker onDate;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    Calendar calendar;
    String ondate;
    int day, month, year;
private String Stream_Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        tvdate = (TextView)findViewById(R.id.tv_date);


        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        Bundle bundle = getIntent().getExtras();
        String Teachername = bundle.getString("TeacherName2");
        TeacherName = (TextView)findViewById(R.id.TeacherName);
        TeacherName.setText(Teachername);

        spnclass = (Spinner)findViewById(R.id.spnclass);
        spnlecno = (Spinner)findViewById(R.id.spnlecno);
        List<String> lect_no = new ArrayList<String>();
        lect_no.add("1");
        lect_no.add("2");
        lect_no.add("3");
        lect_no.add("4");
        lect_no.add("5");
        lect_no.add("6");
        lect_no.add("7");
        ArrayAdapter<String> Spinnerlect  = new ArrayAdapter<String>(AttendanceActivity.this, android.R.layout.simple_spinner_item, lect_no);
        Spinnerlect.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnlecno.setAdapter(Spinnerlect);

        spnstream = (Spinner)findViewById(R.id.spnstream);
        List<String> stream_spin = new ArrayList<String>();
        stream_spin.add("Bsc Computer Science");
        stream_spin.add("B.M.M");
        stream_spin.add("B.M.S");
        ArrayAdapter<String> SpinnerAdapter = new ArrayAdapter<String>(AttendanceActivity.this, android.R.layout.simple_spinner_item, stream_spin);
        SpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnstream.setAdapter(SpinnerAdapter);

        spnstream.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (spnstream.getSelectedItem().equals("Bsc Computer Science")) {
                    setCsData();
                }
                else if(spnstream.getSelectedItem().equals("B.M.M")){
                    setBmmData();
                }
                else if(spnstream.getSelectedItem().equals("B.M.S")) {
                    setBmsData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spnclass.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                setStreamId streamID = (setStreamId)parent.getSelectedItem();
                Stream_Id = streamID.getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lastlecchk = (CheckBox)findViewById(R.id.lastlecchk);


        btnupdateattendance = (Button) findViewById(R.id.btnupdateattendance);
        btnupdateattendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(lastlecchk.isChecked())
                {
                    LastLecOfDay = spnlecno.getSelectedItem().toString();
                }
                ondate =tvdate.getText().toString();
                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    connect = conStr.connection();        // Connect to database
                    if (connect == null) {
                        ConnectionResult = "Check Your Internet Access!";
                    } else {
                        String query = "{call usp_InsertLectureMaster(?,?,?)}";
                        CallableStatement stmt = connect.prepareCall(query);
                        stmt.setString(1,Stream_Id);
                        stmt.setString(2,LastLecOfDay);
                        stmt.setString(3,ondate);
                        ResultSet rs = stmt.executeQuery();

                        ConnectionResult = " successful";
                        isSuccess = true;
                        connect.close();
                    }
                } catch (Exception ex) {
                    isSuccess = false;
                    ConnectionResult = ex.getMessage();
                }

                String lecture_no = spnlecno.getSelectedItem().toString();
                Intent attendance_update = new Intent(AttendanceActivity.this, Attendance_Update_Activity.class);
                Bundle bundle = new Bundle();
                bundle.putString("StreamID",Stream_Id);
                bundle.putString("LectureNo",lecture_no);
                bundle.putString("LastLec",LastLecOfDay);
                bundle.putString("Date",ondate);
                attendance_update.putExtras(bundle);
                startActivity(attendance_update);
            }
        });

        }

    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca",
                Toast.LENGTH_SHORT)
                .show();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        tvdate.setText(new StringBuilder().append(year).append("-")
                .append(month).append("-").append(day));
    }

    private void setCsData()
    {
        setStreamId streamID;
        ArrayList<setStreamId> CsStream = new ArrayList<>();
        CsStream.add(new setStreamId("1","CS-First Semester"));
        CsStream.add(new setStreamId("2","CS-Second Semester"));
        CsStream.add(new setStreamId("3","CS-Third Semester"));
        CsStream.add(new setStreamId("4","CS-Fourth Semester"));
        CsStream.add(new setStreamId("5","CS-Fifth Semester"));
        CsStream.add(new setStreamId("6","CS-Sixth Semester"));

        ArrayAdapter<setStreamId> adapter = new ArrayAdapter<setStreamId>(AttendanceActivity.this,android.R.layout.simple_spinner_dropdown_item,CsStream);
        spnclass.setAdapter(adapter);

        int position = adapter.getPosition(new setStreamId("1","First Semester"));
        spnclass.setSelection(position);
    }

    private void setBmmData()
    {
        ArrayList<setStreamId> CsStream = new ArrayList<>();
        CsStream.add(new setStreamId("7","BMM-First Semester"));
        CsStream.add(new setStreamId("8","BMM-Second Semester"));
        CsStream.add(new setStreamId("9","BMM-Third Semester"));
        CsStream.add(new setStreamId("10","BMM-Fourth Semester"));
        CsStream.add(new setStreamId("11","BMM-Fifth Semester"));
        CsStream.add(new setStreamId("12","BMM-Sixth Semester"));

        ArrayAdapter<setStreamId> adapter = new ArrayAdapter<setStreamId>(AttendanceActivity.this,android.R.layout.simple_spinner_dropdown_item,CsStream);
        spnclass.setAdapter(adapter);
    }

    private void setBmsData()
    {
        ArrayList<setStreamId> CsStream = new ArrayList<>();
        CsStream.add(new setStreamId("13","BMS-First Semester"));
        CsStream.add(new setStreamId("14","BMS-Second Semester"));
        CsStream.add(new setStreamId("15","BMS-Third Semester"));
        CsStream.add(new setStreamId("16","BMS-Fourth Semester"));
        CsStream.add(new setStreamId("17","BMS-Fifth Semester"));
        CsStream.add(new setStreamId("18","BMS-Sixth Semester"));

        ArrayAdapter<setStreamId> adapter = new ArrayAdapter<setStreamId>(AttendanceActivity.this,android.R.layout.simple_spinner_dropdown_item,CsStream);
        spnclass.setAdapter(adapter);
    }

}
