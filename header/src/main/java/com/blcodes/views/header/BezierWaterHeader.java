package com.blcodes.views.header;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.blcodes.views.refresh.header.BaseHeaderView;

public class BezierWaterHeader extends BaseHeaderView {
    private static final String TAG = "BezierWaterHeader";
    /*刷新头的状态*/
    private int status;
    /*头布局高度*/
    private int childHeight;
    /*布局偏移量*/
    private  float totalOffset;
    private Context mContext;
    /**/
    private Path mPath;
    private Paint mPaint;
    private int mColor;
    private float mWidth;
    private float radius;
    private ImageView load;


    public BezierWaterHeader(Context context) {
        this(context,null);
    }

    public BezierWaterHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public BezierWaterHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs,defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setWillNotDraw(false);
        mContext = context;
        childHeight = mContext.getResources().getDimensionPixelSize(R.dimen.bezier_water_height);
        radius = mContext.getResources().getDimensionPixelSize(R.dimen.water_radius);
        status = HEADER_DRAG;
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);//抗锯齿
        mPaint.setDither(true);//防抖动
        mPaint.setStyle(Paint.Style.FILL);//填充方式
        mColor = Color.BLUE;
        mPaint.setColor(mColor);
        setLayerType(View.LAYER_TYPE_SOFTWARE, mPaint);
        rectF = new RectF();



    }
    public void addLoad(){
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams((int)radius -10,(int)radius - 10);
        load = new ImageView(mContext);
        load.setBackgroundResource(R.drawable.loading);
        lp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        lp.bottomMargin = (int) (10 + radius/2);
        this.addView(load,lp);
    }

    /**
     * 控制状态、位置、大小
     * @param dragY
     */
    @Override
    public void handleDrag(float dragY) {

        if (!isLoading) {
            this.totalOffset = dragY;
        }
        if (status == HEADER_REFRESHING) {//如果是正在刷新，不能改变位置大小和移动
            return;
        }
        //设置header大小和位置------------start---------
        FrameLayout.LayoutParams lp = (LayoutParams) getLayoutParams();
        if (lp!=null) {
            lp.gravity = Gravity.TOP;
            lp.height =  dragY>0? (int) dragY :0;
            if(lp.height>=0 && load==null){
                addLoad();
            }
            lp.topMargin = (int) - dragY;
            setLayoutParams(lp);
        }
        //设置header大小和位置------------end---------
        setTranslationY((int)dragY);//跟随BounceLayout移动
        FrameLayout.LayoutParams lpLoad = (LayoutParams) load.getLayoutParams();
        if (dragY >= 3 * childHeight) {
            lpLoad.bottomMargin = (int) (totalOffset - height +(10 + radius/2));
        }else{
            lpLoad.bottomMargin = (int) (10 + radius/2);
        }
        load.setLayoutParams(lpLoad);
        rotateLoad();
        if (dragY<=0) {
            status = HEADER_DRAG;
        }

        if (status == HEADER_DRAG) {//开始拖动
            if(dragY >= 2* childHeight) {//一旦超过刷新头高度
                status = HEADER_RELEASE;
            }
        }
        if(status == HEADER_RELEASE){//还未释放拖拉回去
            if(dragY <= childHeight) {//一旦低于刷新头高度
                status = HEADER_DRAG;
            }
        }
        updatePath();
        invalidate();
    }

    @Override
    public boolean doRefresh() {
        if (status == HEADER_REFRESHING && totalOffset == childHeight) {//正在刷新，并且偏移量==刷新头高度才认为刷新
            return true;
        }
        return false;
    }

    @Override
    public void setParent(ViewGroup parent) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.TOP;
        lp.topMargin = (int) - totalOffset;
        parent.addView(this,lp);
    }

    /**
     * 由于固定后手指抬起将不会回弹header，所以手动回弹
     * @return
     */
    @Override
    public boolean checkRefresh() {
        if ((status == HEADER_RELEASE || status == HEADER_REFRESHING) && totalOffset>= 2 * childHeight) {
            //
            bounceHeader();
            return true;
        }else{
            return false;
        }
    }

    @Override
    public void refreshCompleted() {
        loadingAnimation.cancel();
        status = HEADER_COMPLETED;
    }

    @Override
    public int getHeaderHeight() {
        return childHeight;
    }

    @Override
    public void autoRefresh() {
        status = HEADER_REFRESHING;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setColor(Color.BLUE);
        canvas.drawPath(mPath,mPaint);
        rectF.left = mWidth/2-radius;
        rectF.top = height - radius*2;
        rectF.right = rectF.left + radius*2;
        rectF.bottom = height;
//        mPaint.setColor(Color.GRAY);
        canvas.drawOval(rectF,mPaint);
        mPaint.setColor(Color.GRAY);
        rectF.left = rectF.left + 16;
        rectF.right = rectF.right -16;
        rectF.top = rectF.top +16;
        rectF.bottom = rectF.bottom -16;
        canvas.drawOval(rectF,mPaint);
        mPath.reset();
    }
    public void updatePath(){
        mPath.moveTo(0,0);
        mPath.lineTo(mWidth,0);
        float angle = totalOffset / (3 * childHeight) * 110;
        height = totalOffset;
        if(totalOffset >= 3*childHeight){
            angle = 110;
            height = 3*childHeight;
        }
        double radian = Math.toRadians(angle);
        float x = mWidth/2 + (float) (radius * Math.sin(radian)) +
                (float)((height  - radius + radius * Math.cos(radian)) / Math.tan(radian));
        //(float) (totalOffset - radius + (float) (radius * Math.cos(radian))/Math.tan(radian))
        mPath.quadTo(x
                ,0
                ,mWidth/2 + (float) (radius * Math.sin(radian))
                ,height - radius + (float) (radius * Math.cos(radian)));
        mPath.lineTo(mWidth/2 - (float) (radius * Math.sin(radian))
                ,height - radius + (float) (radius * Math.cos(radian)));
        mPath.quadTo(mWidth/2 - (float) (radius * Math.sin(radian))
                        - (float)( (height  - radius + radius * Math.cos(radian)) / Math.tan(radian))
                ,0
                ,0
                ,0);
    }

    public void setColor(int mColor) {
        this.mColor = mColor;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
    }
    private float height;
    RectF rectF;
    public void rotateLoad(){
        float angle = totalOffset / (3* childHeight) * 360;
        load.setRotation(angle);
    }
    public void bounceHeader(){
        animator =  ValueAnimator.ofFloat(0,1f);
        if (totalOffset >3 * childHeight) {
            totalOffset = childHeight * 3;
        }
        final float distance = totalOffset - childHeight;
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                float value = (float) valueAnimator.getAnimatedValue();
                totalOffset = (1 -value) * distance  + childHeight;
                handleDrag(totalOffset);
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                status = HEADER_REFRESHING;
                isLoading = false;
                startRotate();

            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                isLoading = true;
            }


        });
        animator.setDuration(300);
        animator.start();
    }
    ValueAnimator animator;
    private boolean isLoading;

    public void startRotate(){
        load.clearAnimation();
        if (loadingAnimation == null) {
            loadingAnimation = ValueAnimator.ofFloat(0,1f);
            loadingAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    float value = (float) valueAnimator.getAnimatedValue();
                    load.setRotation(value * 360);
                }
            });
            loadingAnimation.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);


                }

                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);

                }


            });
            loadingAnimation.setDuration(500);
            loadingAnimation.setRepeatCount(-1);
            loadingAnimation.setInterpolator(new LinearInterpolator());
            loadingAnimation.start();
        }else{
            loadingAnimation.start();
        }

    }
    private ValueAnimator loadingAnimation;
}
