/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.core;

/**
 * 运行时上下文，可见范围会根据当前节点的不同而不同
 * 
 * @author junwen.chenjw
 * @version $Id: Context.java, v 0.1 2013年11月19日 下午1:11:35 junwen.chenjw Exp $
 */
public interface Context {
    /**
     * 从该节点的可见范围内查找目标节点的值
     * 
     * @param key key
     * @return key对应的值
     */
    public String use(String key);

    /**
     * 获得当前值，正常情况下当前值都是null的，除非该节点为sharedItem，才会有当前值
     * 
     * @return 当前值
     */
    public String getValue();

}
