package com.truemi.bottombar;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View  mRootView = inflater.inflate(setLayoutResouceId(), container, false);
        initView(mRootView,savedInstanceState);
        return mRootView;
    }


    protected abstract int setLayoutResouceId();
    protected abstract void initView(View view, Bundle savedInstanceState);
}
