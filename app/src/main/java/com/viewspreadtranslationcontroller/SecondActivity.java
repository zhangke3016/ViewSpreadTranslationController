package com.viewspreadtranslationcontroller;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;

import com.library.viewspread.helper.BaseViewHelper;

/**
 * Created by zhangke on 2017/1/7.
 */

public class SecondActivity extends AppCompatActivity {
    BaseViewHelper helper;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_second);

        setTitle("第二页");

        switch (getIntent().getIntExtra("id",-1)){
            case R.id.btn_translation1:
                startTranslation1();
                break;
            case R.id.btn_translation2:
                startTranslationNoFullWindow();
                break;
            case R.id.btn_translation3:
                startTranslationNoShowTranslation();
                break;
            case R.id.btn_translation4:
                startTranslationShowTranslationY();
                break;
            case R.id.fab:
                startTranslation1();
                break;
            case R.id.ib_customImage:
                startTranslation(findViewById(R.id.iv_second));
                break;
        }



    }

    private void startTranslationShowTranslationY() {
        helper = new BaseViewHelper
                .Builder(SecondActivity.this)
                .isFullWindow(true)//是否全屏显示
                .isShowTransition(true)//是否显示过渡动画
                .setDimColor(Color.WHITE)//遮罩颜色
                .setDimAlpha(200)//遮罩透明度
                .setTranslationY(-600)
                .setInterpolator(new AnticipateOvershootInterpolator())
                .create();//开始动画
    }

    private void startTranslationNoShowTranslation() {
        helper = new BaseViewHelper
                .Builder(SecondActivity.this)
                .isFullWindow(true)//是否全屏显示
                .isShowTransition(false)//是否显示过渡动画
                .setDimColor(Color.WHITE)//遮罩颜色
                .setDimAlpha(200)//遮罩透明度
                .create();//开始动画
    }

    public void startTranslation(View v){
        helper = new BaseViewHelper
                .Builder(SecondActivity.this)
                //.setEndView()//如果是两个切换的视图  这里设定最终显示的视图
                .setTranslationView(v)//设置过渡视图
                .isFullWindow(true)//是否全屏显示
                .isShowTransition(true)//是否显示过渡动画
                .setDimColor(Color.WHITE)//遮罩颜色
                .setDimAlpha(200)//遮罩透明度
                //.setTranslationX(0)//x轴平移
                //.setRotation(360)//旋转
                //.setScaleX(0)//x轴缩放
                //.setScaleY(0)//y轴缩放
                //.setTranslationY(0)//y轴平移
                //.setDuration(800)//过渡时长
                //.setInterpolator(new AccelerateDecelerateInterpolator())//设置插值器
                //设置监听
//                .setOnAnimationListener(new BaseViewHelper.OnAnimationListener() {
//                    @Override
//                    public void onAnimationStartIn() {
//                        Log.e("TAG","onAnimationStartIn");
//                    }
//
//                    @Override
//                    public void onAnimationEndIn() {
//                        Log.e("TAG","onAnimationEndIn");
//                    }
//
//                    @Override
//                    public void onAnimationStartOut() {
//                        Log.e("TAG","onAnimationStartOut");
//                    }
//
//                    @Override
//                    public void onAnimationEndOut() {
//                        Log.e("TAG","onAnimationEndOut");
//                    }
//                })
                .create();//开始动画
    }



    public void startTranslation1(){
        helper = new BaseViewHelper
                .Builder(SecondActivity.this)
                //.setEndView()//如果是两个切换的视图  这里设定最终显示的视图
                //.setTranslationView(viewById)//设置过渡视图
                .isFullWindow(true)//是否全屏显示
                .isShowTransition(true)//是否显示过渡动画
                .setDimColor(Color.WHITE)//遮罩颜色
                .setDimAlpha(200)//遮罩透明度
                .setTranslationX(0)//x轴平移
                .setRotation(360)//旋转
                .setScaleX(0)//x轴缩放
                .setScaleY(0)//y轴缩放
                .setTranslationY(0)//y轴平移
                .setDuration(800)//过渡时长
                .setInterpolator(new AccelerateDecelerateInterpolator())//设置插值器
                //设置监听
                .setOnAnimationListener(new BaseViewHelper.OnAnimationListener() {
                    @Override
                    public void onAnimationStartIn() {
                        Log.e("TAG","onAnimationStartIn");
                    }

                    @Override
                    public void onAnimationEndIn() {
                        Log.e("TAG","onAnimationEndIn");
                    }

                    @Override
                    public void onAnimationStartOut() {
                        Log.e("TAG","onAnimationStartOut");
                    }

                    @Override
                    public void onAnimationEndOut() {
                        Log.e("TAG","onAnimationEndOut");
                    }
                })
                .create();//开始动画
    }
    public void startTranslationNoFullWindow(){
        helper = new BaseViewHelper
                .Builder(SecondActivity.this)
                .isFullWindow(false)//是否全屏显示
                .isShowTransition(true)//是否显示过渡动画
                .setDimColor(Color.WHITE)//遮罩颜色
                .setDimAlpha(200)//遮罩透明度
                .create();//开始动画
    }
    @Override
    public void onBackPressed() {

        if (helper!=null && helper.isShowing()){
            helper.backActivity(this);
        }else {
            super.onBackPressed();
        }
    }
}
