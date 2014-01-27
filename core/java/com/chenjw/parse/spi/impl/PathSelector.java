/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.spi.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import com.chenjw.parse.core.DomNode;
import com.chenjw.parse.core.DomNodeIterator;
import com.chenjw.parse.core.SelectContext;
import com.chenjw.parse.node.MultipleDomNodeIterator;
import com.chenjw.parse.spi.Selector;

/**
 * 路径选择器
 * 
 * @author junwen.chenjw
 * @version $Id: PathSelector.java, v 0.1 2013年11月6日 上午11:41:24 junwen.chenjw Exp $
 */
public class PathSelector implements Selector {
    /** 依赖的选择器 */
    private Selector depend;
    /** 执行的路径 */
    private String   path;

    /**
     * 构造函数
     * @param path 路径
     * @param depend 依赖的选择器
     */
    public PathSelector(String path, Selector depend) {
        this.path = path;
        this.depend = depend;
    }

    /**
     * 构造函数
     * @param path 路径
     */
    public PathSelector(String path) {
        this.path = path;
    }

    
    
    /**
     * 获得当前节点，如果有依赖的选择器就先执行依赖选择器
     * @param context 上下文
     * @param node 节点
     * @return 节点列表
     */
    private DomNode[] getRootNode(SelectContext context, DomNode node) {
        if (depend == null) {
            return new DomNode[] { node };
        }
        DomNode[] dependNodes = context.getCachedResult(depend);
        // 为null表示没有初始化过，会重新初始化
        if (dependNodes == null) {
            // 如果父节点找不到
            DomNodeIterator domNodeIterator = depend.parseNodeList(context, node);
            if (domNodeIterator == null) {
                context.putCachedResult(depend, SelectContext.MARKED_LOAD_FAIL);
                return null;
            } else {
                List<DomNode> dependNodeList = new ArrayList<DomNode>();
                while (domNodeIterator.hasNext()) {
                    dependNodeList.add(domNodeIterator.next());
                }
                dependNodes = dependNodeList.toArray(new DomNode[dependNodeList.size()]);
                context.putCachedResult(depend, dependNodes);
                return dependNodes;
            }
        }
        // 表示初始化过了，但是没有找到结果
        else if (dependNodes == SelectContext.MARKED_LOAD_FAIL) {
            return null;
        }
        // 初始化过了，并且找到结果了
        else {
            return dependNodes;
        }
    }

    /**
     * 是否是属性节点
     * @return
     */
    public boolean isAttr() {
        return false;
    }

    /** 
     * 解析字符串
     * 
     * @see com.chenjw.parse.spi.alipay.aggrbillinfo.core.design.parse.spi.tools.Selector#parseString(com.chenjw.parse.core.alipay.aggrbillinfo.core.design.parse.spi.tools.SelectContext, com.alipay.aggrbillinfo.core.design.parse.core.DomNode)
     */
    public String parseString(SelectContext context, DomNode node) {
        DomNode[] roots = getRootNode(context, node);
        if (roots == null) {
            return null;
        } else {
            for (DomNode root : roots) {
                String value = root.parseString(path, isAttr());
                if (!StringUtils.isBlank(value)) {
                    return value;
                }
            }
            return null;
        }
    }

    /** 
     * 解析节点
     * 
     * @see com.chenjw.parse.spi.alipay.aggrbillinfo.core.design.parse.spi.tools.Selector#parseNode(com.chenjw.parse.core.alipay.aggrbillinfo.core.design.parse.spi.tools.SelectContext, com.alipay.aggrbillinfo.core.design.parse.core.DomNode)
     */
    public DomNode parseNode(SelectContext context, DomNode node) {
        DomNode[] roots = getRootNode(context, node);
        if (roots == null) {
            return null;
        } else {
            for (DomNode root : roots) {
                DomNode value = root.parseNode(path);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }
    }

    /** 
     * 解析节点列表
     * 
     * @see com.chenjw.parse.spi.alipay.aggrbillinfo.core.design.parse.spi.tools.Selector#parseNodeList(com.chenjw.parse.core.alipay.aggrbillinfo.core.design.parse.spi.tools.SelectContext, com.alipay.aggrbillinfo.core.design.parse.core.DomNode)
     */
    public DomNodeIterator parseNodeList(SelectContext context, DomNode node) {
        DomNode[] roots = getRootNode(context, node);
        if (roots == null) {
            return null;
        } else {
            List<DomNodeIterator> list = new ArrayList<DomNodeIterator>();
            for (DomNode root : roots) {
                DomNodeIterator value = root.parseNodeList(path);
                if (value != null && value.hasNext()) {
                    list.add(value);
                }
            }
            if (list.isEmpty()) {
                return null;
            }
            return new MultipleDomNodeIterator(list.toArray(new DomNodeIterator[list.size()]));
        }
    }

    /**
     * 依赖的选择器
     * @return 依赖的选择器
     */
    public Selector getDepend() {
        return depend;
    }

    /**
     * 路径
     * @return 路径
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置依赖的选择器
     * @param depend 依赖的选择器
     */
    public void setDepend(Selector depend) {
        this.depend = depend;
    }

    /**
     * 设置路径
     * @param path 路径
     */
    public void setPath(String path) {
        this.path = path;
    }

}
