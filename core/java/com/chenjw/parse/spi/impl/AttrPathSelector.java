/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2013 All Rights Reserved.
 */
package com.chenjw.parse.spi.impl;

import com.chenjw.parse.spi.Selector;

/**
 * 属性选择器
 * 
 * @author junwen.chenjw
 * @version $Id: PathSelector.java, v 0.1 2013年11月6日 上午11:41:24 junwen.chenjw Exp $
 */
public class AttrPathSelector extends PathSelector {

    public AttrPathSelector(String path, Selector depend) {
        super(path, depend);
    }
    
    public AttrPathSelector(String path) {
        super(path);
    }

    /** 
     * @see com.chenjw.parse.spi.impl.alipay.aggrbillinfo.core.design.parse.spi.tools.PathSelector#isAttr()
     */
    @Override
    public boolean isAttr() {
        return true;
    }

}
