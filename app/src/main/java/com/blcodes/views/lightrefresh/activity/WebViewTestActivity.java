package com.blcodes.views.lightrefresh.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.webkit.WebView;
import android.widget.FrameLayout;

import com.blcodes.views.lightrefresh.R;
import com.blcodes.views.refresh.BounceCallBack;
import com.blcodes.views.refresh.BounceLayout;
import com.blcodes.views.refresh.EventForwardingHelper;
import com.blcodes.views.refresh.NormalBounceHandler;
import com.blcodes.views.refresh.header.DefaultHeader;

public class WebViewTestActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview_test);
        FrameLayout rootView = findViewById(R.id.fl_root);
        final BounceLayout bounceLayout = findViewById(R.id.bl);
        WebView webView = findViewById(R.id.wv_test);
        webView.loadUrl("file:///android_asset/index.html");
        bounceLayout.setBounceHandler(new NormalBounceHandler(),webView);
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
