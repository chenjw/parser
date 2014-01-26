/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.node;

import com.chenjw.parse.core.DomNode;
import com.chenjw.parse.core.DomNodeIterator;
import com.chenjw.parse.statistics.Statistics;

/**
 * 带统计分析功能的节点
 * @author junwen.chenjw
 * @version $Id: StatisticsNode.java, v 0.1 2013年11月6日 上午11:33:57 junwen.chenjw Exp $
 */
public class StatisticsNode implements DomNode {
    /** delegate */
    private final DomNode delegate;

    /**
     * 构造函数
     * 
     * @param delegate delegate
     */
    public StatisticsNode(DomNode delegate) {
        this.delegate = delegate;
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.DomNode#parseString(java.lang.String)
     */
    @Override
    public String parseString(String path, boolean isAttr) {
        return delegate.parseString(path, isAttr);
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.DomNode#parseNode(java.lang.String)
     */
    @Override
    public DomNode parseNode(String path) {
        long start = System.currentTimeMillis();
        try {
            return delegate.parseNode(path);
        } finally {
            Statistics.XPATH_COSTS.addCost(path, System.currentTimeMillis() - start);
        }
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.DomNode#parseNodeList(java.lang.String)
     */
    @Override
    public DomNodeIterator parseNodeList(String path) {
        long start = System.currentTimeMillis();
        try {
            DomNodeIterator iterator = delegate.parseNodeList(path);
            if (iterator == null) {
                return null;
            } else {
                return new StatisticsNodeIterator(iterator);
            }
        } finally {
            Statistics.XPATH_COSTS.addCost(path, System.currentTimeMillis() - start);
        }
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.DomNode#toText()
     */
    @Override
    public String toText() {
        return delegate.toText();
    }

}
