package com.thomas.frame.mvp;

import androidx.databinding.ViewDataBinding;

import com.thomas.frame.android.LazyFragment;

public abstract class LazyMvpFragment<VDB extends ViewDataBinding, P extends BaseMvpPresenter> extends LazyFragment<VDB> implements IBaseMvpView {
    protected P presenter;

    @Override
    public void setContentView() {
        super.setContentView();
        //创建present
        presenter = createPresenter();
        if (presenter != null) {
            presenter.attachView(this);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //解除绑定，避免内存泄露
        if (presenter != null) {
            presenter.detachView();
            presenter = null;
        }
    }

    protected abstract P createPresenter();
}
