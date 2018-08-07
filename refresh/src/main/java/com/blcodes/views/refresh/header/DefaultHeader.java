package com.blcodes.views.refresh.header;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.blcodes.views.refresh.R;

public class DefaultHeader extends BaseHeaderView {
    private static final String TAG = "DefaultHeader";
    private int status;
    private int childHeight;
    private float totalOffset;
    private Context mContext;
    private TextView tvHeaderTip;
    private ImageView ivHeaderTip;
    private ProgressBar pbRefreshing;
    public DefaultHeader(Context context) {
        this(context,null);
    }

    public DefaultHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DefaultHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }
    protected  void init(Context context, AttributeSet attrs, int defStyleAttr){
        setBackgroundColor(Color.parseColor("#eeeeee"));
        mContext = context;
        status = HEADER_DRAG;
        childHeight = mContext.getResources().getDimensionPixelSize(R.dimen.default_height);
        //添加头内容
        View view = LayoutInflater.from(context).inflate(R.layout.header_default,this,false);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                ,childHeight);
        addView(view,lp);
        tvHeaderTip = view.findViewById(R.id.tv_tip);
        pbRefreshing = view.findViewById(R.id.pb_refreshing);
        ivHeaderTip = view.findViewById(R.id.iv_tip);
    }

    /**
     * 将移动的距离传递过来
     */
    @Override
    public void handleDrag(float dragY) {
        this.totalOffset = dragY;
        setTranslationY(dragY);
        if (status == HEADER_REFRESHING) {//只要是正在刷新
            return;
        }
        if(dragY <= 0){//回到初始位置
            status = HEADER_DRAG;
            ivHeaderTip.setBackgroundResource(R.drawable.down);
            ivHeaderTip.setVisibility(VISIBLE);
            tvHeaderTip.setText(mContext.getResources().getString(R.string.refresh_drag));
            pbRefreshing.setVisibility(GONE);
        }
        if (status == HEADER_DRAG) {//开始拖动
            if(dragY >= childHeight) {//一旦超过刷新头高度
                status = HEADER_RELEASE;
                ivHeaderTip.setVisibility(VISIBLE);
                startRotateAnimDown();
                tvHeaderTip.setText(mContext.getResources().getString(R.string.refresh_release));
                pbRefreshing.setVisibility(GONE);
            }
        }
        if(status == HEADER_RELEASE){//还未释放拖拉回去
            if(dragY <= childHeight) {//一旦低于刷新头高度
                status = HEADER_DRAG;
                ivHeaderTip.setVisibility(VISIBLE);
                startRotateAnimUp();
                tvHeaderTip.setText(mContext.getResources().getString(R.string.refresh_drag));
                pbRefreshing.setVisibility(GONE);
            }
        }
    }

    @Override
    public boolean doRefresh() {
        if (status == HEADER_REFRESHING && totalOffset == childHeight) {//正在刷新，并且偏移量==刷新头高度
            return true;
        }
        return false;
    }

    @Override
    public void setParent(ViewGroup parent) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = - childHeight;
        parent.addView(this,lp);
    }

    @Override
    public boolean checkRefresh() {
        if ((status == HEADER_RELEASE || status == HEADER_REFRESHING) && totalOffset>=childHeight) {
            status = HEADER_REFRESHING;
            ivHeaderTip.clearAnimation();
            ivHeaderTip.setVisibility(GONE);
            tvHeaderTip.setText(mContext.getResources().getString(R.string.refreshing));
            pbRefreshing.setVisibility(VISIBLE);
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void refreshCompleted() {
        status = HEADER_COMPLETED;
        ivHeaderTip.clearAnimation();
        ivHeaderTip.setBackgroundResource(R.drawable.completed);
        ivHeaderTip.setVisibility(VISIBLE);
        tvHeaderTip.setText(mContext.getResources().getString(R.string.refresh_completed));
        pbRefreshing.setVisibility(GONE);
    }

    @Override
    public int getHeaderHeight() {
        return childHeight;
    }
    private void startRotateAnimDown(){
        RotateAnimation animation = new RotateAnimation(0
                , 180
                , Animation.RELATIVE_TO_SELF
                , 0.5f
                , Animation.RELATIVE_TO_SELF
                ,0.5f);
        animation.setDuration(120);
        animation.setFillAfter(true);//保持最后的状态
        ivHeaderTip.startAnimation(animation);
    }
    private void startRotateAnimUp(){
        RotateAnimation animation = new RotateAnimation(180
                , 0
                , Animation.RELATIVE_TO_SELF
                , 0.5f
                , Animation.RELATIVE_TO_SELF
                ,0.5f);
        animation.setDuration(120);
        animation.setFillAfter(true);//保持最后的状态
        ivHeaderTip.startAnimation(animation);
    }
}
