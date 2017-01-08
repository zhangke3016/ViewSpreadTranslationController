package com.viewspreadtranslationcontroller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.library.viewspread.helper.BaseViewHelper;

import static android.view.View.inflate;

public class MainActivity extends AppCompatActivity {
    BaseViewHelper helper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);


    }
    public void click(View view){

        if (view.getId() == R.id.btn_translation5){
            //显示在当前页面跳转
            helper = new BaseViewHelper.Builder(this,view)
                    .setTranslationView(R.id.iv_second)
                    .setEndView(inflate(this,R.layout.layout_second,null))
                    .create();

            return;
        }

        if (view.getId() == R.id.btn_translation6){
           View v = View.inflate(this,R.layout.layout_second,null);
            //显示在当前页面跳转
            helper = new BaseViewHelper.Builder(this,view)
                    .setEndView(v)
                    .create();
            ((TextView)v.findViewById(R.id.tv_message)).setText("我还在第一个页面");
            return;
        }



        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
        intent.putExtra("id",view.getId());
        new BaseViewHelper
                .Builder(MainActivity.this, view)
                .startActivity(intent);

    }

    @Override
    public void onBackPressed() {

        if (helper!=null && helper.isShowing()){
            helper.back();
        }else {
            super.onBackPressed();
        }
    }
}

