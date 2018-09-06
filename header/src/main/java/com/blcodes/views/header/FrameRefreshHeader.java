
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.blcodes.views.refresh.header.BaseHeaderView;


public class FrameRefreshHeader extends BaseHeaderView {
    private static final String TAG = "DefaultHeader";
    /*刷新头的状态*/
    private int status;
    /*头布局高度*/
    private int childHeight;
    /*布局偏移量*/
    private float totalOffset;
    private Context mContext;
    private AnimationDrawable frameAnimation;
    /*-----------刷新头布局视图内容---------------*/
    private TextView tvHeaderTip;
    private ImageView ivHeaderTip;

    public FrameRefreshHeader(Context context) {
        this(context, null);
    }

    public FrameRefreshHeader(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FrameRefreshHeader(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        setBackgroundColor(Color.parseColor("#ffffff"));
        mContext = context;
        status = HEADER_DRAG;
        childHeight = mContext.getResources().getDimensionPixelSize(R.dimen.frame_header_height);
        //添加头内容
        View view = LayoutInflater.from(context).inflate(R.layout.header_frame_loading, this, false);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                , childHeight);
        addView(view, lp);
        tvHeaderTip = (TextView) view.findViewById(R.id.tv_tip);
        ivHeaderTip = (ImageView) view.findViewById(R.id.iv_tip);
        setXml2FrameAnim();
    }

    /**
     * @param v 整个布局的移动距离
     */
    @Override
    public void handleDrag(float v) {
        this.totalOffset = v;
        if (canTranslation) {
            setTranslationY(v);
        }
        if (status == HEADER_REFRESHING) {//只要是正在刷新
            return;
        }
        if (v <= 0) {//回到初始位置
            status = HEADER_DRAG;
            tvHeaderTip.setText(mContext.getResources().getString(R.string.refresh_drag));
            stopFrameAnim();
        }
        if (status == HEADER_DRAG) {//开始拖动
            if (v >= childHeight) {//一旦超过刷新头高度
                status = HEADER_RELEASE;
                tvHeaderTip.setText(mContext.getResources().getString(R.string.refresh_release));
                stopFrameAnim();
            }
        }
        if (status == HEADER_RELEASE) {//还未释放拖拉回去
            if (v <= childHeight) {//一旦低于刷新头高度
                status = HEADER_DRAG;
                tvHeaderTip.setText(mContext.getResources().getString(R.string.refresh_drag));
                stopFrameAnim();
            }
        }
    }

    @Override
    public boolean doRefresh() {
        if (status == HEADER_REFRESHING && totalOffset == childHeight) {//正在刷新，并且偏移量==刷新头高度才认为刷新
            return true;
        }
        return false;
    }

    @Override
    public void setParent(ViewGroup viewGroup) {
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT
                ,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.topMargin = - childHeight;
        viewGroup.addView(this,lp);
    }

    @Override
    public boolean checkRefresh() {
        if ((status == HEADER_RELEASE || status == HEADER_REFRESHING) && totalOffset >= childHeight) {
            status = HEADER_REFRESHING;
            tvHeaderTip.setText(mContext.getResources().getString(R.string.refreshing));
            startFrameAnim();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void refreshCompleted() {
        status = HEADER_COMPLETED;
        tvHeaderTip.setText(mContext.getResources().getString(R.string.refresh_completed));
        stopFrameAnim();
    }

    @Override
    public int getHeaderHeight() {
        return childHeight;
    }

    @Override
    public void autoRefresh() {
        status = HEADER_REFRESHING;
        tvHeaderTip.setText(mContext.getResources().getString(R.string.refreshing));
        startFrameAnim();
    }

    /**
     * 通过XML添加帧动画
     */
    private void setXml2FrameAnim() {
        // 通过逐帧动画的资源文件获得AnimationDrawable示例
        frameAnimation = (AnimationDrawable) getResources().getDrawable(
                R.drawable.loading_frame);
        ivHeaderTip.setBackground(frameAnimation);
    }
    private void startFrameAnim(){
        Log.i(TAG, "startFrameAnim: start");
        if (frameAnimation != null && !frameAnimation.isRunning()) {
            frameAnimation.start();
        }
    }
    private void stopFrameAnim(){
        Log.i(TAG, "stopFrameAnim: stop");
        if (frameAnimation != null && frameAnimation.isRunning()) {
            frameAnimation.stop();
        }
    }

}
