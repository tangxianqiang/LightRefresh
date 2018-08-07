package com.blcodes.views.lightrefresh.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.FrameLayout;

import com.blcodes.views.lightrefresh.R;
import com.blcodes.views.refresh.BounceLayout;
import com.blcodes.views.refresh.EventForwardingHelper;
import com.blcodes.views.refresh.NormalBounceHandler;
import com.blcodes.views.refresh.BounceCallBack;
import com.blcodes.views.refresh.footer.DefaultFooter;
import com.blcodes.views.refresh.header.DefaultHeader;

public class NormalViewTestActivity extends Activity {
    private static final String TAG = "NormalViewTestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_view_test);
        FrameLayout rootView = findViewById(R.id.fl_root);
        final BounceLayout bounceLayout = findViewById(R.id.bl);
        bounceLayout.setBounceHandler(new NormalBounceHandler(),bounceLayout.getChildAt(0));
        bounceLayout.setEventForwardingHelper(new EventForwardingHelper() {
            @Override
            public boolean notForwarding(float downX, float downY, float moveX, float moveY) {
                return true;
            }
        });
        bounceLayout.setHeaderView(new DefaultHeader(this),rootView);
        bounceLayout.setFooterView(new DefaultFooter(this),rootView);
        bounceLayout.setBounceCallBack(new BounceCallBack() {
            @Override
            public void startRefresh() {
                Log.i(TAG, "startRefresh: ");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bounceLayout.setRefreshCompleted();
                    }
                },6000);
            }

            @Override
            public void startLoadingMore() {
                Log.i(TAG, "startLoadingMore: ");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bounceLayout.setLoadingMoreCompleted();
                    }
                },2000);
            }
        });

    }
}
