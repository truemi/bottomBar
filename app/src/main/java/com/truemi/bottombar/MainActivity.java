package com.truemi.bottombar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.truemi.mbottombar.BottomBar;
import com.truemi.mbottombar.callback.IBottomBarOnClick;

public class MainActivity extends AppCompatActivity implements IBottomBarOnClick {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomBar bottomBar = findViewById(R.id.bottom_bar);
        /**
         * R.id.fl_home 是fragment将要填充的界面
         * setRippleColor(R.drawable.bottom_bar_bg)//设置水波纹效果
         * addItem("标题", 图片选择器资源, Fragment, 是否可以显示小红点true/false)界面会从左到右依次添加
         * carete(1);默认显示第几个界面从0开始,从左到右,必须调用create()
         */
        bottomBar.init(getSupportFragmentManager(),R.id.fl_home)//.setRippleColor(R.drawable.bottom_bar_bg)//水波纹效果
                .addItem("消息", getResources().getDrawable(R.drawable.bottom_bar_selected_01), new MessageFragment(), true)
                .addItem("首页", getResources().getDrawable(R.drawable.bottom_bar_selected_02), new HomeFragment(), false)
                .addItem("我的", getResources().getDrawable(R.drawable.bottom_bar_selected_03), new MyFragment(), true)
                .create(1);
        bottomBar.setOnBottomBarOnClick(this);//设置点击监听
        //bottomBar.showRedPoint(1,0,0);//只有初始化时设置可显示小红点为true时,设置显示才有效
        bottomBar.showRed(2);//显示小红点,从0开始,从左到右
        //bottomBar.hideRed(0);//隐藏小红点,从0开始,从左到右
        //显示第几个界面,从0开始,从左到右
        //bottomBar.showSubPage(1);
    }

    @Override
    public void onClickBar(View view, int tag) {
    //tag是点击的第几个item,从0开始,从左到右
        Log.e("tag",tag+"----------");
    }
}
