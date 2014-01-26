/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.config;

import java.util.Map;
import java.util.Map.Entry;

import com.chenjw.parse.enums.NodeTypeEnum;
import com.chenjw.parse.spi.Filter;
import com.chenjw.parse.spi.Selectors;
import com.chenjw.parse.utils.ParseUtils;

/**
 * item表示解析结果中的一个字段，比如“姓名”，“账单金额”，“还款日期”等
 * 
 * @author junwen.chenjw
 * @version $Id: ItemConfig.java, v 0.1 2013年8月1日 下午9:35:51 junwen.chenjw Exp $
 */
public class ItemConfig {

    /** 字段key */
    private String                  key;
    /** 这个字段是否是“必须”的，“必须”的字段如果没解析到会导致整个样本解析失败，否则则不会，默认为false */
    private boolean                 required = false;
    /** 节点的类型  */
    private NodeTypeEnum                type     = null;
    /** 节点的选择器，用来从节点中选取子节点，选择器如果有多个会做failover */
    private Selectors               selectors;
    /** 当type为非“list”时有效，如果设置为true，当选择器结果有多个时会做failover，否则只会选取第一个，默认为false */
    private boolean                 multiple = false;

    /** 当multiple为false时有效，如果设置为true，当选择器选择的节点有多个时，内容会进行拼接，默认为false.*/
    private boolean                 join     = false;

    /** 父节点的key */
    private String                  parent;

    /**只有当type为组节点（group或者html）时有效，表示该节点的子节点，key为子节点key */
    private Map<String, ItemConfig> groupKeys;
    /** 选择结果会执行这个过滤器链 */
    private Filter[]                filters;
    /**
     * 下列属性只有当type=list时才有效
     */
    /** 共享节点，列表（list）维度的变量，生命周期为整个列表，整个列表遍历时都可以操作这个变量 */
    private Map<String, ItemConfig> sharedItems;
    /** 行节点，行（frame）维度的变量，生命周期只在这一行 */
    private Map<String, ItemConfig> items;

    public Filter[] getFilters() {
        return filters;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public NodeTypeEnum getType() {
        return type;
    }

    public void setType(NodeTypeEnum type) {
        this.type = type;
    }

    public Selectors getSelectors() {
        return selectors;
    }

    public void setSelectors(Selectors selectors) {
        this.selectors = selectors;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public void setFilters(Filter[] filters) {
        this.filters = filters;
    }

    public Map<String, ItemConfig> getGroupKeys() {
        return groupKeys;
    }

    public void setGroupKeys(Map<String, ItemConfig> groupKeys) {
        this.groupKeys = groupKeys;
    }

    public boolean isMultiple() {
        return multiple;
    }

    public void setMultiple(boolean multiple) {
        this.multiple = multiple;
    }

    public boolean isJoin() {
        return join;
    }

    public void setJoin(boolean join) {
        this.join = join;
    }

    public Map<String, ItemConfig> getSharedItems() {
        return sharedItems;
    }

    public void setSharedItems(Map<String, ItemConfig> sharedItems) {
        this.sharedItems = sharedItems;
    }

    public Map<String, ItemConfig> getItems() {
        return items;
    }

    public void setItems(Map<String, ItemConfig> items) {
        this.items = items;
    }

    /** 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        if (type == NodeTypeEnum.DATA) {
            return "[DATA] " + key;
        } else if (type == NodeTypeEnum.GROUP) {
            StringBuffer sb = new StringBuffer();
            sb.append("[GROUP] " + key + "\n");
            for (Entry<String, ItemConfig> entry : groupKeys.entrySet()) {
                sb.append(ParseUtils.addDepth(1, entry.getValue().toString()) + "\n");
            }
            sb.append("[/GROUP]");
            return sb.toString();
        } else if (type == NodeTypeEnum.LIST) {
            StringBuffer sb = new StringBuffer();
            sb.append("[ITEMS]\n");
            for (Entry<String, ItemConfig> entry : items.entrySet()) {
                sb.append(ParseUtils.addDepth(1, entry.getValue().toString()) + "\n");
            }
            sb.append("[/ITEMS]\n");
            return sb.toString();
        } else {
            return null;
        }
    }

}
