package com.example.anandinarvekar.pillaiattendanceandnoticeapplication;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

public class NoticeActivity extends AppCompatActivity {
    ImageButton btnupdatenotice;
    TextInputEditText etnotice;
    Connection connect;

    String ConnectionResult;
    Boolean isSuccess;
    String Notices;
    private ArrayList<NoticeItems> itemsArrayList;
    private NoticeItemsAdapter myAdapter;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice);
        etnotice = (TextInputEditText) findViewById(R.id.etnotice);

        recyclerView = findViewById(R.id.notice_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);

        itemsArrayList = new ArrayList<NoticeItems>();
        myAdapter =  new NoticeItemsAdapter(itemsArrayList, getApplicationContext());
        recyclerView.setAdapter(myAdapter);

        SyncNotice notices = new SyncNotice();
        notices.execute();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }

            @Override
            public void onLongClick(View view, int position) {
                Notices = ((TextView)recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.notice_text)).getText().toString();

                final AlertDialog.Builder builder = new AlertDialog.Builder(NoticeActivity.this);
                builder.setTitle("Delete Notice?");
                builder.setMessage(Notices+"\nDo you want to delete this Notice?");
                builder.setPositiveButton(
                        "DELETE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                DeleteNotice del_notices = new DeleteNotice();
                                del_notices.execute();
                            }
                        });
                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.show();
            }
        }));

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
     private class SyncNotice extends AsyncTask<String,String,String>
     {
         String msg = "";
         ProgressDialog progress;
         @Override
         protected void onPreExecute() {
             progress = ProgressDialog.show(NoticeActivity.this, "Synchronising",
                     "Notices Loading! Please Wait...", true);
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
                     String query = "{call usp_GetNotices()}";
                     CallableStatement stmt = connect.prepareCall(query);
                     ResultSet rs = stmt.executeQuery();
                     while (rs.next()) {
                         String notice = rs.getString("notice");

                         itemsArrayList.add(new NoticeItems(notice));
                     }
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
             progress.dismiss();
                     myAdapter = new NoticeItemsAdapter(itemsArrayList, NoticeActivity.this);
                     recyclerView.setAdapter(myAdapter);


             }

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
            finish();
            startActivity(getIntent());



        }
    }

    private class DeleteNotice extends AsyncTask<String,String,String>
    {
        String msg = "";
        ProgressDialog progress;
        @Override
        protected void onPreExecute() {
            progress = ProgressDialog.show(NoticeActivity.this, "Synchronising",
                    "Deleting Notice! Please Wait...", true);
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
                    String query = "{call usp_DeleteNotice(?)}";
                    CallableStatement stmt = connect.prepareCall(query);
                    stmt.setString(1,Notices);
                    ResultSet rs = stmt.executeQuery();

                    msg = "Successfully inserted data";
                }
                connect.close();
            } catch (SQLException se) {
                msg = "Sql Exception";
                Log.e("error here 1 :  ", se.getMessage());
            } catch (ClassNotFoundException e) {
                msg = "Class Not Found";
                Log.e("error here 2 : ", e.getMessage());
            }
            return msg;
        }
        @Override
        protected void onPostExecute(String s) {
            progress.dismiss();
            Toast.makeText(NoticeActivity.this, "Notice Delete",Toast.LENGTH_LONG).show();

           finish();
           startActivity(getIntent());

        }

    }


}

