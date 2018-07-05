package com.truemi.mbottombar;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.truemi.mbottombar.callback.IBottomBarOnClick;
import com.truemi.mbottombar.utils.Uiutils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Description: 底部导航栏
 * Author     : YJ
 * Date       : 2017-11-24 16:31
 */
public class BottomBar extends LinearLayout {

    private float textSize;
    private int TEXTVIEW_ID ;
    private int IMAGEVIEW_ID ;
    private int ITEM_ID ;
    private ArrayList<RelativeLayout> items = new ArrayList<>();
    private ArrayList<TextView> itemTextViews = new ArrayList<>();
    private HashMap<View, Fragment> itemFragment = new HashMap<>();
    private HashMap<Integer, ImageView> itemImageViews = new HashMap<>();
    private Context           context;
    private FragmentManager   supportFragmentManager;
    private int               layoutFragment;
    private IBottomBarOnClick onClick;
    private ColorStateList    colorStateList;
    private int               textNormalColor;
    private int               textSelectColor;
    private int itemRippleColor =-1;
    private float iconSize;//图片的大小单位dp

    public BottomBar(Context context) {
        this(context, null);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public BottomBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init(attrs);
        TEXTVIEW_ID = (int) (System.currentTimeMillis()%100*10000);
        IMAGEVIEW_ID = (int) (System.currentTimeMillis()%100*1000);
        ITEM_ID = (int) (System.currentTimeMillis()%100*100);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BottomBar);
            float dimension = typedArray.getDimension(R.styleable.BottomBar_mtextSize, 0);
            textSize = Uiutils.px2sp(context,dimension);
            textNormalColor = typedArray.getColor(R.styleable.BottomBar_mTextNormalColor, 0);
            textSelectColor = typedArray.getColor(R.styleable.BottomBar_mTextSelectColor, 0);
            iconSize = typedArray.getDimension(R.styleable.BottomBar_mIconSize, 0);

