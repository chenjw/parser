/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.spi.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.chenjw.parse.core.Context;
import com.chenjw.parse.spi.Selector;
import com.chenjw.parse.spi.Selectors;

/**
 * 带switch功能的选择器
 * @author junwen.chenjw
 * @version $Id: SwitchSelectors.java, v 0.1 2013年11月19日 下午1:09:46 junwen.chenjw Exp $
 */
public class SwitchSelectors implements Selectors {
    /** 关键字 */
    private String                 key;
    /** 选择映射 */
    private Map<String, Selectors> selectorsMap;
    /** 当没有找到时默认的key */
    private static final String    DEFAULT_KEY = "_default";

    /**
     * 构造函数
     * 
     * @param key key
     * @param config 配置
     */
    public SwitchSelectors(String key, Map<String, List<Object>> config) {
        this.key = key;
        selectorsMap = new HashMap<String, Selectors>();
        for (Entry<String, List<Object>> entry : config.entrySet()) {
            List<Object> value = entry.getValue();
            selectorsMap.put(entry.getKey(), new SimpleSelectors(value));
        }
    }

    /** 
     * @see com.chenjw.parse.spi.alipay.aggrbillinfo.core.design.parse.spi.tools.Selectors#getSelectors(com.alipay.aggrbillinfo.core.design.parse.core.Context)
     */
    @Override
    public Selector[] getSelectors(Context context) {
        String value = context.use(key);
        Selectors r = null;
        if (value == null) {
            r = selectorsMap.get(DEFAULT_KEY);
        } else {
            r = selectorsMap.get(value);
            if (r == null) {
                r = selectorsMap.get(DEFAULT_KEY);
            }
        }
        if (r == null) {
            return new Selector[0];
        }
        return r.getSelectors(context);
    }
}
