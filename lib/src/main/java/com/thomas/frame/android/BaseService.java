package com.thomas.frame.android;

import android.app.Service;

/**
 * 框架基于Google官方的 JetPack 构建，在使用时，需遵循一些规范：
 *
 * 如果您继承使用了BaseService或其子类，你需要参照如下方式添加@AndroidEntryPoint注解
 *
 * @example Service
 * //-------------------------
 *    @AndroidEntryPoint
 *    public class YourService extends BaseService {
 *
 *    }
 * //-------------------------
 *
 * @author <a href="mailto:tom_flying@163.com">thomas</a>
 */
public abstract class BaseService extends Service {
}
