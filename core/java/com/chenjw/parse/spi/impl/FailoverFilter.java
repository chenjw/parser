/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.spi.impl;

import com.chenjw.parse.core.Context;
import com.chenjw.parse.spi.Filter;

/**
 * 用于标记failover的filter，注册为“====”
 * 
 * @author junwen.chenjw
 * @version $Id: FailoverFilter.java, v 0.1 2013年8月1日 下午9:45:27 junwen.chenjw Exp $
 */
public class FailoverFilter implements Filter {
    /** 
     * @see com.chenjw.parse.spi.alipay.aggrbillinfo.core.design.parse.spi.tools.Filter#doFilter(java.lang.String, com.alipay.aggrbillinfo.core.design.parse.core.Context)
     */
    @Override
    public String doFilter(String input, Context context) {
        // 只是一个标记，不做任何事
        return null;
    }
}
