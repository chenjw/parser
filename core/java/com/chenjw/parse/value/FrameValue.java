/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.value;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.chenjw.parse.config.ItemConfig;
import com.chenjw.parse.core.DomNode;
import com.chenjw.parse.core.XpathValue;

/**
 * frame节点表现为一堆key的集合，他可以是一个列表中的一行，也可以是最外面的根节点
 * 
 * 
 * @author junwen.chenjw
 * @version $Id: FrameValue.java, v 0.1 2013年11月19日 下午1:50:21 junwen.chenjw Exp $
 */
public class FrameValue extends BaseValue {

    /** 配置信息 */
    protected Map<String, ItemConfig> configs = new HashMap<String, ItemConfig>();

    /** 子节点 */
    protected Map<String, XpathValue> children;

    /** 记录当加载失败时导致加载失败的那个key，用于辅助修改模板 */
    protected String                  notLoadedKey;

    /**
     * 构造函数
     * 
     * @param configs 配置
     * @param sharedValues 共享节点
     * @param parent 父节点
     */
    public FrameValue(Map<String, ItemConfig> configs, Map<String, StringValue> sharedValues,
                      BaseValue parent) {
        super(parent);
        this.configs = configs;
        init(sharedValues);
    }

    /**
     * 根据配置信息，先创建子节点(未初始化状态)
     * 
     * @param sharedValues 共享节点
     */
    private void init(Map<String, StringValue> sharedValues) {
        // 保证按照插入顺序排序，因为：sharedValue要优先于其他value之前加载
        children = new LinkedHashMap<String, XpathValue>();
        // 添加共享的节点
        if (sharedValues != null) {
            for (Entry<String, StringValue> entry : sharedValues.entrySet()) {
                children.put(entry.getKey(),
                    ValueFactory.createLocalStringValue(entry.getValue(), this));
            }
        }
        // frame本身的节点
        for (Entry<String, ItemConfig> entry : configs.entrySet()) {
            children.put(entry.getKey(), ValueFactory.createValue(entry.getValue(), this));
        }
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.BaseValue#doLoad(com.alipay.aggrbillinfo.core.design.parse.core.DomNode)
     */
    @Override
    public boolean doLoad(DomNode root) {
        // 初始化所有的子节点
        this.currentRoot = root;
        boolean success = true;
        for (Entry<String, XpathValue> entry : children.entrySet()) {
            if (!entry.getValue().load(root)) {
                notLoadedKey = entry.getKey();
                success = false;
                break;
            }
        }
        return success;
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.XpathValue#writeResult(java.util.Map)
     */
    @Override
    public void writeResult(Map<String, Object> result) {

        // 把所有子节点的值写入result
        for (Entry<String, XpathValue> entry : children.entrySet()) {
            entry.getValue().writeResult(result);
        }
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.BaseValue#find(java.lang.String)
     */
    @Override
    protected XpathValue find(String key) {
        // 在所有子节点中查找，如果子节点中有group节点，也需要在这些group节点中查找
        for (Entry<String, XpathValue> entry : children.entrySet()) {
            XpathValue value = entry.getValue();
            if (key.equals(entry.getKey())) {
                return value;
            } else if (value instanceof GroupValue) {
                XpathValue found = ((GroupValue) value).findSubValue(key);
                if (found != null) {
                    return found;
                }
            }
        }
        if (parent == null) {
            return null;
        }
        return parent.find(key);
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.BaseValue#doUnload()
     */
    @Override
    protected void doUnload() {
        throw new java.lang.IllegalStateException("frame节点是不会重新加载的，因为创建后如果加载失败是直接丢弃的");
    }

    /**
     * Getter method for property <tt>notLoadedKey</tt>.
     * 
     * @return property value of notLoadedKey
     */
    public String getNotLoadedKey() {
        return notLoadedKey;
    }

    /**
     * Setter method for property <tt>notLoadedKey</tt>.
     * 
     * @param notLoadedKey value to be assigned to property notLoadedKey
     */
    public void setNotLoadedKey(String notLoadedKey) {
        this.notLoadedKey = notLoadedKey;
    }

}
