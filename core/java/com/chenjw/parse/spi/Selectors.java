/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.spi;

import com.chenjw.parse.core.Context;

/**
 * 选择器组合
 * 
 * @author junwen.chenjw
 * @version $Id: Selectors.java, v 0.1 2013年11月19日 下午1:08:00 junwen.chenjw Exp $
 */
public interface Selectors {

    /**
     * 获得选择器列表
     * @param context 上下文
     * @return 选择器列表
     */
    public Selector[] getSelectors(Context context);
}
