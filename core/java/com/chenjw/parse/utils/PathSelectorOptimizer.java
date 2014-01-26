/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;

import com.chenjw.parse.spi.Selector;
import com.chenjw.parse.spi.impl.PathSelector;

/**
 *路径选择优化器
 * @author junwen.chenjw
 * @version $Id: PathSelectorOptimizer.java, v 0.1 2013年8月14日 下午5:00:48 junwen.chenjw Exp $
 */
public class PathSelectorOptimizer {
    /**
     * 优化选择器路径，当某些中间路径会被重复执行时，会缓存这些中间结果，用于提高性能
     * 
     * @param selectors 选择器
     * @return 优化后的结果
     */
    public static Selector[] optimize(Selector[] selectors) {
        if (selectors == null) {
            return null;
        }
        Map<String, List<PathSelector>> canOptimizeSelectors = new HashMap<String, List<PathSelector>>();
        for (Selector selector : selectors) {
            if (!(selector instanceof PathSelector)) {
                continue;
            }
            PathSelector pathSelector = (PathSelector) selector;
            String path = pathSelector.getPath();
            String searchPath = getSearchPath(path);
            if (searchPath == null) {
                continue;
            }
            List<PathSelector> canOptimizeSelector = canOptimizeSelectors.get(searchPath);
            if (canOptimizeSelector == null) {
                canOptimizeSelector = new ArrayList<PathSelector>();
                canOptimizeSelectors.put(searchPath, canOptimizeSelector);
            }
            canOptimizeSelector.add(pathSelector);
        }
        for (Entry<String, List<PathSelector>> entry : canOptimizeSelectors.entrySet()) {
            String searchPath = entry.getKey();
            List<PathSelector> canOptimizeSelector = entry.getValue();
            // 只出现1次的不优化
            if (canOptimizeSelector.size() < 2) {
                continue;
            }
            PathSelector parent = new PathSelector(searchPath, null);
            for (PathSelector s : canOptimizeSelector) {

                s.setDepend(parent);
                if (StringUtils.equals(s.getPath(), searchPath)) {
                    s.setPath(".");
                } else {
                    s.setPath(getRelatePath(s.getPath(), searchPath));
                }
            }
        }
        return selectors;

    }

    /**
     * 获得相对路径
     * @param path 路径
     * @param searchPath 搜索路径
     * @return 相对路径
     */
    private static String getRelatePath(String path, String searchPath) {
        return StringUtils.substringAfter(path, searchPath + "/");
    }

    /**
     * 获得xpath表达式中的搜索部分
     * 
     * @param path 路径
     * @return 搜索部分
     */
    private static String getSearchPath(String path) {
        if (!path.startsWith("//*[")) {
            return null;
        }
        return "//*[" + StringUtils.substringBetween(path, "//*[", "]") + "]";
    }
}
