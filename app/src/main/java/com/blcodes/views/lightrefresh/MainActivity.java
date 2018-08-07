package com.blcodes.views.lightrefresh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blcodes.views.lightrefresh.activity.NormalViewTestActivity;
import com.blcodes.views.lightrefresh.activity.RecyclerAndViewPagerActivity;
import com.blcodes.views.lightrefresh.activity.RecyclerViewTestActivity;
import com.blcodes.views.lightrefresh.activity.ScrollerViewTestActivity;
import com.blcodes.views.lightrefresh.activity.ViewpagerBannerTestActivity;
import com.blcodes.views.lightrefresh.activity.WebViewTestActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnView = findViewById(R.id.btn_view_test);
        btnView.setOnClickListener(this);
        Button btnBanner = findViewById(R.id.btn_banner_test);
        btnBanner.setOnClickListener(this);
        Button btnScrollerView = findViewById(R.id.btn_scrollerview_test);
        btnScrollerView.setOnClickListener(this);
        Button btnRecycler = findViewById(R.id.btn_recycler_test);
        btnRecycler.setOnClickListener(this);
        Button btnWebView = findViewById(R.id.btn_webview_test);
        btnWebView.setOnClickListener(this);
        Button btnRVAndVP = findViewById(R.id.btn_rv_vp_test);
        btnRVAndVP.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id. btn_view_test:
                Intent intentView = new Intent(this,NormalViewTestActivity.class);
                startActivity(intentView);
                break;
            case R.id.btn_banner_test:
                Intent intentBanner = new Intent(this,ViewpagerBannerTestActivity.class);
                startActivity(intentBanner);
                break;
            case R.id. btn_scrollerview_test:
                Intent intentScrollerView = new Intent(this,ScrollerViewTestActivity.class);
                startActivity(intentScrollerView);
                break;
            case R.id. btn_recycler_test:
                Intent intentRecycler = new Intent(this,RecyclerViewTestActivity.class);
                startActivity(intentRecycler);
                break;
            case R.id. btn_webview_test:
                Intent intentWebView = new Intent(this,WebViewTestActivity.class);
                startActivity(intentWebView);
                break;
            case R.id. btn_rv_vp_test:
                Intent intentRVAndVP = new Intent(this,RecyclerAndViewPagerActivity.class);
                startActivity(intentRVAndVP);
                break;
        }
    }
}
