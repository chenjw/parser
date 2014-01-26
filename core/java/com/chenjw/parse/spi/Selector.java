/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.spi;

import com.chenjw.parse.core.DomNode;
import com.chenjw.parse.core.DomNodeIterator;
import com.chenjw.parse.core.SelectContext;

/**
 * 选择器，用于从一个节点开始选择到另一个节点或节点组或字符串
 * 
 * @author junwen.chenjw
 * @version $Id: Selector.java, v 0.1 2013年8月14日 上午11:20:33 junwen.chenjw Exp $
 */
public interface Selector extends Tool {
    /**
     * 是否是属性节点
     * @return
     */
    public boolean isAttr();

    /**
     * 从节点中选择字符串
     * @param context 上下文
     * @param node 节点
     * @return 结果
     */
    public String parseString(SelectContext context, DomNode node);

    /**
     * 从节点中选择子节点
     * @param context 上下文
     * @param node 节点
     * @return 节点
     */
    public DomNode parseNode(SelectContext context, DomNode node);

    /**
     * 从节点中选择子节点列表
     * @param context 上下文
     * @param node 节点
     * @return 节点列表
     */
    public DomNodeIterator parseNodeList(SelectContext context, DomNode node);

}
