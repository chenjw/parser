/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.value;

import org.apache.commons.lang.StringUtils;

import com.chenjw.parse.core.DomNode;

/**
 * sharedValue的本地版本
 * <p>
 * 一个sharedValue的值可以在多个frame之间共享，通过进入frame时创建LocalStringValue的实例来实现，LocalStringValue有以下特性：
 * </p>
 * <ul>
 * <li>1. LocalStringValue的初始值来自于共享value。</li>
 * <li>2. LocalStringValue加载后会覆盖共享value的值。</li>
 * <li>3. 共享value的值在外部发生变化的时候，LocalStringValue不会跟着发生变化。</li>
 * </ul>
 * 
 * @author junwen.chenjw
 * @version $Id: LocalStringValue.java, v 0.1 2013年11月19日 下午1:53:05 junwen.chenjw Exp $
 */
public class LocalStringValue extends StringValue {

    /** 共享value */
    protected StringValue sharedValue;

    /**
     * 构造函数
     * 
     * @param sharedValue 共享变量
     * @param parent 父节点
     */
    public LocalStringValue(StringValue sharedValue, BaseValue parent) {
        super(sharedValue.config, parent);
        this.sharedValue = sharedValue;
        this.value = sharedValue.value;
    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.StringValue#checkSuccess()
     */
    @Override
    protected boolean checkSuccess() {
        return !StringUtils.equals(sharedValue.value, value);

    }

    /** 
     * @see com.alipay.aggrbillinfo.core.design.parse.value.StringValue#doLoad(com.alipay.aggrbillinfo.core.design.parse.core.DomNode)
     */
    @Override
    protected boolean doLoad(DomNode root) {
        // 如果成功，覆盖掉共享value的值，否则不覆盖
        boolean success = super.doLoad(root);
        sharedValue.value = this.value;
        return success;
    }
}
