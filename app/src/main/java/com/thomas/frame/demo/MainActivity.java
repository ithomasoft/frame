package com.thomas.frame.demo;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.thomas.core.ToastUtils;
import com.thomas.frame.android.BaseActivity;
import com.thomas.frame.demo.databinding.ActivityMainBinding;
import com.thomas.frame.mvp.BaseMvpActivity;

public class MainActivity extends BaseActivity<ActivityMainBinding> {

    @Override
    protected boolean isNeedAdapt() {
        return false;
    }

    @Override
    protected int setAdaptScreen() {
        return 0;
    }

    @Override
    public boolean isNeedRegister() {
        return false;
    }

    @Override
    public boolean isTransparent() {
        return false;
    }

    @Override
    public void initData(@Nullable Bundle bundle) {

    }

    @Override
    public int bindLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState, @Nullable View contentView) {

        applyThomasClickListener(mBinding.button, mBinding.button2, mBinding.button3);
    }

    @Override
    public void doBusiness() {

    }

    @Override
    public void onThomasClick(@NonNull View view) {
        if (view == mBinding.button) {
            ToastUtils.showShort("button");
            return;
        }
        if (view == mBinding.button2) {
            ToastUtils.showShort("button2");
            return;
        }
        if (view == mBinding.button3) {
            ToastUtils.showShort("button3");
            return;
        }
    }
}