package com.library.viewspread.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.library.viewspread.helper.BaseViewHelper;

/**
 * Created by zhangke on 2017/1/6.
 */

 public class BaseView extends View {
    /** 绘制视图的大小 **/
    private Rect mRect = new Rect();
    /** 绘制的视图 **/
    private View mView;
    /** 绘制遮罩的画笔 **/
    private Paint mPaint;
    /** 清除遮罩的画笔 **/
    private Paint mClearPaint;
    /** 遮罩控制器 **/
    private BaseViewHelper mHelper;
    /** 是否开始绘制遮罩 **/
    private boolean isStart = false;
    /** 半径 **/
    private float radius = 0;
    BaseViewHelper.Builder mBuilder;
    float mScaleXCanvas = 0;
    float mScaleYCanvas = 0;
    float mRotationCanvas = 0;
    float mTranslationX, mTranslationY;
    Matrix matrix = new Matrix();
    public BaseView(Context context,int mDimcolor, int mDimalpha){
        super(context);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mDimcolor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10);
        mPaint.setAlpha(mDimalpha);

        mClearPaint = new Paint(mPaint);
        mClearPaint.setColor(Color.RED);
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        setLayerType(LAYER_TYPE_SOFTWARE,null);
    }

    public BaseView(Context context, Rect mRect, View mView, int mDimcolor, int mDimalpha) {
        super(context);
        this.mRect = mRect;
        this.mView = mView;
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mDimcolor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(10);
        mPaint.setAlpha(mDimalpha);

        mClearPaint = new Paint(mPaint);
        mClearPaint.setColor(Color.RED);
        mClearPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        setLayerType(LAYER_TYPE_SOFTWARE,null);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0,0,getWidth(),getBottom(),mPaint);

        if (!isStart && mView!=null){
            canvas.save();

            matrix.reset();
            matrix.postTranslate(mRect.left ,mRect.top);
            matrix.postScale(mScaleXCanvas,mScaleYCanvas,mRect.centerX(),mRect.centerY());
            matrix.postRotate(mRotationCanvas,mRect.centerX(),mRect.centerY());

            canvas.concat(matrix);
            mView.draw(canvas);
            canvas.restore();
        }else {
            canvas.drawCircle(mRect.centerX(),mRect.centerY(),radius,mClearPaint);
        }

    }

    public void startAnim(float start,float end){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(start,end);
        valueAnimator.setDuration(800);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radius = (float) animation.getAnimatedValue();
                invalidate();
                if (animation.getAnimatedFraction() == 1 && mHelper!=null){
                    BaseView.this.animate().alpha(0).setDuration(300).setListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                        }
                        @Override
                        public void onAnimationEnd(Animator animation) {
                           remove();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {
                            remove();
                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).start();
                }
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }
            @Override
            public void onAnimationEnd(Animator animation) {
                if (mBuilder != null
                        && mBuilder.mOnAnimationListener!=null){
                    mBuilder.mOnAnimationListener.onAnimationEndIn();
                    mBuilder = null;
                }
            }
            @Override
            public void onAnimationCancel(Animator animation) {
                if (mBuilder != null
                        && mBuilder.mOnAnimationListener!=null){
                    mBuilder.mOnAnimationListener.onAnimationEndIn();
                    mBuilder = null;
                }
            }
            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setInterpolator(new AccelerateDecelerateInterpolator());
        valueAnimator.start();
    }


    private void remove(){
        if (mHelper!=null){
            mHelper.removeAll();
            if (mHelper.builder != null && mHelper.builder.mOnAnimationListener!=null){
                mHelper.builder.mOnAnimationListener.onAnimationEndOut();
            }
            mHelper = null;
        }
    }

    public void setRect(Rect mRect){
        this.mRect = mRect;
    }

    public void setScaleXYCanvas(float mScaleXCanvasm,float mScaleYCanvas,float mRotationCanvas,float mTranslationX,float mTranslationY) {
        this.mScaleXCanvas = mScaleXCanvasm;
        this.mScaleYCanvas = mScaleYCanvas;
        this.mRotationCanvas = mRotationCanvas;

        mRect.offset((int) (mTranslationX - this.mTranslationX),(int) (mTranslationY - this.mTranslationY));

        this.mTranslationX = mTranslationX;
        this.mTranslationY = mTranslationY;


        isStart = false;
        invalidate();
    }
    public void setScaleXYCanvas(float mScaleXCanvasm,float mScaleYCanvas,float mRotationCanvas) {
        this.mScaleXCanvas = mScaleXCanvasm;
        this.mScaleYCanvas = mScaleYCanvas;
        this.mRotationCanvas=mRotationCanvas;
        invalidate();
    }

    public void go(boolean isback) {

        radius = 0;
        isStart =true;

        int width = getWidth() ;
        int height =getHeight();

       float maxline = (float) Math.sqrt(width * width + height * height);
        float maxlineRect = (float) Math.sqrt(mRect.right * mRect.right + mRect.bottom * mRect.bottom);
        if (isback){
            startAnim(Math.max(maxline,maxlineRect),0);
        }else {
            startAnim(0,Math.max(maxline,maxlineRect));
        }
    }

    /**
     * 返回
     * @param mHelper
     */
    public void back(BaseViewHelper mHelper){
        this.mHelper=mHelper;
        go(true);
    }
    /**
     * 开始
     * @param mBuilder
     */
    public void start(BaseViewHelper.Builder mBuilder) {
        this.mBuilder=mBuilder;
        go(false);
    }
}
