/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.statistics;

/**
 * 用于存放一些全局统计数据
 * 
 * @author junwen.chenjw
 * @version $Id: Statistics.java, v 0.1 2013年10月14日 下午2:08:25 junwen.chenjw Exp $
 */
public class Statistics {

    /** 每句xpath语句的执行时间统计 */
    public static final TimeStatistics XPATH_COSTS = new TimeStatistics();
}
