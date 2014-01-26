/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.value;

import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.chenjw.parse.config.ItemConfig;
import com.chenjw.parse.core.DomNode;
import com.chenjw.parse.core.DomNodeIterator;
import com.chenjw.parse.core.SelectContext;
import com.chenjw.parse.core.XpathValue;
import com.chenjw.parse.spi.Selector;
import com.chenjw.parse.utils.ParseUtils;

/**
 * string节点，用于表示一个值
 * 
 * @author junwen.chenjw
 * @version $Id: StringValue.java, v 0.1 2013年8月1日 下午9:58:46 junwen.chenjw Exp $
 */
public class StringValue extends BaseValue {

    /** 属性配置 */
    protected ItemConfig config;
    /** 执行的结果 */
    protected String     value;

    /**
     * 构造函数
     * 
     * @param config 配置
     * @param parent 父节点
     */
    public StringValue(ItemConfig config, BaseValue parent) {
        super(parent);
        this.config = config;
    }

    /**
     * 判断是否加载成功
     * @return 是否加载成功
     */
    protected boolean checkSuccess() {
        return StringUtils.isNotBlank(this.value);
    }

    /**
     * 加载
     * @param text 文本
     * @return 是否加载成功
     */
    protected boolean tryLoadFromText(String text) {
        text = ParseUtils.doFilter(text, config, this);
        this.value = text;
        return checkSuccess();
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.BaseValue#doLoad(com.alipay.aggrbillinfo.core.design.parse.core.DomNode)
     */
    @Override
    protected boolean doLoad(DomNode root) {
        if (config.getSelectors() == null) {
            // 如果没有配置selector也会进入过滤器，因为过滤器中可以取得其他参数的值
            if (tryLoadFromText(null)) {
                return true;
            }
        } else {
            SelectContext context = new SelectContext();
            for (Selector selector : config.getSelectors().getSelectors(this)) {
                // 如果配置了multiple，selector的结果当做nodelist来遍历
                if (config.isMultiple()) {
                    DomNodeIterator nodeIterator = selector.parseNodeList(context, root);
                    if (nodeIterator != null) {
                        try {
                            while (nodeIterator.hasNext()) {
                                if (tryLoadFromText(nodeIterator.next().parseString(".",
                                    selector.isAttr()))) {
                                    return true;
                                }
                            }
                        } finally {
                            nodeIterator.close();
                        }
                    }
                }
                // 如果没有配置multiple，selector的结果直接当做string来处理
                else {
                    // 如果有配置join，则获取selector结果集合text内容进行拼接，否则选取第一个selector结果text内容
                    if (config.isJoin()) {
                        DomNodeIterator nodeIterator = selector.parseNodeList(context, root);
                        if (nodeIterator != null) {
                            try {
                                String text = "";
                                while (nodeIterator.hasNext()) {
                                    String nextText = nodeIterator.next().parseString(".",
                                        selector.isAttr());
                                    if (nextText != null) {
                                        text += nextText;
                                    }
                                }
                                this.value = ParseUtils.doFilter(text, config, this);
                            } finally {
                                nodeIterator.close();
                            }
                        }

                    } else if (tryLoadFromText(selector.parseString(context, root))) {
                        return true;
                    }
                }
            }
        }
        if (checkSuccess()) {
            return true;
        } else {
            return !config.getRequired();
        }
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.BaseValue#getValue()
     */
    @Override
    public String getValue() {
        return value;
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.BaseValue#toString()
     */
    @Override
    public String toString() {
        return config.getKey();
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.XpathValue#writeResult(java.util.Map)
     */
    @Override
    public void writeResult(Map<String, Object> result) {
        result.put(config.getKey(), value);
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
        value = null;
    }

}
