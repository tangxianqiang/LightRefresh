package com.blcodes.views.lightrefresh.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.blcodes.views.lightrefresh.R;
import com.blcodes.views.lightrefresh.banner.ConvenientBanner;
import com.blcodes.views.lightrefresh.banner.holder.CBViewHolderCreator;
import com.blcodes.views.lightrefresh.banner.holder.Holder;

import java.util.ArrayList;
import java.util.List;

public class RVAndVPAdapter extends RecyclerView.Adapter<RVAndVPAdapter.ViewHolder> {
    private boolean startBanner;
    private static final int HEADER = 0;
    private static final int CONTENT = 1;
    private Context context;
    private ArrayList<String> data = new ArrayList<>();
    private List<Integer> localImages = new ArrayList<>();

    public RVAndVPAdapter(Context context) {
        this.context = context;
        for (int i = 0; i < 20; i++) {
            data.add("文本"+i);
        }
        localImages.add(R.drawable.img1);
        localImages.add(R.drawable.img2);
        localImages.add(R.drawable.img3);
        localImages.add(R.drawable.img4);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == CONTENT) {
            view = LayoutInflater.from(context).inflate(R.layout.item_rv_test,parent,false);
        }else {
            view = LayoutInflater.from(context).inflate(R.layout.item_rv_vp_header,parent,false);
        }
        return new ViewHolder(view,viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position == 0){
            if (!startBanner) {
                holder.convenientBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
                    @Override
                    public LocalImageHolderView createHolder() {
                        return new LocalImageHolderView();
                    }
                }, localImages)
                        //设置指示器是否可见
                        .setPointViewVisible(true)
                        //设置自动切换（同时设置了切换时间间隔）
                        .startTurning(2000)
                        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
                        //设置指示器的方向（左、中、右）
                        .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
                //设置点击监听事
                startBanner = true;
            }else{

            }


        }else{
            holder.textView.setText(data.get(position-1));
        }
    }



    @Override
    public int getItemCount() {
        return data.size()+1;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER;
        }else {
            return CONTENT;
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        ConvenientBanner convenientBanner;
        TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
        }
        public ViewHolder(View itemView,int type) {
            super(itemView);
            if (type == HEADER) {
                convenientBanner = itemView.findViewById(R.id.cb);
            }else{
                textView = itemView.findViewById(R.id.tv_content);
            }
        }
    }

    private class LocalImageHolderView implements Holder<Integer> {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            //可以通过layout文件来创建，不一定是Image，任何控件都可以进行翻页
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, int position, Integer data) {
            imageView.setImageResource(data);
        }
    }

}
