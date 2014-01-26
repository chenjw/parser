/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.spi.impl;

import java.util.ArrayList;
import java.util.List;

import com.chenjw.parse.ParseConfig;
import com.chenjw.parse.core.Context;
import com.chenjw.parse.spi.Selector;
import com.chenjw.parse.spi.Selectors;
import com.chenjw.parse.utils.PathSelectorOptimizer;

/**
 * 默认使用的选择器列表
 * 
 * @author junwen.chenjw
 * @version $Id: SimpleSelectors.java, v 0.1 2013年11月19日 下午1:08:44 junwen.chenjw Exp $
 */
public class SimpleSelectors implements Selectors {
    /** 选择器列表 */
    private Selector[] selectors;

    /**
     * 构造函数
     * 
     * @param str 字符串
     */
    public SimpleSelectors(String str) {
        selectors = new Selector[] { new PathSelector(str, null) };
    }

    /**
     * 构造函数
     * 
     * @param config 配置
     */
    public SimpleSelectors(List<Object> config) {
        List<Selector> ss = new ArrayList<Selector>();
        if (config != null) {
            for (Object obj : config) {
                if (obj instanceof Selector) {
                    ss.add((Selector) obj);
                } else if (obj instanceof String) {
                    ss.add(new PathSelector((String) obj, null));
                }
            }
        }
        if (ParseConfig.isOptimizeXpath) {
            selectors = PathSelectorOptimizer.optimize(ss.toArray(new Selector[ss.size()]));
        } else {
            selectors = ss.toArray(new Selector[ss.size()]);
        }
    }

    /** 
     * @see com.chenjw.parse.spi.alipay.aggrbillinfo.core.design.parse.spi.tools.Selectors#getSelectors(com.alipay.aggrbillinfo.core.design.parse.core.Context)
     */
    public Selector[] getSelectors(Context context) {
        return selectors;
    }
}
