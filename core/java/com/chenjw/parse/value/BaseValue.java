/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.value;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.chenjw.parse.core.Context;
import com.chenjw.parse.core.DomNode;
import com.chenjw.parse.core.XpathValue;
import com.chenjw.parse.enums.LoadStateEnum;

/**
 * value的一些基本属性和方法
 * 
 * @author junwen.chenjw
 * @version $Id: BaseValue.java, v 0.1 2013年11月19日 下午1:49:28 junwen.chenjw Exp $
 */
public abstract class BaseValue implements XpathValue, Context {

    /**  执行过程中的当前root节点*/
    protected DomNode       currentRoot;

    /** 当前节点的父节点，如果该节点为根节点，parent为空 */
    protected BaseValue     parent;

    /** 当前加载状态，默认为NOTLOADED */
    protected LoadStateEnum loadState = LoadStateEnum.NOTLOADED;

    /**
     * 构造函数
     * 
     * @param parent 父节点
     */
    public BaseValue(BaseValue parent) {
        this.parent = parent;
    }

    /**
     * 在该节点的可见范围中查找目标结点
     * 
     * @param key key
     * @return 目标节点
     */
    protected abstract XpathValue find(String key);

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.XpathValue#unload()
     */
    public void unload() {
        if (loadState == LoadStateEnum.NOTLOADED) {
            return;
        }
        doUnload();
        loadState = LoadStateEnum.NOTLOADED;
    }

    /**
     * 加载该节点，一般当这个节点还未加载，但是在tools工具中被其他节点引用到了，才会执行这个方法
     */
    protected void load() {
        if (loadState == LoadStateEnum.SUCCESS || loadState == LoadStateEnum.FAIL) {
            return;
        }
        if (parent == null) {
            return;
        }
        if (parent.loadState == LoadStateEnum.NOTLOADED) {
            parent.load();
        } else if (parent.loadState == LoadStateEnum.LOADING) {
            load(parent.currentRoot);
        }
        // 否则表示父节点已经加载失败了，且该子节点没有加载
        return;
        // 不可能
        //throw new IllegalStateException("can't get here " + " node:" + this);
    }

    /*
     * 用于checker和transformer方法执行时取得当前上下文的值
     * @see com.alipay.aggrbillinfo.core.design.parse.core.Context#use(java.lang.String)
     */
    @Override
    public String use(String key) {
        // 在当前节点得可见范围内查找目标节点
        BaseValue value = (BaseValue) find(key);
        if (value == null) {
            return null;
        }
        // 只有当知道的节点为string节点时才会有返回值
        if (!(value instanceof StringValue)) {
            return null;
        }
        // string节点在getValue时如果还未初始化，会自动加载自身
        value.load();
        return ((StringValue) value).getValue();
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.XpathValue#load(com.alipay.aggrbillinfo.core.design.parse.core.DomNode)
     */
    public final boolean load(DomNode root) {
        switch (loadState) {
            case LOADING:
                // 循环加载导致死锁，需要检查下配置里面是否有循环依赖
                throw new IllegalStateException("dead lock" + "node:" + this);
            case SUCCESS:
                return true;
            case FAIL:
                return false;
            case NOTLOADED:
                loadState = LoadStateEnum.LOADING;
                boolean success = doLoad(root);
                loadState = success ? LoadStateEnum.SUCCESS : LoadStateEnum.FAIL;
                return success;
            default:
                // 不可能
                throw new IllegalStateException("can't get here" + "node:" + this);
        }

    }

    /**
     * 供子类实现各种节点加载时的特殊逻辑
     * 
     * @param node 节点
     * @return 是否成功
     */
    protected abstract boolean doLoad(DomNode node);

    /**
     * 供子类实现各种节点取消加载时的特殊逻辑
     */
    protected abstract void doUnload();

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.XpathValue#getLoadState()
     */
    public LoadStateEnum getLoadState() {
        return loadState;
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.core.Context#getValue()
     */
    public String getValue() {
        return null;
    }
}
