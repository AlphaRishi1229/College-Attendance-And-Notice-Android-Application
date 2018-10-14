package com.example.anandinarvekar.pillaiattendanceandnoticeapplication;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class NoticeActivity extends AppCompatActivity {
    ImageButton btnupdatenotice;
    TextInputEditText etnotice;
    Connection connect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        etnotice = (TextInputEditText) findViewById(R.id.etnotice);
        btnupdatenotice = (ImageButton) findViewById(R.id.btnupdatenotice);
        btnupdatenotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = "Notice has been updated";
                Toast.makeText(getApplicationContext(), title, Toast.LENGTH_LONG).show();
                String notice = etnotice.getText().toString();

                if(notice.equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Please type some notice",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Send objSend = new Send();
                    objSend.execute("");
                }

            }
        });
    }

    private class Send extends AsyncTask<String, String, String> {

        String msg = "";
        String notice = etnotice.getText().toString();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new java.util.Date());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                Class.forName("net.sourceforge.jtds.jdbc.Driver");
                ConnectionHelper conStr = new ConnectionHelper();
                connect = conStr.connection();
                if (connect == null) {
                    msg = "Check connection";
                    Toast.makeText(getApplicationContext(), "Check Connection", Toast.LENGTH_SHORT).show();
                } else {
                    String query = "Insert into dbo.notices(notice,notice_date)" +
                            "values ('"+notice+"','"+date+"')";

                    Statement statement = connect.createStatement();
                    statement.executeUpdate(query);
                    msg = "Successfully inserted data";
                }
                connect.close();
            } catch (SQLException se) {
                msg = "Sql Exception";
//              Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
                Log.e("error here 1 :  ", se.getMessage());
            } catch (ClassNotFoundException e) {
                msg = "Class Not Found";
                //                        Toast.makeText(getActivity(),msg,Toast.LENGTH_LONG).show();
                Log.e("error here 2 : ", e.getMessage());
            }
            return msg;
        }
        @Override
        protected void onPostExecute(String s) {

            etnotice.setEnabled(false);

        }
    }

}

