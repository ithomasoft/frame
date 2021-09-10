package com.thomas.frame.component;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 用于解析 AndroidManifest 中的 Meta 属性
 *
 * @author Thomas
 * @date 2019/4/17
 * @updatelog
 */
public class ManifestParser {
    private static final String MODULE_VALUE = "ModuleConfig";

    private final Context context;

    public ManifestParser(Context context) {
        this.context = context;
    }

    private static ModuleConfig parseModule(String className) {
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException("Unable to find ModuleConfig implementation", e);
        }

        Object module;
        try {
            module = clazz.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException("Unable to instantiate ModuleConfig implementation for " + clazz, e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Unable to instantiate ModuleConfig implementation for " + clazz, e);
        }

        if (!(module instanceof ModuleConfig)) {
            throw new RuntimeException("Expected instanceof ModuleConfig, but found: " + module);
        }
        return (ModuleConfig) module;
    }

    public List<ModuleConfig> parse() {
        List<ModuleConfig> modules = new ArrayList();
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo.metaData != null) {
                for (String key : appInfo.metaData.keySet()) {
                    if (MODULE_VALUE.equals(appInfo.metaData.get(key))) {
                        modules.add(parseModule(key));
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Unable to find metadata to parse ModuleConfig", e);
        }

        return modules;
    }

}
