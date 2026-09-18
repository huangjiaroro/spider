package com.hexin.cbas.spider.core.loader;


import com.hexin.cbas.spider.core.node.NodeFactory;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.ServiceLoader;

/**
 * @description: 加载工厂类
 * @author: huangjiarong
 * @create: 2021/9/15
 * @version: v1.0
 */
public class PluginLoader {

    private static final String USER_DIR = System.getProperty("user.dir");

    /**
     * 查找加载对应的工厂类
     *
     * @param pluginInfo
     * @return
     */
    public static NodeFactory loadFactory(String pluginInfo) {
        File pluginDir = new File(String.format("%s/plugin", USER_DIR));
        ClassLoader defaultLoader = Thread.currentThread().getContextClassLoader();
        if (pluginDir.isDirectory()) {
            File[] pluginFiles = pluginDir.listFiles();
            URL[] urls = Arrays.stream(pluginFiles).filter((file -> file.isFile() && file.getName().endsWith(".jar")))
                    .map((file -> {
                                try {
                                    return file.toURI().toURL();
                                } catch (MalformedURLException e) {
                                    throw new RuntimeException(e);
                                }
                            })
                    ).toArray(URL[]::new);
            defaultLoader = new PluginClassLoader(urls, defaultLoader);
        }
        ServiceLoader<NodeFactory> loader = ServiceLoader.load(NodeFactory.class, defaultLoader);
        for (NodeFactory nodeFactory : loader) {
            if (pluginInfo.equals(nodeFactory.type())) {
                return nodeFactory;
            }
        }
        throw new RuntimeException("no factory matched!");
    }


}