            typedArray.recycle();
        }
        setOrientation(LinearLayout.HORIZONTAL);
    }
    /**
     * 初始化的第一步
     *
     * @param Manager FragmentManager
     * @param layout  Fragment将要显示的容器控件 ViewGroup...
     * @return
     */
    public BottomBar init(FragmentManager Manager, @IdRes int layout) {
        this.supportFragmentManager = Manager;
        this.layoutFragment = layout;

        return this;
    }

    /**
     * 设置Item
     *
     * @param title item标题
     * @param drawable 图标选择器
     * @param fragment fragment
     * @param redPoint Item是否有小红点
     * @return
     */
    public BottomBar addItem(final String title, Drawable drawable, Fragment fragment, boolean redPoint) {
        final RelativeLayout relativeLayout = initItem(title, drawable, redPoint);
        relativeLayout.setId(items.size() + ITEM_ID);
        LayoutParams lp = new LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);//设置权重为1
        relativeLayout.setLayoutParams(lp);
        relativeLayout.setTag(items.size());
        relativeLayout.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showFragment(view.getId());
                RelativeLayout view1 = (RelativeLayout) view;
                view1.setSelected(true);
                int tag = (int)relativeLayout.getTag();
                onClick.onClickBar(view,tag);
            }
        });
        items.add(relativeLayout);
       itemFragment.put(relativeLayout, fragment);

        return this;
    }

    /**
     * 初始化Item数据
     *
     * @param title
     * @param drawable
     * @param haveRedPoint
     * @return
     */
    private RelativeLayout initItem(String title, Drawable drawable, boolean haveRedPoint) {

        TextView textView = new TextView(context,null);
        textView.setText(title);
        textView.setTextSize(textSize);
        textView.setTextColor(textNormalColor);
        textView.setId(TEXTVIEW_ID);
        itemTextViews.add(textView);
        if (iconSize>0){
            drawable.setBounds(0, 0, Uiutils.dp2px(context,iconSize), Uiutils.dp2px(context,iconSize));
        }else {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        }
        textView.setCompoundDrawables(null, drawable, null, null);
        RelativeLayout relativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        relativeLayout.addView(textView, layoutParams);
        if (haveRedPoint) {
            ImageView  imageView = new ImageView(context);
            int imageId = IMAGEVIEW_ID + items.size();
            imageView.setId(imageId);
            Drawable redPoint = context.getResources().getDrawable(R.drawable.red_dot_circle);
            imageView.setImageDrawable(redPoint);
            RelativeLayout.LayoutParams layoutParamsDot = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParamsDot.addRule(RelativeLayout.ALIGN_RIGHT, TEXTVIEW_ID);
            layoutParamsDot.addRule(RelativeLayout.ALIGN_TOP, TEXTVIEW_ID);
            relativeLayout.addView(imageView, layoutParamsDot);
            imageView.setVisibility(View.GONE);
            itemImageViews.put(itemTextViews.size(),imageView);
        }
        TEXTVIEW_ID++;
        return relativeLayout;
    }


    /**
     * 显示fragment
     *
     * @param viewId
     */
    private void showFragment(int viewId) {
        Fragment selectedFragment = null;
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        for (Map.Entry<View, Fragment> viewFragmentEntry : itemFragment.entrySet()) {
            Fragment fragment = viewFragmentEntry.getValue();
            if (!fragment.isAdded()) {
                fragmentTransaction.add(layoutFragment, fragment);
            }
            RelativeLayout view = (RelativeLayout) viewFragmentEntry.getKey();
            int id = view.getId();
            TextView childAt = (TextView) view.getChildAt(0);
            if (viewId == id) {
                selectedFragment = fragment;
                view.setSelected(true);
                if (childAt!=null){
                    childAt.setTextColor(textSelectColor);
                }
            } else {
                fragmentTransaction.hide(fragment);
                view.setSelected(false);
                if (childAt!=null){
                    childAt.setTextColor(textNormalColor);
                }
            }
        }
        if (selectedFragment != null) {
            fragmentTransaction.show(selectedFragment);
        }
        fragmentTransaction.commit();
    }

    /**
     * 设置标题大小
     *
     * @param sp
     * @return
     */
    public BottomBar setTextSize(float sp) {
        this.textSize = sp;
        for (TextView itemTextView : itemTextViews) {
            itemTextView.setTextSize(textSize);
        }
        return this;
    }

    /**
     * 设置text颜色选择器
     *
     * @param resColor
     * @return
     */
    public BottomBar setTextColor(int resColor) {
         colorStateList = getResources().getColorStateList(resColor);
        for (TextView itemTextView : itemTextViews) {
            itemTextView.setTextColor(colorStateList);
        }
        return this;
    }

    /**
     * 设置item水波纹颜色,apis>21
     *
     * @param rippleColorRes
     * @return
     */
    @TargetApi(21)
    public BottomBar setRippleColor(int rippleColorRes) {
        itemRippleColor =rippleColorRes;
        if (Build.VERSION.SDK_INT>=21) {
            for (RelativeLayout item : items) {
                Drawable itemBg = context.getResources().getDrawable(rippleColorRes);//R.drawable.bottom_bar_bg
                item.setBackground(itemBg);
            }

        }
        return this;
    }
    /**
     * 显示小红点
     * @param pointPlace 从左到右第几个小红点,从 0 开始
     *
     */
    public void showRed(int pointPlace)  {
        ImageView imageView = itemImageViews.get(pointPlace+1);
        if (imageView!=null) {
            imageView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏小红点
     * @param pointPlace 从左到右第几个小红点,从 0 开始
     *
     */
    public void hideRed(int pointPlace)  {
        ImageView imageView = itemImageViews.get(pointPlace+1);
        if (imageView!=null) {
            imageView.setVisibility(View.GONE);
        }
    }

    /**
     * 设置全部的小红点, setRedPoint(1,0,0) 表示从左到右第一个小红点显示,后面两个不显示
     * @param show 当前位置的小红点是否显示
     */
    public void showRedPoint(int... show)  {
        for (int i = 0; i < show.length; i++) {
            int point = show[i];
            ImageView imageView = itemImageViews.get(i+1);
            if (imageView!=null) {
                if (point > 0) {
                    imageView.setVisibility(View.VISIBLE);
                } else {
                    imageView.setVisibility(View.GONE);
                }
            }else{
                Log.e("BottomBar","Not founded fragment");
            }
        }
    }

    /**
     * 把Item添加到bottomBar中
     * @param defshow 默认显示第几个fragment,从 0 开始
     */
    public void create(int defshow) {
        for (int i = 0; i < items.size(); i++) {
            RelativeLayout relativeLayout = (RelativeLayout) items.get(i);
            if (itemRippleColor!=-1&&Build.VERSION.SDK_INT>=21) {
                Drawable itemBg = context.getResources().getDrawable(itemRippleColor);//R.drawable.bottom_bar_bg
                relativeLayout.setBackground(itemBg);
            }
            addView(relativeLayout);
        }
        RelativeLayout relativeLayout =null;
        try {
            relativeLayout = items.get(defshow);
        }catch (Exception e){
            Log.e("BottomBar","Not founded fragment");

        }
        int id = relativeLayout.getId();
       showFragment(id);
    }

    /**
     * 公开的外地调用方法,跳转到指定fragment
     * @param page 为导航栏从左到右Item的位置,最左边Item为0
     */
    public void showSubPage(int page){
        RelativeLayout relativeLayout =null;
        try {
            relativeLayout = items.get(page);
        }catch (Exception e){
            Log.e("BottomBar","Not founded fragment");

        }
        int viewId = relativeLayout.getId();
        Fragment selectedFragment = null;
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        for (Map.Entry<View, Fragment> viewFragmentEntry : itemFragment.entrySet()) {
            Fragment fragment = viewFragmentEntry.getValue();
            RelativeLayout view = (RelativeLayout) viewFragmentEntry.getKey();
            int id = view.getId();
            TextView childAt = (TextView) view.getChildAt(0);
            if (viewId == id) {
                selectedFragment = fragment;
                view.setSelected(true);
                if (childAt!=null){
                    childAt.setTextColor(textSelectColor);
                }
            } else {
                fragmentTransaction.hide(fragment);
                view.setSelected(false);
                if (childAt!=null){
                    childAt.setTextColor(textNormalColor);
                }
            }
        }
        if (selectedFragment != null) {
            fragmentTransaction.show(selectedFragment);
        }
        fragmentTransaction.commit();
    }

    public void setOnBottomBarOnClick(IBottomBarOnClick click) {
        this.onClick = click;
    }
}
