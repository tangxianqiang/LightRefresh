package com.blcodes.views.lightrefresh.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.blcodes.views.lightrefresh.R;
import com.blcodes.views.lightrefresh.adapter.RVAndVPAdapter;
import com.blcodes.views.refresh.BounceLayout;
import com.blcodes.views.refresh.EventForwardingHelper;
import com.blcodes.views.refresh.ForwardingHelper;
import com.blcodes.views.refresh.NormalBounceHandler;

public class RecyclerAndViewPagerActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_vp_test);
        final BounceLayout bounceLayout = findViewById(R.id.bl);
        RecyclerView recyclerView = findViewById(R.id.rv_test);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        RVAndVPAdapter adapter = new RVAndVPAdapter(this);
        recyclerView.setAdapter(adapter);
        bounceLayout.setBounceHandler(new NormalBounceHandler(),bounceLayout.getChildAt(0));
        bounceLayout.setEventForwardingHelper(new EventForwardingHelper() {
            @Override
            public boolean notForwarding(float downX, float downY, float moveX, float moveY) {
                return !ForwardingHelper.isXMore(downX,downY,moveX,moveY);
            }
        });
    }
}
