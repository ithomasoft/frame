package com.thomas.frame.android;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.thomas.core.ClickUtils;

/**
 * 框架基于Google官方的 JetPack 构建，在使用时，需遵循一些规范：
 *
 * 如果您继承使用了LazyFragment或其子类，你需要参照如下方式添加@AndroidEntryPoint注解
 *
 * @example Fragment
 * //-------------------------
 *    @AndroidEntryPoint
 *    public class YourFragment extends LazyFragment {
 *
 *    }
 * //-------------------------
 *
 * @author <a href="mailto:tom_flying@163.com">thomas</a>
 */
public abstract class LazyFragment<VDB extends ViewDataBinding> extends Fragment implements IBaseView {

    private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";
    protected AppCompatActivity mActivity;
    protected LayoutInflater mInflater;
    protected View mContentView;
    protected VDB mBinding;
    private boolean isFirstVisible = true;
    private boolean isFirstInvisible = true;
    private View.OnClickListener mClickListener = v -> onThomasClick(v);


    @Override
    public boolean isBinding() {
        return true;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fm = getParentFragmentManager();
        if (fm == null) {
            return;
        }
        if (savedInstanceState != null) {
            boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
            FragmentTransaction ft = fm.beginTransaction();
            if (isSupportHidden) {
                ft.hide(this);
            } else {
                ft.show(this);
            }
            ft.commitAllowingStateLoss();
        }
        Bundle bundle = getArguments();
        initData(bundle);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        mInflater = inflater;
        setContentView();
        return mContentView;
    }

    @Override
    public void setContentView() {
        if (bindLayout() <= 0) {
            return;
        }
        mContentView = mInflater.inflate(bindLayout(), null);
        if (isBinding()) {
            mBinding = DataBindingUtil.bind(mContentView);
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(savedInstanceState, mContentView);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstVisible) {
            isFirstVisible = false;
            onFirstUserVisible();
        } else {
            onUserVisible();
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        if (isFirstInvisible) {
            isFirstInvisible = false;
            onFirstUserInvisible();
        } else {
            onUserInvisible();
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        destroyViewAndThing();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mBinding != null) {
            mBinding.unbind();
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
    }

    public void applyThomasClickListener(View... views) {
        ClickUtils.applySingleDebouncing(views, mClickListener);

    }

    public void applyThomasClickScaleListener(View... views) {
        ClickUtils.applySingleDebouncing(views, mClickListener);
        ClickUtils.applyPressedViewScale(views);
    }

    public <T extends View> T findViewById(@IdRes int id) {
        if (mContentView == null) {
            throw new NullPointerException("ContentView is null.");
        }
        return mContentView.findViewById(id);
    }


    @Override
    public void doBusiness() {

    }

    protected abstract void onFirstUserVisible();//加载数据，开启动画/广播..

    protected abstract void onUserVisible();///开启动画/广播..

    private void onFirstUserInvisible() {

    }

    protected abstract void onUserInvisible();//暂停动画，暂停广播

    protected abstract void destroyViewAndThing();//销毁动作
}
