/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.core;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * 和xpath相关的一些方法api，执行时如果有异常不会暴露到外面
 * 
 * @version $Id: XpathExecutor.java, v 0.1 2013年11月19日 下午1:15:00 junwen.chenjw Exp $
 */
public interface XpathExecutor {

    /**
     * 解析xpath返回字符串
     * @param node 节点
     * @param path 路径
     * @return 值
     */
    public String parseString(Node node, String path);

    /**
     * 解析xpath返回节点
     * @param node 节点
     * @param path 路径
     * @return 节点
     */
    public Node parseNode(Node node, String path);

    /**
     * 解析xpath返回节点列表
     * @param node 节点
     * @param path 路径
     * @return 节点列表
     */
    public NodeList parseNodeList(Node node, String path);

}
