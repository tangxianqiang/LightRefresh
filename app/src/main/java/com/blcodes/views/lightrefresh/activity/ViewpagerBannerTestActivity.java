package com.blcodes.views.lightrefresh.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blcodes.views.lightrefresh.R;
import com.blcodes.views.lightrefresh.banner.ConvenientBanner;
import com.blcodes.views.lightrefresh.banner.holder.CBViewHolderCreator;
import com.blcodes.views.lightrefresh.banner.holder.Holder;
import com.blcodes.views.refresh.BounceCallBack;
import com.blcodes.views.refresh.BounceLayout;
import com.blcodes.views.refresh.EventForwardingHelper;
import com.blcodes.views.refresh.ForwardingHelper;
import com.blcodes.views.refresh.NormalBounceHandler;
import com.blcodes.views.refresh.header.DefaultHeader;

import java.util.ArrayList;
import java.util.List;

public class ViewpagerBannerTestActivity extends Activity {
    private static final String TAG = "ViewpagerBannerTestAct";
    private List<Integer> localImages = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_banner_test);
        FrameLayout rootView = findViewById(R.id.fl_root);
        final BounceLayout bl = findViewById(R.id.bl);
        ConvenientBanner convenientBanner = findViewById(R.id.cb);
        bl.setBounceHandler(new NormalBounceHandler(),convenientBanner);
        bl.setEventForwardingHelper(new EventForwardingHelper() {
            @Override
            public boolean notForwarding(float downX, float downY, float moveX, float moveY) {
                return !ForwardingHelper.isXMore(downX,downY,moveX,moveY);
            }
        });
        bl.setHeaderView(new DefaultHeader(this),rootView);
        bl.setBounceCallBack(new BounceCallBack() {
            @Override
            public void startRefresh() {
                Log.i(TAG, "startRefresh: ");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bl.setRefreshCompleted();
                    }
                },2000);
            }

            @Override
            public void startLoadingMore() {

            }
        });
        localImages.add(R.drawable.img1);
        localImages.add(R.drawable.img2);
        localImages.add(R.drawable.img3);
        localImages.add(R.drawable.img4);



        convenientBanner.setPages(new CBViewHolderCreator<LocalImageHolderView>() {
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
