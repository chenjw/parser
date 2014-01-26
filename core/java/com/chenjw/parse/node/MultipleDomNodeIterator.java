/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.node;

import com.chenjw.parse.core.DomNode;
import com.chenjw.parse.core.DomNodeIterator;

/**
 * 多个迭代器组合成一个迭代器（迭代是依次迭代所有迭代器的元素）
 * 
 * @author junwen.chenjw
 * @version $Id: MultipleDomNodeIterator.java, v 0.1 2013年8月15日 下午4:54:31 junwen.chenjw Exp $
 */
public class MultipleDomNodeIterator implements DomNodeIterator {
    /** 子迭代器列表 */
    private final DomNodeIterator[] iterators;
    /** 当前执行到迭代器的索引 */
    private int                     currentIndex = 0;

    /**
     * 构造函数
     * 
     * @param iterators 迭代器列表
     */
    public MultipleDomNodeIterator(DomNodeIterator[] iterators) {
        this.iterators = iterators;
    }

    /** 
     * @see java.util.Iterator#hasNext()
     */
    @Override
    public boolean hasNext() {
        if (currentIndex >= iterators.length) {
            return false;
        }
        boolean success = iterators[currentIndex].hasNext();
        if (!success) {
            currentIndex++;
            return hasNext();
        }
        return success;
    }

    /** 
     * @see java.util.Iterator#next()
     */
    @Override
    public DomNode next() {
        if (hasNext()) {
            return iterators[currentIndex].next();
        }
        return null;
    }


    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.DomNodeIterator#close()
     */
    @Override
    public void close() {
        for (DomNodeIterator iterator : iterators) {
            iterator.close();
        }
    }

}
