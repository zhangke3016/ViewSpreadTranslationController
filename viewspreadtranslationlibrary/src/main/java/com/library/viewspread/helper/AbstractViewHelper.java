package com.library.viewspread.helper;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import com.library.viewspread.view.BaseView;
import com.library.viewspread.view.BaseFramlayout;

/**
 * Created by zhangke on 2017/1/7.
 */

public abstract class AbstractViewHelper {
    /** 当前即将跳转页面的 **/
    protected static View mStartView = null;

    protected static Activity mStartActivity = null;
    //跳转页面过渡视图的位置
    protected Rect rectInWindowByTranslationView;
    //当前页面过渡视图的位置
    protected Rect rectInWindow;
    //跟布局控件
    protected BaseFramlayout frmlayout;
    //绘制过渡内容的控件
    protected BaseView baseView ;
    //切换视图后显示的视图
    protected View inflate;
    //boolean isFullWindow = false;
    /** 当前是否正在显示 **/
    protected boolean isShowing = false;

    public BaseViewHelper.Builder builder;

    /**
     * 页面返回
     * @param mActivity
     */
    protected abstract void backActivity(Activity mActivity);
    /**
     *  页面返回
     * @param mActivity
     * @param resultCode
     * @param mIntent
     */
    protected abstract void backActivity(Activity mActivity,int resultCode,Intent mIntent);

    /**
     *  开启另一个Activity
     * @param mActivity
     * @param startView
     * @param mIntent
     * @param mIsActivityForResult
     * @param mRequestCode
     */
    protected abstract void addViewInWindow(final Activity mActivity
            , final View startView
            , final Intent mIntent, final boolean mIsActivityForResult
            , final int mRequestCode);

    /**
     *
     * @param mActivity
     * @param mStartView
     * @param mEndView
     * @param isFullWindow
     * @param mDimcolor
     * @param mDimalpha
     * @param mIsShowTransition
     */
    protected abstract void addViewInWindow(final Activity mActivity
            , final View mStartView, final View mEndView, final boolean isFullWindow
            , final int mDimcolor, final int mDimalpha
            , final boolean mIsShowTransition);

    /**
     * 获取控件位置
     * @param view
     * @param mIsFullWindow  是否全屏
     * @return
     */
    protected Rect getRectInWindow(View view, boolean mIsFullWindow){
            int[] location = new int[2];
            view.getLocationInWindow(location);
            return new Rect(location[0],location[1],location[0]+view.getMeasuredWidth(),location[1]+view.getMeasuredHeight());
    }

    public boolean isShowing(){
        return isShowing;
    }
    /**
     * 移除视图页面
     */
    public void removeAll() {
        isShowing = false;
        ViewGroup viewGroup = (ViewGroup) frmlayout.getParent();
        viewGroup.removeView(frmlayout);
        mStartView = null;
        mStartActivity = null;
    }


    /**
     *  获取控件的视图
     * @param v
     * @return
     */
    protected Bitmap getBitmapWithView(View v){

        Bitmap bitmap = Bitmap.createBitmap(v.getWidth(), v.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(bitmap);
        v.draw(canvas);
        canvas.setBitmap(null);
        return bitmap;
    }
}
