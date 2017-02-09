package com.library.viewspread.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

/**
 * Created by zhangke on 2017/1/6.
 */

 public class BaseFramlayout extends FrameLayout {
    private boolean isTouchIntecepted = false;
    public BaseFramlayout(Context context) {
        super(context);
    }

    public BaseFramlayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BaseFramlayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setTouchIntecepted(boolean isTouchIntecepted){
        this.isTouchIntecepted=isTouchIntecepted;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isTouchIntecepted ? true :super.onTouchEvent(event);
    }
}
