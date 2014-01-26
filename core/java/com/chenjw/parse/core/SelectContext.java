/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.core;

import java.util.Map;

import org.apache.commons.collections.map.Flat3Map;

import com.chenjw.parse.spi.Selector;

/**
 * 选择器上下文
 * @author junwen.chenjw
 * @version $Id: SelectContext.java, v 0.1 2013年11月6日 上午11:45:57 junwen.chenjw Exp $
 */
public class SelectContext {

    /** 用来标记某个节点加载失败了，如果加载失败resultCache中就存放MARKED_LOAD_FAIL这个值 */
    public static final DomNode[]    MARKED_LOAD_FAIL = new DomNode[0];
    /** 结果缓存  */
    private Map<Selector, DomNode[]> resultCache;

    /**
     * 获得结果缓存
     * @param selector 选择器
     * @return 缓存的结果
     */
    public DomNode[] getCachedResult(Selector selector) {
        if (resultCache == null) {
            return null;
        }
        return resultCache.get(selector);
    }

    /**
     * 写入结果缓存
     * @param selector 选择器
     * @param nodes 结果
     */
    @SuppressWarnings("unchecked")
    public void putCachedResult(Selector selector, DomNode[] nodes) {
        if (resultCache == null) {
            // 数据量很少，所以这里用flat3map提高性能
            resultCache = new Flat3Map();
        }
        resultCache.put(selector, nodes);
    }

}
