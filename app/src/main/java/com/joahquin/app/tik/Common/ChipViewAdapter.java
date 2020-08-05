package com.joahquin.app.tik.Common;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.joahquin.app.tik.R;
import com.joahquin.app.tik.WaterMonitor.KeyValueItem;

import java.util.ArrayList;


public class ChipViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    ArrayList<T> contentList;
    ArrayList<T> oldList;
    public static final String TAG = "ChipViewAdapter";
    Context c;
    NotifyInterface nI;


    public ChipViewAdapter(Context c, ArrayList<T> contentList) {
        this.contentList = contentList;
        this.oldList = contentList;
        this.c = c;
    }

    public ChipViewAdapter(Context c, ArrayList<T> contentList, NotifyInterface nI) {
        this.contentList = contentList;
        this.oldList = contentList;
        this.c = c;
        this.nI = nI;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_interval_chip, parent, false);
        MyViewHolder holder = new MyViewHolder(rowView);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof ChipViewAdapter.MyViewHolder) {

            final MyViewHolder mHolder = (MyViewHolder) holder;
            final T mT = contentList.get(position);
            if (mT instanceof String) {
                mHolder.tvChipText.setText(mT.toString());
            }

            if (mT instanceof KeyValueItem) {
                mHolder.tvChipText.setText(((KeyValueItem)mT).getKey());

                if(((KeyValueItem) mT).isSelected()){
                    mHolder.tvChipText.setBackgroundResource(R.drawable.rectangle_text_filled);
                    mHolder.tvChipText.setTextColor(ContextCompat.getColor(c,R.color.white));
                }
                else{
                    mHolder.tvChipText.setBackgroundResource(R.drawable.rectangle_text);
                    mHolder.tvChipText.setTextColor(ContextCompat.getColor(c,R.color.colorText));
                }

                mHolder.tvChipText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for(KeyValueItem item : ((ArrayList<KeyValueItem>) contentList)){
                            item.setSelected(false);
                        }

                        ((KeyValueItem) mT).setSelected(true);
                        notifyDataSetChanged();

                        if(nI!=null)
                            nI.notify(position);
                    }
                });
            }


        }


    }

    @Override
    public int getItemCount() {
        return contentList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvChipText;

        public MyViewHolder(View view) {
            super(view);
            tvChipText = (TextView) view.findViewById(R.id.tvChipText);
        }

    }

}
