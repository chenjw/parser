/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.value;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.chenjw.parse.config.ItemConfig;
import com.chenjw.parse.core.DomNode;
import com.chenjw.parse.core.DomNodeIterator;
import com.chenjw.parse.core.SelectContext;
import com.chenjw.parse.core.XpathValue;
import com.chenjw.parse.spi.Selector;

/**
 * 拥有多行的值节点，对应到html中的账单明细等表格
 * 
 * @author junwen.chenjw 2013年7月28日 上午12:07:39
 */
public class ListValue extends BaseValue {

    /** 字段配置 */
    private ItemConfig       config;
    /** 结果值 */
    private List<FrameValue> frames;

    /**
     * 构造函数
     * 
     * @param config 配置
     * @param parent 父节点
     */
    public ListValue(ItemConfig config, BaseValue parent) {
        super(parent);
        this.config = config;
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.BaseValue#doLoad(com.alipay.aggrbillinfo.core.design.parse.core.DomNode)
     */
    @Override
    public boolean doLoad(DomNode root) {
        if (config.getSelectors() == null) {
            return false;
        }
        SelectContext context = new SelectContext();
        for (Selector selector : config.getSelectors().getSelectors(this)) {
            // 每次进入的时候取消加载，为了failover后清除掉那些已经加载的值
            DomNodeIterator nodeIterator = selector.parseNodeList(context, root);
            if (nodeIterator == null) {
                continue;
            }
            try {
                if (!nodeIterator.hasNext()) {
                    continue;
                }
                // 共享变量，不太常用的功能，一般为空
                Map<String, StringValue> sharedValues = loadSharedValues();
                // 根据节点行的数量循环创建frame
                Map<String, ItemConfig> itemConfigs = config.getItems();
                List<FrameValue> value = new ArrayList<FrameValue>();
                while (nodeIterator.hasNext()) {
                    DomNode line = nodeIterator.next();
                    this.currentRoot = line;
                    FrameValue frameValue = ValueFactory.createFrameValue(itemConfigs,
                        sharedValues, this);
                    boolean lineSuccess = frameValue.load(line);
                    if (!lineSuccess) {
                        continue;
                    }
                    value.add(frameValue);
                }
                // 如果查出来的为0就自动continue
                if (value.isEmpty()) {
                    continue;
                }
                this.frames = value;
                return true;
            } finally {
                nodeIterator.close();
            }
        }
        // required设成false的可以免死
        return !config.getRequired();
    }

    /**
     * 加载共享变量
     * @return 共享变量
     */
    private Map<String, StringValue> loadSharedValues() {
        if (config.getSharedItems() == null || config.getSharedItems().size() == 0) {
            return null;
        }
        Map<String, StringValue> result = new HashMap<String, StringValue>();
        for (Entry<String, ItemConfig> entry : config.getSharedItems().entrySet()) {
            result.put(entry.getKey(),
                (StringValue) ValueFactory.createValue(entry.getValue(), null));
        }
        return result;
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.XpathValue#writeResult(java.util.Map)
     */
    @Override
    public void writeResult(Map<String, Object> result) {
        if (frames == null) {
            return;
        }
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (FrameValue frameValue : frames) {
            Map<String, Object> r = new HashMap<String, Object>();
            frameValue.writeResult(r);
            list.add(r);
        }
        result.put(config.getKey(), list);
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.BaseValue#find(java.lang.String)
     */
    @Override
    protected XpathValue find(String key) {
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
        frames = null;
    }
}
