package com.example.hunter.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hunter.R;
import com.example.hunter.base.GlideApp;
import com.example.hunter.mode.HunterData;
import com.example.hunter.mode.HunterData;
import com.example.hunter.utils.DateUtils;

import java.util.ArrayList;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.ViewHolder> implements View.OnClickListener, View.OnLongClickListener {

    private static final String TAG = "MainListAdapter";

    private Context context;
    private ArrayList<HunterData> Hunter = new ArrayList<HunterData>();

    private OnItemClickListener mOnItemClickListener = null;

    public MainListAdapter(Context context, ArrayList<HunterData> Hunter) {
        this.context = context;
        this.Hunter = Hunter;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_main_list, parent, false);
        ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(this);
        view.setOnLongClickListener(this);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 将position保存在itemView的Tag中，以便点击时进行获取
        holder.itemView.setTag(position);

        Integer id = Hunter.get(position).getId();
        Integer status = Hunter.get(position).getStatus();
        String title = Hunter.get(position).getTitle();
        String image = Hunter.get(position).getImage();
        Log.i(TAG, "onBindViewHolder: id = " + id.intValue() + " title = " + title + " image = " + image);

        if (title == null || title.length() <= 0) {
            holder.textView.setText(DateUtils.getInstance().getNowDateFormat());
        } else {
            holder.textView.setText(title);
        }

        int res;
        switch (status.intValue()) {
            case 0:
                res = R.color.hunter_item_status_normal;
                break;
            case 1:
                res = R.color.hunter_item_status_end;
                break;
            case 2:
                res = R.color.hunter_item_status_error;
                break;
            default:
                res = R.color.hunter_item_status_normal;
                break;
        }
        holder.textView.setBackgroundResource(res);

        GlideApp.with(context)
                .load(image)
                .fallback(R.drawable.ic_launcher_background)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return Hunter.size();
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            // 使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if (mOnItemClickListener != null) {
            // 使用getTag方法获取position
            mOnItemClickListener.onItemLongClick(v, (int) v.getTag());
        }
        return true;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image);
            textView = itemView.findViewById(R.id.title);
        }
    }

    public static interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }
}
