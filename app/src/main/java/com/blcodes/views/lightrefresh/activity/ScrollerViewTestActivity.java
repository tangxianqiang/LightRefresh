package com.blcodes.views.lightrefresh.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.blcodes.views.lightrefresh.R;
import com.blcodes.views.refresh.BounceCallBack;
import com.blcodes.views.refresh.BounceLayout;
import com.blcodes.views.refresh.EventForwardingHelper;
import com.blcodes.views.refresh.NormalBounceHandler;
import com.blcodes.views.refresh.header.DefaultHeader;

public class ScrollerViewTestActivity extends Activity {
    private static final String TAG = "ScrollerViewTestActivit";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrollerview_test);
        final BounceLayout bounceLayout = findViewById(R.id.bl);
        FrameLayout rootView = findViewById(R.id.fl_root);
        ScrollView sv = findViewById(R.id.sv);
        bounceLayout.setBounceHandler(new NormalBounceHandler(),sv);
        bounceLayout.setEventForwardingHelper(new EventForwardingHelper() {
            @Override
            public boolean notForwarding(float downX, float downY, float moveX, float moveY) {
                return true;
            }
        });
        bounceLayout.setHeaderView(new DefaultHeader(this),rootView);
        bounceLayout.setBounceCallBack(new BounceCallBack() {
            @Override
            public void startRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bounceLayout.setRefreshCompleted();
                    }
                },2000);
            }

            @Override
            public void startLoadingMore() {

            }
        });

    }
}
