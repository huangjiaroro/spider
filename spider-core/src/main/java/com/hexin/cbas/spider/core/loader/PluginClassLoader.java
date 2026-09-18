package com.hexin.cbas.spider.core.loader;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * @description: 插件类加载器
 * @author: huangjiarong
 * @create: 2021/9/15
 * @version: v1.0
 */
public class PluginClassLoader extends URLClassLoader {

    public PluginClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }
}
