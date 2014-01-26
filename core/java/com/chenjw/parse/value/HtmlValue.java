/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.value;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.chenjw.parse.clean.CleanerFactory;
import com.chenjw.parse.config.ItemConfig;
import com.chenjw.parse.core.DomNode;
import com.chenjw.parse.core.XpathValue;

/**
 * html节点的结果是一个字符串，这个字符串
 * 
 * @author junwen.chenjw
 * @version $Id: HtmlValue.java, v 0.1 2013年10月13日 上午4:04:14 junwen.chenjw Exp $
 */
public class HtmlValue extends StringValue {
    /** 使用framevalue来管理子节点 */
    private FrameValue frameValue;
    /** 从html解析得到的节点树 */
    private DomNode    nodeParsedFromHtml;

    /**
     * 构造函数
     * 
     * @param config 配置
     * @param parent 父节点
     */
    public HtmlValue(ItemConfig config, BaseValue parent) {
        super(config, parent);
        if (config.getGroupKeys() == null) {
            throw new IllegalStateException("html value have no items");
        }
        frameValue = ValueFactory.createFrameValue(config.getGroupKeys(), null, parent);
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.StringValue#doLoad(com.alipay.aggrbillinfo.core.design.parse.core.DomNode)
     */
    @Override
    protected boolean doLoad(DomNode root) {
        boolean success = super.doLoad(root);
        if (!success) {
            return false;
        }
        String html = this.getValue();
        if (StringUtils.isBlank(html)) {
            return false;
        }
        nodeParsedFromHtml = CleanerFactory.getXpathEngine().clean(html);
        return frameValue.doLoad(nodeParsedFromHtml);
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.StringValue#writeResult(java.util.Map)
     */
    @Override
    public void writeResult(Map<String, Object> result) {
        frameValue.writeResult(result);
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.StringValue#find(java.lang.String)
     */
    @Override
    protected XpathValue find(String key) {
        return frameValue.find(key);
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.StringValue#doUnload()
     */
    @Override
    protected void doUnload() {
        super.doUnload();
        nodeParsedFromHtml = null;
        frameValue.doUnload();
    }

}
