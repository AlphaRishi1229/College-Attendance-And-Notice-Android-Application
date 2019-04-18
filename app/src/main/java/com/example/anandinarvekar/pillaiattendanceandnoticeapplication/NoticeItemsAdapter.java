package com.example.anandinarvekar.pillaiattendanceandnoticeapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NoticeItemsAdapter extends RecyclerView.Adapter<NoticeItemsAdapter.ViewHolder> {

    public Context context;
    private List<NoticeItems> values;

    public NoticeItemsAdapter(List<NoticeItems> myDataset, Context context) {
        values = myDataset;
        this.context = context;

    }

    @Override
    public NoticeItemsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.notice_items, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final NoticeItems noticeItems = values.get(position);
        holder.notice_text.setText(noticeItems.getNotice_text());

    }
    @Override
    public int getItemCount() {
        return values.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView notice_text;
        public View layout;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            notice_text = v.findViewById(R.id.notice_text);

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


}
