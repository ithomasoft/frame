package com.thomas.frame.android;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.thomas.core.AdaptScreenUtils;
import com.thomas.core.BarUtils;
import com.thomas.core.ClickUtils;
import com.thomas.core.ScreenUtils;

/**
 * 框架基于Google官方的 JetPack 构建，在使用时，需遵循一些规范：
 *
 * 如果您继承使用了BaseActivity或其子类，你需要参照如下方式添加@AndroidEntryPoint注解
 *
 * @example Activity
 * //-------------------------
 *    @AndroidEntryPoint
 *    public class YourActivity extends BaseActivity {
 *
 *    }
 * //-------------------------
 *
 * @author <a href="mailto:tom_flying@163.com">thomas</a>
 */
public abstract class BaseActivity<VDB extends ViewDataBinding> extends AppCompatActivity implements IBaseView {
    protected View mContentView;
    protected AppCompatActivity mActivity;
    protected VDB mBinding;

    private View.OnClickListener mClickListener = v -> onThomasClick(v);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mActivity = this;
        super.onCreate(savedInstanceState);
        //默认实现沉浸式状态栏
        initStatusBar();
        initData(getIntent().getExtras());
        setContentView();
        initView(savedInstanceState, mContentView);
        doBusiness();
    }

    public void initStatusBar() {
        //默认强制竖屏
        ScreenUtils.setPortrait(this);
        if (BarUtils.isNavBarVisible(this)) {
            BarUtils.setNavBarVisibility(this, !isTransparent());
        }
        if (BarUtils.isSupportNavBar()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                BarUtils.setNavBarColor(this, ContextCompat.getColor(this, android.R.color.black));
            }
        }
    }

    /**
     * 是否进行屏幕适配
     *
     * @return true：进行；false：不进行
     */
    protected abstract boolean isNeedAdapt();

    /**
     * 设置设计图的宽度（pt）
     *
     * @return
     */
    protected abstract int setAdaptScreen();

    @Override
    public boolean isBinding() {
        return true;
    }

    @Override
    public void setContentView() {
        if (bindLayout() <= 0) {
            return;
        }
        mContentView = LayoutInflater.from(this).inflate(bindLayout(), null);
        if (isBinding()) {
            mBinding = DataBindingUtil.setContentView(this, bindLayout());
        } else {
            setContentView(mContentView);
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBinding != null) {
            mBinding.unbind();
        }
    }

    public void applyThomasClickListener(View... views) {
        ClickUtils.applySingleDebouncing(views, mClickListener);

    }

    public void applyThomasClickScaleListener(View... views) {
        ClickUtils.applySingleDebouncing(views, mClickListener);
        ClickUtils.applyPressedViewScale(views);
    }

    @Override
    public Resources getResources() {
        if (isNeedAdapt()) {
            //今日头条屏幕适配方案
            if (ScreenUtils.isPortrait()) {
                //竖屏以宽度为基准
                return AdaptScreenUtils.adaptWidth(super.getResources(), setAdaptScreen());
            } else {
                //横屏以高度为基准
                return AdaptScreenUtils.adaptHeight(super.getResources(), setAdaptScreen());
            }

        } else {
            return super.getResources();
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS
                );
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            return !(event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom);
        }
        return false;
    }
}
