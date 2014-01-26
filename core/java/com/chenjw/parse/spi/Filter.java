/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.spi;

import com.chenjw.parse.core.Context;

/**
 * 扩展点，用于过滤xpath结果
 * 
 * @author junwen.chenjw
 * @version $Id: Filter.java, v 0.1 2013年8月1日 下午9:45:59 junwen.chenjw Exp $
 */
public interface Filter extends Tool {
    /**
     * 传入当前节点的结果，传出过滤后的值
     * @param input 输入
     * @param context 上下文
     * @return 结果
     */
    public String doFilter(String input, Context context);
}
