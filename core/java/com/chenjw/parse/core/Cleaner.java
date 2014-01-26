/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.core;

/**
 *  cleaner负责清洗掉不规范html的节点，并把html转化成dom树
 *  
 * @author junwen.chenjw
 * @version $Id: Cleaner.java, v 0.1 2013年8月1日 下午9:38:58 junwen.chenjw Exp $
 */
public interface Cleaner {

    /**
     * 清洗html并生成节点树
     * @param html 文本
     * @return 节点
     */
    public DomNode clean(String html);

}
