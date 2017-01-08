package com.library.viewspread.helper;

import android.animation.Animator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import com.library.viewspread.view.BaseView;
import com.library.viewspread.view.BaseFramlayout;

/**
 * Created by zhangke on 2017/1/6.
 */

public class BaseViewHelper extends AbstractViewHelper{
    /** 虚拟的显示过渡效果的控件 **/
    protected View mVirtalView;
    private BaseViewHelper(){}
    /**
     * @param mActivity
     * @param mStartView
     * @param mEndView
     * @param isFullWindow
     * @param mDimcolor
     * @param mDimalpha
     * @param mIsShowTransition
     */
    protected void addViewInWindow(final Activity mActivity
            , final View mStartView, final View mEndView, final boolean isFullWindow
            , final int mDimcolor, final int mDimalpha
            , final boolean mIsShowTransition){

        if (mStartView==null){
            return;
        }

        isShowing = true;
        final ViewGroup decorView;
        if (isFullWindow){
             decorView = (ViewGroup) mActivity.getWindow().getDecorView();
        }else {
             decorView = (ViewGroup) mActivity.getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
        }
        baseView = new BaseView(mActivity, builder.mDimcolor,builder.dimalpha);
        decorView.addView(baseView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mActivity.getWindow().getDecorView().post(new Runnable() {
                @Override
                public void run() {

                    frmlayout = new BaseFramlayout(mActivity);
                    if (AbstractViewHelper.mStartView ==null)
                        frmlayout.setBackgroundColor(Color.WHITE);
                    inflate = mEndView;
                    if (inflate!=null) {
                        builder.mTranslationView = inflate.findViewById(builder.mTranslationLayoutID);
                        frmlayout.addView(inflate, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                    }
                     rectInWindow = getRectInWindow(mStartView,isFullWindow);
                    //设置过渡效果动画
                    //有过渡视图
                    if (baseView!=null){
                        decorView.removeView(baseView);
                    }
                    if (builder.mTranslationView != null){
                        rectInWindowByTranslationView = getRectInWindow(builder.mTranslationView,isFullWindow);
                        baseView = new BaseView(mActivity,rectInWindowByTranslationView,null,mDimcolor,mDimalpha);
                    }else {
                        if (!mIsShowTransition){
                            //没有过渡视图
                            baseView = new BaseView(mActivity,rectInWindow,null,mDimcolor,mDimalpha);
                        }else {
                            baseView = new BaseView(mActivity,rectInWindow,mStartView,mDimcolor,mDimalpha);
                        }
                    }

                    frmlayout.addView(baseView,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

                    decorView.addView(frmlayout,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));


                    if (builder.mTranslationView!=null){
                        builder.mTranslationView.post(new Runnable() {
                            @Override
                            public void run() {
                                rectInWindowByTranslationView = getRectInWindow(builder.mTranslationView,isFullWindow);

                                builder.translationX = rectInWindowByTranslationView.centerX() - rectInWindow.centerX();
                                builder.translationY = rectInWindowByTranslationView.centerY() - rectInWindow.centerY();
                                builder.scaleX = rectInWindowByTranslationView.width() / (float) rectInWindow.width();
                                builder.scaleY = rectInWindowByTranslationView.height()  / (float) rectInWindow.height();
                                baseView.setRect(rectInWindowByTranslationView);
                            }
                        });
                    }
                    //
                    baseView.post(new Runnable() {
                        @Override
                        public void run() {
                            if (builder.mOnAnimationListener!=null){
                                //开始
                                builder.mOnAnimationListener.onAnimationStartIn();
                            }
                            if (builder.mTranslationView !=null ){
                                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(rectInWindow.width(), rectInWindow.height());
                                params.setMargins(rectInWindow.left, rectInWindow.top, rectInWindow.right, rectInWindow.bottom);

                                mVirtalView = new View(mActivity);
                                mVirtalView.setBackgroundDrawable(new BitmapDrawable(getBitmapWithView(builder.mTranslationView)));
                                mVirtalView.setLayoutParams(params);
                                frmlayout.addView(mVirtalView);
                                runEnterAnim(mVirtalView,builder.mTranslationView);
                                return;
                            }

                            if (mStartView == null){
                                isShowing = true;
                                baseView.start(builder);
                            }else {
                                if (mIsShowTransition){
                                    startAnim();
                                }else{
                                    isShowing = true;
                                    baseView.start(builder);
                                    AbstractViewHelper.mStartView = null;
                                }
                            }
                        }
                    });

                }
            });
    }

    /**
     * 模拟入场动画
     */
    private void runEnterAnim(View next_view,final View realNextView) {

        next_view.animate()
                .setInterpolator(new LinearInterpolator())
                .setDuration(300)
                .scaleX(builder.scaleX)
                .scaleY(builder.scaleY)
                .translationX(builder.translationX)
                .translationY(builder.translationY)
                .setListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        isShowing = true;

                    }
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        baseView.start(builder);
                        mStartView = null;
                    }
                    @Override
                    public void onAnimationCancel(Animator animation) {
                    }
                    @Override
                    public void onAnimationRepeat(Animator animation) {
                    }
                });
    }

    /**
     * 开始扩散动画
     */
    private void startAnim() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1)
                .setDuration(builder.mTranslationDuration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                    baseView.setScaleXYCanvas(1-(1-builder.scaleX)*animation.getAnimatedFraction(),1 - (1-builder.scaleY)*animation.getAnimatedFraction()
                            ,builder.rotation*animation.getAnimatedFraction()
                            ,builder.translationX*animation.getAnimatedFraction()
                            ,builder.translationY*animation.getAnimatedFraction());

            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isShowing = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                baseView.start(builder);
                mStartView = null;
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                mStartView = null;
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.setInterpolator(builder.interpolator);
        valueAnimator.start();
    }
    protected void addViewInWindow(final Activity mActivity
            , final View startView
            , final Intent mIntent, final boolean mIsActivityForResult
            , final int mRequestCode){

        if (mIntent!=null){
            this.mStartView =startView;
            this.mStartActivity = mActivity;
            if (!mIsActivityForResult){
                mActivity.startActivity(mIntent);
            }else {
                mActivity.startActivityForResult(mIntent,mRequestCode);
            }
            mActivity.overridePendingTransition(0,0);
        }
    }
    /**
     * 后退返回
     */
     public void back(){
         if (mVirtalView !=null){
             baseView.setRect(rectInWindow);
             mVirtalView.animate()
                     .setInterpolator(new LinearInterpolator())
                     .setDuration(builder.duration-200)
                     .scaleX(1)
                     .scaleY(1)
                     .translationX(0)
                     .translationY(0)
                     .setListener(new Animator.AnimatorListener() {
                         @Override
                         public void onAnimationStart(Animator animation) {
                         }
                         @Override
                         public void onAnimationEnd(Animator animation) {
                             frmlayout.removeView(mVirtalView);
                         }

                         @Override
                         public void onAnimationCancel(Animator animation) {
                             frmlayout.removeView(mVirtalView);
                         }
                         @Override
                         public void onAnimationRepeat(Animator animation) {
                         }
                     });
         }
         if (builder.mOnAnimationListener!=null){
             builder.mOnAnimationListener.onAnimationStartOut();
         }
         frmlayout.setBackgroundColor(Color.TRANSPARENT);
         if (inflate!=null)
            inflate.animate().alpha(0).setDuration(builder.duration+200).start();
         baseView.back(this);
         mStartView = null;
     }

    /**
     *  页面返回
     * @param mActivity
     * @param resultCode
     * @param mIntent
     */
    public void backActivity(Activity mActivity,int resultCode,Intent mIntent){

        if (mStartActivity!=null){

            ViewGroup viewGroup = (ViewGroup) frmlayout.getParent();
            viewGroup.removeView(frmlayout);
            inflate = new View(mActivity);
            inflate.setBackgroundDrawable(new BitmapDrawable(getBitmapWithView(viewGroup)));

            frmlayout.addView(inflate,new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            ViewGroup decorView;
            if (builder.isFullWindow){
                decorView = (ViewGroup) mStartActivity.getWindow().getDecorView();
            }else {
                decorView = (ViewGroup) mStartActivity.getWindow().getDecorView().findViewById(Window.ID_ANDROID_CONTENT);
            }
            decorView.addView(frmlayout);
            mStartActivity = null;
        }
        mActivity.finish();
        if (mIntent!=null){
            mActivity.setResult(resultCode,mIntent);
        }
        mActivity.overridePendingTransition(0,0);

        back();
    }

    /**
     * 页面返回
     * @param mActivity
     */
    public void backActivity(Activity mActivity){
        backActivity(mActivity,-1,null);
    }

    public static class Builder {
        /**  **/
        private Activity mStartActivity;

        private View mStartView ;
        /** 是否铺满屏幕 **/
        private boolean isFullWindow = true;
        /** 遮罩颜色 **/
        private int mDimcolor = Color.WHITE;
        /** 是否显示元素过渡效果 **/
        private boolean isShowTransition = true;
        /** 遮罩透明度 **/
        private int dimalpha = 205;
        /** 界面过渡视图 **/
        private Intent mIntent;
        /** 是否是startActivityForResult **/
        private boolean isActivityForResult = false;
        /** 请求码 **/
        private int requestCode = -1;
        /** 结束显示的视图 **/
        private View mEndView;
        /** x轴缩放 **/
        private float scaleY = 0;
        /** y轴缩放 **/
        private float scaleX = 0;
        /** 旋转角度 **/
        private float rotation = 0;
        /** 水平平移距离 **/
        private float translationX = 0;
        /** 垂直平移距离 **/
        private float translationY = 0;

        private long duration = 800;
        /** 第二个页面的过渡控件 **/
        private View mTranslationView;
        /**  **/
        private int mTranslationLayoutID = -1;
        /** 插值器 **/
        private TimeInterpolator interpolator = new LinearInterpolator();
        /** 过渡动画时长 **/
        private long mTranslationDuration = 400;

        public OnAnimationListener mOnAnimationListener;
        public Builder(Activity mStartActivity){
            this.mStartActivity = mStartActivity;
        }
        public Builder(Activity mStartActivity, View mStartView){
            this.mStartActivity = mStartActivity;
            this.mStartView=mStartView;
        }

        /** 设置最终显示的视图 **/
        public Builder setEndView(View mEndView){
            this.mEndView=mEndView;
            return this;
        }
        /** 设置是否显示过渡视图 **/
        public Builder isShowTransition(boolean isShowTransition){
            this.isShowTransition = isShowTransition;
            return this;
        }
        /** 设置是否铺满整个窗口 **/
        public Builder isFullWindow(boolean isFullWindow){
            this.isFullWindow = isFullWindow;
            return this;
        }

        /** 设置遮罩颜色 **/
        public Builder setDimColor(int dimcolor){
            this.mDimcolor = dimcolor;
            return this;
        }
        /** 设置遮罩透明度 **/
        public Builder setDimAlpha(int dimalpha){
            this.dimalpha = dimalpha;
            return this;
        }
        /** 过渡视图平移 **/
        public Builder setTranslationX(float translationX){
            this.translationX = translationX;
            return this;
        }
        public Builder setOnAnimationListener(OnAnimationListener mOnAnimationListener){
            this.mOnAnimationListener = mOnAnimationListener;
            return this;
        }
        /** 过渡视图平移 **/
        public Builder setTranslationY(float translationY){
            this.translationY = translationY;
            return this;
        }
        /** 过渡视图缩放 **/
        public Builder setScaleX(float scaleX){
            this.scaleX = scaleX;
            return this;
        }
        /** 过渡视图缩放 **/
        public Builder setScaleY(float scaleY){
            this.scaleY = scaleY;
            return this;
        }
        /** 过渡视图旋转 **/
        public Builder setRotation(float rotation){
            this.rotation = rotation;
            return this;
        }
        /** 设置第二个页面过渡视图 **/
        public Builder setTranslationView(View mTranslationView){
            this.mTranslationView = mTranslationView;
            return this;
        }
        /** 设置第二个页面过渡视图 **/
        public Builder setTranslationView(int mTranslationLayoutID){
            this.mTranslationLayoutID = mTranslationLayoutID;
            return this;
        }

        public Builder setDuration(long mTranslationDuration) {
            this.mTranslationDuration = mTranslationDuration;
            return this;
        }
        public Builder setInterpolator(TimeInterpolator interpolator) {
            this.interpolator = interpolator;
            return this;
        }
        /** 设置界面过渡意图 **/
        public void startActivityForResult(Intent mIntent,boolean isActivityForResult,int requestCode){
            this.mIntent = mIntent;
            this.isActivityForResult = isActivityForResult;
            this.requestCode = requestCode;
            create();
        }

        /**
         * 页面跳转
         * @param mIntent
         */
        public void startActivity(Intent mIntent){
            this.mIntent = mIntent;
            create();
        }

        /** 创建过渡辅助器 **/
        public BaseViewHelper create(){
            BaseViewHelper helper = new BaseViewHelper();
            helper.builder=this;
            if (mIntent == null) {

                if (AbstractViewHelper.mStartView !=null && mStartView == null){
                    mStartView = AbstractViewHelper.mStartView;
                }
                helper.addViewInWindow(mStartActivity
                        , mStartView
                        , mEndView
                        , isFullWindow
                        , mDimcolor
                        , dimalpha
                        , isShowTransition
                );
            }else {
                helper.addViewInWindow(mStartActivity
                        , mStartView
                        ,mIntent
                        ,isActivityForResult
                        ,requestCode
                );
            }
            return helper;
        }
    }

    class DrawView extends View{
        private View mView;
        public DrawView(Context context) {
            super(context);
        }

        public DrawView(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
            super(context, attrs, defStyleAttr);
        }

        public void setView(View mView){
            this.mView = mView;
            invalidate();
        }
        @Override
        protected void onDraw(Canvas canvas) {
            if (mView!=null){
                mView.draw(canvas);
            }
        }
    }


   public interface OnAnimationListener{
       /** 过渡动画开始开始 **/
       void onAnimationStartIn();

       /**  结束**/
       void onAnimationEndIn();

       /** 过渡动画开始开始 **/
       void onAnimationStartOut();

       /**  结束**/
       void onAnimationEndOut();
   }
}
