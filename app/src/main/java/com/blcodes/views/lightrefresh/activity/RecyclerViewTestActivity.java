package com.blcodes.views.lightrefresh.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.FrameLayout;

import com.blcodes.views.lightrefresh.R;
import com.blcodes.views.lightrefresh.adapter.RVTestAdapter;
import com.blcodes.views.refresh.BounceLayout;
import com.blcodes.views.refresh.EventForwardingHelper;
import com.blcodes.views.refresh.NormalBounceHandler;
import com.blcodes.views.refresh.BounceCallBack;
import com.blcodes.views.refresh.footer.DefaultFooter;
import com.blcodes.views.refresh.header.DefaultHeader;

import java.util.ArrayList;

/**
 * 列表添加刷新和加载更多
 */

public class RecyclerViewTestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_test);
        FrameLayout rootView = findViewById(R.id.fl_root);
        final BounceLayout bounceLayout = findViewById(R.id.bl);
        RecyclerView recyclerView = findViewById(R.id.rv_test);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final RVTestAdapter adapter = new RVTestAdapter(this);
        recyclerView.setAdapter(adapter);
        bounceLayout.setBounceHandler(new NormalBounceHandler(),recyclerView);//设置滚动冲突的控制类
        bounceLayout.setEventForwardingHelper(new EventForwardingHelper() {//自定义事件分发处理
            @Override
            public boolean notForwarding(float downX, float downY, float moveX, float moveY) {
                return true;
            }
        });
        bounceLayout.setHeaderView(new DefaultHeader(this),rootView);//设置刷新头，null意味着没有刷新头，不调用该函数意为着空
        bounceLayout.setFooterView(new DefaultFooter(this),rootView);
        bounceLayout.setBounceCallBack(new BounceCallBack() {//刷新回调
            @Override
            public void startRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> data = new ArrayList<>();
                        for (int i = 0; i < 16; i++) {
                            data.add("新文本"+i);
                        }
                        adapter.setData(data);
                        bounceLayout.setRefreshCompleted();
                    }
                },2000);
            }

            @Override
            public void startLoadingMore() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ArrayList<String> data = new ArrayList<>();
                        for (int i = 0; i < 6; i++) {
                            data.add("新增文本"+i);
                        }
                        adapter.addData(data);
                        bounceLayout.setLoadingMoreCompleted();
                    }
                },2000);
            }
        });

    }
}
