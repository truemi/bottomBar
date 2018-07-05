package com.truemi.bottombar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.truemi.mbottombar.BottomBar;
import com.truemi.mbottombar.callback.IBottomBarOnClick;

public class MainActivity extends AppCompatActivity implements IBottomBarOnClick {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomBar bottomBar = findViewById(R.id.bottom_bar);
        bottomBar.init(getSupportFragmentManager(),R.id.fl_home)//.setRippleColor(R.drawable.bottom_bar_bg)
                .addItem("消息", getResources().getDrawable(R.drawable.bottom_bar_selected_01), new MessageFragment(), true)
                .addItem("首页", getResources().getDrawable(R.drawable.bottom_bar_selected_02), new HomeFragment(), false)
                .addItem("我的", getResources().getDrawable(R.drawable.bottom_bar_selected_03), new MyFragment(), false)
                .carete(2);
        bottomBar.setOnBottomBarOnClick(this);
    }

    @Override
    public void onClickBar(View view, int tag) {

    }
}
