package com.blcodes.views.lightrefresh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.blcodes.views.lightrefresh.R;

import java.util.ArrayList;

public class RVTestAdapter extends RecyclerView.Adapter<RVTestAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> data = new ArrayList<>();
    public RVTestAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < 20; i++) {
            data.add("文本"+i);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_rv_test,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvContent.setText(""+data.get(position));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvContent;
        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
        }
    }

    public void setData(ArrayList<String> data) {
        this.data.clear();
        this.data = data;
        notifyDataSetChanged();
    }
    public void addData(ArrayList<String> data){
        for (int i = 0; i < data.size(); i++) {
            this.data.add(data.get(i));
        }
        notifyDataSetChanged();
    }

}
