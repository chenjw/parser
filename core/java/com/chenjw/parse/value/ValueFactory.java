/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.value;

import java.util.Map;

import com.chenjw.parse.config.ItemConfig;
import com.chenjw.parse.core.XpathValue;

/**
 * 
 * value产生的工厂，所有value的new操作都收拢到这里
 * 
 * @author junwen.chenjw
 * @version $Id: ValueFactory.java, v 0.1 2013年8月1日 下午9:49:36 junwen.chenjw Exp $
 */
public class ValueFactory {
    /**
     * 创建localValue
     * @param sharedValue 共享值
     * @param parent 父节点
     * @return 本地值
     */
    public static LocalStringValue createLocalStringValue(StringValue sharedValue, BaseValue parent) {
        return new LocalStringValue(sharedValue, parent);
    }

    /**
     * 创建frameValue
     * @param configs 配置
     * @param sharedValues 共享值
     * @param parent 父节点
     * @return FrameValue
     */
    public static FrameValue createFrameValue(Map<String, ItemConfig> configs,
                                              Map<String, StringValue> sharedValues,
                                              BaseValue parent) {
        return new FrameValue(configs, sharedValues, parent);
    }

    /**
     * 创建frameValue
     * @param configs 配置
     * @return FrameValue
     */
    public static FrameValue createFrameValue(Map<String, ItemConfig> configs) {
        return new FrameValue(configs, null, null);
    }

    /**
     * 根据节点类型创建Value
     * @param config 配置
     * @param parent 父节点
     * @return 创建的value
     */
    public static XpathValue createValue(ItemConfig config, BaseValue parent) {

        switch (config.getType()) {
            case DATA:
                return new StringValue(config, parent);
            case GROUP:
                return new GroupValue(config, parent);
            case LIST:
                return new ListValue(config, parent);
            case HTML:
                return new HtmlValue(config, parent);
            default:
                throw new IllegalStateException("not support type " + config.getType());
        }
    }
}
