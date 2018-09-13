package com.example.data.tracker.datatracker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.data.tracker.datatracker.R;
import com.example.data.tracker.datatracker.bean.DownloadInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private List<DownloadInfo> dataList;
    private OnDownloadClickListener onDownloadClickListener;

    private OnItemClickListener onItemClickListener;
    final String appName = "";

    public RecyclerAdapter(List<DownloadInfo> dataList) {
        this.dataList = dataList;
    }

    public void setOnDownloadClickListener(OnDownloadClickListener onDownloadClickListener) {
        this.onDownloadClickListener = onDownloadClickListener;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    DownloadInfo downloadInfo = dataList.get((Integer) v.getTag());
                    onItemClickListener.onItemClick(downloadInfo);
                }

            }
        });

        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDownloadClickListener != null) {
                    onDownloadClickListener.onDownload(dataList.get((Integer) v.getTag()));
                }
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(dataList.get(position).getAppName());
        holder.itemView.setTag(position);
        Map<String,String> params = new HashMap<>();
        params.put("1",dataList.get(position).getAppName());
        holder.button.setTag(position);
        holder.button.setTag(R.string.view_tag,params);
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public Button button;//绑定从dataList中获取的数据，index如何获取？

        private ViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            button = itemView.findViewById(R.id.download);
        }
    }

    public interface OnDownloadClickListener{
        void onDownload(DownloadInfo downloadInfo);
    }

    public interface OnItemClickListener{
        void onItemClick(DownloadInfo downloadInfo);
    }
}
