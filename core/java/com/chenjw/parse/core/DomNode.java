/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.core;

/**
 * 对应html中的一个标签节点
 * 
 * @version $Id: DomNode.java, v 0.1 2013年11月19日 下午1:12:27 junwen.chenjw Exp $
 */
public interface DomNode {

    /**
     * 从该节点中根据路径解析出字符串
     * 
     * @param path 路径
     * @param isAttr 是否是属性
     * @return 值
     */
    public String parseString(String path, boolean isAttr);

    /**
     * 
     * 从该节点中根据路径解析出另一个节点
     * 
     * @param path 路径
     * @return 节点
     */
    public DomNode parseNode(String path);

    /**
     * 从该节点中根据路径解析出一组新节点
     * 
     * @param path 路径
     * @return 节点列表
     */
    public DomNodeIterator parseNodeList(String path);

    /**
     * 
     * 从节点中提取出纯文字信息
     * 
     * @return 文本
     */
    public String toText();

}
