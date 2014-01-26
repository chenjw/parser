/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.core;

import java.util.Map;

import com.chenjw.parse.enums.LoadStateEnum;

/**
 * 某个结果节点的值
 * 
 * @version $Id: XpathValue.java, v 0.1 2013年11月19日 下午1:15:51 junwen.chenjw Exp $
 */
public interface XpathValue {

    /**
     * 加载该节点，求取节点的值
     * 
     * @param root 根节点
     * @return 是否成功
     */
    public boolean load(DomNode root);

    /**
     * 把value恢复到非初始化状态，主要用于failover后清除原来已经加载的字段
     */
    public void unload();

    /**
     * 获得该节点当前的加载状态
     * 
     * @return 当前加载状态
     */
    public LoadStateEnum getLoadState();

    /**
     * 把节点的加载结果写入到目标集合中
     * 
     * @param result 目标集合
     */
    public void writeResult(Map<String, Object> result);

}
