package com.example.anandinarvekar.pillaiattendanceandnoticeapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class AttendanceItemsAdapter extends RecyclerView.Adapter<AttendanceItemsAdapter.ViewHolder> {

    private List<AttendanceRecycledItems> values;
    public Context context;

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public TextView rv_month, rv_percent;
        public View layout;

        public ViewHolder(View v)
        {
            super(v);
            layout = v;
            rv_month = (TextView)v.findViewById(R.id.rv_month);
            rv_percent = (TextView)v.findViewById(R.id.rv_percent);

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int p = getLayoutPosition();
                    System.out.println("LongClick: "+p);
                    return true;
                }
            });

        }
    }

    public AttendanceItemsAdapter(List<AttendanceRecycledItems> myDataset, Context context)
    {
        values = myDataset;
        this.context = context;
    }

    @NonNull
    @Override
    public AttendanceItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.recycled_student_attendance_items,parent,false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        AttendanceRecycledItems recycledItems = values.get(position);
        holder.rv_month.setText(recycledItems.getMonth());
        holder.rv_percent.setText(recycledItems.getAttendancePercent());
    }

    @Override
    public int getItemCount() {
        return values.size();
    }
}
