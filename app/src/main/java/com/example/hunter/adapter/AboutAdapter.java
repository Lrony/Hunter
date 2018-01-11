package com.example.hunter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.hunter.R;
import com.example.hunter.mode.HunterAbout;

import java.util.ArrayList;

public class AboutAdapter extends RecyclerView.Adapter<AboutAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private ArrayList<HunterAbout> abouts = new ArrayList<HunterAbout>();

    private OnItemClickListener mOnItemClickListener = null;

    public AboutAdapter(Context context, ArrayList<HunterAbout> abouts) {
        this.context = context;
        this.abouts = abouts;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_about_list,parent,false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);

        holder.textView.setText(abouts.get(position).getTitle());
    }

    @Override
    public int getItemCount() {
        return abouts.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            // 使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.tv_title);
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
