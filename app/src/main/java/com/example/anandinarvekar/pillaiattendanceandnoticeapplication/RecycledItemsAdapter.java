package com.example.anandinarvekar.pillaiattendanceandnoticeapplication;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecycledItemsAdapter extends RecyclerView.Adapter<RecycledItemsAdapter.ViewHolder> {

    private List<RecycledItems> values;
    public Context context;
    public String lecture_no;
    private String setDate;

    Connection connect;
    String ConnectionResult = "";
    Boolean isSuccess = false;


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView rv_roll, rv_name;
        public View layout;
        public CheckBox present_chk;


        public ViewHolder(View v)
        {
            super(v);
            layout = v;
            rv_roll = (TextView)v.findViewById(R.id.rv_roll);
            rv_name = (TextView)v.findViewById(R.id.rv_name);
            present_chk = (CheckBox)v.findViewById(R.id.present_chk);

            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            rv_roll = (TextView)v.findViewById(R.id.rv_roll);
            rv_name = (TextView)v.findViewById(R.id.rv_name);
        }
    }

    public RecycledItemsAdapter(List<RecycledItems> myDataset, Context context, String lectNo,String setDate)
    {
        values = myDataset;
        this.context = context;
        lecture_no = lectNo;
        this.setDate = setDate;
    }

    @Override
    public RecycledItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycled_student_items, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        final RecycledItems recycledItems = values.get(position);
        holder.rv_name.setText(recycledItems.getName());
        holder.rv_roll.setText(recycledItems.getRoll());
        holder.present_chk.setChecked(recycledItems.isSelected());
        holder.present_chk.setTag(recycledItems);

        // holder.present_chk.setChecked(values.get(position).isSelected());

        holder.present_chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                CheckBox cb = (CheckBox) buttonView;
                RecycledItems stu_roll = (RecycledItems) cb.getTag();
                stu_roll.setSelected(cb.isChecked());
                recycledItems.setSelected(cb.isChecked());

                if (isChecked) {

                    Toast.makeText(context, "Attendance Marked For: "+holder.rv_roll.getText(),Toast.LENGTH_LONG).show();

                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        connect = conStr.connection();        // Connect to database
                        if (connect == null) {
                            ConnectionResult = "Check Your Internet Access!";
                        } else {
                            //String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new java.util.Date());
                            String sql = "{call usp_InsertAttendaceMaster(?,?,?)}";
                            CallableStatement stmt = connect.prepareCall(sql);
                            stmt.setString(1, holder.rv_roll.getText().toString());
                            stmt.setString(2, setDate);
                            stmt.setString(3,lecture_no);
                            ResultSet rs = stmt.executeQuery();

                        }
                        ConnectionResult = " successful";
                        isSuccess = true;
                        connect.close();
                    }
                    catch (Exception ex) {
                        isSuccess = false;
                        ConnectionResult = ex.getMessage();
                    }


                } else if(!isChecked) {
                    Toast.makeText(context, "Attendance Removed For: "+holder.rv_roll.getText(),Toast.LENGTH_LONG).show();

                    try {
                        ConnectionHelper conStr = new ConnectionHelper();
                        connect = conStr.connection();        // Connect to database
                        if (connect == null) {
                            ConnectionResult = "Check Your Internet Access!";
                        } else {
                            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new java.util.Date());
                            String sql = "{call usp_DeleteFromAttendaceMaster(?,?,?)}";
                            CallableStatement stmt = connect.prepareCall(sql);
                            stmt.setString(1, holder.rv_roll.getText().toString());
                            stmt.setString(2, date);
                            stmt.setString(3,lecture_no);
                            ResultSet rs = stmt.executeQuery();

                        }
                        ConnectionResult = " successful";
                        isSuccess = true;
                        connect.close();
                    }
                    catch (Exception ex) {
                        isSuccess = false;
                        ConnectionResult = ex.getMessage();
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return values.size();
    }

    public List<RecycledItems> getStudentList()
    {
       return values;
    }
}
