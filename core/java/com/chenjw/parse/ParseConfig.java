/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse;

import com.chenjw.parse.clean.JsoupVtdCleaner;

/**
 * 一些解析环境的全局配置
 * 
 * 
 * @author junwen.chenjw
 * @version $Id: ParseConstants.java, v 0.1 2013年8月16日 上午10:33:01 junwen.chenjw Exp $
 */
public class ParseConfig {
    /** 是否进行xpath路径优化 */
    public static boolean  isOptimizeXpath      = true;
    /**xpath引擎 */
    public static Class<?> cleanerClass = JsoupVtdCleaner.class;
}
