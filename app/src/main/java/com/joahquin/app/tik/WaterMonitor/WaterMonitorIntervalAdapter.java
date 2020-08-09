package com.joahquin.app.tik.WaterMonitor;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.joahquin.app.tik.Common.NotifyInterface;
import com.joahquin.app.tik.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;


public class WaterMonitorIntervalAdapter<Date> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<Date> contentList;
    public static final String TAG = "WaterMonitorIntervalAdapter";
    Context c;
    NotifyInterface nI;
    SimpleDateFormat sdf;


    public WaterMonitorIntervalAdapter(Context c, ArrayList<Date> contentList, String dateFormat, NotifyInterface nI) {
        this.contentList = contentList;
        this.c = c;
        this.nI = nI;
        sdf = new SimpleDateFormat(dateFormat);

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_time_interval_preview, parent, false);
        MyViewHolder holder = new MyViewHolder(rowView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof WaterMonitorIntervalAdapter.MyViewHolder) {

            final MyViewHolder mHolder = (MyViewHolder) holder;
            final Date item = contentList.get(position);

            ((MyViewHolder) holder).tvTime.setText(sdf.format(item));

            ((MyViewHolder) holder).ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    contentList.remove(position);
                    nI.notify(position);
                    notifyDataSetChanged();
                }
            });

        }


    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime;
        ImageView ivDelete;

        public MyViewHolder(View view) {
            super(view);
            tvTime =  view.findViewById(R.id.tvTime);
            ivDelete =  view.findViewById(R.id.ivDelete);
        }

    }

}
