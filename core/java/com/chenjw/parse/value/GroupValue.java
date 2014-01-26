/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.value;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.chenjw.parse.config.ItemConfig;
import com.chenjw.parse.core.DomNode;
import com.chenjw.parse.core.DomNodeIterator;
import com.chenjw.parse.core.SelectContext;
import com.chenjw.parse.core.XpathValue;
import com.chenjw.parse.spi.Selector;

/**
 * 集合节点，用于实现一个节点组
 * 
 * @author junwen.chenjw
 * @version $Id: GroupValue.java, v 0.1 2013年11月19日 下午1:51:01 junwen.chenjw Exp $
 */
public class GroupValue extends BaseValue {

    /**字段配置  */
    protected ItemConfig            config;
    /** 子节点配置 */
    private Map<String, XpathValue> children = new HashMap<String, XpathValue>();

    /**
     * 构造函数
     * 
     * @param config 配置
     * @param parent 父节点
     */
    public GroupValue(ItemConfig config, BaseValue parent) {
        super(parent);
        this.config = config;
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        if (config.getGroupKeys() != null) {
            for (Entry<String, ItemConfig> entry : config.getGroupKeys().entrySet()) {
                children.put(entry.getKey(), ValueFactory.createValue(entry.getValue(), this));
            }
        }
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.BaseValue#doUnload()
     */
    @Override
    public void doUnload() {
        for (Entry<String, XpathValue> entry : children.entrySet()) {
            entry.getValue().unload();
        }
    }

    /**
     * 尝试从某个节点开始加载
     * 
     * @param node 节点
     * @return 加载结果
     */
    private boolean tryLoadFromNode(DomNode node) {
        if (node == null) {
            return false;
        }
        this.currentRoot = node;
        for (Entry<String, XpathValue> entry : children.entrySet()) {
            if (!entry.getValue().load(node)) {
                // 一旦有失败的时候取消加载，为了failover后清除掉那些已经加载的值
                doUnload();
                return false;
            }
        }
        return true;
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.BaseValue#doLoad(com.alipay.aggrbillinfo.core.design.parse.core.DomNode)
     */
    @Override
    public boolean doLoad(DomNode root) {
        if (config.getSelectors() != null) {
            SelectContext context = new SelectContext();
            for (Selector selector : config.getSelectors().getSelectors(this)) {
                if (config.isMultiple()) {
                    DomNodeIterator nodeIterator = selector.parseNodeList(context, root);
                    if (nodeIterator != null) {
                        try {
                            while (nodeIterator.hasNext()) {
                                if (tryLoadFromNode(nodeIterator.next())) {
                                    return true;
                                }
                            }
                        } finally {
                            nodeIterator.close();
                        }
                    }
                } else {
                    DomNode node = selector.parseNode(context, root);
                    if (tryLoadFromNode(node)) {
                        return true;
                    }
                }
            }
        }
        // required设成false的可以免死
        return !config.getRequired();
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.XpathValue#writeResult(java.util.Map)
     */
    @Override
    public void writeResult(Map<String, Object> result) {
        for (Entry<String, XpathValue> entry : children.entrySet()) {
            entry.getValue().writeResult(result);
        }
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.BaseValue#find(java.lang.String)
     */
    @Override
    protected XpathValue find(String key) {
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
     * 查找子节点
     * @param key 子节点key
     * @return 子节点
     */
    protected XpathValue findSubValue(String key) {
        for (Entry<String, XpathValue> entry : children.entrySet()) {
            XpathValue value = entry.getValue();
            if (key.equals(entry.getKey())) {
                return value;
            }
        }
        return null;
    }

}
