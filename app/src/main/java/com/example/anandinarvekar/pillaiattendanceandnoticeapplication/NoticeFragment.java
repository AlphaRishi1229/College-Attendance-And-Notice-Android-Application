package com.example.anandinarvekar.pillaiattendanceandnoticeapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class NoticeFragment extends Fragment{

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;
    private ArrayList<NoticeItems> itemsArrayList;
    private NoticeAdapter noticeAdapter;
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_notices, container, false);

        listView = (ListView)view.findViewById(R.id.list_notice);


        SyncData noticedata = new SyncData();
        noticedata.execute("");
        itemsArrayList = new ArrayList<NoticeItems>();

        return view;

    }

    public class SyncData extends AsyncTask<String,String,String>
    {
        String notice;
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "Syncronising Notices","Fetching Notices from database",true);

        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... strings) {
            String msg = "";
            try {
                ConnectionHelper conStr = new ConnectionHelper();
                connect = conStr.connection();        // Connect to database
                if (connect == null) {
                    ConnectionResult = "Check Your Internet Access!";
                } else {
                    String query = "select * from dbo.notices";
                    Statement stmt = connect.createStatement();
                    ResultSet rs = stmt.executeQuery(query);
                    while (rs.next()) {
                        final String notice = rs.getString("notice");
                        String date = rs.getString("notice_date");
                        try{
                            itemsArrayList.add(new NoticeItems(notice));

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String news = ((TextView)view.findViewById(R.id.notice_text)).getText().toString();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                                    builder.setTitle("Notice");
                                    builder.setMessage(news);
                                    builder.show();
                                }
                            });
                        }
                        catch (Exception ex)
                        {
                            ex.printStackTrace();
                        }
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
            super.onPostExecute(s);
            progressDialog.dismiss();

            noticeAdapter = new NoticeAdapter(itemsArrayList,getActivity());
            listView.setAdapter(noticeAdapter);

        }


    }


    public class NoticeAdapter extends BaseAdapter
    {
        public List<NoticeItems> noticelist;
        public Context context;
        ArrayList<NoticeItems> arrayList;
        private NoticeAdapter(List<NoticeItems> notice, Context context)
        {
            this.noticelist = notice;
            this.context = context;
            arrayList = new ArrayList<NoticeItems>();
            arrayList.addAll(noticelist);
        }

        @Override
        public int getCount() {
            return noticelist.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View rowview = convertView;
            ViewHolder viewHolder = null;
            if(rowview == null)
            {
                LayoutInflater inflater = getLayoutInflater();
                rowview = inflater.inflate(R.layout.notice_items,parent,false);
                viewHolder = new ViewHolder();
                viewHolder.notices = (TextView)rowview.findViewById(R.id.notice_text);
                rowview.setTag(viewHolder);
            }
            else
            {
                viewHolder = (ViewHolder)convertView.getTag();
            }
            viewHolder.notices.setText(noticelist.get(position).getNotice_text());
            return rowview;
        }

        public class ViewHolder
        {
            TextView notices;
        }
    }

}
