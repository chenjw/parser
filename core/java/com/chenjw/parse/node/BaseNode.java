/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.node;

import com.chenjw.parse.core.DomNode;

/**
 * 节点抽象基类
 * @author junwen.chenjw
 * @version $Id: BaseNode.java, v 0.1 2013年11月6日 上午11:30:22 junwen.chenjw Exp $
 */
public abstract class BaseNode implements DomNode {

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.DomNode#parseString(java.lang.String)
     */
    @Override
    public String parseString(String path, boolean isAttr) {
        if (isAttr) {
            return parseAttr(path);
        }
        DomNode node = this.parseNode(path);
        if (node == null) {
            return null;
        }
        return node.toText();
    }

    /**
     * 
     * 解析属性的xpath 
     * @param path 表达式
     * @return 结果
     */
    protected abstract String parseAttr(String path);

}
