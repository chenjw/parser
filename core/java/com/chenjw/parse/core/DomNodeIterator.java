/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.core;


/**
 * 对应html中的一组标签节点列表
 * <p>这里之所以定义为迭代器而不是数组，是因为某些节点集合是延迟加载的，没必要用到的就不需要加载</p>
 * 
 * @author junwen.chenjw 2013年7月28日 上午12:13:12
 */
public interface DomNodeIterator  {
    /**
     * 是否存在下一个元素
     * @return 是否存在下一个元素
     */
    public boolean hasNext();

    /**
     * 获得下一个元素
     * @return 下一个元素
     */
    public DomNode next();
    
    /**
     * 结束迭代器
     */
    public void close();
}
