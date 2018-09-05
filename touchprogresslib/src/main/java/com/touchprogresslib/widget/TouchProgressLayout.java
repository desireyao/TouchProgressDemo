package com.touchprogresslib.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.touchprogresslib.R;

/**
 * Created by yaoh on 2018/8/22.
 */

public class TouchProgressLayout extends FrameLayout implements GestureDetector.OnGestureListener {

    private static final String TAG = "CurtainLayout";

    private Context mContext;
    private TouchProgressView mTouchProgressView;
    private TextView tv_text_progress;

    private GestureDetector mGestureDetector;

    private float mCurProgress = 100.f;
    private int mStartTouchY = -1;

    private OnProgressChangedLisnener mProgressChangedLisnener;

    private boolean isScrool;

    public void setOnProgressChangedLisnener(OnProgressChangedLisnener mLisnener) {
        this.mProgressChangedLisnener = mLisnener;
    }

    public TouchProgressLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;

        mGestureDetector = new GestureDetector(context, this);
        mGestureDetector.setIsLongpressEnabled(false);

        initView();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        LogTool.LogE_DEBUG(TAG, "onTouchEvent---> event: " + event);

        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isScrool) {
                // 抬起手指
                if (mProgressChangedLisnener != null) {
                    mProgressChangedLisnener.onStopChanged((int) mCurProgress);
                }
                isScrool = false;
            }
        }
        return mGestureDetector.onTouchEvent(event);
    }

    private void initView() {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.include_item_touch_progress_layout, null);
        mTouchProgressView = mView.findViewById(R.id.curtainview);
        tv_text_progress = mView.findViewById(R.id.tv_text_progress);

        initProgress(100);

        addView(mView);
    }


    public void initProgress(int progress) {
        tv_text_progress.setText(String.valueOf(progress));
        mCurProgress = progress;
    }

    /**
     * 设置幅度
     *
     * @param progress
     */
    public void updateProgress(int progress) {
        tv_text_progress.setText(String.valueOf(progress));
        mTouchProgressView.setProgress(progress);
    }

    @Override
    public boolean onDown(MotionEvent e) {
//        LogTool.LogE_DEBUG(TAG, "onDown ---> e = " + e.toString());
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
//        LogTool.LogE_DEBUG(TAG, "onShowPress ---> e = " + e.toString());
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
//        LogTool.LogE_DEBUG(TAG, "onSingleTapUp ---> e = " + e.toString());
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        Log.e(TAG, "onScroll ---> distanceY = " + distanceY
//                + " e1.getY() = " + e1.getY()
//                + " e2.getY() = " + e2.getY()
//                + " mHeight = " + getHeight());

        if (mStartTouchY == -1) {
            mStartTouchY = (int) (mCurProgress / 100 * getHeight());
        }

        mStartTouchY -= distanceY;
        if (mStartTouchY > getHeight()) {
            mStartTouchY = getHeight();
        } else if (mStartTouchY < 0) {
            mStartTouchY = 0;
        }

        mCurProgress = mStartTouchY * 1.f / getHeight() * 100.f;
        isScrool = true;

        updateProgress((int) mCurProgress);

        if (mProgressChangedLisnener != null) {
            mProgressChangedLisnener.onScrollChanged((int) mCurProgress);
        }

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    public interface OnProgressChangedLisnener {

        public void onScrollChanged(int progress);

        public void onStopChanged(int progress);
    }
}
