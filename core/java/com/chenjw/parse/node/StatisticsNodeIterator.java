/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.node;

import com.chenjw.parse.core.DomNode;
import com.chenjw.parse.core.DomNodeIterator;

/**
 * 带统计分析功能的节点迭代器
 * @author junwen.chenjw
 * @version $Id: StatisticsNodeIterator.java, v 0.1 2013年11月6日 上午11:35:26 junwen.chenjw Exp $
 */
public class StatisticsNodeIterator implements DomNodeIterator {

    /** delegate */
    private final DomNodeIterator delegate;

    /**
     * 构造函数
     * 
     * @param delegate
     */
    public StatisticsNodeIterator(DomNodeIterator delegate) {
        this.delegate = delegate;
    }

    /** 
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        return delegate.hasNext();
    }

    /** 
     * @see java.util.Iterator#next()
     */
    @Override
    public DomNode next() {
        return new StatisticsNode(delegate.next());
    }



    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.DomNodeIterator#close()
     */
    @Override
    public void close() {
        delegate.close();
    }

}
