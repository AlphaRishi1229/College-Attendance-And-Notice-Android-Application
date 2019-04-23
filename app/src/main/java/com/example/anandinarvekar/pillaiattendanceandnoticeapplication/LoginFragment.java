package com.example.anandinarvekar.pillaiattendanceandnoticeapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.Fragment;


import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class LoginFragment extends Fragment {

    TextInputEditText etrollno, etidno;
    public TextWatcher loginTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String id = etidno.getText().toString();
            String roll = etrollno.getText().toString();

            if(!id.isEmpty())
            {

                etrollno.setFocusableInTouchMode(false);
                etrollno.setFocusable(false);
                etrollno.setEnabled(false);

            }
            else if(!roll.isEmpty())
            {

                etidno.setFocusableInTouchMode(false);
                etidno.setFocusable(false);
                etidno.setEnabled(false);
            }

        }
        @Override
        public void afterTextChanged(Editable s) {
            String id = etidno.getText().toString();
            String roll = etrollno.getText().toString();

            if(id.isEmpty() && roll.isEmpty())
            {
                etidno.setEnabled(true);
                etidno.setFocusable(true);
                etidno.setFocusableInTouchMode(true);

                etrollno.setEnabled(true);
                etrollno.setFocusable(true);
                etrollno.setFocusableInTouchMode(true);


            }
            else
            {
                // btnlogin.setEnabled(true);
            }

        }
    };
    Button btnlogin;
    ImageButton qr_scan;
    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_login, container, false);

        etidno = (TextInputEditText)view.findViewById(R.id.etidno);
        etrollno=(TextInputEditText)view.findViewById(R.id.etrollno);
        etidno.addTextChangedListener(loginTextWatcher);
        etrollno.addTextChangedListener(loginTextWatcher);

        SharedPreferences preferences = getActivity().getSharedPreferences("value_pref",0);
        String admno = preferences.getString("admid",null);
        etidno.setText(admno);

        qr_scan = (ImageButton)view.findViewById(R.id.qr_scan);
        qr_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent scan = new Intent(getActivity(), QrScanActivity.class);
                startActivity(scan);
            }
        });

        btnlogin=(Button)view.findViewById(R.id.btnlogin);
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String IDno = etidno.getText().toString();
                String Rollnumber=etrollno.getText().toString();


                try {
                    ConnectionHelper conStr = new ConnectionHelper();
                    connect = conStr.connection();        // Connect to database
                    if (connect == null) {
                        ConnectionResult = "Check Your Internet Access!";
                    } else {
                        String query ="  select Teacher_Login_ID,Teacher_Name from Teacher_Master ";
                        Statement stmt = connect.createStatement();
                        ResultSet rs = stmt.executeQuery(query);
                        while (rs.next()) {
                            String Name = rs.getString("Teacher_Name");
                            String Id = rs.getString("Teacher_Login_ID");

                            if(IDno.equals(Id) && Rollnumber.equals(""))
                            {
                                Toast.makeText(getActivity(),"Login Success",Toast.LENGTH_LONG).show();
                                Intent myintent = new Intent(getActivity(), OptionsActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("TeacherName",Name);
                                myintent.putExtras(bundle);
                                startActivity(myintent);
                            }
                            else
                            {
                                //Toast.makeText(getActivity(),"Login UNSuccess",Toast.LENGTH_LONG).show();

                            }

                        }

                        String query_user = "{call usp_GetStudentDetailsForAttendance(?)}";
                        CallableStatement stmt_user = connect.prepareCall(query_user);
                        stmt_user.setString(1,Rollnumber);
                        ResultSet rs_user = stmt_user.executeQuery();
                        while (rs_user.next())
                        {
                            String AdmNo = rs_user.getString("ADMISSION_NO");
                            String Stream = rs_user.getString("STREAM_NAME");
                            String StudentName = rs_user.getString("STUDENT_NAME");
                            String StudentRoll = rs_user.getString("ROLL_NO");

                            if(Rollnumber.equals(StudentRoll) && IDno.equals(""))
                            {
                                Toast.makeText(getActivity(),"Login Success",Toast.LENGTH_LONG).show();
                                Intent myintent = new Intent(getActivity(), StudentAttendanceActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("StudentName",StudentName);
                                bundle.putString("StudentStream",Stream);
                                bundle.putString("AdmissionNo",AdmNo);
                                bundle.putString("Roll",StudentRoll);
                                myintent.putExtras(bundle);
                                startActivity(myintent);
                            }
                            else
                            {
                               // Toast.makeText(getActivity(),"Login UNSuccess",Toast.LENGTH_LONG).show();

                            }
                        }
                        ConnectionResult = " successful";
                        isSuccess = true;
                        connect.close();
                    }
                } catch (SQLException ex) {
                    isSuccess = false;
                    ConnectionResult = ex.getMessage();
                }
            }
        });
        return view;
    }
}
