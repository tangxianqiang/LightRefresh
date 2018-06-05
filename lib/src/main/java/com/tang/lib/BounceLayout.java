package com.tang.lib;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewConfigurationCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;
import android.widget.Scroller;

/**
 * Created by Administrator on 2018/5/18 0018.
 *
 */

public class BounceLayout extends FrameLayout {
    private static final String TAG = "BounceLayout";
    /*滚动实例*/
    private Scroller mScroller;
    private Context mContext;
    private int mTouchSlop;
    /*手指按下的y位置*/
    private float mYDown;
    /*手机上一次移动的y轴位置*/
    private float mYLastMove;
    /*手指在不断移动时的y轴坐标*/
    private float mYMove;
    /*多点触控的时候，记录总的偏移量*/
    private float totalOffsetY;
    /*移动进行时，此时对应的手指*/
    private int currentPointer;
    /*移动进行时，对应的y*/
    private float currentY;
    /*手指index变化*/
    private boolean pointerChange;
    /*保证随时按下都可以开始滑动*/
    private boolean forceDrag;

    public BounceLayout(@NonNull Context context) {
        this(context, null);
    }

    public BounceLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BounceLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }
    private void init(Context context,  AttributeSet attrs, int defStyleAttr){
        //初始化滚动实例
        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值
        mTouchSlop = ViewConfigurationCompat.getScaledPagingTouchSlop(configuration);
    }

    /**
     * onInterceptTouchEvent方法默认并不会拦截并不会拦击子view的事件，但是我们要在这个方法里面
     * 写逻辑，告诉Bouncelayout什么时候、什么条件进行拦截，并且拦截之后交给自己的onTouchEvent进行消费，还有如何消费
     * @param ev
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN://动作按下,只是第一根手指才会触发
                currentPointer = 0;
                forceDrag = true;
                mYDown = ev.getY();// getRawY表示相对屏幕的位置，也就是绝对位置 和 getY表示相对父亲的位置
                mYLastMove = mYDown;//这样做是为了初始化 mYLastMove
                currentY = mYDown;
                break;
            case MotionEvent.ACTION_POINTER_DOWN://只有第一根以上的手指才会触发，第一根手指不会触发
                break;
            case MotionEvent.ACTION_POINTER_UP://只有第一根以上的手指才会触发，第一根手指不会触发
                break;
            case MotionEvent.ACTION_MOVE://任何手指都会触发
                mYMove = ev.getY();
                float distanceY = Math.abs(mYDown - mYMove);//这个方法表示手指移动距离手指按下的距离
                mYLastMove = mYMove;//每一次用了mYMove都保存起来
                //只要手指移动距离手指按下的距离 大于mTouchSlop，就认为是BounceLayout的下拉
                if (distanceY > mTouchSlop) {
                    return true;//交给自己的onTouchEvent方法。注意，只要onInterceptTouchEvent返回了true或者false，
                    // 那么它的子view将永远收不到该事件了，直到改事件结束？？false也不会传给子view吗。对，说白了，就是直到手指抬起，它的子view都收不到1
                }
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return super.onInterceptTouchEvent(ev);//默认是super
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_POINTER_DOWN://只有第一根以上的手指才会触发，第一根手指不会触发
                currentPointer = event.getActionIndex();
                currentY = event.getY(currentPointer);
                pointerChange = true;
                break;
            case MotionEvent.ACTION_POINTER_UP://只有第一根以上的手指才会触发，第一根手指不会触发，getPointerCount依然是之前的值，尽管松开了
                pointerChange = true;
                if (event.getPointerCount() == 2) {//说明即将只有一根手指了
                    currentPointer = 0;
                    currentY = mYLastMove;
                }else{
                    if (currentPointer == event.getActionIndex()) {//离开的是最近的手指
                        currentPointer = 0;
                        currentY = mYLastMove;
                    }else{

                    }
                }

                break;
            case MotionEvent.ACTION_MOVE://任何一根手指的移动只做累加,累加最后一次按下那根手指的移动距离
                mYMove = event.getY(currentPointer);
                if (pointerChange) {
                    currentY = mYMove;
                }
                float scrollY = mYMove - currentY;
                scrollY = scrollY / 2.5f;
                totalOffsetY += scrollY;
                scrollTo(0, (int) -totalOffsetY);
                currentY = mYMove;
                pointerChange = false;
                break;
            case MotionEvent.ACTION_UP:
                mScroller.startScroll(0,getScrollY(),0,-getScrollY(),1000);
                invalidate();
                forceDrag = false;
                break;
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        for (int i = 0; i < getChildCount(); i++) {
            if (!getChildAt(i).isClickable()) {
                getChildAt(i).setClickable(true);
            }
        }
    }

    @Override
    public void computeScroll() {
        if (forceDrag) {
            return;
        }
        if (mScroller.computeScrollOffset()) {
            totalOffsetY = -mScroller.getCurrY();
            scrollTo(0, mScroller.getCurrY());
            invalidate();
        }
    }

}
