package com.thomas.frame.android;

import android.content.BroadcastReceiver;


/**
 * 框架基于Google官方的 JetPack 构建，在使用时，需遵循一些规范：
 *
 * 如果您继承使用了BaseBroadcastReceiver或其子类，你需要参照如下方式添加@AndroidEntryPoint注解
 *
 * @example BroadcastReceiver
 * //-------------------------
 *    @AndroidEntryPoint
 *    public class YourBroadcastReceiver extends BaseBroadcastReceiver {
 *
 *    }
 * //-------------------------
 *
 * @author <a href="mailto:tom_flying@163.com">thomas</a>
 */
public abstract class BaseBroadcastReceiver  extends BroadcastReceiver {
}
