package com.touchprogresslib.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.touchprogresslib.R;

/**
 * Created by yaoh on 2018/8/22.
 * <p>
 * 显示 窗帘的 view
 */

public class TouchProgressView extends View {

    private int mCurtainWidth;
    private int mCurtainHeight;

    private Bitmap mSrcBitmap;
    private Bitmap mDstBitmap;

    private int mProgress = 100;

    public TouchProgressView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        mSrcBitmap = makeSrcBitmap();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(mSrcBitmap.getWidth(), mSrcBitmap.getHeight());
    }

    @SuppressLint("WrongConstant")
    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if (mCurtainWidth == 0) {
            return;
        }

        if (mProgress == 0) {
            return;
        }

        Paint paint = new Paint();
        paint.setFilterBitmap(false);
        paint.setStyle(Paint.Style.FILL);

        int sc = canvas.saveLayer(0, 0, mCurtainWidth, mCurtainHeight, null,
                Canvas.MATRIX_SAVE_FLAG |
                        Canvas.CLIP_SAVE_FLAG |
                        Canvas.HAS_ALPHA_LAYER_SAVE_FLAG |
                        Canvas.FULL_COLOR_LAYER_SAVE_FLAG |
                        Canvas.CLIP_TO_LAYER_SAVE_FLAG);

        mDstBitmap = makeDstBitmap();
        canvas.drawBitmap(mDstBitmap, 0, 0, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(mSrcBitmap, 0, 0, paint);
        paint.setXfermode(null);

        // 还原画布
        canvas.restoreToCount(sc);
    }

    private Bitmap makeSrcBitmap() {
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.img_item_bg);

        mCurtainWidth = bitmap.getWidth();
        mCurtainHeight = bitmap.getHeight();

        return bitmap;
    }

    private Bitmap makeDstBitmap() {
        int height = (int) (mProgress / 100.f * mCurtainHeight);
        Bitmap bm = Bitmap.createBitmap(mCurtainWidth, height, Bitmap.Config.ARGB_8888);
        Canvas canvcs = new Canvas(bm);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLACK);
        canvcs.drawRect(new RectF(0, 0, mCurtainWidth, height), paint);
        return bm;
    }

    public void setProgress(int progress) {

        if (progress == 0) {
            setVisibility(View.INVISIBLE);

        } else {
            setVisibility(View.VISIBLE);
            mProgress = progress;
            postInvalidate();
        }
    }
}
