package com.thomas.frame.mvp;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author Thomas
 * @describe 基础mvp架构的presenter
 * @date 2019/9/24
 * @updatelog
 * @since 1.0.0
 */
public abstract class BaseMvpPresenter<M extends IBaseMvpModel, V extends IBaseMvpView> {
    private V mProxyView;
    private M model;
    private WeakReference<V> weakReference;

    /**
     * 绑定View
     */
    public void attachView(V view) {
        weakReference = new WeakReference<>(view);
        mProxyView = (V) Proxy.newProxyInstance(
                view.getClass().getClassLoader(),
                view.getClass().getInterfaces(),
                new MvpViewHandler(weakReference.get()));
        if (model == null) {
            model = createModel();
        }
    }

    /**
     * 解绑View
     */
    public void detachView() {
        this.model = null;
        if (isViewAttached()) {
            weakReference.clear();
            weakReference = null;
        }
    }

    /**
     * 是否与View建立连接
     */
    protected boolean isViewAttached() {
        return weakReference != null && weakReference.get() != null;
    }

    protected V getView() {
        return mProxyView;
    }

    protected M getModel() {
        return model;
    }

    /**
     * 通过该方法创建Module
     */
    protected abstract M createModel();


    /**
     * View代理类  防止 页面关闭P异步操作调用V 方法 空指针问题
     */
    private class MvpViewHandler implements InvocationHandler {

        private IBaseMvpView mvpView;

        MvpViewHandler(IBaseMvpView mvpView) {
            this.mvpView = mvpView;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            //如果V层没被销毁, 执行V层的方法.
            if (isViewAttached()) {
                return method.invoke(mvpView, args);
            } //P层不需要关注V层的返回值
            return null;
        }
    }

}
