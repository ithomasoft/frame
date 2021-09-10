package com.thomas.frame.mvp;

/**
 * @author Thomas
 * @describe 基础mvp中view的基础接口
 * @date 2019/9/24
 * @updatelog
 * @since 1.0.0
 */
public interface IBaseMvpView {

    /**
     * 失败数据
     *
     * @param tag    标签
     * @param failed 失败数据
     */
    void onFailed(Object tag, String failed);

}
